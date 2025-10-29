package com.reviews.experiments;

import com.reviews.Models.ReviewRecord;
import com.reviews.datastructures.LinearListReviewStore;
import com.reviews.datastructures.AVLReviewStore;
import com.reviews.datastructures.RBTReviewStore;
import com.reviews.experiments.experiment1.LinearListPerformanceBenchmark;
import com.reviews.experiments.experiment2.AVLPerformanceBenchmark;
import com.reviews.experiments.experiment3.RBTPerformanceBenchmark;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unified benchmark runner that executes performance benchmarks across all three
 * data structure implementations using REAL CSV data from Skytrax dataset.
 * 
 * This class provides a comprehensive comparison of:
 * - Linear List (Experiment 1)
 * - AVL Tree (Experiment 2)
 * - RBT Tree (Experiment 3)
 * 
 * Uses REAL airline reviews from data/airline.csv (41,457 reviews)
 */
public class UnifiedBenchmarkRunner {
    
    /**
     * Run unified benchmarks for all three data structures using REAL CSV data.
     */
    public static void runUnifiedBenchmarks() {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║  Unified Benchmark Runner - All Data Structures              ║");
        System.out.println("║  Using REAL CSV Data (41,457 Airline Reviews)                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Load ALL real data once
        System.out.println("📂 Loading REAL airline reviews from CSV...");
        List<ReviewRecord> allRealData = BenchmarkUtils.loadRealData();
        System.out.println("✅ Loaded " + allRealData.size() + " real reviews!");
        System.out.println();
        
        int[] dataSizes = BenchmarkUtils.getStandardDataSizes();
        int[] kValues = BenchmarkUtils.getStandardKValues();
        
        List<BenchmarkData> allResults = new ArrayList<>();
        
        for (int dataSize : dataSizes) {
            System.out.println("════════════════════════════════════════════════════════════════");
            System.out.println("Testing with " + dataSize + " REAL reviews");
            System.out.println("════════════════════════════════════════════════════════════════");
            
            // Get subset of real data
            List<ReviewRecord> testData = dataSize >= allRealData.size() 
                ? allRealData 
                : allRealData.subList(0, dataSize);
            
            // Run benchmarks for each data structure
            BenchmarkResults linearResults = benchmarkLinearList(testData, kValues);
            BenchmarkResults avlResults = benchmarkAVLTree(testData, kValues);
            BenchmarkResults rbtResults = benchmarkRBT(testData, kValues);
            
            // Store results
            allResults.add(new BenchmarkData(dataSize, "Linear List", linearResults));
            allResults.add(new BenchmarkData(dataSize, "AVL Tree", avlResults));
            allResults.add(new BenchmarkData(dataSize, "RBT", rbtResults));
            
            // Print comparison for this data size
            printComparison(dataSize, linearResults, avlResults, rbtResults);
        }
        
        // Generate CSV files
        System.out.println();
        System.out.println("Generating results.csv files...");
        generateResultCSVs(allResults);
        
        System.out.println();
        System.out.println("✅ All benchmarks complete!");
        System.out.println("Check individual experiment directories for results.csv files.");
    }
    
    /**
     * Benchmark Linear List implementation.
     */
    private static BenchmarkResults benchmarkLinearList(List<ReviewRecord> testData, int[] kValues) {
        LinearListReviewStore store = new LinearListReviewStore();
        
        // Insert data
        long startInsert = System.nanoTime();
        store.addReviews(testData);
        long endInsert = System.nanoTime();
        double insertTime = (endInsert - startInsert) / 1_000_000.0;
        
        // Search operations  
        LinearListPerformanceBenchmark.BenchmarkResult searchResult = LinearListPerformanceBenchmark.benchmarkSearch(store, 100);
        double searchTime = searchResult.avgTimeMs;
        
        // RBAR calculations
        double rbarTime = LinearListPerformanceBenchmark.benchmarkRBARCalculation(store, 100).avgTimeMs;
        
        // Top-k operations
        Map<String, Double> topKTimes = new HashMap<>();
        for (int k : kValues) {
            topKTimes.put("Top-" + k, 
                LinearListPerformanceBenchmark.benchmarkTopKRetrieval(store, k, 100).avgTimeMs);
        }
        
        return new BenchmarkResults(insertTime, searchTime, rbarTime, topKTimes, 0, false);
    }
    
    /**
     * Benchmark AVL Tree implementation.
     */
    private static BenchmarkResults benchmarkAVLTree(List<ReviewRecord> testData, int[] kValues) {
        AVLReviewStore store = new AVLReviewStore();
        
        // Insert data
        long startInsert = System.nanoTime();
        store.addReviews(testData);
        long endInsert = System.nanoTime();
        double insertTime = (endInsert - startInsert) / 1_000_000.0;
        
        // Search operations
        double searchTime = AVLPerformanceBenchmark.benchmarkSearch(store, 100).avgTimeMs;
        
        // RBAR calculations
        double rbarTime = AVLPerformanceBenchmark.benchmarkRBARCalculation(store, 100).avgTimeMs;
        
        // Top-k operations
        Map<String, Double> topKTimes = new HashMap<>();
        for (int k : kValues) {
            topKTimes.put("Top-" + k, 
                AVLPerformanceBenchmark.benchmarkTopKRetrieval(store, k, 100).avgTimeMs);
        }
        
        // Tree statistics
        var treeStats = store.getTreeStatistics();
        int height = (Integer) treeStats.get("treeHeight");
        boolean balanced = (Boolean) treeStats.get("isBalanced");
        
        return new BenchmarkResults(insertTime, searchTime, rbarTime, topKTimes, height, balanced);
    }
    
    /**
     * Benchmark RBT implementation.
     */
    private static BenchmarkResults benchmarkRBT(List<ReviewRecord> testData, int[] kValues) {
        RBTReviewStore store = new RBTReviewStore();
        
        // Insert data
        long startInsert = System.nanoTime();
        store.addReviews(testData);
        long endInsert = System.nanoTime();
        double insertTime = (endInsert - startInsert) / 1_000_000.0;
        
        // Search operations
        double searchTime = RBTPerformanceBenchmark.benchmarkSearch(store, 100).avgTimeMs;
        
        // RBAR calculations
        double rbarTime = RBTPerformanceBenchmark.benchmarkRBARCalculation(store, 100).avgTimeMs;
        
        // Top-k operations
        Map<String, Double> topKTimes = new HashMap<>();
        for (int k : kValues) {
            topKTimes.put("Top-" + k, 
                RBTPerformanceBenchmark.benchmarkTopKRetrieval(store, k, 100).avgTimeMs);
        }
        
        // Tree statistics
        var treeStats = store.getTreeStatistics();
        int height = (Integer) treeStats.get("treeHeight");
        boolean balanced = (Boolean) treeStats.get("isBalanced");
        
        return new BenchmarkResults(insertTime, searchTime, rbarTime, topKTimes, height, balanced);
    }
    
    /**
     * Print comparison table for a specific data size.
     */
    private static void printComparison(int dataSize, BenchmarkResults linear, BenchmarkResults avl, BenchmarkResults rbt) {
        System.out.println();
        System.out.printf("%-20s %-15s %-15s %-15s %-15s%n", 
                         "Operation", "Linear List", "AVL Tree", "RBT", "Winner");
        System.out.println("────────────────────────────────────────────────────────────────────────────");
        
        // Search comparison
        printComparisonRow("Search (ms)", linear.searchTime, avl.searchTime, rbt.searchTime, 
                          findFastest(linear.searchTime, avl.searchTime, rbt.searchTime));
        
        // RBAR comparison
        printComparisonRow("RBAR (ms)", linear.rbarTime, avl.rbarTime, rbt.rbarTime,
                          findFastest(linear.rbarTime, avl.rbarTime, rbt.rbarTime));
        
        // Top-10 comparison
        printComparisonRow("Top-10 (ms)", linear.topKTimes.get("Top-10"), 
                          avl.topKTimes.get("Top-10"), rbt.topKTimes.get("Top-10"),
                          findFastest(linear.topKTimes.get("Top-10"), 
                                     avl.topKTimes.get("Top-10"), 
                                     rbt.topKTimes.get("Top-10")));
        
        // Top-50 comparison
        printComparisonRow("Top-50 (ms)", linear.topKTimes.get("Top-50"),
                          avl.topKTimes.get("Top-50"), rbt.topKTimes.get("Top-50"),
                          findFastest(linear.topKTimes.get("Top-50"),
                                     avl.topKTimes.get("Top-50"),
                                     rbt.topKTimes.get("Top-50")));
        
        System.out.println();
        
        // Tree-specific info
        System.out.println("Tree Characteristics:");
        System.out.printf("  AVL: Height=%d, Balanced=%s%n", avl.treeHeight, avl.isBalanced);
        System.out.printf("  RBT: Height=%d, Balanced=%s (intentional for recency-bias)%n", 
                         rbt.treeHeight, rbt.isBalanced);
        System.out.println();
    }
    
    /**
     * Print a comparison row.
     */
    private static void printComparisonRow(String operation, Double linear, Double avl, Double rbt, String winner) {
        System.out.printf("%-20s %-15.3f %-15.3f %-15.3f %-15s%n",
                         operation, linear, avl, rbt, winner);
    }
    
    /**
     * Find the fastest implementation.
     */
    private static String findFastest(double linear, double avl, double rbt) {
        if (linear <= avl && linear <= rbt) return "Linear";
        if (avl <= linear && avl <= rbt) return "AVL";
        return "RBT";
    }
    
    /**
     * Generate CSV files for each experiment.
     */
    private static void generateResultCSVs(List<BenchmarkData> allResults) {
        try {
            // Group results by data structure
            Map<String, List<BenchmarkData>> grouped = new HashMap<>();
            grouped.put("LinearList", new ArrayList<>());
            grouped.put("AVLTree", new ArrayList<>());
            grouped.put("RBT", new ArrayList<>());
            
            for (BenchmarkData data : allResults) {
                String prefix = data.structureName.replace(" ", "");
                grouped.get(prefix).add(data);
            }
            
            // Write CSV for each structure to results folder
            writeCSV("results/experiment1_linear_list.csv", grouped.get("LinearList"));
            writeCSV("results/experiment2_avl_tree.csv", grouped.get("AVLTree"));
            writeCSV("results/experiment3_rbt.csv", grouped.get("RBT"));
            
            // Also write to old locations for backward compatibility
            writeCSV("src/main/java/com/reviews/experiments/experiment1/results.csv", grouped.get("LinearList"));
            writeCSV("src/main/java/com/reviews/experiments/experiment2/results.csv", grouped.get("AVLTree"));
            writeCSV("src/main/java/com/reviews/experiments/experiment3/results.csv", grouped.get("RBT"));
            
            // Write unified comparison file
            writeUnifiedComparisonCSV("results/unified_comparison.csv", allResults);
            
        } catch (IOException e) {
            System.err.println("Error writing CSV files: " + e.getMessage());
        }
    }
    
    /**
     * Write CSV file for a specific data structure.
     */
    private static void writeCSV(String filename, List<BenchmarkData> results) throws IOException {
        // Create parent directory if it doesn't exist
        java.io.File file = new java.io.File(filename);
        file.getParentFile().mkdirs();
        
        FileWriter writer = new FileWriter(filename);
        
        // Write header
        writer.append("Data Size,Insertion Time (ms),Search Time (ms),RBAR Calculation (ms),");
        writer.append("Top-5 Retrieval (ms),Top-10 Retrieval (ms),Top-25 Retrieval (ms),Top-50 Retrieval (ms)");
        
        // Add tree-specific columns for AVL and RBT
        if (!results.isEmpty() && results.get(0).results.treeHeight > 0) {
            writer.append(",Tree Height,Is Balanced");
        }
        
        writer.append("\n");
        
        // Write data rows
        for (BenchmarkData data : results) {
            writer.append(String.valueOf(data.dataSize));
            writer.append(",").append(String.format("%.3f", data.results.insertTime));
            writer.append(",").append(String.format("%.3f", data.results.searchTime));
            writer.append(",").append(String.format("%.3f", data.results.rbarTime));
            writer.append(",").append(String.format("%.3f", data.results.topKTimes.get("Top-5")));
            writer.append(",").append(String.format("%.3f", data.results.topKTimes.get("Top-10")));
            writer.append(",").append(String.format("%.3f", data.results.topKTimes.get("Top-25")));
            writer.append(",").append(String.format("%.3f", data.results.topKTimes.get("Top-50")));
            
            if (data.results.treeHeight > 0) {
                writer.append(",").append(String.valueOf(data.results.treeHeight));
                writer.append(",").append(String.valueOf(data.results.isBalanced));
            }
            
            writer.append("\n");
        }
        
        writer.flush();
        writer.close();
        
        System.out.println("✓ Generated: " + filename);
    }
    
    /**
     * Write unified comparison CSV with all three data structures side-by-side.
     */
    private static void writeUnifiedComparisonCSV(String filename, List<BenchmarkData> allResults) throws IOException {
        java.io.File file = new java.io.File(filename);
        file.getParentFile().mkdirs();
        
        FileWriter writer = new FileWriter(filename);
        
        // Write header
        writer.append("Data Size,Structure,Insertion Time (ms),Search Time (ms),RBAR Calculation (ms),");
        writer.append("Top-5 (ms),Top-10 (ms),Top-25 (ms),Top-50 (ms),Tree Height,Is Balanced\n");
        
        // Write data rows sorted by data size, then by structure
        java.util.Map<Integer, java.util.List<BenchmarkData>> groupedBySize = new java.util.LinkedHashMap<>();
        
        for (BenchmarkData data : allResults) {
            groupedBySize.computeIfAbsent(data.dataSize, k -> new java.util.ArrayList<>()).add(data);
        }
        
        for (java.util.Map.Entry<Integer, java.util.List<BenchmarkData>> entry : groupedBySize.entrySet()) {
            for (BenchmarkData data : entry.getValue()) {
                writer.append(String.valueOf(data.dataSize));
                writer.append(",").append(data.structureName);
                writer.append(",").append(String.format("%.3f", data.results.insertTime));
                writer.append(",").append(String.format("%.3f", data.results.searchTime));
                writer.append(",").append(String.format("%.3f", data.results.rbarTime));
                writer.append(",").append(String.format("%.3f", data.results.topKTimes.get("Top-5")));
                writer.append(",").append(String.format("%.3f", data.results.topKTimes.get("Top-10")));
                writer.append(",").append(String.format("%.3f", data.results.topKTimes.get("Top-25")));
                writer.append(",").append(String.format("%.3f", data.results.topKTimes.get("Top-50")));
                
                if (data.results.treeHeight > 0) {
                    writer.append(",").append(String.valueOf(data.results.treeHeight));
                    writer.append(",").append(String.valueOf(data.results.isBalanced));
                } else {
                    writer.append(",N/A,N/A");
                }
                
                writer.append("\n");
            }
        }
        
        writer.flush();
        writer.close();
        
        System.out.println("✓ Generated: " + filename);
    }
    
    /**
     * Result container class.
     */
    private static class BenchmarkResults {
        final double insertTime;
        final double searchTime;
        final double rbarTime;
        final Map<String, Double> topKTimes;
        final int treeHeight;
        final boolean isBalanced;
        
        BenchmarkResults(double insertTime, double searchTime, double rbarTime, 
                        Map<String, Double> topKTimes, int treeHeight, boolean isBalanced) {
            this.insertTime = insertTime;
            this.searchTime = searchTime;
            this.rbarTime = rbarTime;
            this.topKTimes = topKTimes;
            this.treeHeight = treeHeight;
            this.isBalanced = isBalanced;
        }
    }
    
    /**
     * Data container class.
     */
    private static class BenchmarkData {
        final int dataSize;
        final String structureName;
        final BenchmarkResults results;
        
        BenchmarkData(int dataSize, String structureName, BenchmarkResults results) {
            this.dataSize = dataSize;
            this.structureName = structureName;
            this.results = results;
        }
    }
    
    /**
     * Main method to run unified benchmarks.
     */
    public static void main(String[] args) {
        runUnifiedBenchmarks();
    }
}

