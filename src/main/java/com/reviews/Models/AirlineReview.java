package Models;

public class AirlineReview implements ReviewRecord {
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
    private String typeTraveller;
    private String cabinFlown;
    private String route;
    private double seatComfortRating;
    private double cabinStaffRating;
    private double foodBeveragesRating;
    private double inflightEntertainmentRating;
    private double groundServiceRating;
    private double wifiConnectivityRating;
    private double valueMoneyRating;
    private int recommended;
    
    public AirlineReview(String airlineName, String link, String title, String author, String country, 
                        String date, String content, String aircraft, String typeTraveller, 
                        String cabinFlown, String route, double overallRating, double seatComfortRating,
                        double cabinStaffRating, double foodBeveragesRating, double inflightEntertainmentRating,
                        double groundServiceRating, double wifiConnectivityRating, double valueMoneyRating,
                        int recommended) {
        this.airlineName = airlineName;
        this.link = link;
        this.title = title;
        this.author = author;
        this.country = country;
        this.date = date;
        this.content = content;
        this.aircraft = aircraft;
        this.typeTraveller = typeTraveller;
        this.cabinFlown = cabinFlown;
        this.route = route;
        this.overallRating = overallRating;
        this.seatComfortRating = seatComfortRating;
        this.cabinStaffRating = cabinStaffRating;
        this.foodBeveragesRating = foodBeveragesRating;
        this.inflightEntertainmentRating = inflightEntertainmentRating;
        this.groundServiceRating = groundServiceRating;
        this.wifiConnectivityRating = wifiConnectivityRating;
        this.valueMoneyRating = valueMoneyRating;
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
    public String getTypeTraveller() { return typeTraveller; }
    public String getCabinFlown() { return cabinFlown; }
    public String getRoute() { return route; }
    public double getSeatComfortRating() { return seatComfortRating; }
    public double getCabinStaffRating() { return cabinStaffRating; }
    public double getFoodBeveragesRating() { return foodBeveragesRating; }
    public double getInflightEntertainmentRating() { return inflightEntertainmentRating; }
    public double getGroundServiceRating() { return groundServiceRating; }
    public double getWifiConnectivityRating() { return wifiConnectivityRating; }
    public double getValueMoneyRating() { return valueMoneyRating; }
    public int getRecommended() { return recommended; }
}
