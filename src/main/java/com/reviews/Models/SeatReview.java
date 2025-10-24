package com.reviews.Models;

public class SeatReview implements ReviewRecord {
    // Core interface fields
    private String airlineName;
    private String date;
    private double overallRating;
    private String country;
    private String content;
    
    // Additional CSV columns
    private String link;
    private String title;
    private String author;
    private String aircraft;
    private String seatLayout;
    private String dateFlown;
    private String cabinFlown;
    private String typeTraveller;
    private double seatLegroomRating;
    private double seatReclineRating;
    private double seatWidthRating;
    private double aisleSpaceRating;
    private double viewingTvRating;
    private double powerSupplyRating;
    private double seatStorageRating;
    private int recommended;
    
    public SeatReview(String airlineName, String link, String title, String author, String country,
                     String date, String content, String aircraft, String seatLayout, String dateFlown,
                     String cabinFlown, String typeTraveller, double overallRating, double seatLegroomRating,
                     double seatReclineRating, double seatWidthRating, double aisleSpaceRating,
                     double viewingTvRating, double powerSupplyRating, double seatStorageRating,
                     int recommended) {
        this.airlineName = airlineName;
        this.link = link;
        this.title = title;
        this.author = author;
        this.country = country;
        this.date = date;
        this.content = content;
        this.aircraft = aircraft;
        this.seatLayout = seatLayout;
        this.dateFlown = dateFlown;
        this.cabinFlown = cabinFlown;
        this.typeTraveller = typeTraveller;
        this.overallRating = overallRating;
        this.seatLegroomRating = seatLegroomRating;
        this.seatReclineRating = seatReclineRating;
        this.seatWidthRating = seatWidthRating;
        this.aisleSpaceRating = aisleSpaceRating;
        this.viewingTvRating = viewingTvRating;
        this.powerSupplyRating = powerSupplyRating;
        this.seatStorageRating = seatStorageRating;
        this.recommended = recommended;
    }
    
    // Interface methods
    @Override
    public String getName() {
        return airlineName;
    }
    
    @Override
    public String getDate() {
        return date;
    }
    
    @Override
    public double getOverallRating() {
        return overallRating;
    }
    
    @Override
    public String getCountry() {
        return country;
    }
    
    @Override
    public String getContent() {
        return content;
    }
    
    // Additional getters
    public String getLink() { return link; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getAircraft() { return aircraft; }
    public String getSeatLayout() { return seatLayout; }
    public String getDateFlown() { return dateFlown; }
    public String getCabinFlown() { return cabinFlown; }
    public String getTypeTraveller() { return typeTraveller; }
    public double getSeatLegroomRating() { return seatLegroomRating; }
    public double getSeatReclineRating() { return seatReclineRating; }
    public double getSeatWidthRating() { return seatWidthRating; }
    public double getAisleSpaceRating() { return aisleSpaceRating; }
    public double getViewingTvRating() { return viewingTvRating; }
    public double getPowerSupplyRating() { return powerSupplyRating; }
    public double getSeatStorageRating() { return seatStorageRating; }
    public int getRecommended() { return recommended; }

    @Override
    public String getAirline() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAirline'");
    }
}
