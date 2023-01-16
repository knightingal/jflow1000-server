package org.nanking.knightingal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nanking.knightingal.bean.*;
import org.nanking.knightingal.dao.Local1000ImgDao;
import org.nanking.knightingal.dao.Local1000SectionDao;
import org.nanking.knightingal.runnable.DownloadImgRunnable;
import org.nanking.knightingal.service.WsMsgService;
import org.nanking.knightingal.util.FileUtil;
import org.nanking.knightingal.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


/**
 * @author Knightingal
 */
@RequestMapping("/local1000")
@RestController
public class Local1000Controller {

    private static final Log log = LogFactory.getLog(Local1000Controller.class);

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public Local1000Controller(Local1000SectionDao local1000SectionDao, Local1000ImgDao local1000ImgDao) {
        this.local1000ImgDao = local1000ImgDao;
        this.local1000SectionDao = local1000SectionDao;
    }

    private Local1000SectionDao local1000SectionDao;

    private Local1000ImgDao local1000ImgDao;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Executor downloadImgThreadPoolExecutor;

    @Autowired
    private Executor downloadSectionThreadPoolExecutor;

    @Autowired
    private WsMsgService wsMsgService;

    @Value("${baseDir}")
    private String baseDir;

    @RequestMapping("/init")
    public Object init() {
        File baseDirFile = new File(baseDir + "/source");
        String[] list = baseDirFile.list();
        final String[] dirList = Arrays.copyOfRange(list, 0, 10);

        executorService.submit(() -> {
            List<Flow1000Section> sectionList = Stream.of(dirList).map(dirName -> {
                    log.error("process " + dirName);
                    log.error("===========================");
                    String timeStamp = dirName.substring(0, 14);
                    String name = dirName.substring(14);
                    Flow1000Section flow1000Section = new Flow1000Section();
                    flow1000Section.setName(name);
                    flow1000Section.setCreateTime(timeStamp);
                    flow1000Section.setDirName(dirName);

                    String[] imgNameArray = listImages(dirName);
                    
                    List<Flow1000Img> imgList = Stream.of(imgNameArray)
                            .sorted(Local1000Controller::compareImgName)
                            .map(imgNameItem -> generate1000Img(imgNameItem, flow1000Section))
                            .collect(Collectors.toList());
                    flow1000Section.setImages(imgList);
                    flow1000Section.setCover(imgList.get(0).getName());
                    flow1000Section.setCoverHeight(imgList.get(0).getHeight());
                    flow1000Section.setCoverWidth(imgList.get(0).getWidth());
                    return flow1000Section;
            }).collect(Collectors.toList());        

            local1000SectionDao.saveEntitiesAllAndFlush(sectionList);

        });

        return null;
    }



    @RequestMapping("/picDetailAjax")
    public SectionDetail picDetailAjax(@RequestParam(value = "id", defaultValue = "1") Long id) {
        log.info("handle /picDetailAjax, id=" + id);
        Flow1000Section flow1000Section = local1000SectionDao.queryFlow1000SectionById(id);
        if (flow1000Section == null) {
            return new SectionDetail();
        }
        List<ImgDetail> imgDetailList = flow1000Section.getImages().stream().map(image -> 
            new ImgDetail(image.getName(), image.getWidth(), image.getHeight())
        ).collect(Collectors.toList());

        return new SectionDetail(flow1000Section.getDirName(), flow1000Section.getId(), imgDetailList);
    }

    @RequestMapping("/picContentAjax")
    public SectionContent picContentAjax(@RequestParam(value = "id", defaultValue = "1") Long id) {
        log.info("handle /picDetailAjax, id=" + id);
        Flow1000Section flow1000Section = local1000SectionDao.queryFlow1000SectionById(id);

        List<String> imgList = flow1000Section.getImages().stream().map(image -> image.getName()).collect(Collectors.toList());

        return new SectionContent(flow1000Section.getDirName(), flow1000Section.getId().intValue(), imgList);
    }

    @RequestMapping("/searchSection")
    public List<Flow1000Section> searchSection(@RequestParam(value = "name", defaultValue = "") String name) {
        log.debug("searchSection request, name=" + name);
        if ("".equals(name)) {
            return new ArrayList<Flow1000Section>();
        }
        name = "%" + name + "%";
        List<Flow1000Section> searchResult = local1000SectionDao.searchFlow1000SectionByName(name);        

        return searchResult;
    }

    @RequestMapping("/picIndexAjax")
    public List<PicIndex> picIndexAjax(
            @RequestParam(value="time_stamp", defaultValue="19700101000000")String timeStamp,
            @RequestParam(value="album", defaultValue="")String album
    ) {
        log.info("handle /picIndexAjax, timeStamp=" + timeStamp);

        Specification<Flow1000Section> specification = new Flow1000SectionSpecification<>() ;
        List<Flow1000Section> flow1000SectionList = local1000SectionDao.findAll( specification);

        return flow1000SectionList.stream().map(flow1000Section -> {
            return new PicIndex(
                    flow1000Section.getId().intValue(),
                    flow1000Section.getDirName(),
                    flow1000Section.getCreateTime(),
                    flow1000Section.getCover(),
                    flow1000Section.getCoverWidth(),
                    flow1000Section.getCoverHeight()
            );
        }).collect(Collectors.toList());
    }

