package com.reviews.Models;

public class AirportReview implements ReviewRecord {
    // Core interface fields
    private String airportName;
    private String date;
    private double overallRating;
    private String country;
    private String content;
    
    // Additional CSV columns
    private String link;
    private String title;
    private String author;
    private String experienceAirport;
    private String dateVisit;
    private String typeTraveller;
    private double queuingRating;
    private double terminalCleanlinessRating;
    private double terminalSeatingRating;
    private double terminalSignsRating;
    private double foodBeveragesRating;
    private double airportShoppingRating;
    private double wifiConnectivityRating;
    private double airportStaffRating;
    private int recommended;
    
    public AirportReview(String airportName, String link, String title, String author, String country,
                        String date, String content, String experienceAirport, String dateVisit,
                        String typeTraveller, double overallRating, double queuingRating,
                        double terminalCleanlinessRating, double terminalSeatingRating, double terminalSignsRating,
                        double foodBeveragesRating, double airportShoppingRating, double wifiConnectivityRating,
                        double airportStaffRating, int recommended) {
        this.airportName = airportName;
        this.link = link;
        this.title = title;
        this.author = author;
        this.country = country;
        this.date = date;
        this.content = content;
        this.experienceAirport = experienceAirport;
        this.dateVisit = dateVisit;
        this.typeTraveller = typeTraveller;
        this.overallRating = overallRating;
        this.queuingRating = queuingRating;
        this.terminalCleanlinessRating = terminalCleanlinessRating;
        this.terminalSeatingRating = terminalSeatingRating;
        this.terminalSignsRating = terminalSignsRating;
        this.foodBeveragesRating = foodBeveragesRating;
        this.airportShoppingRating = airportShoppingRating;
        this.wifiConnectivityRating = wifiConnectivityRating;
        this.airportStaffRating = airportStaffRating;
        this.recommended = recommended;
    }
    
    // Interface methods
    @Override
    public String getName() {
        return airportName;
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
    public String getExperienceAirport() { return experienceAirport; }
    public String getDateVisit() { return dateVisit; }
    public String getTypeTraveller() { return typeTraveller; }
    public double getQueuingRating() { return queuingRating; }
    public double getTerminalCleanlinessRating() { return terminalCleanlinessRating; }
    public double getTerminalSeatingRating() { return terminalSeatingRating; }
    public double getTerminalSignsRating() { return terminalSignsRating; }
    public double getFoodBeveragesRating() { return foodBeveragesRating; }
    public double getAirportShoppingRating() { return airportShoppingRating; }
    public double getWifiConnectivityRating() { return wifiConnectivityRating; }
    public double getAirportStaffRating() { return airportStaffRating; }
    public int getRecommended() { return recommended; }

    @Override
    public String getAirline() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAirline'");
    }
}
