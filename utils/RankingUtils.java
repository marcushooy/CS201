package utils;

import models.AirlineReview;
import models.AirlineRanking;
import java.util.*;

/**
 * RankingUtils - Utility class for calculating airline rankings.
 * 
 * This class provides shared ranking logic that all data structures
 * (Linear List, AVL Tree, RBT Tree) can use to avoid code duplication.
 * 
 * The ranking logic is generic: iterate airlines, calculate RBAR, sort by RBAR.
 */
public class RankingUtils {
    
    /**
     * Calculate airline rankings from a map of airline names to their reviews.
     * This helper method can be used by any data structure.
     * 
     * @param airlineReviewsMap Map of airline name -> list of reviews
     * @return List of AirlineRanking objects sorted by RBAR (descending)
     */
    public static List<AirlineRanking> calculateRankings(Map<String, List<AirlineReview>> airlineReviewsMap) {
        List<AirlineRanking> rankings = new ArrayList<>();
        
        for (Map.Entry<String, List<AirlineReview>> entry : airlineReviewsMap.entrySet()) {
            String airline = entry.getKey();
            List<AirlineReview> reviews = entry.getValue();
            double rbar = RBARCalculator.calculateRBAR(reviews);
            int reviewCount = reviews.size();
            rankings.add(new AirlineRanking(airline, rbar, reviewCount));
        }
        
        // Sort by RBAR in descending order
        rankings.sort((a, b) -> Double.compare(b.rbar, a.rbar));
        
        return rankings;
    }
    
    /**
     * Get top k airlines from a list of rankings.
     * 
     * @param rankings List of AirlineRanking objects (should be pre-sorted)
     * @param k Number of top airlines to return
     * @return List of top k AirlineRanking objects
     */
    public static List<AirlineRanking> getTopK(List<AirlineRanking> rankings, int k) {
        if (rankings == null || rankings.isEmpty()) {
            return new ArrayList<>();
        }
        return rankings.subList(0, Math.min(k, rankings.size()));
    }
}

