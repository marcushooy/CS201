package experiments.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * ResultsWriter - Utility class to write benchmark results to CSV files.
 * 
 * Handles writing individual benchmark results and comparison tables to
 * the experiment_results/ directory.
 */
public class ResultsWriter {
    
    private static final String RESULTS_DIR = "experiments/results";
    
    /**
     * Ensure the experiment_results directory exists.
     * Creates it if it doesn't exist.
     */
    public static void ensureDirectoryExists() {
        File dir = new File(RESULTS_DIR);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("Created directory: " + RESULTS_DIR + "/");
            } else {
                System.err.println("Failed to create directory: " + RESULTS_DIR + "/");
            }
        }
    }
    
    /**
     * Write benchmark results for a single data structure to a CSV file.
     * 
     * @param results List of benchmark results
     * @param filename Output filename (e.g., "linear_list_results.csv")
     */
    public static void writeResults(List<BenchmarkResult> results, String filename) {
        ensureDirectoryExists();
        
        String filepath = RESULTS_DIR + "/" + filename;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filepath))) {
            // Write header
            writer.println("Data Size,Operation,Avg Time (ms),Min Time (ms),Max Time (ms),Iterations");
            
            // Write results
            for (BenchmarkResult result : results) {
                writer.println(result.toCSVRow());
            }
            
            System.out.println("Results written to: " + filepath);
            
        } catch (IOException e) {
            System.err.println("Error writing results to " + filepath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Write a comparison table with all three data structures side by side.
     * 
     * @param linearResults Results from Linear List
     * @param avlResults Results from AVL Tree
     * @param rbtResults Results from RBT
     * @param filename Output filename (e.g., "comparison_summary.csv")
     */
    public static void writeComparisonTable(List<BenchmarkResult> linearResults,
                                           List<BenchmarkResult> avlResults,
                                           List<BenchmarkResult> rbtResults,
                                           String filename) {
        ensureDirectoryExists();
        
        String filepath = RESULTS_DIR + "/" + filename;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filepath))) {
            // Write header
            writer.println("Data Size,Operation,Linear List (ms),AVL Tree (ms),RBT (ms),Winner,Best Time (ms),Speedup vs Worst");
            
            // Group results by data size and operation
            Map<String, ComparisonRow> rowMap = new HashMap<>();
            
            // Process Linear List results
            for (BenchmarkResult result : linearResults) {
                String key = result.getDataSize() + "_" + result.getOperation();
                ComparisonRow row = rowMap.getOrDefault(key, new ComparisonRow());
                row.dataSize = result.getDataSize();
                row.operation = result.getOperation();
                row.linearTime = result.getAvgTimeMs();
                rowMap.put(key, row);
            }
            
            // Process AVL Tree results
            for (BenchmarkResult result : avlResults) {
                String key = result.getDataSize() + "_" + result.getOperation();
                ComparisonRow row = rowMap.getOrDefault(key, new ComparisonRow());
                row.dataSize = result.getDataSize();
                row.operation = result.getOperation();
                row.avlTime = result.getAvgTimeMs();
                rowMap.put(key, row);
            }
            
            // Process RBT results
            for (BenchmarkResult result : rbtResults) {
                String key = result.getDataSize() + "_" + result.getOperation();
                ComparisonRow row = rowMap.getOrDefault(key, new ComparisonRow());
                row.dataSize = result.getDataSize();
                row.operation = result.getOperation();
                row.rbtTime = result.getAvgTimeMs();
                rowMap.put(key, row);
            }
            
            // Write comparison rows
            for (ComparisonRow row : rowMap.values()) {
                row.calculateWinner();
                writer.println(row.toCSVRow());
            }
            
            System.out.println("Comparison table written to: " + filepath);
            
        } catch (IOException e) {
            System.err.println("Error writing comparison to " + filepath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Helper class to represent a single row in the comparison table.
     */
    private static class ComparisonRow {
        int dataSize;
        String operation;
        double linearTime = -1;
        double avlTime = -1;
        double rbtTime = -1;
        String winner;
        double bestTime;
        double speedup;
        
        void calculateWinner() {
            // Find best time (excluding negative values which mean no data)
            bestTime = Double.MAX_VALUE;
            winner = "N/A";
            
            if (linearTime > 0 && linearTime < bestTime) {
                bestTime = linearTime;
                winner = "Linear List";
            }
            if (avlTime > 0 && avlTime < bestTime) {
                bestTime = avlTime;
                winner = "AVL Tree";
            }
            if (rbtTime > 0 && rbtTime < bestTime) {
                bestTime = rbtTime;
                winner = "RBT";
            }
            
            // Calculate speedup (worst time / best time)
            double worstTime = Math.max(linearTime, Math.max(avlTime, rbtTime));
            if (bestTime > 0 && worstTime > 0) {
                speedup = worstTime / bestTime;
            } else {
                speedup = 1.0;
            }
        }
        
        String toCSVRow() {
            String linearStr = linearTime > 0 ? String.format("%.6f", linearTime) : "N/A";
            String avlStr = avlTime > 0 ? String.format("%.6f", avlTime) : "N/A";
            String rbtStr = rbtTime > 0 ? String.format("%.6f", rbtTime) : "N/A";
            String bestStr = bestTime < Double.MAX_VALUE ? String.format("%.6f", bestTime) : "N/A";
            String speedupStr = String.format("%.2fx", speedup);
            
            return String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                               dataSize, operation, linearStr, avlStr, rbtStr, winner, bestStr, speedupStr);
        }
    }
    
    /**
     * Append a single result to a CSV file (for incremental writing).
     * 
     * @param result The benchmark result to append
     * @param filename Output filename
     */
    public static void appendResult(BenchmarkResult result, String filename) {
        ensureDirectoryExists();
        
        String filepath = RESULTS_DIR + "/" + filename;
        File file = new File(filepath);
        boolean fileExists = file.exists();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filepath, true))) {
            // Write header if file doesn't exist
            if (!fileExists) {
                writer.println("Data Size,Operation,Avg Time (ms),Min Time (ms),Max Time (ms),Iterations");
            }
            
            // Write result
            writer.println(result.toCSVRow());
            
        } catch (IOException e) {
            System.err.println("Error appending result to " + filepath + ": " + e.getMessage());
        }
    }
}

