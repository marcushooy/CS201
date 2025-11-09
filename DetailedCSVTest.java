import models.AirlineReview;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Detailed test to capture and analyze CSV parsing errors
 */
public class DetailedCSVTest {
    
    public static void main(String[] args) {
        System.out.println("Detailed CSV Parsing Analysis");
        System.out.println("=".repeat(70));
        
        String csvPath = "data/airline.csv";
        List<AirlineReview> reviews = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            // Skip header
            String header = readCompleteCSVRecord(br);
            
            String record;
            int recordNumber = 0;
            while ((record = readCompleteCSVRecord(br)) != null) {
                recordNumber++;
                
                // Capture stderr to catch error messages
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                PrintStream oldErr = System.err;
                System.setErr(ps);
                
                try {
                    String reviewId = "REV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                    AirlineReview review = parseCSVLine(record, reviewId);
                    if (review != null) {
                        reviews.add(review);
                        successCount++;
                    } else {
                        failCount++;
                        String error = baos.toString().trim();
                        if (!error.isEmpty()) {
                            errorMessages.add("Record " + recordNumber + ": " + error);
                        }
                    }
                } catch (Exception e) {
                    failCount++;
                    errorMessages.add("Record " + recordNumber + ": Exception - " + e.getMessage());
                } finally {
                    System.setErr(oldErr);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        
        System.out.println("\nRESULTS:");
        System.out.println("  Successfully loaded: " + successCount);
        System.out.println("  Failed/Skipped: " + failCount);
        System.out.println("  Success rate: " + String.format("%.2f%%", (successCount * 100.0 / (successCount + failCount))));
        
        if (!errorMessages.isEmpty()) {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("ERROR DETAILS (showing first 20):");
            System.out.println("=".repeat(70));
            int shown = 0;
            for (String error : errorMessages) {
                System.out.println(error);
                shown++;
                if (shown >= 20) {
                    if (errorMessages.size() > 20) {
                        System.out.println("... and " + (errorMessages.size() - 20) + " more errors");
                    }
                    break;
                }
            }
        }
    }
    
    // Copy of readCompleteCSVRecord from CSVLoader
    private static String readCompleteCSVRecord(BufferedReader br) throws IOException {
        StringBuilder record = new StringBuilder();
        String line;
        boolean inQuotes = false;
        
        while ((line = br.readLine()) != null) {
            if (record.length() > 0) {
                record.append('\n');
            }
            record.append(line);
            
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c == '"') {
                    if (i < line.length() - 1 && line.charAt(i + 1) == '"') {
                        i++;
                    } else {
                        inQuotes = !inQuotes;
                    }
                }
            }
            
            if (!inQuotes) {
                return record.toString();
            }
        }
        
        return record.length() > 0 ? record.toString() : null;
    }
    
    // Copy of parseCSVLine from CSVLoader
    private static AirlineReview parseCSVLine(String line, String reviewId) {
        List<String> fields = parseCSVFields(line);
        
        if (fields.size() < 20) {
            System.err.println("Invalid CSV line - expected 20 fields, got " + fields.size());
            return null;
        }
        
        try {
            String airlineName = fields.get(0);
            String link = fields.get(1);
            String title = fields.get(2);
            String author = fields.get(3);
            String authorCountry = fields.get(4);
            String date = fields.get(5);
            String content = fields.get(6);
            String aircraft = fields.get(7);
            String typeTraveller = fields.get(8);
            String cabinFlown = fields.get(9);
            String route = fields.get(10);
            
            double overallRating = parseDouble(fields.get(11), 0.0);
            double seatComfortRating = parseDouble(fields.get(12), 0.0);
            double cabinStaffRating = parseDouble(fields.get(13), 0.0);
            double foodBeveragesRating = parseDouble(fields.get(14), 0.0);
            double inflightEntertainmentRating = parseDouble(fields.get(15), 0.0);
            double groundServiceRating = parseDouble(fields.get(16), 0.0);
            double wifiConnectivityRating = parseDouble(fields.get(17), 0.0);
            double valueMoneyRating = parseDouble(fields.get(18), 0.0);
            int recommended = parseInt(fields.get(19), 0);
            
            return new AirlineReview(
                reviewId, airlineName, link, title, author, authorCountry, date, content,
                aircraft, typeTraveller, cabinFlown, route,
                overallRating, seatComfortRating, cabinStaffRating,
                foodBeveragesRating, inflightEntertainmentRating,
                groundServiceRating, wifiConnectivityRating,
                valueMoneyRating, recommended
            );
        } catch (Exception e) {
            System.err.println("Error creating AirlineReview: " + e.getMessage());
            return null;
        }
    }
    
    // Copy of parseCSVFields from CSVLoader
    private static List<String> parseCSVFields(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (i < line.length() - 1 && line.charAt(i + 1) == '"') {
                    currentField.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString().trim());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        fields.add(currentField.toString().trim());
        
        return fields;
    }
    
    private static double parseDouble(String str, double defaultValue) {
        if (str == null || str.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    private static int parseInt(String str, int defaultValue) {
        if (str == null || str.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}

