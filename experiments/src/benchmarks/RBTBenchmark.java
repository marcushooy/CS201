package experiments.benchmarks;

import datastructures.rbt_tree.RBTReviewStore;
import models.AirlineReview;
import experiments.utils.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * RBTBenchmark - Performance benchmarks for RBT (Recency-Biased Tree) data structure.
 * 
 * Tests all operations using real airline review data.
 * Includes getTopKRecentReviews which is specific to RBT.
 */
public class RBTBenchmark extends PerformanceBenchmark {
    
    private static final String DATA_STRUCTURE_NAME = "RBT";
    
    /**
     * Benchmark CREATE operation (addReview).
     */
    public static BenchmarkResult benchmarkCreate(int dataSize, int iterations) {
        System.out.println("\n[RBT] Benchmarking CREATE with " + dataSize + " reviews...");
        
        // Load real reviews
        List<AirlineReview> reviews = loadReviewsSubset(dataSize);
        if (reviews == null) return null;
        
        // Warmup
        RBTReviewStore warmupStore = new RBTReviewStore();
        warmup(() -> {
            if (warmupStore.size() < dataSize) {
                warmupStore.addReview(reviews.get(warmupStore.size()));
            }
        }, 100);
        
        // Benchmark
        long totalTime = 0;
        double minTime = Double.MAX_VALUE;
        double maxTime = 0;
        
        for (int i = 0; i < iterations; i++) {
            RBTReviewStore store = new RBTReviewStore();
            // Pre-populate
            for (int j = 0; j < Math.min(dataSize - 1, reviews.size() - 1); j++) {
                store.addReview(reviews.get(j));
            }
            
            // Measure adding one more review
            AirlineReview reviewToAdd = reviews.get(Math.min(i % reviews.size(), reviews.size() - 1));
            long startTime = System.nanoTime();
            store.addReview(reviewToAdd);
            long endTime = System.nanoTime();
            
            double timeMs = (endTime - startTime) / 1_000_000.0;
            totalTime += (endTime - startTime);
            minTime = Math.min(minTime, timeMs);
            maxTime = Math.max(maxTime, timeMs);
        }
        
        double avgTimeMs = totalTime / (double)iterations / 1_000_000.0;
        System.out.println("  CREATE: " + formatTime(avgTimeMs) + " average");
        return new BenchmarkResult(DATA_STRUCTURE_NAME, "CREATE", dataSize, avgTimeMs, minTime, maxTime, iterations);
    }
    
    /**
     * Benchmark READ operation (getReviewsByAirline).
     */
    public static BenchmarkResult benchmarkRead(RBTReviewStore store, int dataSize, int iterations) {
        System.out.println("\n[RBT] Benchmarking READ...");
        
        Set<String> airlines = store.getAllAirlines();
        if (airlines.isEmpty()) return null;
        
        // Warmup
        warmup(() -> {
            String airline = getRandomAirline(airlines);
            if (airline != null) store.getReviewsByAirline(airline);
        }, 100);
        
        // Benchmark
        return runBenchmark(DATA_STRUCTURE_NAME, "READ", dataSize, iterations, () -> {
            String airline = getRandomAirline(airlines);
            if (airline != null) store.getReviewsByAirline(airline);
        });
    }
    
    /**
     * Benchmark UPDATE operation (updateReview).
     */
    public static BenchmarkResult benchmarkUpdate(RBTReviewStore store, int dataSize, int iterations) {
        System.out.println("\n[RBT] Benchmarking UPDATE...");
        
        Set<String> airlines = store.getAllAirlines();
        if (airlines.isEmpty()) return null;
        
        // Warmup
        warmup(() -> {
            String airline = getRandomAirline(airlines);
            if (airline != null) {
                List<AirlineReview> reviews = store.getReviewsByAirline(airline);
                if (!reviews.isEmpty()) {
                    AirlineReview oldReview = reviews.get(0);
                    store.updateReview(airline, oldReview, oldReview);
                }
            }
        }, 100);
        
        // Benchmark
        return runBenchmark(DATA_STRUCTURE_NAME, "UPDATE", dataSize, iterations, () -> {
            String airline = getRandomAirline(airlines);
            if (airline != null) {
                List<AirlineReview> reviews = store.getReviewsByAirline(airline);
                if (!reviews.isEmpty()) {
                    AirlineReview oldReview = reviews.get(0);
                    store.updateReview(airline, oldReview, oldReview);
                }
            }
        });
    }
    
    /**
     * Benchmark DELETE operation (deleteReview).
     */
    public static BenchmarkResult benchmarkDelete(int dataSize, int iterations, List<AirlineReview> reviews) {
        System.out.println("\n[RBT] Benchmarking DELETE...");
        
        if (reviews == null || reviews.isEmpty()) return null;
        
        // Benchmark
        long totalTime = 0;
        double minTime = Double.MAX_VALUE;
        double maxTime = 0;
        
        for (int i = 0; i < iterations; i++) {
            // Create fresh store
            RBTReviewStore store = new RBTReviewStore();
            store.addReviews(reviews);
            
            // Get a random review to delete
            AirlineReview reviewToDelete = getRandomReview(reviews);
            if (reviewToDelete == null) continue;
            
            // Measure delete
            long startTime = System.nanoTime();
            store.deleteReview(reviewToDelete.getAirlineName(), reviewToDelete);
            long endTime = System.nanoTime();
            
            double timeMs = (endTime - startTime) / 1_000_000.0;
            totalTime += (endTime - startTime);
            minTime = Math.min(minTime, timeMs);
            maxTime = Math.max(maxTime, timeMs);
        }
        
        double avgTimeMs = totalTime / (double)iterations / 1_000_000.0;
        System.out.println("  DELETE: " + formatTime(avgTimeMs) + " average");
        return new BenchmarkResult(DATA_STRUCTURE_NAME, "DELETE", dataSize, avgTimeMs, minTime, maxTime, iterations);
    }
    
