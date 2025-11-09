package com.reviews.datastructures;

import com.reviews.Models.ReviewRecord;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Baseline implementation using an ArrayList for storing reviews.
 * This provides O(N) time complexity for most operations and serves as our baseline
 * for performance comparison against other  data structures.
 * 
 * Time Complexities:
 * - Insert: O(1) amortized
 * - Top-k Recent Retrieval: O(N log N) due to sorting
 * - Recency-Biased Average Rating: O(N)
 * - Search by airline: O(N)
 */
public class LinearListReviewStore {
    private List<ReviewRecord> reviews;
    private DateTimeFormatter dateFormatter;
    
    public LinearListReviewStore() {
        this.reviews = new ArrayList<>();
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }
    
    // Add a new review to the store.
    // Time Complexity: O(1) amortized
    public void addReview(ReviewRecord review) {
        reviews.add(review);
    }
    
    // Add multiple reviews at once.
    // Time Complexity: O(m) where m is the number of reviews to add
    public void addReviews(List<ReviewRecord> newReviews) {
        reviews.addAll(newReviews);
    }
    
    // Get the total number of reviews stored.
    //Get the total number of reviews stored.
    // Time Complexity: O(1)
    public int size() {
        return reviews.size();
    }
    
    // Retrieve the k most recent reviews for a specific airline.
    // Time Complexity: O(N log N) due to sorting by date
    /**
    * @param airlineName The name of the airline to filter by
    * @param k The number of most recent reviews to retrieve
    * @return List the k most recent reviews for the airline
    */
    public List<ReviewRecord> getTopKRecentReviews(String airlineName, int k) {
        return reviews.stream()
                .filter(review -> review.getName().equals(airlineName))
                .sorted((r1, r2) -> parseDate(r2.getDate()).compareTo(parseDate(r1.getDate())))
                .limit(k)
                .collect(Collectors.toList());
    }
    
    /**
     * Calculate Recency-Biased Average Rating (RB-AR) for a specific airline.
     * This heavily weights recent reviews (last 30 days) and deprioritizes old reviews (3+ years).
     * Time Complexity: O(N)
     * 
     * @param airlineName The name of the airline
     * @return The recency-biased average rating
     */
    public double calculateRecencyBiasedAverageRating(String airlineName) {
        LocalDate now = LocalDate.now();
        LocalDate thirtyDaysAgo = now.minusDays(30);
        LocalDate threeYearsAgo = now.minusYears(3);
        
        List<ReviewRecord> airlineReviews = reviews.stream()
                .filter(review -> review.getName().equals(airlineName))
                .collect(Collectors.toList());
        
        if (airlineReviews.isEmpty()) {
            return 0.0;
        }
        
        double weightedSum = 0.0;
        double totalWeight = 0.0;
        
        for (ReviewRecord review : airlineReviews) {
            LocalDate reviewDate = parseDate(review.getDate());
            double weight = calculateRecencyWeight(reviewDate, thirtyDaysAgo, threeYearsAgo);
            
            weightedSum += review.getOverallRating() * weight;
            totalWeight += weight;
        }
        
        return totalWeight > 0 ? weightedSum / totalWeight : 0.0;
    }
    
    /**
     * Calculate the weight for a review based on its recency.
     * Recent reviews (last 30 days) get high weight, old reviews (3+ years) get low weight.
     */
    private double calculateRecencyWeight(LocalDate reviewDate, LocalDate thirtyDaysAgo, LocalDate threeYearsAgo) {
        if (reviewDate.isAfter(thirtyDaysAgo)) {
            // Recent reviews (last 30 days): weight = 1.0
            return 1.0;
        } else if (reviewDate.isAfter(threeYearsAgo)) {
            // Medium age reviews (30 days to 3 years): linear decay
            long daysSinceThirtyDays = java.time.temporal.ChronoUnit.DAYS.between(thirtyDaysAgo, reviewDate);
            long totalDays = java.time.temporal.ChronoUnit.DAYS.between(threeYearsAgo, thirtyDaysAgo);
            return Math.max(0.1, 1.0 - (double) daysSinceThirtyDays / totalDays);
        } else {
            // Old reviews (3+ years): minimal weight
            return 0.05;
        }
    }
    
    /**
     * Get all reviews for a specific airline.
     * Time Complexity: O(N)
     */
    public List<ReviewRecord> getReviewsByAirline(String airlineName) {
        return reviews.stream()
                .filter(review -> review.getName().equals(airlineName))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all unique airline names in the store.
     * Time Complexity: O(N)
     */
    public Set<String> getAllAirlines() {
        return reviews.stream()
                .map(ReviewRecord::getName)
                .collect(Collectors.toSet());
    }
    
    /**
     * Get reviews within a specific date range.
     * Time Complexity: O(N)
     */
    public List<ReviewRecord> getReviewsInDateRange(LocalDate startDate, LocalDate endDate) {
        return reviews.stream()
                .filter(review -> {
                    LocalDate reviewDate = parseDate(review.getDate());
                    return !reviewDate.isBefore(startDate) && !reviewDate.isAfter(endDate);
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Parse date string to LocalDate.
     * Handles common date formats used in review data.
     */
    private LocalDate parseDate(String dateStr) {
        try {
            // Try the standard format first
            return LocalDate.parse(dateStr, dateFormatter);
        } catch (Exception e) {
            try {
                // Try alternative format
                DateTimeFormatter altFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                return LocalDate.parse(dateStr, altFormatter);
            } catch (Exception e2) {
                try {
                    // Try another common format
                    DateTimeFormatter altFormatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    return LocalDate.parse(dateStr, altFormatter2);
                } catch (Exception e3) {
                    // If all parsing fails, return a very old date to minimize its weight
                    return LocalDate.of(1900, 1, 1);
                }
            }
        }
    }
    
    /**
     * Clear all reviews from the store.
     * Time Complexity: O(1)
     */
    public void clear() {
        reviews.clear();
    }
    
    /**
     * Get all reviews sorted by date (most recent first).
     * Time Complexity: O(N log N)
     */
    public List<ReviewRecord> getAllReviewsSortedByDate() {
        return reviews.stream()
                .sorted((r1, r2) -> parseDate(r2.getDate()).compareTo(parseDate(r1.getDate())))
                .collect(Collectors.toList());
    }
    
    /**
     * Get statistics about the review store.
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReviews", reviews.size());
        stats.put("uniqueAirlines", getAllAirlines().size());
        
        if (!reviews.isEmpty()) {
            LocalDate oldestDate = reviews.stream()
                    .map(r -> parseDate(r.getDate()))
                    .min(LocalDate::compareTo)
                    .orElse(LocalDate.now());
            
            LocalDate newestDate = reviews.stream()
                    .map(r -> parseDate(r.getDate()))
                    .max(LocalDate::compareTo)
                    .orElse(LocalDate.now());
            
            stats.put("oldestReview", oldestDate);
            stats.put("newestReview", newestDate);
        }
        
        return stats;
    }
}
