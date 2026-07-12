package org.nanking.knightingal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.criteria.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nanking.knightingal.AppConfiguration;
import org.nanking.knightingal.warlock.WarlockImage;
import org.nanking.knightingal.warlock.WarlockParser;
import org.nanking.knightingal.warlock.WarlockSection;
import org.nanking.knightingal.bean.*;
import org.nanking.knightingal.dao.Local1000AlbumConfigDao;
import org.nanking.knightingal.dao.Local1000ImgDao;
import org.nanking.knightingal.dao.Local1000SectionDao;
import org.nanking.knightingal.dao.jpa.Local1000SectionRepo;
import org.nanking.knightingal.runnable.DownloadImgRunnable;
import org.nanking.knightingal.service.WsMsgService;
import org.nanking.knightingal.util.AvifUtil;
import org.nanking.knightingal.util.FileUtil;
import org.nanking.knightingal.util.TimeUtil;
import org.nanking.knightingal.util.WebpUtil;
import org.nanking.knightingal.util.AvifUtil.ImgSize;
import org.nanking.knightingal.util.WebpUtil.WebpImageSize;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

/**
 * @author Knightingal
 */
@CrossOrigin("*")
@RequestMapping("/local1000")
@RestController
public class Local1000Controller {

  public static final String WEBP_SUFFIX = ".webp";

  private static final Logger LOG = LogManager.getLogger(Local1000Controller.class);

  private final Local1000SectionRepo local1000SectionRepo;

  private final TimeUtil timeUtil;

  private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

  public Local1000Controller(ApplicationContext applicationContext,
      Local1000SectionDao local1000SectionDao, Local1000ImgDao local1000ImgDao,
      Local1000AlbumConfigDao local1000AlbumConfigDao, TimeUtil timeUtil, Local1000SectionRepo local1000SectionRepo,
      WsMsgService wsMsgService) {
    this.applicationContext = applicationContext;
    this.local1000ImgDao = local1000ImgDao;
    this.local1000SectionDao = local1000SectionDao;
    this.local1000AlbumConfigDao = local1000AlbumConfigDao;
    this.timeUtil = timeUtil;
    this.local1000SectionRepo = local1000SectionRepo;
    this.wsMsgService = wsMsgService;
  }

  private final Local1000SectionDao local1000SectionDao;

  private final Local1000ImgDao local1000ImgDao;

  private final Local1000AlbumConfigDao local1000AlbumConfigDao;

  private final ApplicationContext applicationContext;

  private final WsMsgService wsMsgService;

  private Executor downloadImgThreadPoolExecutor = null;

  private Executor downloadSectionThreadPoolExecutor = null;

  @Value("${local1000.base-dir}")
  private String baseDir;

  @Value("${local1000.warlock-dir}")
  private String warlockDir;

  /** Scans the local source directory, parses section and image metadata, and persists them to the database. */
  @RequestMapping("/init")
  public Object init() {
    File baseDirFile = new File(baseDir + "/source");
    final String[] dirList = baseDirFile.list();

    executorService.submit(() -> {
      List<Flow1000Section> sectionList = Stream.of(dirList).map(dirName -> {
        LOG.error("process {}", dirName);
        LOG.error("===========================");
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
            .toList();
        flow1000Section.setImages(imgList);
        flow1000Section.setCover(imgList.get(0).getName());
        flow1000Section.setCoverHeight(imgList.get(0).getHeight());
        flow1000Section.setCoverWidth(imgList.get(0).getWidth());
        return flow1000Section;
      }).toList();

      local1000SectionDao.saveAllAndFlush(sectionList);

    });

    return null;
  }

  /** Scans the warlock directory and imports all sections (with images) into the database in a background thread. */
  @GetMapping("/importWarlock")
  public ResponseEntity<Object> importWarlock() {
    List<WarlockSection> scanWarlockDir = scanWarlockDir();

    new Thread(() -> scanWarlockDir.forEach(Local1000Controller.this::importWarlockSection)).start();

    return ResponseEntity.ok().body(scanWarlockDir);
  }

