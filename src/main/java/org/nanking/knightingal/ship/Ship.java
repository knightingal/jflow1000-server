package org.nanking.knightingal.ship;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
public class Ship implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shipName;

    private int shipType;

    public String getPageHtmlContent() {
        return pageHtmlContent;
    }

    public void setPageHtmlContent(String pageHtmlContent) {
        this.pageHtmlContent = pageHtmlContent;
    }

    private String pageHtmlContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public int getShipType() {
        return shipType;
    }

    public void setShipType(int shipType) {
        this.shipType = shipType;
    }

    public List<ShipImgDetail> getShipImgDetails() {
        return shipImgDetails;
    }

    public void setShipImgDetails(List<ShipImgDetail> shipImgDetails) {
        this.shipImgDetails = shipImgDetails;
    }

    @OneToMany(mappedBy = "ship", cascade = CascadeType.PERSIST)
    private List<ShipImgDetail> shipImgDetails;


    public static final class ShipBuilder {
        private Long id;
        private String shipName;
        private int shipType;
        private String pageHtmlContent;
        private List<ShipImgDetail> shipImgDetails;

        public ShipBuilder() {
        }

        public ShipBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ShipBuilder shipName(String shipName) {
            this.shipName = shipName;
            return this;
        }

        public ShipBuilder shipType(int shipType) {
            this.shipType = shipType;
            return this;
        }

        public ShipBuilder shipImgDetails(List<ShipImgDetail> shipImgDetails) {
            this.shipImgDetails = shipImgDetails;
            return this;
        }

        public ShipBuilder pageHtmlContent(String pageHtmlContent) {
            this.pageHtmlContent = pageHtmlContent;
            return this;
        }

        public Ship build() {
            Ship ship = new Ship();
            ship.setId(id);
            ship.setShipName(shipName);
            ship.setShipType(shipType);
            ship.setShipImgDetails(shipImgDetails);
            ship.setPageHtmlContent(pageHtmlContent);
            return ship;
        }
    }
}
