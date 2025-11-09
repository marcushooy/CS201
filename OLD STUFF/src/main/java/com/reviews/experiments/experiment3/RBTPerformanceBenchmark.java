package com.reviews.experiments.experiment3;

import com.reviews.Models.ReviewRecord;
import com.reviews.datastructures.RBTReviewStore;
import com.reviews.experiments.BenchmarkUtils;

import java.util.List;
import java.util.Random;

/**
 * Performance benchmarking class for the RBTReviewStore implementation.
 * Measures execution times for various operations, with special focus on
 * demonstrating the recency-biased performance characteristics.
 */
public class RBTPerformanceBenchmark {
    
    /**
     * Benchmark the top-k recent retrieval operation.
     */
    public static BenchmarkResult benchmarkTopKRetrieval(RBTReviewStore store, int k, int iterations) {
        String[] airlines = store.getAllAirlines();
        
        // Safety check: ensure we have airlines to test with
        if (airlines.length == 0) {
            return new BenchmarkResult("Top-K Recent Retrieval", 0.0, 0, k);
        }
        
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
    public static BenchmarkResult benchmarkRBARCalculation(RBTReviewStore store, int iterations) {
        String[] airlines = store.getAllAirlines();
        
        // Safety check: ensure we have airlines to test with
        if (airlines.length == 0) {
            return new BenchmarkResult("RBAR Calculation", 0.0, 0, 0);
        }
        
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
    public static BenchmarkResult benchmarkInsertion(RBTReviewStore store, List<ReviewRecord> reviews, int batchSize) {
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
    public static BenchmarkResult benchmarkSearch(RBTReviewStore store, int iterations) {
        String[] airlines = store.getAllAirlines();
        
        // Safety check: ensure we have airlines to test with
        if (airlines.length == 0) {
            return new BenchmarkResult("Search by Airline", 0.0, 0, 0);
        }
        
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
        System.out.println("=== RBTReviewStore Performance Benchmark ===");
        System.out.println();
        
        int[] dataSizes = BenchmarkUtils.getStandardDataSizes();
        int[] kValues = BenchmarkUtils.getStandardKValues();
        
        for (int dataSize : dataSizes) {
            System.out.println("Testing with " + dataSize + " reviews:");
            System.out.println("----------------------------------------");
            
            // Generate test data using shared utility
            List<ReviewRecord> testData = BenchmarkUtils.getRealDataSubset(dataSize);
            RBTReviewStore store = new RBTReviewStore();
            
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
            
            // Show RBT specific statistics
            var treeStats = store.getTreeStatistics();
            System.out.printf("Tree Height: %d, Is Balanced: %s (intentional for RBT)%n", 
                           treeStats.get("treeHeight"), treeStats.get("isBalanced"));
            
            System.out.println();
        }
        
        // Test scalability
        System.out.println("Scalability Analysis:");
        System.out.println("===================");
        testScalability();
        
        // Test recency bias effectiveness
        System.out.println();
        System.out.println("Recency-Bias Performance Analysis:");
        System.out.println("==================================");
        testRecencyBias();
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
            List<ReviewRecord> testData = BenchmarkUtils.getRealDataSubset(size);
            RBTReviewStore store = new RBTReviewStore();
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
     * Test recency bias by comparing performance with different splay depths.
     */
    private static void testRecencyBias() {
        int[] sizes = {1000, 5000, 10000};
        
        System.out.printf("%-10s %-15s %-15s %-15s%n", 
                         "Size", "No Splay", "Shallow (5)", "Deep (∞)");
        System.out.println("-----------------------------------------------------------");
        
        for (int size : sizes) {
            List<ReviewRecord> testData = BenchmarkUtils.getRealDataSubset(size);
            
            long noSplayTime = 0;
            long shallowSplayTime = 0;
            long deepSplayTime = 0;
            
            for (int depth : new int[]{0, 5, Integer.MAX_VALUE}) {
                RBTReviewStore store = new RBTReviewStore(depth);
                store.addReviews(testData);
                
                long startTime = System.nanoTime();
                for (int i = 0; i < 100; i++) {
                    store.getTopKRecentReviews("Delta", 10);
                }
                long endTime = System.nanoTime();
                long avgTime = (endTime - startTime) / 100;
                
                if (depth == 0) noSplayTime = avgTime;
                else if (depth == 5) shallowSplayTime = avgTime;
                else deepSplayTime = avgTime;
            }
            
            System.out.printf("%-10d %-15.3f %-15.3f %-15.3f%n", 
                size, noSplayTime / 1_000_000.0, 
                shallowSplayTime / 1_000_000.0, 
                deepSplayTime / 1_000_000.0);
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
