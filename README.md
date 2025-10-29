# ✈️ CS201 Data Structures & Algorithms Project  
### Project Title: Finding the Best Airline by Review Trends  
**Team:** G3T4
**Course:** CS201 – Data Structures and Algorithms (AY2025/26 Term 1)  
**Institution:** Singapore Management University  

---

## 📌 Project Overview

This project analyzes user reviews from the **Skytrax User Reviews Dataset** to identify and maintain the **top-rated airlines** globally.  
The focus is on studying **how different data structures affect the performance** of real-time ranking updates when new reviews are added, with special emphasis on **recency-biased operations**.

We simulate a **dynamic leaderboard system**, where each new review may change the rankings. By keeping the same algorithmic logic constant but varying the data structure, we can observe the **trade-offs in time and space efficiency** for recency-biased average rating calculations and top-k retrieval operations.

---

## 🎯 Problem Statement

> Given a collection of airline reviews (including ratings, dates, and traveler types), design an algorithm that maintains the top-𝑘 airlines based on **recency-biased average rating** and efficiently retrieves the most recent reviews.  
> Each time a new review is added, the system must efficiently update rankings using a **recency weighting system** that prioritizes recent reviews over older ones.

---

## ⚙️ Algorithmic Context

We use a consistent **recency-biased ranking algorithm** across all experiments:

### Recency Weighting System:
- **Recent reviews (last 30 days)**: weight = 1.0 (full impact)
- **Medium age reviews (30 days to 3 years)**: linear decay from 1.0 to 0.1
- **Old reviews (3+ years)**: minimal weight = 0.05

### Core Operations:
1. **Recency-Biased Average Rating (RB-AR)**: Calculate weighted average considering review age
2. **Top-K Recent Retrieval**: Get k most recent reviews for any airline
3. **Dynamic Updates**: Efficiently handle new review insertions
4. **Search Operations**: Find reviews by airline name

---

## 🧩 Data Structures Compared

| Experiment | Data Structure | Status | Description | Key Operations Compared |
|------------|----------------|--------|--------------|--------------------------|
| **1** | **Linear List (ArrayList)** | ✅ **Complete** | Baseline implementation using ArrayList for storing reviews | Insert: O(1), Search: O(N), Top-K: O(N log N), RBAR: O(N) |
| **2** | **AVL Tree** | ✅ **Complete** | Self-balancing binary search tree with automatic rotations | Insert: O(log N), Search: O(log N), Top-K: O(N log N), RBAR: O(N) |
| **3** | **Recency-Biased Tree (RBT)** | ✅ **Complete** | Splay tree that intentionally biases recent reviews to root for fast access | Insert: O(log N), Recent Search: O(1), Old Search: O(N), Top-K: O(k) |

All implementations use the **same recency weighting algorithm**, allowing fair comparison of performance based solely on data structure design.

---

## 🧠 Experimental Goals

- Measure and compare **runtime performance** for different data structures as dataset size increases.  
- Observe **memory usage differences** across structures.  
- Identify scenarios where theoretical complexity diverges from **real-world performance**.

---

## 🧪 Evaluation Metrics

| Metric | Description |
|---------|--------------|
| **Runtime (ms)** | Time to update rankings after each new review |
| **Memory Usage (MB)** | Space used to store data structure |
| **Scalability** | Performance trend as input size increases |
| **Update Efficiency** | Time complexity for rank recalculation |

---

## 🧮 Theoretical Complexity Summary

| Data Structure | Insert | Search | Top-K Retrieval | RBAR Calculation | Space Complexity |
|----------------|---------|---------|-----------------|------------------|------------------|
| **Linear List** | O(1) amortized | O(N) | O(N log N) | O(N) | O(N) |
| **AVL Tree** | O(log N) | O(log N) | O(N log N) | O(N) | O(N) |
| **RBT Tree** | O(log N) | O(log N)* | O(k) for recent | O(N) | O(N) |

*Recent reviews: O(log N) to O(1), Old reviews: O(N)

### Key Performance Insights:
- **AVL Tree** provides significant improvement in search operations: O(N) → O(log N)
- **Linear List** has fastest insertion but slowest search
- **Top-K and RBAR** operations are dominated by sorting/processing, not data structure choice

