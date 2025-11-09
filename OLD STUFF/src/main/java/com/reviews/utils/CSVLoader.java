package com.reviews.utils;

import com.reviews.Models.AirlineReview;
import com.reviews.Models.ReviewRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to load real review data from CSV files.
 * This allows testing with the actual 41,457 airline reviews from the dataset!
 */
public class CSVLoader {
    
    /**
     * Load airline reviews from the CSV file.
     * 
     * @param csvPath Path to the airline.csv file
     * @return List of ReviewRecord objects from the CSV
     */
    public static List<ReviewRecord> loadAirlineReviews(String csvPath) {
        List<ReviewRecord> reviews = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                // Skip header row
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                try {
                    // Parse CSV line (handle quoted fields with commas)
                    String[] values = parseCSVLine(line);
                    
                    if (values.length < 19) {
                        System.err.println("Skipping invalid line: " + line);
                        continue;
                    }
                    
                    // Extract fields from CSV
                    String airlineName = values[0];
                    String link = values[1];
                    String title = values[2];
                    String author = values[3];
                    String country = values[4];
                    String date = values[5];
                    String content = values[6];
                    String aircraft = values[7];
                    String typeTraveller = values[8];
                    String cabinFlown = values[9];
                    String route = values[10];
                    
                    // Parse numeric ratings (with defaults for missing values)
                    double overallRating = parseDouble(values[11], 0.0);
                    double seatComfortRating = parseDouble(values[12], 0.0);
                    double cabinStaffRating = parseDouble(values[13], 0.0);
                    double foodBeveragesRating = parseDouble(values[14], 0.0);
                    double inflightEntertainmentRating = parseDouble(values[15], 0.0);
                    double groundServiceRating = parseDouble(values[16], 0.0);
                    double wifiConnectivityRating = parseDouble(values[17], 0.0);
                    double valueMoneyRating = parseDouble(values[18], 0.0);
                    int recommended = values.length > 19 ? parseInt(values[19], 0) : 0;
                    
                    AirlineReview review = new AirlineReview(
                        airlineName, link, title, author, country, date, content,
                        aircraft, typeTraveller, cabinFlown, route, overallRating,
                        seatComfortRating, cabinStaffRating, foodBeveragesRating,
                        inflightEntertainmentRating, groundServiceRating,
                        wifiConnectivityRating, valueMoneyRating, recommended
                    );
                    
                    reviews.add(review);
                    
                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line);
                    System.err.println("Error: " + e.getMessage());
                }
            }
            
        } catch (IOException e) {
            System.err.println("❌ ERROR reading CSV file: " + csvPath);
            System.err.println("   " + e.getMessage());
            System.err.println("   Current directory: " + new java.io.File(".").getAbsolutePath());
            return new ArrayList<>();
        }
        
        if (reviews.size() == 0) {
            System.err.println("⚠️  WARNING: Loaded 0 reviews from " + csvPath);
            System.err.println("   This will cause all benchmark times to be 0.000 ms!");
        } else {
            System.out.println("✅ Loaded " + reviews.size() + " reviews from " + csvPath);
        }
        return reviews;
    }
    
    /**
     * Parse a CSV line handling quoted fields with commas.
     */
    private static String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(currentValue.toString().trim());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }
        
        // Add last value
        values.add(currentValue.toString().trim());
        
        return values.toArray(new String[0]);
    }
    
    /**
     * Safely parse a double value with a default fallback.
     */
    private static double parseDouble(String value, double defaultValue) {
        try {
            value = value.replace("\"", "").trim();
            if (value.isEmpty()) {
                return defaultValue;
            }
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Safely parse an integer value with a default fallback.
     */
    private static int parseInt(String value, int defaultValue) {
        try {
            value = value.replace("\"", "").trim();
            if (value.isEmpty()) {
                return defaultValue;
            }
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Get the path to the airline CSV file.
     * Works whether running from project root or src/main/java/
     */
    public static String getAirlineCSVPath() {
        // Try multiple possible paths
        String[] possiblePaths = {
            "data/airline.csv",                    // From project root
            "../../../data/airline.csv",           // From src/main/java/
            "../../../../data/airline.csv"         // Alternative path
        };
        
        for (String path : possiblePaths) {
            java.io.File file = new java.io.File(path);
            if (file.exists()) {
                System.out.println("📂 Found CSV at: " + file.getAbsolutePath());
                return path;
            }
        }
        
        // If not found, return default and let it fail with a clear error
        System.err.println("❌ ERROR: Could not find data/airline.csv!");
        System.err.println("   Tried paths:");
        for (String path : possiblePaths) {
            System.err.println("   - " + path);
        }
        return "data/airline.csv";
    }
}

