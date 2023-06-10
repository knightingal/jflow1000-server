package org.nanking.knightingal.dao;

import org.nanking.knightingal.annotation.Repo;
import org.nanking.knightingal.bean.Flow1000Section;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * @author Knightingal
 */
@Repo("local1000SectionRepo")
public interface Local1000SectionDao {
    Flow1000Section queryFlow1000SectionById(Long id);

    List<Flow1000Section> queryFlow1000SectionByCreateTime(String timeStamp);


    /**
     * 根据sectoinName模糊查询
     * @param name name参数，需已拼接%
     * 
     * @return 返回查询结果
     */
    List<Flow1000Section> searchFlow1000SectionByName(String name);

    // void insertFlow1000Section(Flow1000Section flow1000Section);


    /**
     * 根据id删除section记录
     * @param id section id
     */
    void deleteById(Long id);



	/**
	 * Returns a single entity matching the given {@link Specification} or {@link Optional#empty()} if none found.
	 *
	 * @param spec can be {@literal null}.
	 * @return never {@literal null}.
	 * @throws org.springframework.dao.IncorrectResultSizeDataAccessException if more than one entity found.
	 */
	Optional<Flow1000Section> findOne(@Nullable Specification<Flow1000Section> spec);

	/**
	 * Returns all entities matching the given {@link Specification}.
	 *
	 * @param spec can be {@literal null}.
	 * @return never {@literal null}.
	 */
	List<Flow1000Section> findAll(@Nullable Specification<Flow1000Section> spec);


	List<Flow1000Section> saveAllAndFlush(Iterable<Flow1000Section> entities);


}
