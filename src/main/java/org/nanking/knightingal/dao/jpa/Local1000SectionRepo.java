package org.nanking.knightingal.dao.jpa;

import java.util.List;
import java.util.Optional;

import org.nanking.knightingal.bean.Flow1000Section;
import org.nanking.knightingal.dao.Local1000SectionDao;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

public interface Local1000SectionRepo extends JpaRepository<Flow1000Section, Long>, JpaSpecificationExecutor<Flow1000Section> {

    @Query("select s from Flow1000Section s where s.id=:id")
    Flow1000Section queryFlow1000SectionById(@Param("id") Long id);

    @Query("select s from Flow1000Section s where s.createTime > :timeStamp")
    List<Flow1000Section> queryFlow1000SectionByCreateTime(@Param("timeStamp") String timeStamp);


    // @Query("select s from Flow1000Section s where s.creatTime > :timeStamp")
    // List<Flow1000Section> queryFlow1000Section(Flow1000Section flow1000Section);

    /**
     * 根据sectoinName模糊查询
     * @param name name参数，需已拼接%
     * 
     * @return 返回查询结果
     */
    @Query("select s from Flow1000Section s where s.name like :name")
    List<Flow1000Section> searchFlow1000SectionByName(@Param("name") String name);

    // void insertFlow1000Section(Flow1000Section flow1000Section);

	default List<Flow1000Section> saveEntitiesAllAndFlush(Iterable<Flow1000Section> entities) {
        return saveAllAndFlush(entities);
    }

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





}
