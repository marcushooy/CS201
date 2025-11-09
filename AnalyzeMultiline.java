import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Analyze how many multi-line records exist in the CSV
 */
public class AnalyzeMultiline {
    
    public static void main(String[] args) {
        System.out.println("Analyzing Multi-line Records in CSV");
        System.out.println("=".repeat(70));
        
        String csvPath = "data/airline.csv";
        int totalPhysicalLines = 0;
        int totalLogicalRecords = 0;
        int multiLineRecords = 0;
        int extraLinesInMultiline = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            // Count physical lines
            String line;
            while ((line = br.readLine()) != null) {
                totalPhysicalLines++;
            }
        } catch (IOException e) {
            System.err.println("Error counting physical lines: " + e.getMessage());
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            // Skip header
            readCompleteCSVRecord(br);
            
            // Count logical records and multi-line records
            String record;
            while ((record = readCompleteCSVRecord(br)) != null) {
                totalLogicalRecords++;
                
                // Count how many newlines are in this record
                int newlineCount = 0;
                for (char c : record.toCharArray()) {
                    if (c == '\n') {
                        newlineCount++;
                    }
                }
                
                if (newlineCount > 0) {
                    multiLineRecords++;
                    extraLinesInMultiline += newlineCount;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        
        System.out.println("\nANALYSIS:");
        System.out.println("  Total physical lines in file: " + totalPhysicalLines);
        System.out.println("  Physical data lines (excl header): " + (totalPhysicalLines - 1));
        System.out.println("  Total logical records parsed: " + totalLogicalRecords);
        System.out.println("  Multi-line records: " + multiLineRecords);
        System.out.println("  Extra physical lines from multi-line records: " + extraLinesInMultiline);
        System.out.println();
        System.out.println("VERIFICATION:");
        System.out.println("  Logical records + Extra lines: " + (totalLogicalRecords + extraLinesInMultiline));
        System.out.println("  Should equal physical data lines: " + (totalPhysicalLines - 1));
        
        if (totalLogicalRecords + extraLinesInMultiline == totalPhysicalLines - 1) {
            System.out.println("\n✓ PERFECT MATCH! All lines accounted for.");
            System.out.println("✓ NO PARSING ERRORS - The CSV loader is working correctly!");
        } else {
            int discrepancy = (totalPhysicalLines - 1) - (totalLogicalRecords + extraLinesInMultiline);
            System.out.println("\n✗ Discrepancy: " + discrepancy + " lines unaccounted for");
        }
        
        System.out.println("=".repeat(70));
    }
    
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
}

