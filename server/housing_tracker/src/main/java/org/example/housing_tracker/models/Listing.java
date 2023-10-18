package org.example.housing_tracker.models;

import java.util.Objects;

public class Listing {

    private int listingId;
    private String link;
    private int locationId;
    private int cost;
    private int numBeds;
    private int numBaths;
    private int appUserId;
    private boolean petFriendly;
    private String laundryAvailability;
    private String parking;
    private boolean gym;

    public Listing() {
    }

    public Listing(int listingId, String link, int locationId, int cost, int numBeds, int numBaths, int appUserId, boolean petFriendly, String laundryAvailability, String parking, boolean gym) {
        this.listingId = listingId;
        this.link = link;
        this.locationId = locationId;
        this.cost = cost;
        this.numBeds = numBeds;
        this.numBaths = numBaths;
        this.appUserId = appUserId;
        this.petFriendly = petFriendly;
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

    public boolean isPetFriendly() {
        return petFriendly;
    }

    public void setPetFriendly(boolean petFriendly) {
        this.petFriendly = petFriendly;
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

    public boolean hasGym() {
        return gym;
    }

    public void setGym(boolean gym) {
        this.gym = gym;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Listing that = (Listing) o;
        return listingId == that.listingId && link.equals(that.link) && locationId == that.locationId && appUserId == that.appUserId && cost == that.cost && numBeds == that.numBeds && numBaths == that.numBaths && petFriendly == that.petFriendly && laundryAvailability.equals(that.laundryAvailability) && parking.equals(that.parking) && gym == that.gym;
    }

    @Override
    public int hashCode() {
        return Objects.hash(listingId, listingId, appUserId, cost, numBeds, numBaths, petFriendly, laundryAvailability, parking, gym);
    }
}
