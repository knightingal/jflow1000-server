package org.nanking.knightingal.bean;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Knightingal
 */
@Entity
@Getter
@Setter
public class Flow1000Img implements Serializable{

    public Flow1000Img() {
    }

    public Flow1000Img(Long id, String name, int inCover, int width, int height) {
        this.id = id;
        this.name = name;
        this.inCover = inCover;
        this.width = width;
        this.height = height;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    private int inCover;

    private int width;

    private int height;

    private String src;

    private String href;

    @ManyToOne
    @JoinColumn(name = "sectionId", referencedColumnName = "id")
    @JsonIgnore
    private Flow1000Section flow1000Section;

}
