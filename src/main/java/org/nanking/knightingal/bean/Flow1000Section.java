package org.nanking.knightingal.bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Knightingal
 */
@Entity
@Getter
@Setter
public class Flow1000Section implements Serializable{

    public Flow1000Section() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String dirName;

    private String createTime;

    private String cover;

    private String album;

    private int coverWidth;

    private int coverHeight;

    @OneToMany(mappedBy = "flow1000Section", cascade = CascadeType.PERSIST)
    private List<Flow1000Img> images;

}
