package com.reviews.experiments;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Automatically generates chart-ready CSV files from benchmark results.
 * Creates 4 separate CSV files - one for each operation - optimized for Excel/Sheets charting.
 */
public class ChartGenerator {
    
    /**
     * Generate all 4 chart files from unified comparison data.
     */
    public static void generateAllCharts(String unifiedComparisonPath, String outputDir) {
        try {
            // Read the unified comparison CSV
            Map<String, Map<Integer, OperationData>> data = parseUnifiedComparison(unifiedComparisonPath);
            
            // Generate each chart file
            generateSearchChart(data, outputDir + "/1_search_comparison.csv");
            generateInsertionChart(data, outputDir + "/2_insertion_comparison.csv");
            generateRBARChart(data, outputDir + "/3_rbar_comparison.csv");
            generateTopKChart(data, outputDir + "/4_topk_comparison.csv");
            
            System.out.println("\n✅ Generated 4 chart-ready CSV files:");
            System.out.println("   • results/1_search_comparison.csv");
            System.out.println("   • results/2_insertion_comparison.csv");
            System.out.println("   • results/3_rbar_comparison.csv");
            System.out.println("   • results/4_topk_comparison.csv");
            System.out.println("   📊 Ready to import into Excel/Google Sheets!");
            
        } catch (IOException e) {
            System.err.println("Error generating chart files: " + e.getMessage());
        }
    }
    
    /**
     * Parse unified comparison CSV into structured data.
     */
    private static Map<String, Map<Integer, OperationData>> parseUnifiedComparison(String filepath) throws IOException {
        Map<String, Map<Integer, OperationData>> data = new HashMap<>();
        
        // Initialize structure names
        data.put("Linear List", new TreeMap<>());
        data.put("AVL Tree", new TreeMap<>());
        data.put("RBT", new TreeMap<>());
        
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(filepath));
        String line;
        boolean isFirstLine = true;
        
        while ((line = reader.readLine()) != null) {
            if (isFirstLine) {
                isFirstLine = false;
                continue; // Skip header
            }
            
            String[] parts = line.split(",");
            if (parts.length < 11) continue;
            
            try {
                int dataSize = Integer.parseInt(parts[0]);
                String structure = parts[1];
                double insertTime = Double.parseDouble(parts[2]);
                double searchTime = Double.parseDouble(parts[3]);
                double rbarTime = Double.parseDouble(parts[4]);
                double topKTime = Double.parseDouble(parts[8]); // Top-50
                
                OperationData opData = new OperationData(insertTime, searchTime, rbarTime, topKTime);
                data.get(structure).put(dataSize, opData);
                
            } catch (Exception e) {
                // Skip malformed lines
            }
        }
        
        reader.close();
        return data;
    }
    
    /**
     * Generate search comparison chart CSV.
     */
    private static void generateSearchChart(Map<String, Map<Integer, OperationData>> data, String filepath) throws IOException {
        FileWriter writer = new FileWriter(filepath);
        
        // Header
        writer.write("Data Size,Linear List,AVL Tree,RBT\n");
        
        // Get all data sizes (sorted)
        Set<Integer> sizes = data.get("Linear List").keySet();
        
        for (Integer size : sizes) {
            writer.write(String.valueOf(size));
            writer.write(",");
            writer.write(String.format("%.3f", data.get("Linear List").get(size).searchTime));
            writer.write(",");
            writer.write(String.format("%.3f", data.get("AVL Tree").get(size).searchTime));
            writer.write(",");
            writer.write(String.format("%.3f", data.get("RBT").get(size).searchTime));
            writer.write("\n");
        }
        
        writer.close();
    }
    
    /**
     * Generate insertion comparison chart CSV.
     */
    private static void generateInsertionChart(Map<String, Map<Integer, OperationData>> data, String filepath) throws IOException {
        FileWriter writer = new FileWriter(filepath);
        
        // Header
        writer.write("Data Size,Linear List,AVL Tree,RBT\n");
        
        // Get all data sizes (sorted)
        Set<Integer> sizes = data.get("Linear List").keySet();
        
        for (Integer size : sizes) {
            writer.write(String.valueOf(size));
            writer.write(",");
            writer.write(String.format("%.3f", data.get("Linear List").get(size).insertTime));
            writer.write(",");
            writer.write(String.format("%.3f", data.get("AVL Tree").get(size).insertTime));
            writer.write(",");
            writer.write(String.format("%.3f", data.get("RBT").get(size).insertTime));
            writer.write("\n");
        }
        
        writer.close();
    }
    
    /**
     * Generate RBAR comparison chart CSV.
     */
    private static void generateRBARChart(Map<String, Map<Integer, OperationData>> data, String filepath) throws IOException {
        FileWriter writer = new FileWriter(filepath);
        
        // Header
        writer.write("Data Size,Linear List,AVL Tree,RBT\n");
        
        // Get all data sizes (sorted)
        Set<Integer> sizes = data.get("Linear List").keySet();
        
        for (Integer size : sizes) {
            writer.write(String.valueOf(size));
            writer.write(",");
            writer.write(String.format("%.3f", data.get("Linear List").get(size).rbarTime));
            writer.write(",");
            writer.write(String.format("%.3f", data.get("AVL Tree").get(size).rbarTime));
            writer.write(",");
            writer.write(String.format("%.3f", data.get("RBT").get(size).rbarTime));
            writer.write("\n");
        }
        
        writer.close();
    }
    
    /**
     * Generate Top-K comparison chart CSV.
     */
    private static void generateTopKChart(Map<String, Map<Integer, OperationData>> data, String filepath) throws IOException {
        FileWriter writer = new FileWriter(filepath);
        
        // Header
        writer.write("Data Size,Linear List,AVL Tree,RBT\n");
        
        // Get all data sizes (sorted)
        Set<Integer> sizes = data.get("Linear List").keySet();
        
        for (Integer size : sizes) {
            writer.write(String.valueOf(size));
            writer.write(",");
            writer.write(String.format("%.3f", data.get("Linear List").get(size).topKTime));
            writer.write(",");
            writer.write(String.format("%.3f", data.get("AVL Tree").get(size).topKTime));
            writer.write(",");
            writer.write(String.format("%.3f", data.get("RBT").get(size).topKTime));
            writer.write("\n");
        }
        
        writer.close();
    }
    
    /**
     * Data class to hold operation times.
     */
    private static class OperationData {
        final double insertTime;
        final double searchTime;
        final double rbarTime;
        final double topKTime;
        
        OperationData(double insertTime, double searchTime, double rbarTime, double topKTime) {
            this.insertTime = insertTime;
            this.searchTime = searchTime;
            this.rbarTime = rbarTime;
            this.topKTime = topKTime;
        }
    }
    
    /**
     * Main method for standalone chart generation.
     */
    public static void main(String[] args) {
        String unifiedCsvPath = "results/unified_comparison.csv";
        String outputDir = "results";
        
        System.out.println("📊 Generating chart-ready CSV files...");
        generateAllCharts(unifiedCsvPath, outputDir);
        System.out.println("\n✅ Done! Import these files into Excel/Sheets to create charts.");
    }
}

