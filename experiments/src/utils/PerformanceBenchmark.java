package experiments.utils;

import models.AirlineReview;
import utils.CSVLoader;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * PerformanceBenchmark - Base utility class for performance benchmarking.
 * 
 * This class provides shared utilities for all benchmark implementations:
 * - Loading real data from CSV
 * - JVM warmup for stable performance measurement
 * - Random selection of airlines and reviews for testing
 * - Precision timing utilities
 */
public class PerformanceBenchmark {
    
    // Test configuration constants
    public static final int[] TEST_SIZES = {1000, 5000, 10000, 25000, 41000};
    public static final int WARMUP_ITERATIONS = 1000;
    public static final String CSV_PATH = "data/airline.csv";
    
    // Random number generator for consistent test data selection
    protected static final Random random = new Random(42); // Fixed seed for reproducibility
    
    /**
     * Load a subset of reviews from the CSV file.
     * Loads the first N reviews from the real airline dataset.
     * 
     * @param count Number of reviews to load
     * @return List of AirlineReview objects
     */
    public static List<AirlineReview> loadReviewsSubset(int count) {
        try {
            System.out.println("Loading " + count + " reviews from CSV...");
            List<AirlineReview> allReviews = CSVLoader.loadAirlineReviews(CSV_PATH);
            
            if (allReviews == null || allReviews.isEmpty()) {
                System.err.println("ERROR: No reviews loaded from CSV!");
                return null;
            }
            
            // Return subset (first N reviews)
            int actualCount = Math.min(count, allReviews.size());
            List<AirlineReview> subset = allReviews.subList(0, actualCount);
            System.out.println("Loaded " + subset.size() + " reviews successfully.");
            return subset;
            
        } catch (Exception e) {
            System.err.println("ERROR loading reviews: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get a random airline name from a set of airlines.
     * Used for READ, UPDATE, DELETE, and RBAR benchmarks.
     * 
     * @param airlines Set of airline names
     * @return Random airline name, or null if set is empty
     */
    public static String getRandomAirline(Set<String> airlines) {
        if (airlines == null || airlines.isEmpty()) {
            return null;
        }
        
        int index = random.nextInt(airlines.size());
        int i = 0;
        for (String airline : airlines) {
            if (i == index) {
                return airline;
            }
            i++;
        }
        return null;
    }
    
    /**
     * Get a random review from a list of reviews.
     * Used for UPDATE and DELETE benchmarks.
     * 
     * @param reviews List of reviews
     * @return Random review, or null if list is empty
     */
    public static AirlineReview getRandomReview(List<AirlineReview> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return null;
        }
        return reviews.get(random.nextInt(reviews.size()));
    }
    
    /**
     * Measure execution time of a runnable operation with high precision.
     * Uses System.nanoTime() for nanosecond-precision timing.
     * 
     * @param operation The operation to time
     * @return Execution time in milliseconds
     */
    public static double measureTime(Runnable operation) {
        long startTime = System.nanoTime();
        operation.run();
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
    }
    
    /**
     * Perform JVM warmup to stabilize performance measurements.
     * Runs a series of operations to allow JIT compilation to optimize the code.
     * 
     * @param warmupOp The warmup operation to execute repeatedly
     * @param iterations Number of warmup iterations (default: WARMUP_ITERATIONS)
     */
    public static void warmup(Runnable warmupOp, int iterations) {
        System.out.println("Warming up JVM (" + iterations + " iterations)...");
        for (int i = 0; i < iterations; i++) {
            warmupOp.run();
        }
        System.out.println("Warmup complete.");
    }
    
    /**
     * Perform JVM warmup with default iteration count.
     * 
     * @param warmupOp The warmup operation to execute repeatedly
     */
    public static void warmup(Runnable warmupOp) {
        warmup(warmupOp, WARMUP_ITERATIONS);
    }
    
    /**
     * Run a benchmark multiple times and collect statistics.
     * Returns a BenchmarkResult with avg, min, and max times.
     * 
     * @param dataStructure Name of the data structure being tested
     * @param operation Name of the operation being tested
     * @param dataSize Size of the dataset
     * @param iterations Number of times to run the operation
     * @param benchmarkOp The operation to benchmark
     * @return BenchmarkResult with timing statistics
     */
    public static BenchmarkResult runBenchmark(String dataStructure, String operation, 
                                               int dataSize, int iterations, Runnable benchmarkOp) {
        long totalTime = 0;
        double minTime = Double.MAX_VALUE;
        double maxTime = 0;
        
        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            benchmarkOp.run();
            long endTime = System.nanoTime();
            
            double timeMs = (endTime - startTime) / 1_000_000.0;
            totalTime += (endTime - startTime);
            minTime = Math.min(minTime, timeMs);
            maxTime = Math.max(maxTime, timeMs);
        }
        
        double avgTimeMs = totalTime / (double)iterations / 1_000_000.0;
        return new BenchmarkResult(dataStructure, operation, dataSize, avgTimeMs, minTime, maxTime, iterations);
    }
    
    /**
     * Print a progress indicator for long-running benchmarks.
     * 
     * @param current Current progress
     * @param total Total expected
     * @param message Progress message
     */
    public static void printProgress(int current, int total, String message) {
        int percentage = (int)((current / (double)total) * 100);
        System.out.printf("[%d%%] %s (%d/%d)\n", percentage, message, current, total);
    }
    
    /**
     * Format time in milliseconds to a human-readable string.
     * 
     * @param timeMs Time in milliseconds
     * @return Formatted string (e.g., "1.234 ms" or "0.001 ms")
     */
    public static String formatTime(double timeMs) {
        if (timeMs < 0.001) {
            return String.format("%.6f ms", timeMs);
        } else if (timeMs < 1.0) {
            return String.format("%.3f ms", timeMs);
        } else if (timeMs < 1000.0) {
            return String.format("%.2f ms", timeMs);
        } else {
            return String.format("%.2f s", timeMs / 1000.0);
        }
    }
}

