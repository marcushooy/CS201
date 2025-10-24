package com.reviews.Models;

public class AirportLoungeReview implements ReviewRecord {
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
    private String loungeName;
    private String airport;
    private String loungeType;
    private String dateVisit;
    private String typeTraveller;
    private double comfortRating;
    private double cleanlinessRating;
    private double barBeveragesRating;
    private double cateringRating;
    private double washroomsRating;
    private double wifiConnectivityRating;
    private double staffServiceRating;
    private int recommended;
    
    public AirportLoungeReview(String airlineName, String link, String title, String author, String country,
                              String date, String content, String loungeName, String airport, String loungeType,
                              String dateVisit, String typeTraveller, double overallRating, double comfortRating,
                              double cleanlinessRating, double barBeveragesRating, double cateringRating,
                              double washroomsRating, double wifiConnectivityRating, double staffServiceRating,
                              int recommended) {
        this.airlineName = airlineName;
        this.link = link;
        this.title = title;
        this.author = author;
        this.country = country;
        this.date = date;
        this.content = content;
        this.loungeName = loungeName;
        this.airport = airport;
        this.loungeType = loungeType;
        this.dateVisit = dateVisit;
        this.typeTraveller = typeTraveller;
        this.overallRating = overallRating;
        this.comfortRating = comfortRating;
        this.cleanlinessRating = cleanlinessRating;
        this.barBeveragesRating = barBeveragesRating;
        this.cateringRating = cateringRating;
        this.washroomsRating = washroomsRating;
        this.wifiConnectivityRating = wifiConnectivityRating;
        this.staffServiceRating = staffServiceRating;
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
    public String getLoungeName() { return loungeName; }
    public String getAirport() { return airport; }
    public String getLoungeType() { return loungeType; }
    public String getDateVisit() { return dateVisit; }
    public String getTypeTraveller() { return typeTraveller; }
    public double getComfortRating() { return comfortRating; }
    public double getCleanlinessRating() { return cleanlinessRating; }
    public double getBarBeveragesRating() { return barBeveragesRating; }
    public double getCateringRating() { return cateringRating; }
    public double getWashroomsRating() { return washroomsRating; }
    public double getWifiConnectivityRating() { return wifiConnectivityRating; }
    public double getStaffServiceRating() { return staffServiceRating; }
    public int getRecommended() { return recommended; }

    @Override
    public String getAirline() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAirline'");
    }
}
