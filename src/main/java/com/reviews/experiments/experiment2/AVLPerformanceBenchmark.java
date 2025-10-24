package com.reviews.experiments.experiment2;

import com.reviews.Models.AirlineReview;
import com.reviews.Models.ReviewRecord;
import com.reviews.datastructures.AVLReviewStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Performance benchmarking class for the AVLReviewStore implementation.
 * Measures execution times for various operations to compare against the linear list baseline.
 */
public class AVLPerformanceBenchmark {
    
    private static final String[] AIRLINE_NAMES = {
        "Delta", "United", "American", "Southwest", "JetBlue", 
        "Alaska", "Spirit", "Frontier", "Hawaiian", "Virgin America",
        "Lufthansa", "British Airways", "Air France", "KLM", "Emirates",
        "Singapore Airlines", "Cathay Pacific", "Qantas", "Turkish Airlines", "ANA"
    };
    
    private static final String[] COUNTRIES = {"USA", "Canada", "Mexico", "UK", "Germany", "France", "Netherlands", "UAE", "Singapore", "Japan"};
    private static final String[] AIRCRAFT_TYPES = {"Boeing 737", "Airbus A320", "Boeing 777", "Airbus A350", "Boeing 787"};
    private static final String[] CABIN_TYPES = {"Economy", "Business", "First"};
    private static final String[] TRAVELER_TYPES = {"Business", "Leisure"};
    
    /**
     * Generate a large dataset of random airline reviews for performance testing.
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
     */
    private static double generateRealisticRating(Random random) {
        // Use normal distribution centered around 3.5 with std dev 1.0
        double rating = random.nextGaussian() * 1.0 + 3.5;
        // Clamp to valid range [1.0, 5.0]
        return Math.max(1.0, Math.min(5.0, Math.round(rating * 10.0) / 10.0));
    }
    
    /**
     * Benchmark the top-k recent retrieval operation.
     */
    public static BenchmarkResult benchmarkTopKRetrieval(AVLReviewStore store, int k, int iterations) {
        String[] airlines = store.getAllAirlines().toArray(new String[0]);
        Random random = new Random(123);
        
        long totalTime = 0;
        int totalOperations = 0;
        
        for (int i = 0; i < iterations; i++) {
            String airline = airlines[random.nextInt(airlines.length)];
            
            long startTime = System.nanoTime();
            store.getTopKRecentReviews(airline, k);
            long endTime = System.nanoTime();
            
            totalTime += (endTime - startTime);
            totalOperations++;
        }
        
        double avgTimeMs = (totalTime / (double) totalOperations) / 1_000_000.0;
        return new BenchmarkResult("Top-K Recent Retrieval", avgTimeMs, totalOperations, k);
    }
    
    /**
     * Benchmark the recency-biased average rating calculation.
     */
    public static BenchmarkResult benchmarkRBARCalculation(AVLReviewStore store, int iterations) {
        String[] airlines = store.getAllAirlines().toArray(new String[0]);
        Random random = new Random(456);
        
        long totalTime = 0;
        int totalOperations = 0;
        
        for (int i = 0; i < iterations; i++) {
            String airline = airlines[random.nextInt(airlines.length)];
            
            long startTime = System.nanoTime();
            store.calculateRecencyBiasedAverageRating(airline);
            long endTime = System.nanoTime();
            
            totalTime += (endTime - startTime);
            totalOperations++;
        }
        
        double avgTimeMs = (totalTime / (double) totalOperations) / 1_000_000.0;
        return new BenchmarkResult("RBAR Calculation", avgTimeMs, totalOperations, 0);
    }
    
    /**
     * Benchmark insertion operations.
     */
    public static BenchmarkResult benchmarkInsertion(AVLReviewStore store, List<ReviewRecord> reviews, int batchSize) {
        long totalTime = 0;
        int totalOperations = 0;
        
        // Clear store first
        store.clear();
        
        for (int i = 0; i < reviews.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, reviews.size());
            List<ReviewRecord> batch = reviews.subList(i, endIndex);
            
            long startTime = System.nanoTime();
            store.addReviews(batch);
            long endTime = System.nanoTime();
            
            totalTime += (endTime - startTime);
            totalOperations++;
        }
        