    @RequestMapping("/picIndexAjaxByPage")
    public PicIndexPage picIndexAjaxByPage(
            @RequestParam(value="time_stamp", defaultValue="19700101000000")String timeStamp
    ) {
        log.info("handle /picIndexAjax, timeStamp=" + timeStamp);
        List<Flow1000Section> flow1000SectionList = local1000SectionDao.queryFlow1000SectionByCreateTime(timeStamp);
        List<PicIndex> picIndexList = new ArrayList<>();

        for (Flow1000Section flow1000Section : flow1000SectionList) {
            picIndexList.add(new PicIndex(
                    flow1000Section.getId().intValue(),
                    flow1000Section.getDirName(),
                    flow1000Section.getCreateTime(),
                    flow1000Section.getCover(),
                    flow1000Section.getCoverWidth(),
                    flow1000Section.getCoverHeight()
            ));
        }

        return new PicIndexPage(picIndexList.subList(0, 10), picIndexList.size());
    }

    @RequestMapping(value="/urls1000", method={RequestMethod.POST})
    public void urls1000(@RequestBody Urls1000Body urls1000Body) {
        log.info("handle /urls1000, body=" + urls1000Body.toString());
        String timeStamp = ((TimeUtil) applicationContext.getBean("timeUtil")).timeStamp();
        FileUtil fileUtil = (FileUtil) applicationContext.getBean("fileUtil");
        String dirName = timeStamp + urls1000Body.getTitle();
        String absPath = "/home/knightingal/download/linux1000/source/" + dirName + "/";
        File dirFile = new File(absPath);
        if (!dirFile.mkdirs()) {
            return;
        }

        Flow1000Section flow1000Section = new Flow1000Section();
        flow1000Section.setName(urls1000Body.getTitle());
        flow1000Section.setDirName(dirName);
        flow1000Section.setCreateTime(timeStamp);
        flow1000Section.setCover(
                fileUtil.getFileNameByUrl(urls1000Body.getImgSrcArray().get(0).getSrc())
        );
        flow1000Section.setAlbum("flow1000");
        List<Flow1000Img> flow1000ImgList = new ArrayList<>();
        for (Urls1000Body.ImgSrcBean imgSrcBean : urls1000Body.getImgSrcArray()) {
            String fileName = fileUtil.getFileNameByUrl(imgSrcBean.getSrc());
            Flow1000Img flow1000Img = new Flow1000Img();
            flow1000Img.setName(fileName);
            flow1000Img.setInCover(
                    urls1000Body.getImgSrcArray().lastIndexOf(imgSrcBean) == 0 ? 1 : 0
            );
            flow1000Img.setSrc(imgSrcBean.getSrc());
            flow1000Img.setHref(imgSrcBean.getRef());
            flow1000ImgList.add(flow1000Img);
        }
        downloadSectionThreadPoolExecutor.execute(() -> {
            CountDownLatch countDownLatch = new CountDownLatch(flow1000ImgList.size());

            for (Flow1000Img flow1000Img : flow1000ImgList) {
                downloadImgThreadPoolExecutor.execute(new DownloadImgRunnable(
                        flow1000Img, dirName, countDownLatch, baseDir
                ));
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info(flow1000Section.getDirName() + " download complete");
            // local1000SectionDao.insertFlow1000Section(flow1000Section);
            flow1000ImgList.forEach(flow1000Img -> {
            });
            // local1000ImgDao.insertFlow1000Img(flow1000ImgList);
            PicIndex picIndex = new PicIndex(
                    flow1000Section.getId().intValue(),
                    flow1000Section.getDirName(),
                    flow1000Section.getCreateTime(),
                    flow1000Section.getCover(),
                    flow1000Section.getCoverWidth(),
                    flow1000Section.getCoverHeight()
            );
            ObjectMapper mapper = new ObjectMapper();
            try {
                wsMsgService.sendWsMsg(mapper.writeValueAsString(picIndex));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @RequestMapping(value="/deleteSection", method={RequestMethod.POST})
    public void deleteSection(@RequestBody SectionDetail sectionDetail) {
        log.info("handle /deleteSection, sectionDetail=" + sectionDetail.toString());
        if (sectionDetail.getId() == null || sectionDetail.getId() <= 0) {
            return;
        }
        local1000ImgDao.deleteById(sectionDetail.getId());
        local1000SectionDao.deleteById(sectionDetail.getId());
    }

    private String[] listImages(String dirName) {
        File dirFile = new File(baseDir + "/source" + "/" + dirName);
        return dirFile.list((dir, fileName) -> 
            fileName != null && (
                fileName.endsWith(".jpg") 
                    || fileName.endsWith(".JPG") 
                    || fileName.endsWith(".jpeg") 
                    || fileName.endsWith(".JPEG") 
                    || fileName.endsWith(".png") 
                    || fileName.endsWith(".PNG")
            )
        );
    }

    private static int compareImgName(String name1, String name2) {
        name1 = name1.split("\\.")[0];
        name2 = name2.split("\\.")[0];

        try {
            return Integer.parseInt(name1) - Integer.parseInt(name2);
        } catch (Exception e) {
            return name1.compareTo(name2);
        }
    }

    private Flow1000Img generate1000Img(String imgName, Flow1000Section flow1000Section) {
        try {
            BufferedImage sourceImg = ImageIO.read(Files.newInputStream(Paths.get(baseDir + "/source" + "/" + flow1000Section.getDirName() + "/" + imgName)));
            int width = sourceImg.getWidth();
            int height = sourceImg.getHeight();
            Flow1000Img flow1000Img = new Flow1000Img();
            flow1000Img.setName(imgName);
            flow1000Img.setHeight(height);
            flow1000Img.setWidth(width);
            flow1000Img.setFlow1000Section(flow1000Section);
            return flow1000Img;
        } catch (IOException e) {
        }
        return null;
    }
}
