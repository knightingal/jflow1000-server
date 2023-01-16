package org.nanking.knightingal.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

public class Flow1000SectionSpecification<Flow1000Section> implements Specification<Flow1000Section>{

    public String album = null;

    public String timeStamp = "19700101000000";

    @Override
    @Nullable
    public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder builder) {
        // TODO Auto-generated method stub
        List<Predicate> predicates = new ArrayList<>();
        if (album != null && album.length() != 0) {
            Predicate albumPredicate = builder.equal(root.get("album"), album);
            predicates.add(albumPredicate);
        }

        Predicate createTimePredicate = builder.greaterThan(root.get("createTime"), timeStamp);
        predicates.add(createTimePredicate);
        return builder.and(predicates.toArray(new Predicate[] {}));
    }
    
}
