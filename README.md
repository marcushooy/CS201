# CS201 - Airline Review Data Structures Performance Analysis

This project implements and benchmarks three data structures for managing airline reviews: **Linear List**, **AVL Tree**, and **Recency-Biased Tree (RBT)**. It provides comprehensive performance comparisons across all CRUD operations plus specialized airline ranking operations.

## Project Structure

```
CS201/
├── datastructures/                # Data structure implementations
│   ├── Linear_List/
│   │   └── LinearListReviewStore.java
│   ├── AVL_Tree/
│   │   ├── AVLNode.java
│   │   └── AVLReviewStore.java
│   └── Recency_Biased_Tree/
│       ├── RecencyBiasedTree.java
│       └── RBTReviewStore.java
├── models/                        # Data models
│   ├── AirlineReview.java
│   └── AirlineRanking.java
├── utils/                         # Shared utilities
│   ├── CSVLoader.java
│   ├── RankingUtils.java
│   └── RBARCalculator.java
├── tests/                         # Unit tests
│   ├── LinearListTest.java
│   ├── AVLTest.java
│   ├── RBTTest.java
│   └── run_tests.sh
├── experiments/                   # Performance benchmarks
│   ├── src/
│   │   ├── benchmarks/
│   │   │   ├── LinearListBenchmark.java
│   │   │   ├── AVLTreeBenchmark.java
│   │   │   └── RBTBenchmark.java
│   │   ├── utils/
│   │   │   ├── BenchmarkResult.java
│   │   │   ├── PerformanceBenchmark.java
│   │   │   └── ResultsWriter.java
│   │   └── UnifiedBenchmarkRunner.java
│   ├── classes/                   # Compiled .class files
│   └── results/                   # Benchmark output (CSV files)
├── data/
│   └── airline.csv                # Real airline review dataset
├── out/                           # Compiled output directory
├── compile.sh                     # Compilation script
├── run.sh                         # Benchmark execution script
├── CS201_Analysis.ipynb           # Jupyter notebook for data analysis
└── .ipynb_checkpoints/            # Jupyter checkpoint files (gitignored)
```

## Overview

This project analyzes the performance characteristics of three different data structures for storing and querying airline reviews. It uses **real airline review data** and measures runtime performance across multiple operations and dataset sizes.

## Data Structures

### 1. Linear List (`datastructures/Linear_List/`)
- **Implementation**: ArrayList-based storage
- **Characteristics**: Simple, fast insertion, linear search
- **Time Complexity**:
  - INSERT: O(1) amortized
  - SEARCH: O(N)
  - UPDATE: O(N)
  - DELETE: O(N)

### 2. AVL Tree (`datastructures/AVL_Tree/`)
- **Implementation**: Self-balancing binary search tree
- **Characteristics**: Guarantees O(log N) for all operations, strict balancing
- **Time Complexity**:
  - INSERT: O(log N)
  - SEARCH: O(log N)
  - UPDATE: O(log N)
  - DELETE: O(log N)
- **Special Features**: Tree height tracking, balance verification

### 3. Recency-Biased Tree (`datastructures/Recency_Biased_Tree/`)
- **Implementation**: Splay tree with temporal recency optimization
- **Characteristics**: Most recent reviews (by date) kept at or near root
- **Time Complexity**:
  - INSERT: O(log N) with splay to most recent
  - SEARCH: O(N) worst case, O(log N) amortized
  - UPDATE: O(N) (rebuilds tree)
  - DELETE: O(N) (rebuilds tree)
  - **TOPK_RECENT: O(k)** ⭐ Optimized for recent review access
- **Special Features**: 
  - Temporal Recency: Most recent review by date always at root after insert
  - Efficient `getTopKRecentReviews()` operation

## Operations Benchmarked

1. **CREATE** - `addReview()` - Insert a single review
2. **READ** - `getReviewsByAirline()` - Search reviews by airline name
3. **UPDATE** - `updateReview()` - Update an existing review
4. **DELETE** - `deleteReview()` - Remove a review
5. **RBAR** - `calculateRBAR()` - Calculate Recency-Biased Average Rating
6. **RANKINGS** - `getAirlineRankings()` - Get all airlines ranked by RBAR
7. **TOPK** - `getTopKAirlines(k)` - Get top k airlines by RBAR
8. **TOPK_RECENT** - `getTopKRecentReviews()` - Get k most recent reviews for an airline ⭐ Available in all data structures

