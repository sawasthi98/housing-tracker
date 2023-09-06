package org.example.models;

public class Listings {

    private int listingId;
    private int locationId;
    private int cost;
    private int numBeds;
    private int numBaths;
    private int appUserId;
    private boolean pet_friendly;
    private String laundryAvailability;
    private String parking;
    private boolean gym;

    public Listings() {
    }

    public Listings(int listingId, int locationId, int cost, int numBeds, int numBaths, int appUserId, boolean pet_friendly, String laundryAvailability, String parking, boolean gym) {
        this.listingId = listingId;
        this.locationId = locationId;
        this.cost = cost;
        this.numBeds = numBeds;
        this.numBaths = numBaths;
        this.appUserId = appUserId;
        this.pet_friendly = pet_friendly;
        this.laundryAvailability = laundryAvailability;
        this.parking = parking;
        this.gym = gym;
    }

    public int getListingId() {
        return listingId;
    }

    public void setListingId(int listingId) {
        this.listingId = listingId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getNumBeds() {
        return numBeds;
    }

    public void setNumBeds(int numBeds) {
        this.numBeds = numBeds;
    }

    public int getNumBaths() {
        return numBaths;
    }

    public void setNumBaths(int numBaths) {
        this.numBaths = numBaths;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public boolean isPet_friendly() {
        return pet_friendly;
    }

    public void setPet_friendly(boolean pet_friendly) {
        this.pet_friendly = pet_friendly;
    }

    public String getLaundryAvailability() {
        return laundryAvailability;
    }

    public void setLaundryAvailability(String laundryAvailability) {
        this.laundryAvailability = laundryAvailability;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public boolean isGym() {
        return gym;
    }

    public void setGym(boolean gym) {
        this.gym = gym;
    }
}