  private Flow1000Section storeFlow1000Section(WarlockSection warlockSection) {
    Optional<Flow1000Section> flow1000SectionOption = local1000SectionDao.searchFlow1000SectionByNameAndAlbum(
          warlockSection.getSectionName(),
          "1808");
    if (flow1000SectionOption.isEmpty()) {
      Flow1000Section flow1000Section = new Flow1000Section();
      flow1000Section.setAlbum("1808");
      flow1000Section.setDirName(warlockSection.getSectionName());
      flow1000Section.setName(warlockSection.getSectionName());
      flow1000Section.setCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
      return local1000SectionDao.saveAndFlush(flow1000Section);
    } else {
      return flow1000SectionOption.get();
    }
  }

  private File copyWarlockImageFile(WarlockImage image, WarlockSection warlockSection) {

    File destWarlockImageFile = Paths.get(baseDir, "1808", warlockSection.getSectionName(), image.getName()).toFile();
    File srcWarlockImageFile = image.getFile();
    try {
      if (destWarlockImageFile.createNewFile()) {
        FileCopyUtils.copy(srcWarlockImageFile, destWarlockImageFile);
        LOG.info("file copy from {} to {} ", srcWarlockImageFile, destWarlockImageFile);
      } else {
        LOG.info("file {} already exists", destWarlockImageFile);
      }
    } catch (Exception e) {
      LOG.error("file copy from {} to {} failed", srcWarlockImageFile, destWarlockImageFile, e);
    }
    return destWarlockImageFile;
  }

  private void storeWarlockImage(WarlockImage image, WarlockSection warlockSection, Flow1000Section flow1000Section) {
    File destWarlockImageFile = copyWarlockImageFile(image, warlockSection);

    Flow1000Img flow1000Img;
    Optional<Flow1000Img> flow1000Optional = local1000ImgDao.searchFlow1000ImgByNameAndFlow1000Section(
        image.getName(),
        flow1000Section);
    if (!flow1000Optional.isPresent()) {
      flow1000Img = new Flow1000Img();
      flow1000Img.setName(image.getName());
      flow1000Img.setFlow1000Section(flow1000Section);
    } else {
      flow1000Img = flow1000Optional.get();
    }
    try {
      if (destWarlockImageFile.getAbsolutePath().endsWith(WEBP_SUFFIX)) {
        InputStream fileInputStream = new FileInputStream(destWarlockImageFile);
        WebpImageSize webpImageSize = WebpUtil.parseWebpImage(fileInputStream);
        fileInputStream.close();
        flow1000Img.setHeight(webpImageSize.height);
        flow1000Img.setWidth(webpImageSize.width);
      } else if (destWarlockImageFile.getAbsolutePath().endsWith(".avif")) {
        ImgSize imgSize = AvifUtil.parseImgSize(destWarlockImageFile);
        flow1000Img.setHeight(imgSize.getHeight());
        flow1000Img.setWidth(imgSize.getWidth());
      } else {
        BufferedImage sourceImg = ImageIO.read(Files.newInputStream(Path.of(destWarlockImageFile.getAbsolutePath())));
        int width = sourceImg.getWidth();
        int height = sourceImg.getHeight();
        flow1000Img.setHeight(height);
        flow1000Img.setWidth(width);
      }
    } catch (Exception e) {
      // empty
    }

    local1000ImgDao.saveAndFlush(flow1000Img);
    if (warlockSection.getImageList().indexOf(image) == 0) {
      flow1000Section.setCover(flow1000Img.getName());
      flow1000Section.setCoverHeight(flow1000Img.getHeight());
      flow1000Section.setCoverWidth(flow1000Img.getWidth());
      local1000SectionDao.saveAndFlush(flow1000Section);
    }
  }

  private void importWarlockSection(WarlockSection warlockSection) {
    File warlockSectionFile = Paths.get(baseDir, "1808", warlockSection.getSectionName()).toFile();

    boolean ret = warlockSectionFile.mkdir();
    if (!ret) {
      LOG.error("create section path failed {}", warlockSectionFile.getAbsolutePath());
      return;
    }
    Flow1000Section flow1000Section = storeFlow1000Section(warlockSection);
    for (WarlockImage image : warlockSection.getImageList()) {
      storeWarlockImage(image, warlockSection, flow1000Section);
    }
  }

  /** Scans all configured album directories and parses their sections in a background thread. */
  @RequestMapping("/initv2")
  public ResponseEntity<Object> initV2() {
    final List<AlbumConfig> albumConfigs = local1000AlbumConfigDao.findAll();

    new Thread(() -> {
      Map<String, List<Map<String, List<String>>>> albumConfigRest = new HashMap<>();
      albumConfigs.forEach(albumConfig -> {
        List<Map<String, List<String>>> resp = scanLocal1000AlbumDir(albumConfig);
        albumConfigRest.put(albumConfig.getName(), resp);
      });
    }).start();

    return ResponseEntity.ok().body(albumConfigs);
  }