## Test Configuration

### Data Sizes
- 1,000 reviews (small)
- 5,000 reviews (medium-small)
- 10,000 reviews (medium)
- 25,000 reviews (large)
- 41,000+ reviews (full dataset)

### Iteration Counts
- CREATE: 1000 iterations
- READ: 100 iterations
- UPDATE: 100 iterations
- DELETE: 100 iterations
- RBAR: 50 iterations
- RANKINGS: 20 iterations
- TOPK: 100 iterations
- TOPK_RECENT: 100 iterations

### Warmup
- Silent JVM warmup before each operation (no console output)
- Ensures stable, consistent measurements

### Metrics Collected
- **Average time** (milliseconds)
- **Minimum time** (milliseconds)
- **Maximum time** (milliseconds)
- **Number of iterations**

## Quick Start

### 1. Run Tests

From the `tests/` directory:

```bash
cd tests
chmod +x run_tests.sh
./run_tests.sh
```

This will:
- Compile all models, utilities, data structures, and tests
- Run comprehensive tests for all three data structures
- Verify all CRUD operations, RBAR calculations, and rankings
- Display formatted test results

### 2. Compile Everything

From the project root:

```bash
chmod +x compile.sh
./compile.sh
```

This will:
- Compile models and utilities
- Compile all data structures (Linear_List, AVL_Tree, Recency_Biased_Tree)
- Compile experiment utilities
- Compile benchmark implementations
- Compile the unified runner
- Place all `.class` files in `out/` directory

### 3. Run Benchmarks

From the project root:

```bash
chmod +x run.sh
./run.sh
```

This executes `UnifiedBenchmarkRunner` which:
- Tests all three data structures
- Uses all configured data sizes (1K, 5K, 10K, 25K, 41K)
- Runs all 8 operations (CREATE, READ, UPDATE, DELETE, RBAR, RANKINGS, TOPK, TOPK_RECENT)
- Displays comparison tables showing winners for each operation
- Saves detailed results to `experiments/results/` directory

### 4. Manual Execution

From the project root:

```bash
java -cp out experiments.UnifiedBenchmarkRunner
```

## Output Files

Results are saved to `experiments/results/`:

| File | Description |
|------|-------------|
| `linear_list_results.csv` | All Linear List benchmark results |
| `avl_tree_results.csv` | All AVL Tree benchmark results |
| `rbt_results.csv` | All RBT benchmark results |
| `comparison_summary.csv` | Side-by-side comparison with winners |

### CSV Format (Individual Results)

```csv
Data Size,Operation,Avg Time (ms),Min Time (ms),Max Time (ms),Iterations
1000,CREATE,0.001234,0.000987,0.001567,1000
1000,READ,0.456789,0.423456,0.489012,100
...
```

### CSV Format (Comparison Summary)

```csv
Data Size,Operation,Linear List (ms),AVL Tree (ms),RBT (ms),Winner,Best Time (ms),Speedup vs Worst
1000,CREATE,0.001,0.002,0.003,Linear List,0.001,3.00x
1000,READ,0.500,0.001,0.002,AVL Tree,0.001,500.00x
1000,TOPK_RECENT,0.050,0.010,0.001,RBT,0.001,50.00x
...
```

## Console Output

### Benchmark Progress
```
[Linear List] Benchmarking CREATE with 1000 reviews...
  CREATE: 0.000029 ms average

[AVL Tree] Benchmarking READ...
  READ: 0.000818 ms average

[RBT] Benchmarking TOPK_RECENT (k=10)...
  TOPK_RECENT: 0.001 ms average
```

