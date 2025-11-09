import utils.CSVLoader;
import models.AirlineReview;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 * Test to check for remaining CSV parsing errors
 */
public class TestCSVErrors {
    public static void main(String[] args) {
        System.out.println("Analyzing CSV parsing errors...");
        System.out.println("=".repeat(60));
        
        String csvPath = "data/airline.csv";
        
        // First, count total lines in CSV
        int totalLines = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            while (br.readLine() != null) {
                totalLines++;
            }
        } catch (Exception e) {
            System.err.println("Error counting lines: " + e.getMessage());
        }
        
        System.out.println("Total lines in CSV (including header): " + totalLines);
        System.out.println("Expected records (excluding header): " + (totalLines - 1));
        System.out.println();
        
        // Now load reviews and capture output
        System.out.println("Loading reviews...");
        System.out.println("-".repeat(60));
        List<AirlineReview> reviews = CSVLoader.loadAirlineReviews(csvPath);
        System.out.println("-".repeat(60));
        
        System.out.println();
        System.out.println("=".repeat(60));
        System.out.println("SUMMARY:");
        System.out.println("  Expected records: " + (totalLines - 1));
        System.out.println("  Successfully loaded: " + reviews.size());
        System.out.println("  Failed/Skipped: " + ((totalLines - 1) - reviews.size()));
        System.out.println("  Success rate: " + String.format("%.2f%%", 
            (reviews.size() * 100.0 / (totalLines - 1))));
        System.out.println("=".repeat(60));
    }
}

