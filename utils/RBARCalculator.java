package utils;

import models.AirlineReview;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * RBARCalculator - Utility class for calculating Recency-Biased Average Rating.
 * 
 * This class provides shared RBAR calculation logic that all data structures
 * (Linear List, AVL Tree, RBT Tree) will use to ensure consistent results.
 * 
 * Weighting scheme:
 * - Recent reviews (last 30 days): weight = 1.0 (full weight)
 * - Medium age reviews (30 days to 3 years): linear decay from 1.0 to 0.1
 * - Old reviews (3+ years): weight = 0.05 (minimal weight)
 */
public class RBARCalculator {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Calculate RBAR for a list of reviews.
     * This method can be used by any data structure that provides a list of reviews.
     * 
     * @param reviews List of AirlineReview objects
     * @return Recency-Biased Average Rating
     */
    public static double calculateRBAR(List<AirlineReview> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        
        LocalDate now = LocalDate.now();
        LocalDate thirtyDaysAgo = now.minusDays(30);
        LocalDate threeYearsAgo = now.minusYears(3);
        
        double weightedSum = 0.0;
        double totalWeight = 0.0;
        
        for (AirlineReview review : reviews) {
            LocalDate reviewDate = parseDate(review.getDate());
            double weight = calculateRecencyWeight(reviewDate, thirtyDaysAgo, threeYearsAgo);
            
            weightedSum += review.getOverallRating() * weight;
            totalWeight += weight;
        }
        
        return totalWeight > 0 ? weightedSum / totalWeight : 0.0;
    }
    
    /**
     * Calculate the weight for a review based on its recency.
     * This is the core weighting algorithm shared by all data structures.
     * 
     * @param reviewDate The date of the review
     * @param thirtyDaysAgo Date 30 days ago (cutoff for recent reviews)
     * @param threeYearsAgo Date 3 years ago (cutoff for old reviews)
     * @return Weight value between 0.05 and 1.0
     */
    public static double calculateRecencyWeight(LocalDate reviewDate, LocalDate thirtyDaysAgo, LocalDate threeYearsAgo) {
        if (reviewDate.isAfter(thirtyDaysAgo)) {
            // Recent reviews (last 30 days): weight = 1.0
            return 1.0;
        } else if (reviewDate.isAfter(threeYearsAgo)) {
            // Medium age reviews (30 days to 3 years): linear decay
            long daysSinceThirtyDays = ChronoUnit.DAYS.between(thirtyDaysAgo, reviewDate);
            long totalDays = ChronoUnit.DAYS.between(threeYearsAgo, thirtyDaysAgo);
            return Math.max(0.1, 1.0 - (double) daysSinceThirtyDays / totalDays);
        } else {
            // Old reviews (3+ years): minimal weight
            return 0.05;
        }
    }
    
    /**
     * Parse date string to LocalDate.
     * Handles the standard CSV date format (YYYY-MM-DD).
     * 
     * @param dateStr Date string in format "YYYY-MM-DD"
     * @return LocalDate object, or very old date (1900-01-01) if parsing fails
     */
    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (Exception e) {
            // If parsing fails, return a very old date to minimize its weight
            return LocalDate.of(1900, 1, 1);
        }
    }
}

