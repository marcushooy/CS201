package com.reviews.experiments;

/**
 * SIMPLE MASTER RUNNER - For Testing
 * 
 * If MasterRunner gives errors, try this simpler version first to diagnose the issue.
 */
public class SimpleMasterRunner {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              SIMPLE MASTER RUNNER - DIAGNOSTIC TEST                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        try {
            System.out.println("Testing Experiment 1...");
            com.reviews.experiments.experiment1.Main1.main(args);
            System.out.println("✅ Experiment 1 completed successfully!\n");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR in Experiment 1:");
            e.printStackTrace();
            return;
        }
        
        try {
            System.out.println("\nTesting Experiment 2...");
            com.reviews.experiments.experiment2.Main2.main(args);
            System.out.println("✅ Experiment 2 completed successfully!\n");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR in Experiment 2:");
            e.printStackTrace();
            return;
        }
        
        try {
            System.out.println("\nTesting Experiment 3...");
            com.reviews.experiments.experiment3.Main3.main(args);
            System.out.println("✅ Experiment 3 completed successfully!\n");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR in Experiment 3:");
            e.printStackTrace();
            return;
        }
        
        System.out.println("\n✅ ALL TESTS PASSED! Now you can run the full MasterRunner.");
    }
}

