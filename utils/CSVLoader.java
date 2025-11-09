package utils;

import models.AirlineReview;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CSVLoader - Utility class to load airline reviews from CSV file.
 */
public class CSVLoader {
    
    /**
     * Load airline reviews from the CSV file.
     * 
     * @param csvPath Path to the airline.csv file
     * @return List of AirlineReview objects
     */
    public static List<AirlineReview> loadAirlineReviews(String csvPath) {
        List<AirlineReview> reviews = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            // Skip header line
            String header = br.readLine();
            if (header == null) {
                System.err.println("CSV file is empty or missing header");
                return reviews;
            }
            
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                try {
                    AirlineReview review = parseCSVLine(line);
                    if (review != null) {
                        reviews.add(review);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + e.getMessage());
                    // Continue processing other lines
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
        }
        
        return reviews;
    }
    
    /**
     * Parse a single CSV line into an AirlineReview object.
     * CSV format: "airline_name","link","title","author","author_country","date","content","aircraft","type_traveller","cabin_flown","route","overall_rating","seat_comfort_rating","cabin_staff_rating","food_beverages_rating","inflight_entertainment_rating","ground_service_rating","wifi_connectivity_rating","value_money_rating","recommended"
     */
    private static AirlineReview parseCSVLine(String line) {
        // Parse CSV line with quoted fields
        List<String> fields = parseCSVFields(line);
        
        if (fields.size() < 20) {
            System.err.println("Invalid CSV line - expected 20 fields, got " + fields.size());
            return null;
        }
        
        try {
            String airlineName = fields.get(0);
            String link = fields.get(1);
            String title = fields.get(2);
            String author = fields.get(3);
            String authorCountry = fields.get(4);
            String date = fields.get(5);
            String content = fields.get(6);
            String aircraft = fields.get(7);
            String typeTraveller = fields.get(8);
            String cabinFlown = fields.get(9);
            String route = fields.get(10);
            
            // Parse ratings (handle empty strings)
            double overallRating = parseDouble(fields.get(11), 0.0);
            double seatComfortRating = parseDouble(fields.get(12), 0.0);
            double cabinStaffRating = parseDouble(fields.get(13), 0.0);
            double foodBeveragesRating = parseDouble(fields.get(14), 0.0);
            double inflightEntertainmentRating = parseDouble(fields.get(15), 0.0);
            double groundServiceRating = parseDouble(fields.get(16), 0.0);
            double wifiConnectivityRating = parseDouble(fields.get(17), 0.0);
            double valueMoneyRating = parseDouble(fields.get(18), 0.0);
            int recommended = parseInt(fields.get(19), 0);
            
            return new AirlineReview(
                airlineName, link, title, author, authorCountry, date, content,
                aircraft, typeTraveller, cabinFlown, route,
                overallRating, seatComfortRating, cabinStaffRating,
                foodBeveragesRating, inflightEntertainmentRating,
                groundServiceRating, wifiConnectivityRating,
                valueMoneyRating, recommended
            );
        } catch (Exception e) {
            System.err.println("Error creating AirlineReview: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Parse CSV fields handling quoted strings.
     */
    private static List<String> parseCSVFields(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString().trim());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        // Add last field
        fields.add(currentField.toString().trim());
        
        return fields;
    }
    
    private static double parseDouble(String str, double defaultValue) {
        if (str == null || str.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    private static int parseInt(String str, int defaultValue) {
        if (str == null || str.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}

