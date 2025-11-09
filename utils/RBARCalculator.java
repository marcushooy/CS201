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
 * - Recent reviews (last 6 months): weight = 1.0 (full weight)
 * - Medium age reviews (6 months to 3 years): exponential decay using formula:
 *   weight = 0.05 + 0.95 * (1 - ((m - 6) / (N - 6))^p)
 *   where m = age in months, N = 36 months, p = power parameter for decay curve
 *   p = 1.26, reviews that are 2 years oldget approximately 0.5 weight
 * - Old reviews (3+ years): weight = 0.05 (minimal weight)
 */
public class RBARCalculator {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final double POWER_PARAMETER = 1.26; // Controls steepness of exponential decay
    
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

        // Set to the most recent date in airline.csv, but will use LocalDate.now() if updated datasets are used with updated rankings
        LocalDate now = LocalDate.of(2015, 8, 2);
        LocalDate sixMonthsAgo = now.minusMonths(6);
        LocalDate threeYearsAgo = now.minusYears(3);
        
        double weightedSum = 0.0;
        double totalWeight = 0.0;
        
        for (AirlineReview review : reviews) {
            LocalDate reviewDate = parseDate(review.getDate());
            double weight = calculateRecencyWeight(reviewDate, now, sixMonthsAgo, threeYearsAgo);
            
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
     * @param now Current reference date (2015-08-02 for this dataset)
     * @param sixMonthsAgo Date 6 months ago (cutoff for recent reviews)
     * @param threeYearsAgo Date 3 years ago (cutoff for old reviews)
     * @return Weight value between 0.05 and 1.0
     */
    public static double calculateRecencyWeight(LocalDate reviewDate, LocalDate now, LocalDate sixMonthsAgo, LocalDate threeYearsAgo) {
        if (reviewDate.isAfter(sixMonthsAgo) || reviewDate.isEqual(sixMonthsAgo)) {
            // Recent reviews (last 6 months): weight = 1.0
            return 1.0;
        } else if (reviewDate.isAfter(threeYearsAgo)) {
            // Medium age reviews (6 months to 3 years): exponential decay
            // Calculate age in months
            long monthsOld = ChronoUnit.MONTHS.between(reviewDate, now);
            double m = (double) monthsOld; // Age in months
            double N = 36.0; // 3 years in months
            
            // Formula: weight = 0.05 + 0.95 * (1 - ((m - 6) / (N - 6))^p)
            double normalizedAge = (m - 6.0) / (N - 6.0); // Range: 0 to 1
            double decayFactor = Math.pow(normalizedAge, POWER_PARAMETER);
            double weight = 0.05 + 0.95 * (1.0 - decayFactor);
            
            return Math.max(0.05, Math.min(1.0, weight)); // Clamp between 0.05 and 1.0
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

