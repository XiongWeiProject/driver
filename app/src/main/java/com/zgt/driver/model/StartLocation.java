package com.zgt.driver.model;

public class StartLocation {


    /**
     * type : stopLocation
     * driver_id : 94
     * vehicle_id : 103
     * shipid : 214
     * shippingNoteNumber : P20051900006
     * serialNumber : 0000
     * startCountrySubdivisionCode : 110100
     * endCountrySubdivisionCode : 110100
     */

    private String type;
    private int driver_id;
    private int vehicle_id;
    private int shipid;
    private String shippingNoteNumber;
    private String serialNumber;
    private String startCountrySubdivisionCode;
    private String endCountrySubdivisionCode;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public int getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public int getShipid() {
        return shipid;
    }

    public void setShipid(int shipid) {
        this.shipid = shipid;
    }

    public String getShippingNoteNumber() {
        return shippingNoteNumber;
    }

    public void setShippingNoteNumber(String shippingNoteNumber) {
        this.shippingNoteNumber = shippingNoteNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getStartCountrySubdivisionCode() {
        return startCountrySubdivisionCode;
    }

    public void setStartCountrySubdivisionCode(String startCountrySubdivisionCode) {
        this.startCountrySubdivisionCode = startCountrySubdivisionCode;
    }

    public String getEndCountrySubdivisionCode() {
        return endCountrySubdivisionCode;
    }

    public void setEndCountrySubdivisionCode(String endCountrySubdivisionCode) {
        this.endCountrySubdivisionCode = endCountrySubdivisionCode;
    }
}