---

## 📂 Project Structure

```
CS201/
├── results/                             # ⭐ MAIN RESULTS FOLDER
│   ├── experiment1_linear_list.csv      # Linear List detailed results
│   ├── experiment2_avl_tree.csv         # AVL Tree detailed results
│   ├── experiment3_rbt.csv              # RBT detailed results
│   ├── unified_comparison.csv           # Side-by-side comparison
│   ├── ANALYSIS_REPORT.md               # Comprehensive analysis
│   └── SUMMARY.txt                      # Quick reference summary
│
├── src/main/java/com/reviews/
│   ├── Models/                          # Data model classes
│   │   ├── ReviewRecord.java           # Common interface for all review types
│   │   ├── AirlineReview.java          # Airline review implementation
│   │   ├── AirportReview.java          # Airport review implementation
│   │   ├── AirportLoungeReview.java    # Airport lounge review implementation
│   │   └── SeatReview.java             # Seat review implementation
│   │
│   ├── utils/                           # Utility classes
│   │   └── CSVLoader.java              # Loads real CSV data
│   │
│   ├── experiments/                     # Experiment implementations
│   │   ├── BenchmarkUtils.java         # Shared benchmark utilities
│   │   ├── MasterRunner.java           # ⭐ ONE-CLICK runner (all experiments)
│   │   ├── UnifiedBenchmarkRunner.java # Cross-experiment comparison
│   │   ├── ResultsAnalyzer.java        # Generates analysis reports
│   │   ├── experiment1/                # Experiment 1: Linear List
│   │   │   ├── Main1.java              # Experiment 1 main runner
│   │   │   ├── LinearListDemo.java     # Demonstration
│   │   │   ├── LinearListReviewStoreTest.java  # Test suite
│   │   │   ├── LinearListPerformanceBenchmark.java  # Performance analysis
│   │   │   └── results.csv             # Benchmark results (copy)
│   │   ├── experiment2/                # Experiment 2: AVL Tree
│   │   │   ├── Main2.java              # Experiment 2 main runner
│   │   │   ├── AVLDemo.java            # Demonstration
│   │   │   ├── AVLReviewStoreTest.java # Test suite
│   │   │   ├── AVLPerformanceBenchmark.java # Performance analysis
│   │   │   └── results.csv             # Benchmark results (copy)
│   │   └── experiment3/                # Experiment 3: RBT
│   │       ├── Main3.java              # Experiment 3 main runner
│   │       ├── RBTDemo.java            # Demonstration
│   │       ├── RBTReviewStoreTest.java # Test suite
│   │       ├── RBTPerformanceBenchmark.java # Performance analysis
│   │       └── results.csv             # Benchmark results (copy)
│   │
│   └── datastructures/                  # Data structure implementations
│       ├── LinearListReviewStore.java  # Experiment 1 implementation
│       ├── AVLNode.java                # AVL tree node
│       ├── AVLReviewStore.java         # Experiment 2 implementation
│       ├── RBTNode.java                # RBT tree node
│       └── RBTReviewStore.java         # Experiment 3 implementation
│
├── data/                                # Dataset files (REAL DATA)
│   ├── airline.csv                     # 41,457 airline reviews ⭐
│   ├── airport.csv                     # 17,748 airport reviews  
│   ├── lounge.csv                      # 2,277 lounge reviews
│   └── seat.csv                        # 1,261 seat reviews
│
├── compile.sh                           # Compilation script
├── run.sh                               # One-click run script
├── .gitignore                           # Git ignore file
└── README.md                            # This file
```

---

## 🚀 How to Run the Experiments

### ⚡ ONE-CLICK SOLUTION (NEW!)

**Easiest Way - Run Everything at Once:**

1. **Using Shell Script (Mac/Linux):**
   ```bash
   ./run.sh
   ```

2. **Using IDE (IntelliJ/VS Code/Eclipse):**
   - Open `MasterRunner.java`
   - Click the **Run** button
   - Done! ✅

3. **Using Terminal:**
   ```bash
   cd src/main/java
   javac com/reviews/experiments/MasterRunner.java
   java com.reviews.experiments.MasterRunner
   ```