    /**
     * Benchmark RBAR calculation (calculateRBAR).
     */
    public static BenchmarkResult benchmarkRBAR(RBTReviewStore store, int dataSize, int iterations) {
        System.out.println("\n[RBT] Benchmarking RBAR...");
        
        Set<String> airlines = store.getAllAirlines();
        if (airlines.isEmpty()) return null;
        
        // Warmup
        warmup(() -> {
            String airline = getRandomAirline(airlines);
            if (airline != null) store.calculateRBAR(airline);
        }, 50);
        
        // Benchmark
        return runBenchmark(DATA_STRUCTURE_NAME, "RBAR", dataSize, iterations, () -> {
            String airline = getRandomAirline(airlines);
            if (airline != null) store.calculateRBAR(airline);
        });
    }
    
    /**
     * Benchmark getAirlineRankings operation.
     */
    public static BenchmarkResult benchmarkRankings(RBTReviewStore store, int dataSize, int iterations) {
        System.out.println("\n[RBT] Benchmarking RANKINGS...");
        
        // Warmup
        warmup(() -> store.getAirlineRankings(), 10);
        
        // Benchmark
        return runBenchmark(DATA_STRUCTURE_NAME, "RANKINGS", dataSize, iterations, () -> {
            store.getAirlineRankings();
        });
    }
    
    /**
     * Benchmark getTopKAirlines operation.
     */
    public static BenchmarkResult benchmarkTopK(RBTReviewStore store, int dataSize, int k, int iterations) {
        System.out.println("\n[RBT] Benchmarking TOPK (k=" + k + ")...");
        
        // Warmup
        warmup(() -> store.getTopKAirlines(k), 10);
        
        // Benchmark
        return runBenchmark(DATA_STRUCTURE_NAME, "TOPK", dataSize, iterations, () -> {
            store.getTopKAirlines(k);
        });
    }
    
    /**
     * Benchmark getTopKRecentReviews operation (RBT-specific).
     */
    public static BenchmarkResult benchmarkTopKRecent(RBTReviewStore store, int dataSize, int k, int iterations) {
        System.out.println("\n[RBT] Benchmarking TOPK_RECENT (k=" + k + ")...");
        
        Set<String> airlines = store.getAllAirlines();
        if (airlines.isEmpty()) return null;
        
        // Warmup
        warmup(() -> {
            String airline = getRandomAirline(airlines);
            if (airline != null) store.getTopKRecentReviews(airline, k);
        }, 50);
        
        // Benchmark
        return runBenchmark(DATA_STRUCTURE_NAME, "TOPK_RECENT", dataSize, iterations, () -> {
            String airline = getRandomAirline(airlines);
            if (airline != null) store.getTopKRecentReviews(airline, k);
        });
    }
    
    /**
     * Run all benchmarks for a specific data size.
     */
    public static List<BenchmarkResult> runAllBenchmarks(int dataSize) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("RBT BENCHMARKS - Data Size: " + dataSize);
        System.out.println("=".repeat(60));
        
        List<BenchmarkResult> results = new ArrayList<>();
        
        // Load data
        List<AirlineReview> reviews = loadReviewsSubset(dataSize);
        if (reviews == null) {
            System.err.println("Failed to load reviews!");
            return results;
        }
        
        // Create and populate store
        RBTReviewStore store = new RBTReviewStore();
        store.addReviews(reviews);
        System.out.println("Store populated with " + store.size() + " reviews");
        System.out.println("Unique airlines: " + store.getAllAirlines().size());
        
        // Run benchmarks
        BenchmarkResult result;
        
        result = benchmarkCreate(dataSize, 1000);
        if (result != null) results.add(result);
        
        result = benchmarkRead(store, dataSize, 100);
        if (result != null) results.add(result);
        
        result = benchmarkUpdate(store, dataSize, 100);
        if (result != null) results.add(result);
        
        result = benchmarkDelete(dataSize, 100, reviews);
        if (result != null) results.add(result);
        
        result = benchmarkRBAR(store, dataSize, 50);
        if (result != null) results.add(result);
        
        result = benchmarkRankings(store, dataSize, 20);
        if (result != null) results.add(result);
        
        result = benchmarkTopK(store, dataSize, 10, 100);
        if (result != null) results.add(result);
        
        // RBT-specific: Top K Recent Reviews
        result = benchmarkTopKRecent(store, dataSize, 10, 100);
        if (result != null) results.add(result);
        
        System.out.println("\nRBT benchmarks complete for " + dataSize + " reviews.");
        return results;
    }
}

