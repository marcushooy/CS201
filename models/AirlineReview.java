/**
 * AirlineReview class to represent a single airline review from the CSV data.
 * This class encapsulates all the review information and provides methods
 * to access review data for RBAR calculations and comparisons.
 */
public class AirlineReview {
    // Core fields from CSV
    private String airlineName;
    private String link;
    private String title;
    private String author;
    private String authorCountry;
    private String date;  // Format: YYYY-MM-DD
    private String content;
    private String aircraft;
    private String typeTraveller;
    private String cabinFlown;
    private String route;
    
    // Rating fields
    private double overallRating;
    private double seatComfortRating;
    private double cabinStaffRating;
    private double foodBeveragesRating;
    private double inflightEntertainmentRating;
    private double groundServiceRating;
    private double wifiConnectivityRating;
    private double valueMoneyRating;
    private int recommended;  // 1 = yes, 0 = no
    
    /**
     * Constructor to create an AirlineReview from CSV data.
     */
    public AirlineReview(String airlineName, String link, String title, String author, 
                        String authorCountry, String date, String content, String aircraft,
                        String typeTraveller, String cabinFlown, String route,
                        double overallRating, double seatComfortRating, double cabinStaffRating,
                        double foodBeveragesRating, double inflightEntertainmentRating,
                        double groundServiceRating, double wifiConnectivityRating,
                        double valueMoneyRating, int recommended) {
        this.airlineName = airlineName;
        this.link = link;
        this.title = title;
        this.author = author;
        this.authorCountry = authorCountry;
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
    
    // Getters for core fields
    public String getAirlineName() { return airlineName; }
    public String getDate() { return date; }
    public double getOverallRating() { return overallRating; }
    public String getContent() { return content; }
    public String getAuthor() { return author; }
    public String getAuthorCountry() { return authorCountry; }
    public String getLink() { return link; }
    public String getTitle() { return title; }
    public String getAircraft() { return aircraft; }
    public String getTypeTraveller() { return typeTraveller; }
    public String getCabinFlown() { return cabinFlown; }
    public String getRoute() { return route; }
    
    // Getters for rating fields
    public double getSeatComfortRating() { return seatComfortRating; }
    public double getCabinStaffRating() { return cabinStaffRating; }
    public double getFoodBeveragesRating() { return foodBeveragesRating; }
    public double getInflightEntertainmentRating() { return inflightEntertainmentRating; }
    public double getGroundServiceRating() { return groundServiceRating; }
    public double getWifiConnectivityRating() { return wifiConnectivityRating; }
    public double getValueMoneyRating() { return valueMoneyRating; }
    public int getRecommended() { return recommended; }
    
    @Override
    public String toString() {
        return String.format("AirlineReview[%s, %s, Rating: %.1f]", 
                           airlineName, date, overallRating);
    }
}

