package org.example.housing_tracker.models;

import java.util.Objects;

public class Location {

    private int locationId;
    private String city;
    private String state;
    private int zipCode;
    private int appUserId;

    public Location() {
    }

    public Location(int locationId, String city, String state, int zipCode, int appUserId) {
        this.locationId = locationId;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.appUserId = appUserId;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location that = (Location) o;
        return locationId == that.locationId && city.equals(that.city) && state.equals(that.state) && zipCode == that.zipCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, city, state, zipCode);
    }
}
