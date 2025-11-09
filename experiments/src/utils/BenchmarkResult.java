package experiments.utils;

/**
 * BenchmarkResult - Data class to store benchmark results (runtime metrics only).
 * 
 * This class captures the essential performance metrics for a single benchmark test:
 * - Which data structure was tested
 * - Which operation was benchmarked
 * - How much data was in the store
 * - Runtime statistics (avg, min, max)
 * - Number of iterations performed
 */
public class BenchmarkResult {
    private final String dataStructure;  // e.g., "Linear List", "AVL Tree", "RBT"
    private final String operation;      // e.g., "CREATE", "READ", "UPDATE", "DELETE", "RBAR", "RANKINGS", "TOPK"
    private final int dataSize;          // Number of reviews in the store
    private final double avgTimeMs;      // Average time in milliseconds
    private final double minTimeMs;      // Minimum time observed
    private final double maxTimeMs;      // Maximum time observed
    private final int iterations;        // Number of iterations run
    
    /**
     * Constructor for BenchmarkResult.
     * 
     * @param dataStructure Name of the data structure tested
     * @param operation Name of the operation benchmarked
     * @param dataSize Number of reviews in the store
     * @param avgTimeMs Average time in milliseconds
     * @param minTimeMs Minimum time in milliseconds
     * @param maxTimeMs Maximum time in milliseconds
     * @param iterations Number of iterations performed
     */
    public BenchmarkResult(String dataStructure, String operation, int dataSize,
                          double avgTimeMs, double minTimeMs, double maxTimeMs, int iterations) {
        this.dataStructure = dataStructure;
        this.operation = operation;
        this.dataSize = dataSize;
        this.avgTimeMs = avgTimeMs;
        this.minTimeMs = minTimeMs;
        this.maxTimeMs = maxTimeMs;
        this.iterations = iterations;
    }
    
    // Getters
    public String getDataStructure() { return dataStructure; }
    public String getOperation() { return operation; }
    public int getDataSize() { return dataSize; }
    public double getAvgTimeMs() { return avgTimeMs; }
    public double getMinTimeMs() { return minTimeMs; }
    public double getMaxTimeMs() { return maxTimeMs; }
    public int getIterations() { return iterations; }
    
    /**
     * Format result as CSV row.
     * Format: DataSize,Operation,AvgTime,MinTime,MaxTime,Iterations
     */
    public String toCSVRow() {
        return String.format("%d,%s,%.6f,%.6f,%.6f,%d",
                           dataSize, operation, avgTimeMs, minTimeMs, maxTimeMs, iterations);
    }
    
    @Override
    public String toString() {
        return String.format("%s [%s] @ %d reviews: avg=%.6f ms, min=%.6f ms, max=%.6f ms (%d iterations)",
                           dataStructure, operation, dataSize, avgTimeMs, minTimeMs, maxTimeMs, iterations);
    }
}

