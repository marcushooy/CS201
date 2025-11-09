package experiments;

import experiments.benchmarks.*;
import experiments.utils.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UnifiedBenchmarkRunner - Main class to run all performance benchmarks.
 * 
 * This class orchestrates the execution of benchmarks for all three data structures
 * (Linear List, AVL Tree, RBT) across different data sizes, and generates comparison reports.
 * 
 * Usage:
 *   java -cp . experiments.UnifiedBenchmarkRunner
 * 
 * Output:
 *   - experiment_results/linear_list_results.csv
 *   - experiment_results/avl_tree_results.csv
 *   - experiment_results/rbt_results.csv
 *   - experiment_results/comparison_summary.csv
 */
public class UnifiedBenchmarkRunner {
    
    public static void main(String[] args) {
        System.out.println("╔" + "═".repeat(78) + "╗");
        System.out.println("║" + centerText("PERFORMANCE BENCHMARK RUNNER", 78) + "║");
        System.out.println("║" + centerText("Comparing Linear List, AVL Tree, and RBT", 78) + "║");
        System.out.println("╚" + "═".repeat(78) + "╝");
        System.out.println();
        
        // Ensure output directory exists
        ResultsWriter.ensureDirectoryExists();
        
        // Data sizes to test
        int[] dataSizes = PerformanceBenchmark.TEST_SIZES;
        
        // Storage for all results
        List<BenchmarkResult> allLinearResults = new ArrayList<>();
        List<BenchmarkResult> allAVLResults = new ArrayList<>();
        List<BenchmarkResult> allRBTResults = new ArrayList<>();
        
        // Run benchmarks for each data size
        for (int dataSize : dataSizes) {
            System.out.println("\n" + "╔" + "═".repeat(78) + "╗");
            System.out.println("║" + centerText("Testing with " + dataSize + " reviews", 78) + "║");
            System.out.println("╚" + "═".repeat(78) + "╝");
            
            try {
                // Linear List benchmarks
                System.out.println("\n>>> Running Linear List benchmarks...");
                List<BenchmarkResult> linearResults = LinearListBenchmark.runAllBenchmarks(dataSize);
                allLinearResults.addAll(linearResults);
                
                // AVL Tree benchmarks
                System.out.println("\n>>> Running AVL Tree benchmarks...");
                List<BenchmarkResult> avlResults = AVLTreeBenchmark.runAllBenchmarks(dataSize);
                allAVLResults.addAll(avlResults);
                
                // RBT benchmarks
                System.out.println("\n>>> Running RBT benchmarks...");
                List<BenchmarkResult> rbtResults = RBTBenchmark.runAllBenchmarks(dataSize);
                allRBTResults.addAll(rbtResults);
                
                // Print comparison for this data size
                printComparison(dataSize, linearResults, avlResults, rbtResults);
                
            } catch (Exception e) {
                System.err.println("ERROR during benchmarks for size " + dataSize + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // Write results to CSV files
        System.out.println("\n" + "╔" + "═".repeat(78) + "╗");
        System.out.println("║" + centerText("Writing Results to CSV Files", 78) + "║");
        System.out.println("╚" + "═".repeat(78) + "╝");
        
        ResultsWriter.writeResults(allLinearResults, "linear_list_results.csv");
        ResultsWriter.writeResults(allAVLResults, "avl_tree_results.csv");
        ResultsWriter.writeResults(allRBTResults, "rbt_results.csv");
        ResultsWriter.writeComparisonTable(allLinearResults, allAVLResults, allRBTResults, "comparison_summary.csv");
        
        // Print final summary
        printFinalSummary(allLinearResults, allAVLResults, allRBTResults);
        
        System.out.println("\n" + "╔" + "═".repeat(78) + "╗");
        System.out.println("║" + centerText("Benchmark Complete! Results saved to experiment_results/", 78) + "║");
        System.out.println("╚" + "═".repeat(78) + "╝");
    }
    
    /**
     * Print comparison table for a specific data size.
     */
    private static void printComparison(int dataSize, List<BenchmarkResult> linear,
                                       List<BenchmarkResult> avl, List<BenchmarkResult> rbt) {
        System.out.println("\n" + "─".repeat(80));
        System.out.println("COMPARISON FOR " + dataSize + " REVIEWS");
        System.out.println("─".repeat(80));
        System.out.printf("%-12s | %-15s | %-15s | %-15s | %-12s%n",
                         "Operation", "Linear List", "AVL Tree", "RBT", "Winner");
        System.out.println("─".repeat(80));
        
        // Group results by operation
        for (int i = 0; i < Math.min(linear.size(), Math.min(avl.size(), rbt.size())); i++) {
            BenchmarkResult lr = linear.get(i);
            BenchmarkResult ar = avl.get(i);
            BenchmarkResult rr = rbt.get(i);
            
            if (!lr.getOperation().equals(ar.getOperation()) ||
                !lr.getOperation().equals(rr.getOperation())) {
                continue; // Skip if operations don't match
            }
            
            // Find winner
            double minTime = Math.min(lr.getAvgTimeMs(), Math.min(ar.getAvgTimeMs(), rr.getAvgTimeMs()));
            String winner = "";
            if (Math.abs(lr.getAvgTimeMs() - minTime) < 0.000001) winner = "Linear List ⭐";
            else if (Math.abs(ar.getAvgTimeMs() - minTime) < 0.000001) winner = "AVL Tree ⭐";
            else if (Math.abs(rr.getAvgTimeMs() - minTime) < 0.000001) winner = "RBT ⭐";
            
            System.out.printf("%-12s | %15s | %15s | %15s | %-12s%n",
                             lr.getOperation(),
                             PerformanceBenchmark.formatTime(lr.getAvgTimeMs()),
                             PerformanceBenchmark.formatTime(ar.getAvgTimeMs()),
                             PerformanceBenchmark.formatTime(rr.getAvgTimeMs()),
                             winner);
        }
        System.out.println("─".repeat(80));
    }
    
    /**
     * Print final summary across all data sizes.
     */
    private static void printFinalSummary(List<BenchmarkResult> linear,
                                         List<BenchmarkResult> avl,
                                         List<BenchmarkResult> rbt) {
        System.out.println("\n" + "╔" + "═".repeat(78) + "╗");
        System.out.println("║" + centerText("FINAL SUMMARY", 78) + "║");
        System.out.println("╚" + "═".repeat(78) + "╝");
        
        System.out.println("\nTotal benchmarks run:");
        System.out.println("  Linear List: " + linear.size() + " tests");
        System.out.println("  AVL Tree:    " + avl.size() + " tests");
        System.out.println("  RBT:         " + rbt.size() + " tests");
        
        // Count winners by operation type
        System.out.println("\nWinner analysis by operation:");
        countWinners("CREATE", linear, avl, rbt);
        countWinners("READ", linear, avl, rbt);
        countWinners("UPDATE", linear, avl, rbt);
        countWinners("DELETE", linear, avl, rbt);
        countWinners("RBAR", linear, avl, rbt);
        countWinners("RANKINGS", linear, avl, rbt);
        countWinners("TOPK", linear, avl, rbt);
        
        System.out.println("\nFiles generated:");
        System.out.println("  ✓ experiment_results/linear_list_results.csv");
        System.out.println("  ✓ experiment_results/avl_tree_results.csv");
        System.out.println("  ✓ experiment_results/rbt_results.csv");
        System.out.println("  ✓ experiment_results/comparison_summary.csv");
    }
    
    /**
     * Count winners for a specific operation across all data sizes.
     */
    private static void countWinners(String operation, List<BenchmarkResult> linear,
                                    List<BenchmarkResult> avl, List<BenchmarkResult> rbt) {
        int linearWins = 0, avlWins = 0, rbtWins = 0;
        
        // Get results for this operation
        List<BenchmarkResult> linearOp = filterByOperation(linear, operation);
        List<BenchmarkResult> avlOp = filterByOperation(avl, operation);
        List<BenchmarkResult> rbtOp = filterByOperation(rbt, operation);
        
        // Compare across data sizes
        for (int i = 0; i < Math.min(linearOp.size(), Math.min(avlOp.size(), rbtOp.size())); i++) {
            double linearTime = linearOp.get(i).getAvgTimeMs();
            double avlTime = avlOp.get(i).getAvgTimeMs();
            double rbtTime = rbtOp.get(i).getAvgTimeMs();
            
            double minTime = Math.min(linearTime, Math.min(avlTime, rbtTime));
            
            if (Math.abs(linearTime - minTime) < 0.000001) linearWins++;
            else if (Math.abs(avlTime - minTime) < 0.000001) avlWins++;
            else if (Math.abs(rbtTime - minTime) < 0.000001) rbtWins++;
        }
        
        if (linearWins + avlWins + rbtWins > 0) {
            System.out.printf("  %-10s: Linear=%d, AVL=%d, RBT=%d%n",
                             operation, linearWins, avlWins, rbtWins);
        }
    }
    
    /**
     * Filter results by operation name.
     */
    private static List<BenchmarkResult> filterByOperation(List<BenchmarkResult> results, String operation) {
        List<BenchmarkResult> filtered = new ArrayList<>();
        for (BenchmarkResult result : results) {
            if (result.getOperation().equals(operation)) {
                filtered.add(result);
            }
        }
        return filtered;
    }
    
    /**
     * Center text within a given width.
     */
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) sb.append(" ");
        sb.append(text);
        while (sb.length() < width) sb.append(" ");
        return sb.toString();
    }
}

