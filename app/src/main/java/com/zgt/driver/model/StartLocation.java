package com.zgt.driver.model;

public class StartLocation {

    /**
     * type : startLocation
     * shippingNoteNumber : P20052300061
     * serialNumber : 0000
     * startCountrySubdivisionCode : 110100
     * endCountrySubdivisionCode : 110100
     */

    private String type;
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
