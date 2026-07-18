package org.nanking.knightingal.ship.vo;

import org.nanking.knightingal.ship.Ship;

public class ShipVo {

    private Long id;

    private String shipName;

    private int shipType;

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

    public static ShipVo fromEntity(Ship ship) {
        ShipVoBuilder builder = new ShipVoBuilder();
        return builder
                .id(ship.getId())
                .shipName(ship.getShipName())
                .shipType(ship.getShipType())
                .build();
    }


    public static final class ShipVoBuilder {
        private Long id;
        private String shipName;
        private int shipType;

        private ShipVoBuilder() {
        }

        public static ShipVoBuilder shipVo() {
            return new ShipVoBuilder();
        }

        public ShipVoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ShipVoBuilder shipName(String shipName) {
            this.shipName = shipName;
            return this;
        }

        public ShipVoBuilder shipType(int shipType) {
            this.shipType = shipType;
            return this;
        }

        public ShipVo build() {
            ShipVo shipVo = new ShipVo();
            shipVo.setId(id);
            shipVo.setShipName(shipName);
            shipVo.setShipType(shipType);
            return shipVo;
        }
    }
}