  /** Re-parses and refreshes the section with the given id from its source directory. */
  @GetMapping("/refreshSectionById")
  public ResponseEntity<Object> refreshSectionById(@RequestParam long id) {
    Flow1000Section flow1000Section = local1000SectionDao.queryFlow1000SectionById(id);
    Optional<AlbumConfig> albumConfigOpt = local1000AlbumConfigDao.searchAlbumConfigByName(flow1000Section.getAlbum());
    if (albumConfigOpt.isEmpty()) {
      return ResponseEntity.internalServerError().body("cannot find album:" + flow1000Section.getAlbum());
    }
    AlbumConfig albumConfig = albumConfigOpt.get();
    File section = Paths.get(baseDir, albumConfig.getSourcePath(), flow1000Section.getDirName()).toFile();
    parseSection(section, albumConfig);

    return ResponseEntity.ok().build();
  }


  private static long getFileTimeStampe(File file) {
    try {
      Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(file.getName().substring(0, 14));
      return date.getTime();
    } catch (Exception e) {
      return file.lastModified();
    }
  }


  private void parseSection(File section, AlbumConfig albumConfig) {
    LOG.info(section.getName());
    Map<String, List<String>> sectionItem = new HashMap<>();
    sectionItem.put(section.getName(), new ArrayList<>());
    Optional<Flow1000Section> flow1000SectionOption 
        = local1000SectionDao.searchFlow1000SectionByNameAndAlbum(section.getName(), albumConfig.getName());
    Flow1000Section flow1000Section = WarlockParser.buildFlow1000Section(flow1000SectionOption, albumConfig, section);
    if (!flow1000SectionOption.isPresent()) {
      flow1000Section = local1000SectionDao.saveAndFlush(flow1000Section);
    }

    File[] images = section.listFiles();
    List<File> imagesList = Arrays.stream(images)
      .filter(WarlockParser::isImageFile)
      .sorted(WarlockParser::fileNameComparator).toList();
    for (File image : imagesList) {
      LOG.info(image.getName());
      sectionItem.get(section.getName()).add(image.getName());
      Optional<Flow1000Img> flow1000Optional = local1000ImgDao.searchFlow1000ImgByNameAndFlow1000Section(
          albumConfig.isEncrypted() ? image.getName() + ".bin" : image.getName(),
          flow1000Section);
      Flow1000Img flow1000Img = WarlockParser.buildFlow1000Img(flow1000Optional, flow1000Section, albumConfig, image);
      try {
        if (image.getAbsolutePath().endsWith(WEBP_SUFFIX)) {
          InputStream fileInputStream = new FileInputStream(image.getAbsolutePath());
          WebpImageSize webpImageSize = WebpUtil.parseWebpImage(fileInputStream);
          fileInputStream.close();
          flow1000Img.setHeight(webpImageSize.height);
          flow1000Img.setWidth(webpImageSize.width);
        } else if (image.getAbsolutePath().endsWith(".avif")) {
          ImgSize imgSize = AvifUtil.parseImgSize(image);
          flow1000Img.setHeight(imgSize.getHeight());
          flow1000Img.setWidth(imgSize.getWidth());
        } else {
          BufferedImage sourceImg = ImageIO.read(Files.newInputStream(Path.of(image.getAbsolutePath())));
          int width = sourceImg.getWidth();
          int height = sourceImg.getHeight();
          flow1000Img.setHeight(height);
          flow1000Img.setWidth(width);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      local1000ImgDao.saveAndFlush(flow1000Img);
      if (imagesList.indexOf(image) == 0) {
        flow1000Section.setCover(flow1000Img.getName());
        flow1000Section.setCoverHeight(flow1000Img.getHeight());
        flow1000Section.setCoverWidth(flow1000Img.getWidth());
        local1000SectionDao.saveAndFlush(flow1000Section);
      }
    }
  }

  private List<Map<String, List<String>>> scanLocal1000AlbumDir(AlbumConfig albumConfig) {
    File basePath = Paths.get(baseDir, albumConfig.getSourcePath()).toFile();
    File[] sections = basePath.listFiles();
    List<Map<String, List<String>>> resp = new ArrayList<>();

    List<File> sectionList = Arrays.stream(sections).sorted((file1, file2) -> 
      getFileTimeStampe(file1) - getFileTimeStampe(file2) < 0 ? -1 : 1
    ).toList();
    for (File section : sectionList) {
      parseSection(section, albumConfig);
    }
    return resp;
  }

  private List<WarlockSection> scanWarlockDir() {
    String pathName = warlockDir;
    File basePath = new File(pathName);
    File[] sections = basePath.listFiles();

    List<File> sectionList = Arrays.stream(sections)
        .filter(File::isDirectory)
        .toList();
    Map<String, WarlockSection> realNameMap = new HashMap<>();
    for (File section : sectionList) {
      LOG.info("section name:{}", section.getAbsolutePath());
      String dirName = section.getName();
      String realName = parseWarlockRealName(dirName);
      realNameMap.putIfAbsent(realName, new WarlockSection(realName));
      realNameMap.get(realName).addWarlockImages(parseWarlockImageList(section));
    }
    return realNameMap.values()
        .stream()
        .map(section -> {
          section.setImageList(
            section.getImageList().stream().sorted(
              (f1, f2) -> f1.getName().compareTo(f2.getName())).toList()
          );
          return section;
        })
        .sorted((f1, f2) -> f1.getSectionName().compareTo(f2.getSectionName()))
        .toList();
  }

  private static String parseWarlockRealName(String dirName) {
    int lastIndex = dirName.lastIndexOf("-");
    if (lastIndex == -1) {
      return dirName;
    }
    return dirName.substring(0, lastIndex);
  }

  private static List<WarlockImage> parseWarlockImageList(File section) {
    return Arrays.stream(section.listFiles())
        .filter(f -> f.isFile() && (f.getName().endsWith(".jpg") || f.getName().endsWith(".png")
            || f.getName().endsWith(WEBP_SUFFIX) || f.getName().endsWith("avif")))
        .map(f -> {
          String originName = f.getName();
          int lastIndex = originName.lastIndexOf(".");
          String suffix = originName.substring(lastIndex);
          String pureName = originName.substring(0, lastIndex);
          String newFileName;
          try {
            int imgIndex = Integer.parseInt(pureName);
            String newPureName = String.format("%03d", imgIndex);
            newFileName = newPureName + suffix;
          } catch (NumberFormatException e) {
            newFileName = f.getName();
          }
          return new WarlockImage(newFileName, f);
        })
        .sorted((i1, i2) -> i1.getName().compareTo(i2.getName()))
        .toList();
  }

  /** Returns section detail (id, title, images with dimensions, album, client status) for the given section id. */
  @RequestMapping("/picDetailAjax")
  public SectionDetail picDetailAjax(@RequestParam(value = "id", defaultValue = "1") Long id) {
    LOG.info("handle /picDetailAjax, id={}", id);
    Flow1000Section flow1000Section = local1000SectionDao.queryFlow1000SectionById(id);
    if (flow1000Section == null) {
      return new SectionDetail();
    }
    List<ImgDetail> imgDetailList = flow1000Section.getImages().stream()
        .map(image -> new ImgDetail(image.getId(), image.getName(), image.getWidth(), image.getHeight()))
        .toList();

    return SectionDetail.SectionDetailBuilder.instance()
            .id(flow1000Section.getId())
            .dirName(flow1000Section.getDirName())
            .picPage(flow1000Section.getId())
            .pics(imgDetailList)
            .album(flow1000Section.getAlbum())
            .title(flow1000Section.getName())
            .mtime(flow1000Section.getCreateTime())
            .clientStatus(flow1000Section.getClientStatus().name()).build();
  }

  /** Returns the list of image names within a section for the given section id. */
  @RequestMapping("/picContentAjax")
  public SectionContent picContentAjax(@RequestParam(value = "id", defaultValue = "1") Long id) {
    LOG.info("handle /picContentAjax, id={}", id);
    Flow1000Section flow1000Section = local1000SectionDao.queryFlow1000SectionById(id);

    List<String> imgList = flow1000Section.getImages().stream().map(Flow1000Img::getName)
        .toList();
    return new SectionContent(flow1000Section.getDirName(), flow1000Section.getId().intValue(), imgList);
  }

  /** Searches sections by name using a LIKE query. Returns empty list if name is blank. */
  @RequestMapping("/searchSection")
  public List<Flow1000Section> searchSection(@RequestParam(value = "name", defaultValue = "") String name) {
    LOG.info("searchSection request, name={}", name);
    if ("".equals(name)) {
      return new ArrayList<>();
    }
    name = "%" + name + "%";

    return local1000SectionDao.searchFlow1000SectionByName(name);
  }

  /** Queries section indices created after the given timestamp, with optional filters for album, search key, and client status. */
  @RequestMapping("/picIndexAjax")
  public List<PicIndex> picIndexAjax(
      @RequestParam(value = "time_stamp", defaultValue = "19700101000000") String timeStamp,
      @RequestParam(value = "searchKey", required = false) String searchKey,
      @RequestParam(value = "client_status", required = false) String clientStatus,
      @RequestParam(value = "album", required = false) String album) {
    LOG.info("handle /picIndexAjax, timeStamp={}", timeStamp);
    Flow1000Section.ClientStatus clientStatusCondition = null;
    if (clientStatus != null && !clientStatus.isEmpty()) {
      try {
        clientStatusCondition = Flow1000Section.ClientStatus.valueOf(clientStatus);
      } catch (IllegalArgumentException ignored) {
        // empty
      }
    }

    Flow1000Section.ClientStatus finalClientStatusCondition = clientStatusCondition;
    List<Flow1000Section> flow1000SectionList = local1000SectionDao.findAll(
        (Specification<Flow1000Section>) (root, query, builder) -> {
          List<Predicate> predicates = new ArrayList<>();
          if (album != null && !album.isEmpty()) {
            Predicate albumPredicate = builder.equal(root.get("album"), album);
            predicates.add(albumPredicate);
          }

          if (searchKey != null && !searchKey.isEmpty()) {
            String name = "%" + searchKey + "%";
            Predicate albumPredicate = builder.like(root.get("name"), name);
            predicates.add(albumPredicate);
          }
          if (finalClientStatusCondition != null) {
            Predicate albumPredicate = builder.equal(root.get("clientStatus"), finalClientStatusCondition);
            predicates.add(albumPredicate);
          }
          Predicate createTimePredicate = builder.greaterThan(root.get("createTime"), timeStamp);
          predicates.add(createTimePredicate);
          return builder.and(predicates.toArray(new Predicate[] {}));
        });

    return flow1000SectionList.stream().map(flow1000Section -> new PicIndex(
        flow1000Section.getId(),
        flow1000Section.getDirName(),
        flow1000Section.getCreateTime(),
        flow1000Section.getCover(),
        flow1000Section.getCoverWidth(),
        flow1000Section.getCoverHeight(),
        flow1000Section.getAlbum(),
        flow1000Section.getName(),
        PicIndex.ClientStatus.valueOf(flow1000Section.getClientStatus().name()))).toList();
  }

  /** Downloads images from the given URLs, saves them to disk, and notifies clients via WebSocket when complete. */
  @PostMapping(value = "/urls1000")
  public void urls1000(@RequestBody Urls1000Body urls1000Body) {
    LOG.info("handle /urls1000, body={}", urls1000Body);
    String timeStamp = ((TimeUtil) applicationContext.getBean("timeUtil")).timeStamp();
    FileUtil fileUtil = (FileUtil) applicationContext.getBean("fileUtil");
    String dirName = timeStamp + urls1000Body.getTitle();
    String absPath = "/home/knightingal/download/linux1000/source/" + dirName;
    File dirFile = new File(absPath);
    if (!dirFile.mkdirs()) {
      return;
    }

    Flow1000Section flow1000Section = new Flow1000Section();
    flow1000Section.setName(urls1000Body.getTitle());
    flow1000Section.setDirName(dirName);
    flow1000Section.setCreateTime(timeStamp);
    flow1000Section.setCover(
        fileUtil.getFileNameByUrl(urls1000Body.getImgSrcArray().get(0).getSrc()));
    flow1000Section.setAlbum("flow1000");
    List<Flow1000Img> flow1000ImgList = new ArrayList<>();
    for (Urls1000Body.ImgSrcBean imgSrcBean : urls1000Body.getImgSrcArray()) {
      String fileName = fileUtil.getFileNameByUrl(imgSrcBean.getSrc());
      Flow1000Img flow1000Img = new Flow1000Img();
      flow1000Img.setName(fileName);
      flow1000Img.setInCover(
          urls1000Body.getImgSrcArray().lastIndexOf(imgSrcBean) == 0 ? 1 : 0);
      flow1000Img.setSrc(imgSrcBean.getSrc());
      flow1000Img.setHref(imgSrcBean.getRef());
      flow1000ImgList.add(flow1000Img);
    }
    downloadSectionThreadPoolExecutor.execute(() -> {
      CountDownLatch countDownLatch = new CountDownLatch(flow1000ImgList.size());

      for (Flow1000Img flow1000Img : flow1000ImgList) {
        downloadImgThreadPoolExecutor.execute(new DownloadImgRunnable(
            flow1000Img, dirName, countDownLatch, baseDir));
      }
      try {
        countDownLatch.await();
      } catch (InterruptedException e) {
        LOG.error("Interrupted while waiting for downloads to complete", e);
        Thread.currentThread().interrupt();
      }
      LOG.info("{} download complete", flow1000Section.getDirName());
      /*
       * we used called 'local1000SectionDao.insertFlow1000Section(flow1000Section);'
       * here
       */
      flow1000ImgList.forEach(flow1000Img -> {
      });
      /*
       * we used called 'local1000SectionDao.insertFlow1000Img(flow1000ImgList);' here
       */

      PicIndex picIndex = new PicIndex(
          flow1000Section.getId(),
          flow1000Section.getDirName(),
          flow1000Section.getCreateTime(),
          flow1000Section.getCover(),
          flow1000Section.getCoverWidth(),
          flow1000Section.getCoverHeight(),
          flow1000Section.getAlbum(),
          flow1000Section.getName(),
          PicIndex.ClientStatus.valueOf(flow1000Section.getClientStatus().name()));

      ObjectMapper mapper = new ObjectMapper();
      try {
        wsMsgService.sendWsMsg(mapper.writeValueAsString(picIndex));
      } catch (Exception ignored) {
        // empty
      }
    });
  }


  private String[] listImages(String dirName) {
    File dirFile = Paths.get(baseDir, "source", dirName).toFile();
    return dirFile.list((dir, fileName) -> fileName != null && (fileName.endsWith(".jpg")
        || fileName.endsWith(".JPG")
        || fileName.endsWith(".jpeg")
        || fileName.endsWith(".JPEG")
        || fileName.endsWith(".png")
        || fileName.endsWith(".PNG")));
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
      BufferedImage sourceImg = ImageIO.read(Files.newInputStream(
          Paths.get(baseDir + "/source" + "/" + flow1000Section.getDirName() + "/" + imgName)));
      int width = sourceImg.getWidth();
      int height = sourceImg.getHeight();
      Flow1000Img flow1000Img = new Flow1000Img();
      flow1000Img.setName(imgName);
      flow1000Img.setHeight(height);
      flow1000Img.setWidth(width);
      flow1000Img.setFlow1000Section(flow1000Section);
      return flow1000Img;
    } catch (IOException e) {
      LOG.error("Error reading image file: {}", imgName, e);
    }
    return null;
  }

  /** Marks the section with the given id as PENDING for download and returns the updated index. */
  @PostMapping("/downloadSection")
  public ResponseEntity<PicIndex> downloadSection(@RequestParam(value = "id", defaultValue = "1") Long id) {
    Flow1000Section flow1000Section = local1000SectionDao.queryFlow1000SectionById(id);
    if (flow1000Section == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    flow1000Section.setClientStatus(Flow1000Section.ClientStatus.PENDING);
    local1000SectionDao.saveAndFlush(flow1000Section);
    PicIndex picIndex = new PicIndex(
        flow1000Section.getId(),
        flow1000Section.getDirName(),
        flow1000Section.getCreateTime(),
        flow1000Section.getCover(),
        flow1000Section.getCoverWidth(),
        flow1000Section.getCoverHeight(),
        flow1000Section.getAlbum(),
        flow1000Section.getName(),
        PicIndex.ClientStatus.valueOf(flow1000Section.getClientStatus().name()));
    return ResponseEntity.ok(picIndex);
  }

  /** Marks the section with the given id as LOCAL (downloaded) and returns the updated index. */
  @PostMapping("/completeSection")
  public ResponseEntity<PicIndex> completeSection(@RequestParam(value = "id", defaultValue = "1") Long id) {
    Flow1000Section flow1000Section = local1000SectionDao.queryFlow1000SectionById(id);
    if (flow1000Section == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    flow1000Section.setClientStatus(Flow1000Section.ClientStatus.LOCAL);
    local1000SectionDao.saveAndFlush(flow1000Section);
    PicIndex picIndex = new PicIndex(
        flow1000Section.getId(),
        flow1000Section.getDirName(),
        flow1000Section.getCreateTime(),
        flow1000Section.getCover(),
        flow1000Section.getCoverWidth(),
        flow1000Section.getCoverHeight(),
        flow1000Section.getAlbum(),
        flow1000Section.getName(),
        PicIndex.ClientStatus.valueOf(flow1000Section.getClientStatus().name()));
    return ResponseEntity.ok(picIndex);
  }

  /** Resets the section's client status to NONE (unsubscribed) and returns the updated index. */
  @PostMapping("/unsubscribeSection/{id}")
  public ResponseEntity<PicIndex> unsubscribeSection(@PathVariable("id") Long id) {
    Flow1000Section flow1000Section = local1000SectionDao.queryFlow1000SectionById(id);
    if (flow1000Section == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    flow1000Section.setClientStatus(Flow1000Section.ClientStatus.NONE);
    local1000SectionDao.saveAndFlush(flow1000Section);
    PicIndex picIndex = new PicIndex(
        flow1000Section.getId(),
        flow1000Section.getDirName(),
        flow1000Section.getCreateTime(),
        flow1000Section.getCover(),
        flow1000Section.getCoverWidth(),
        flow1000Section.getCoverHeight(),
        flow1000Section.getAlbum(),
        flow1000Section.getName(),
        PicIndex.ClientStatus.valueOf(flow1000Section.getClientStatus().name()));
    return ResponseEntity.ok(picIndex);
  }


  /** Deletes the section and its images from both disk and the database. */
  @DeleteMapping(value = "/section/{id}")
  public ResponseEntity<Void> deleteSection(@PathVariable("id") Long id) {
    LOG.info("handle /deleteSection, sectionid={}", id);
    Flow1000Section flow1000Section = local1000SectionDao.queryFlow1000SectionById(id);
    if (flow1000Section == null) {
      return ResponseEntity.notFound().build();
    }

    Optional<AlbumConfig> albumConfigOpt = local1000AlbumConfigDao.searchAlbumConfigByName(flow1000Section.getAlbum());
    if (albumConfigOpt.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    AlbumConfig albumConfig = albumConfigOpt.get();
    File warlockSectionFile = Paths.get(baseDir, albumConfig.getSourcePath(), flow1000Section.getDirName()).toFile();
    File[] listFiles = warlockSectionFile.listFiles();
    try {
      if (listFiles != null) {
        for (File file : listFiles) {
          Files.delete(file.toPath());
        }
      }
      Files.delete(warlockSectionFile.toPath());
      local1000ImgDao.deleteById(id);
      local1000SectionDao.deleteById(id);
      return ResponseEntity.ok(null);

    } catch (Exception e) {
      LOG.error("delete {} failed", warlockSectionFile.getAbsolutePath(), e);
      return ResponseEntity.internalServerError().build();
    }
  }

  /** Returns all album configurations. */
  @GetMapping("/albumConfig/list")
  public List<AlbumConfig> albumConfigList() {
    return local1000AlbumConfigDao.findAll();
  }

  /** Strips the date-time prefix from section names that match the configured pattern. */
  @GetMapping("/updateTitle")
  @Transactional
  public void updateTitle() {
    List<Flow1000Section> flow1000SectionList = local1000SectionDao
        .findAll((Specification<Flow1000Section>) (root, query, builder) -> {
          List<Predicate> predicates = new ArrayList<>();
          return builder.and(predicates.toArray(new Predicate[] {}));
        });
    for (Flow1000Section section : flow1000SectionList) {
      String name = section.getName();
      String prefix = name.substring(0, AppConfiguration.PATTERN.length());
      try {
        timeUtil.parse(prefix);
        section.setName(name.substring(AppConfiguration.PATTERN.length()));
      } catch (ParseException e) {
        // empty
      }

    }
    local1000SectionRepo.saveAllAndFlush(flow1000SectionList);
  }

}
