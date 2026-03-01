package org.nanking.knightingal.ship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;

/*
-- auto-generated definition
create table ship_img_detail
(
    id              integer not null
        constraint ship_img_detail_pk
            primary key autoincrement,
    img_url         TEXT    not null,
    img_description TEXT    not null,
    source          TEXT,
    ship_id         integer not null
);

 */
@Entity
public class ShipImgDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgUrl;

    private String imgDescription;

    private String source;

    // 0: not downloaded, 1: downloaded
    private int fileStatus;

    @ManyToOne
    @JoinColumn(name = "shipId", referencedColumnName = "id")
    @JsonIgnore
    private Ship ship;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgDescription() {
        return imgDescription;
    }

    public void setImgDescription(String imgDescription) {
        this.imgDescription = imgDescription;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public int getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(int fileStatus) {
        this.fileStatus = fileStatus;
    }

    public static final class ShipImgDetailBuilder {
        private Long id;
        private String imgUrl;
        private String imgDescription;
        private String source;
        private Ship ship;
        private int fileStatus = 0;

        public ShipImgDetailBuilder() {
        }

        public ShipImgDetailBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ShipImgDetailBuilder imgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public ShipImgDetailBuilder imgDescription(String imgDescription) {
            this.imgDescription = imgDescription;
            return this;
        }

        public ShipImgDetailBuilder source(String source) {
            this.source = source;
            return this;
        }

        public ShipImgDetailBuilder ship(Ship ship) {
            this.ship = ship;
            return this;
        }

        public ShipImgDetailBuilder fileStatus(int fileStatus) {
            this.fileStatus = fileStatus;
            return this;
        }

        public ShipImgDetail build() {
            ShipImgDetail shipImgDetail = new ShipImgDetail();
            shipImgDetail.setId(id);
            shipImgDetail.setImgUrl(imgUrl);
            shipImgDetail.setImgDescription(imgDescription);
            shipImgDetail.setSource(source);
            shipImgDetail.setShip(ship);
            shipImgDetail.setFileStatus(fileStatus);

            return shipImgDetail;
        }
    }
}