This will run all three experiments, generate all results, and display comparisons!

---

## 📊 Results System

After running the project, check the **`results/`** folder for:

- ⭐ **`SUMMARY.txt`** - Quick reference with key findings (view in terminal or editor)
- ⭐ **`ANALYSIS_REPORT.md`** - Comprehensive analysis report with recommendations
- ⭐ **`unified_comparison.csv`** - All experiments combined (ready for Excel/charts)
- 📊 **`experiment1_linear_list.csv`** - Linear List detailed metrics
- 📊 **`experiment2_avl_tree.csv`** - AVL Tree detailed metrics
- 📊 **`experiment3_rbt.csv`** - RBT detailed metrics

**👉 See [`RESULTS_GUIDE.md`](RESULTS_GUIDE.md) for detailed information about the results system!**

---

### Prerequisites
- Java 8 or higher
- IDE with Java support (IntelliJ IDEA, Eclipse, VS Code)

### Running Experiments (Individual Control)

#### Run All Experiments (Recommended)
The unified benchmark runner compares all three data structures:
```bash
# Navigate to the experiments directory
cd src/main/java/com/reviews/experiments/

# Compile and run unified benchmark
javac *.java
java UnifiedBenchmarkRunner
```
This will run comparative benchmarks across all experiments and generate results.csv files for each.

#### Individual Experiments

**Experiment 1: Linear List Baseline**
```bash
cd src/main/java/com/reviews/experiments/experiment1/
javac *.java
java Main1
```

**Experiment 2: AVL Tree Implementation**
```bash
cd src/main/java/com/reviews/experiments/experiment2/
javac *.java
java Main2
```

**Experiment 3: RBT Implementation**
```bash
cd src/main/java/com/reviews/experiments/experiment3/
javac *.java
java Main3
```

### Individual Components
Each experiment directory contains:
- **Main Runner**: `java Main1`, `java Main2`, or `java Main3` - Runs the full demo and tests
- **Tests Only**: `java LinearListReviewStoreTest`, `java AVLReviewStoreTest`, `java RBTReviewStoreTest`
- **Benchmarks Only**: `java LinearListPerformanceBenchmark`, `java AVLPerformanceBenchmark`, `java RBTPerformanceBenchmark`
- **Demos Only**: `java LinearListDemo`, `java AVLDemo`, `java RBTDemo`

---

## 📊 Current Implementation Status

### ✅ Completed Features
- **Data Theory**: Complete interface and 4 review type implementations
- **Experiment 1**: Full linear list implementation with comprehensive testing
- **Experiment 2**: Complete AVL tree implementation with automatic balancing
- **Experiment 3**: Complete RBT (Recency-Biased Tree) implementation with splay operations
- **Testing Framework**: Comprehensive test suites for all three experiments
- **Performance Benchmarking**: Detailed performance analysis and comparison
- **Unified Benchmark Runner**: Cross-experiment comparison with automated CSV generation
- **Recency Weighting**: Consistent algorithm across all implementations

### 🔬 Key Findings
- **AVL Tree** shows significant improvement in search operations over linear list: O(N) → O(log N)
- **RBT** provides O(1) to O(log N) access for recent reviews, optimal for recency-biased workloads
- **Recency weighting** effectively prioritizes recent reviews while maintaining historical context
- **Tree balancing** ensures consistent performance regardless of insertion order in AVL
- **Memory usage** is comparable across all implementations (O(N) space complexity)

### 📊 Testing Approach

**ALL benchmarks use REAL CSV data from Skytrax dataset!**

- Uses actual airline reviews from `data/airline.csv` (41,457 reviews!)
- Tests with real-world data distribution
- Validates performance with authentic review patterns  
- Test sizes: 1K, 5K, 10K, 25K, and full 41,457 reviews
- No synthetic/generated data - 100% real user reviews!

---

## 🎯 Future Enhancements
1. Performance Visualization: Generate charts and graphs from benchmark results
2. Memory Profiling: Measure actual memory usage differences  
3. Extended Testing: Add stress tests with even larger datasets
4. CSV Export: Export processed results in various formats