        double avgTimeMs = (totalTime / (double) totalOperations) / 1_000_000.0;
        return new BenchmarkResult("Batch Insertion", avgTimeMs, totalOperations, batchSize);
    }
    
    /**
     * Benchmark search operations.
     */
    public static BenchmarkResult benchmarkSearch(AVLReviewStore store, int iterations) {
        String[] airlines = store.getAllAirlines().toArray(new String[0]);
        Random random = new Random(789);
        
        long totalTime = 0;
        int totalOperations = 0;
        
        for (int i = 0; i < iterations; i++) {
            String airline = airlines[random.nextInt(airlines.length)];
            
            long startTime = System.nanoTime();
            store.getReviewsByAirline(airline);
            long endTime = System.nanoTime();
            
            totalTime += (endTime - startTime);
            totalOperations++;
        }
        
        double avgTimeMs = (totalTime / (double) totalOperations) / 1_000_000.0;
        return new BenchmarkResult("Search by Airline", avgTimeMs, totalOperations, 0);
    }
    
    /**
     * Run comprehensive performance benchmarks.
     */
    public static void runComprehensiveBenchmark() {
        System.out.println("=== AVLReviewStore Performance Benchmark ===");
        System.out.println();
        
        int[] dataSizes = {1000, 5000, 10000, 25000, 50000};
        int[] kValues = {5, 10, 25, 50};
        
        for (int dataSize : dataSizes) {
            System.out.println("Testing with " + dataSize + " reviews:");
            System.out.println("----------------------------------------");
            
            // Generate test data
            List<ReviewRecord> testData = generateTestData(dataSize);
            AVLReviewStore store = new AVLReviewStore();
            
            // Benchmark insertion
            BenchmarkResult insertionResult = benchmarkInsertion(store, testData, 100);
            System.out.printf("Insertion (batch size 100): %.3f ms per batch%n", insertionResult.avgTimeMs);
            
            // Benchmark search
            BenchmarkResult searchResult = benchmarkSearch(store, 100);
            System.out.printf("Search by Airline: %.3f ms per operation%n", searchResult.avgTimeMs);
            
            // Benchmark RBAR calculation
            BenchmarkResult rbarResult = benchmarkRBARCalculation(store, 100);
            System.out.printf("RBAR Calculation: %.3f ms per operation%n", rbarResult.avgTimeMs);
            
            // Benchmark top-k retrieval for different k values
            for (int k : kValues) {
                BenchmarkResult topKResult = benchmarkTopKRetrieval(store, k, 100);
                System.out.printf("Top-%d Retrieval: %.3f ms per operation%n", k, topKResult.avgTimeMs);
            }
            
            // Show AVL tree specific statistics
            var treeStats = store.getTreeStatistics();
            System.out.printf("Tree Height: %d, Is Balanced: %s%n", 
                           treeStats.get("treeHeight"), treeStats.get("isBalanced"));
            
            System.out.println();
        }
        
        // Test scalability
        System.out.println("Scalability Analysis:");
        System.out.println("===================");
        testScalability();
        
        // Test AVL tree balancing
        System.out.println();
        System.out.println("AVL Tree Balancing Analysis:");
        System.out.println("============================");
        testAVLBalancing();
    }
    
    /**
     * Test how performance scales with data size.
     */
    private static void testScalability() {
        int[] sizes = {1000, 2000, 5000, 10000, 20000, 50000};
        
        System.out.printf("%-10s %-15s %-15s %-15s %-15s%n", 
                         "Size", "Search (ms)", "RBAR (ms)", "Top-10 (ms)", "Top-50 (ms)");
        System.out.println("--------------------------------------------------------------------");
        
        for (int size : sizes) {
            List<ReviewRecord> testData = generateTestData(size);
            AVLReviewStore store = new AVLReviewStore();
            store.addReviews(testData);
            
            BenchmarkResult searchResult = benchmarkSearch(store, 50);
            BenchmarkResult rbarResult = benchmarkRBARCalculation(store, 50);
            BenchmarkResult top10Result = benchmarkTopKRetrieval(store, 10, 50);
            BenchmarkResult top50Result = benchmarkTopKRetrieval(store, 50, 50);
            
            System.out.printf("%-10d %-15.3f %-15.3f %-15.3f %-15.3f%n", 
                size, searchResult.avgTimeMs, rbarResult.avgTimeMs, 
                top10Result.avgTimeMs, top50Result.avgTimeMs);
        }
    }
    
    /**
     * Test AVL tree balancing with different insertion patterns.
     */
    private static void testAVLBalancing() {
        int[] sizes = {100, 500, 1000, 5000, 10000};
        
        System.out.printf("%-10s %-15s %-15s %-15s%n", 
                         "Size", "Random Height", "Ordered Height", "Balanced");
        System.out.println("--------------------------------------------------------");
        
        for (int size : sizes) {
            // Test random insertion
            List<ReviewRecord> randomData = generateTestData(size);
            AVLReviewStore randomStore = new AVLReviewStore();
            randomStore.addReviews(randomData);
            
            // Test ordered insertion (worst case for BST, should be handled by AVL)
            AVLReviewStore orderedStore = new AVLReviewStore();
            for (int i = 0; i < size; i++) {
                String airline = "Airline" + String.format("%06d", i); // Alphabetical order
                AirlineReview review = new AirlineReview(airline, "link", "Test", "Author", "USA", 
                                                       LocalDate.now().toString(), "Test content", 
                                                       "Boeing 737", "Business", "Economy", "Route", 
                                                       4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 3.0, 4.0, 1);
                orderedStore.addReview(review);
            }
            
            int randomHeight = randomStore.getTreeHeight();
            int orderedHeight = orderedStore.getTreeHeight();
            boolean bothBalanced = randomStore.isBalanced() && orderedStore.isBalanced();
            
            System.out.printf("%-10d %-15d %-15d %-15s%n", 
                            size, randomHeight, orderedHeight, bothBalanced ? "Yes" : "No");
        }
    }
    
    /**
     * Result class for benchmark measurements.
     */
    public static class BenchmarkResult {
        public final String operation;
        public final double avgTimeMs;
        public final int iterations;
        public final int parameter;
        
        public BenchmarkResult(String operation, double avgTimeMs, int iterations, int parameter) {
            this.operation = operation;
            this.avgTimeMs = avgTimeMs;
            this.iterations = iterations;
            this.parameter = parameter;
        }
        
        @Override
        public String toString() {
            return String.format("%s: %.3f ms (avg over %d iterations)", operation, avgTimeMs, iterations);
        }
    }
    
    /**
     * Main method to run benchmarks.
     */
    public static void main(String[] args) {
        runComprehensiveBenchmark();
    }
}