### Comparison Tables
After each data size, a comparison table shows:
```
────────────────────────────────────────────────────────────────────────────────
COMPARISON FOR 1000 REVIEWS
────────────────────────────────────────────────────────────────────────────────
Operation    | Linear List     | AVL Tree        | RBT             | Winner      
────────────────────────────────────────────────────────────────────────────────
CREATE       |     0.000029 ms |     0.000063 ms |     0.000795 ms | Linear List ⭐
READ         |        0.021 ms |     0.000818 ms |        0.001 ms | AVL Tree ⭐  
TOPK_RECENT  |        0.050 ms |        0.010 ms |        0.001 ms | RBT ⭐       
...
```

## Source Files

### Data Structures (`datastructures/`)
- `Linear_List/LinearListReviewStore.java` - ArrayList-based implementation
- `AVL_Tree/AVLNode.java` - AVL tree node with balance tracking
- `AVL_Tree/AVLReviewStore.java` - AVL tree implementation
- `Recency_Biased_Tree/RecencyBiasedTree.java` - Generic splay tree with temporal recency
- `Recency_Biased_Tree/RBTReviewStore.java` - Recency-biased tree for airline reviews

### Models (`models/`)
- `AirlineReview.java` - Review data model
- `AirlineRanking.java` - Ranking result model

### Utilities (`utils/`)
- `CSVLoader.java` - Loads airline reviews from CSV
- `RBARCalculator.java` - Shared RBAR calculation logic
- `RankingUtils.java` - Shared airline ranking utilities

### Tests (`tests/`)
- `LinearListTest.java` - Comprehensive tests for Linear List
- `AVLTest.java` - Comprehensive tests for AVL Tree (includes balance verification)
- `RBTTest.java` - Comprehensive tests for RBT
- `run_tests.sh` - Script to compile and run all tests

### Benchmarks (`experiments/src/benchmarks/`)
- `LinearListBenchmark.java` - Benchmarks for Linear List (all 8 operations)
- `AVLTreeBenchmark.java` - Benchmarks for AVL Tree (all 8 operations)
- `RBTBenchmark.java` - Benchmarks for RBT (all 8 operations)

### Utilities (`src/utils/`)
- `BenchmarkResult.java` - Data class for storing runtime metrics
- `PerformanceBenchmark.java` - Shared utilities (warmup, timing, data loading)
- `ResultsWriter.java` - CSV output utilities

### Main Runner (`src/`)
- `UnifiedBenchmarkRunner.java` - Orchestrates all benchmarks and generates reports

## Expected Results

### Time Complexity Summary
| Operation | Linear List | AVL Tree | RBT |
|-----------|-------------|----------|-----|
| CREATE | O(1) | O(log N) | O(log N) |
| READ | O(N) | O(log N) | O(N) worst, O(log N) amortized |
| UPDATE | O(N) | O(log N) | O(N) |
| DELETE | O(N) | O(log N) | O(N) |
| RBAR | O(N) | O(M) where M = reviews for airline | O(M) |
| RANKINGS | O(N log N) | O(N log N) | O(N log N) |
| TOPK | O(N log N) | O(N log N) | O(N log N) |
| **TOPK_RECENT** | **O(N + M log M)** | **O(log N + M log M)** | **O(k)** ⭐ |

### Expected Winners by Operation
- **CREATE**: Linear List (O(1) insertion)
- **READ**: AVL Tree (balanced O(log N) search)
- **UPDATE**: AVL Tree (balanced operations)
- **DELETE**: AVL Tree (balanced operations)
- **RBAR**: Similar across all (depends on review count per airline)
- **RANKINGS**: Similar across all (O(N log N) sorting dominates)
- **TOPK**: Similar across all (subset of RANKINGS)
- **TOPK_RECENT**: **RBT** ⭐ (O(k) due to temporal recency optimization)

### Key Insights
1. **Linear List**: Best for simple insertion, poor for search
2. **AVL Tree**: Most consistent performance across all operations
3. **RBT**: Specialized advantage for accessing recent reviews by date

## Cleaning Up

To clean compiled files:

```bash
rm -rf out/*
rm -rf tests/out/*
rm -rf experiments/classes/*
rm -rf experiments/results/*
```

To clean Jupyter checkpoints:

```bash
rm -rf .ipynb_checkpoints/
```

To recompile from scratch:

```bash
rm -rf out/*
./compile.sh
```

## Data Analysis

The project includes `CS201_Analysis.ipynb`, a comprehensive Jupyter notebook for analyzing benchmark results:

### Contents
- **Part 0**: Setup and data loading
- **Part I**: Novelty of RBT - Why it's innovative (dual recency bias, O(k) operation)
- **Part II**: Experimental rigor - Research-grade methodology
- **Part III**: Per-operation performance analysis with visualizations
- **Part IV**: Performance summary and winner analysis
- **Part V**: Potential extensions to other datasets
- **Part VI**: Real-world applications (stock trading, social media, e-commerce, news ranking, healthcare)
- **Part VII**: X-Factor analysis - What makes the project outstanding

### Features
- 15+ professional visualizations using Matplotlib and Seaborn
- Load CSV results from `experiments/results/`
- Performance comparison charts for all 8 operations
- Speedup analysis and winner distribution
- Heatmap of winners by operation and data size
- Extension landscape visualization
- Statistical analysis with min/avg/max metrics

### Key Findings Documented
- Linear List: 131x faster CREATE (O(1) append)
- AVL Tree: 510x faster READ (balanced search)
- RBT: Sub-millisecond TOPK_RECENT (O(k) recency-optimized)
- RBAR: RBT 2.23x faster at 10K scale

### Usage
```bash
jupyter notebook CS201_Analysis.ipynb
```

### Team Information
- **Team:** G3T4
- **Members:** Arnold Leow, Marcus Hooy, Ethan Tiew, Elodie Yeung, Sim Qi Xun
- **Course:** CS201 Data Structures and Algorithms, AY2025/26 Term 1
- **Institution:** Singapore Management University
- **Dataset:** 41,396 real Skytrax airline reviews (100% parsing success)

## Troubleshooting

### Compilation Errors

If you see package errors:
- Ensure you're running from the project root
- Verify directory structure matches the package names:
  - `datastructures.Linear_List`
  - `datastructures.AVL_Tree`
  - `datastructures.Recency_Biased_Tree`
- Clean and recompile: `rm -rf out/* && ./compile.sh`

### Runtime Errors

If you see `ClassNotFoundException`:
- Ensure you're running from project root
- Check that all classes are in `out/` directory
- Verify the classpath in run scripts

If CSV loading fails:
- Verify `data/airline.csv` exists in project root
- Check file permissions
- Ensure CSV format matches expected structure

## Customization

### Change Data Sizes

Edit `src/utils/PerformanceBenchmark.java`:
```java
public static final int[] TEST_SIZES = {1000, 5000, 10000, 25000, 41000};
```

### Change Iteration Counts

Edit individual benchmark methods in `experiments/src/benchmarks/*Benchmark.java` files.

### Add New Operations

1. Implement the operation in all three data structure classes:
   - `datastructures/Linear_List/LinearListReviewStore.java`
   - `datastructures/AVL_Tree/AVLReviewStore.java`
   - `datastructures/Recency_Biased_Tree/RBTReviewStore.java`
2. Add benchmark method to each `*Benchmark.java` in `experiments/src/benchmarks/`
3. Update `runAllBenchmarks()` to include it
4. Add test case to corresponding test file in `tests/`
5. Recompile and run

### Modify Benchmark Output

Edit `experiments/src/utils/PerformanceBenchmark.java`:
- Modify `formatTime()` for different time formatting
- Modify `runBenchmark()` to change output format

## Features

### Shared Utilities
- **RBARCalculator**: Consistent RBAR calculation across all data structures
- **RankingUtils**: Shared ranking logic to avoid code duplication
- **CSVLoader**: Centralized data loading

### Test Suite
- Comprehensive tests for all CRUD operations
- RBAR calculation verification
- Airline ranking tests
- Top-K recent reviews testing
- AVL tree balance verification
- Data structure-specific property testing

### Benchmark Framework
- Silent JVM warmup (no console clutter)
- Standardized output format for all operations
- CSV export for data analysis
- Comparison tables with winners highlighted
- Multiple data size testing

## Author

CS201 Project - Airline Review Data Structures Performance Analysis
