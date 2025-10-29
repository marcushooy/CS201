package com.reviews.experiments;

import com.reviews.Models.ReviewRecord;
import java.util.List;

/**
 * Shared utility class for benchmark operations across all experiments.
 * Provides methods for loading real CSV data and standard test configurations.
 * 
 * ALL data comes from real CSV files - NO synthetic/generated data!
 */
public class BenchmarkUtils {
    
    private static final String[] AIRLINE_NAMES = {
        "Delta", "United", "American", "Southwest", "JetBlue", 
        "Alaska", "Spirit", "Frontier", "Hawaiian", "Virgin America",
        "Lufthansa", "British Airways", "Air France", "KLM", "Emirates",
        "Singapore Airlines", "Cathay Pacific", "Qantas", "Turkish Airlines", "ANA"
    };
    
    /**
     * Load real airline reviews from CSV file.
     * This is the ONLY data source - no synthetic data!
     */
    public static List<ReviewRecord> loadRealData() {
        return com.reviews.utils.CSVLoader.loadAirlineReviews(
            com.reviews.utils.CSVLoader.getAirlineCSVPath()
        );
    }
    
    /**
     * Get a subset of real reviews for testing different data sizes.
     */
    public static List<ReviewRecord> getRealDataSubset(int size) {
        List<ReviewRecord> allData = loadRealData();
        if (size >= allData.size()) {
            return allData;
        }
        return allData.subList(0, size);
    }
    
    /**
     * Get standard airline names for testing.
     */
    public static String[] getAirlineNames() {
        return AIRLINE_NAMES.clone();
    }
    
    /**
     * Get standard data sizes for benchmarking with REAL CSV data.
     * Tests with subsets of the full 41,457 airline reviews.
     */
    public static int[] getStandardDataSizes() {
        return new int[]{1000, 5000, 10000, 25000, 41457}; // 41457 = ALL airline reviews
    }
    
    /**
     * Get standard k values for top-k retrieval benchmarks.
     */
    public static int[] getStandardKValues() {
        return new int[]{5, 10, 25, 50};
    }
}


