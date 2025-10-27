package com.reviews.experiments;

import com.reviews.Models.AirlineReview;
import com.reviews.Models.ReviewRecord;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Shared utility class for benchmark operations across all experiments.
 * Provides common methods for test data generation and rating calculations.
 */
public class BenchmarkUtils {
    
    private static final String[] AIRLINE_NAMES = {
        "Delta", "United", "American", "Southwest", "JetBlue", 
        "Alaska", "Spirit", "Frontier", "Hawaiian", "Virgin America",
        "Lufthansa", "British Airways", "Air France", "KLM", "Emirates",
        "Singapore Airlines", "Cathay Pacific", "Qantas", "Turkish Airlines", "ANA"
    };
    
    private static final String[] COUNTRIES = {
        "USA", "Canada", "Mexico", "UK", "Germany", "France", 
        "Netherlands", "UAE", "Singapore", "Japan"
    };
    
    private static final String[] AIRCRAFT_TYPES = {
        "Boeing 737", "Airbus A320", "Boeing 777", "Airbus A350", "Boeing 787"
    };
    
    private static final String[] CABIN_TYPES = {"Economy", "Business", "First"};
    private static final String[] TRAVELER_TYPES = {"Business", "Leisure"};
    
    /**
     * Generate a large dataset of random airline reviews for performance testing.
     * Uses fixed seed for reproducible results across experiments.
     */
    public static List<ReviewRecord> generateTestData(int numReviews) {
        List<ReviewRecord> reviews = new ArrayList<>();
        Random random = new Random(42); // Fixed seed for reproducible results
        LocalDate now = LocalDate.now();
        
        for (int i = 0; i < numReviews; i++) {
            String airline = AIRLINE_NAMES[random.nextInt(AIRLINE_NAMES.length)];
            String country = COUNTRIES[random.nextInt(COUNTRIES.length)];
            String aircraft = AIRCRAFT_TYPES[random.nextInt(AIRCRAFT_TYPES.length)];
            String cabin = CABIN_TYPES[random.nextInt(CABIN_TYPES.length)];
            String travelerType = TRAVELER_TYPES[random.nextInt(TRAVELER_TYPES.length)];
            
            // Generate dates with bias towards recent reviews (last 2 years)
            LocalDate reviewDate;
            if (random.nextDouble() < 0.7) {
                // 70% chance of recent review (last 2 years)
                int daysAgo = random.nextInt(730);
                reviewDate = now.minusDays(daysAgo);
            } else {
                // 30% chance of older review (2-10 years)
                int daysAgo = 730 + random.nextInt(2920);
                reviewDate = now.minusDays(daysAgo);
            }
            
            // Generate realistic ratings (bias towards higher ratings)
            double overallRating = generateRealisticRating(random);
            double seatComfortRating = generateRealisticRating(random);
            double cabinStaffRating = generateRealisticRating(random);
            double foodBeveragesRating = generateRealisticRating(random);
            double inflightEntertainmentRating = generateRealisticRating(random);
            double groundServiceRating = generateRealisticRating(random);
            double wifiConnectivityRating = generateRealisticRating(random);
            double valueMoneyRating = generateRealisticRating(random);
            
            int recommended = overallRating >= 4.0 ? 1 : 0;
            
            AirlineReview review = new AirlineReview(
                airline, "link" + i, "Review " + i, "Author " + i, country,
                reviewDate.toString(), "Review content for test " + i, aircraft,
                travelerType, cabin, "Route " + i, overallRating, seatComfortRating,
                cabinStaffRating, foodBeveragesRating, inflightEntertainmentRating,
                groundServiceRating, wifiConnectivityRating, valueMoneyRating, recommended
            );
            
            reviews.add(review);
        }
        
        return reviews;
    }
    
    /**
     * Generate realistic ratings with bias towards higher ratings.
     * Uses normal distribution centered around 3.5 with std dev 1.0.
     */
    public static double generateRealisticRating(Random random) {
        // Use normal distribution centered around 3.5 with std dev 1.0
        double rating = random.nextGaussian() * 1.0 + 3.5;
        // Clamp to valid range [1.0, 5.0]
        return Math.max(1.0, Math.min(5.0, Math.round(rating * 10.0) / 10.0));
    }
    
    /**
     * Get standard airline names for testing.
     */
    public static String[] getAirlineNames() {
        return AIRLINE_NAMES.clone();
    }
    
    /**
     * Get standard data sizes for benchmarking.
     */
    public static int[] getStandardDataSizes() {
        return new int[]{1000, 5000, 10000, 25000, 50000};
    }
    
    /**
     * Get standard k values for top-k retrieval benchmarks.
     */
    public static int[] getStandardKValues() {
        return new int[]{5, 10, 25, 50};
    }
}

