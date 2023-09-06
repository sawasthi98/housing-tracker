package org.example.housing_tracker.models;

public class Location {

    private int locationId;
    private String city;
    private String state;
    private int zipCode;

    public Location() {
    }

    public Location(int locationId, String city, String state, int zipCode) {
        this.locationId = locationId;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }
}
