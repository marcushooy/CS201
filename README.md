# Performance Experiments

This directory contains comprehensive performance benchmarking code for comparing three data structures: **Linear List**, **AVL Tree**, and **RBT (Recency-Biased Tree)**.

## Directory Structure

```
experiments/
├── src/                          # Source code
│   ├── benchmarks/               # Benchmark implementations
│   │   ├── LinearListBenchmark.java
│   │   ├── AVLTreeBenchmark.java
│   │   └── RBTBenchmark.java
│   ├── utils/                    # Shared utilities
│   │   ├── BenchmarkResult.java
│   │   ├── PerformanceBenchmark.java
│   │   └── ResultsWriter.java
│   └── UnifiedBenchmarkRunner.java  # Main runner
├── classes/                      # Compiled .class files
├── results/                      # Benchmark output (CSV files)
├── compile.sh                    # Compilation script
├── run.sh                        # Execution script
└── README.md                     # This file
```

## Overview

The experiments measure **runtime performance** for all key operations using **real airline review data** from CSV. Results are saved to the `results/` subdirectory for analysis.

## Operations Benchmarked

1. **CREATE** - `addReview()` - Insert a single review
2. **READ** - `getReviewsByAirline()` - Search reviews by airline name
3. **UPDATE** - `updateReview()` - Update an existing review
4. **DELETE** - `deleteReview()` - Remove a review
5. **RBAR** - `calculateRBAR()` - Calculate Recency-Biased Average Rating
6. **RANKINGS** - `getAirlineRankings()` - Get all airlines ranked by RBAR
7. **TOPK** - `getTopKAirlines(k)` - Get top k airlines
8. **TOPK_RECENT** - `getTopKRecentReviews()` - Get k most recent reviews (RBT only)

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

### Metrics Collected
- **Average time** (milliseconds)
- **Minimum time** (milliseconds)
- **Maximum time** (milliseconds)
- **Number of iterations**

## Quick Start

### 1. Compile Everything

```bash
chmod +x compile.sh
./compile.sh
```

This will:
- Compile models and utilities
- Compile all data structures
- Compile experiment utilities
- Compile benchmark implementations
- Compile the unified runner
- Place all `.class` files in `experiments/classes/` directory

### 2. Run Benchmarks

```bash
chmod +x run.sh
./run.sh
```

This executes `UnifiedBenchmarkRunner` which:
- Tests all three data structures
- Uses all configured data sizes
- Saves results to `experiments/results/` subdirectory

### 3. Manual Execution

From the project root:

```bash
java -cp .:models:utils:datastructures/linear_list:datastructures/avl_tree:datastructures/rbt_tree:experiments/classes experiments.UnifiedBenchmarkRunner
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
...
```

## Source Files

### Benchmarks (`src/benchmarks/`)
- `LinearListBenchmark.java` - Benchmarks for Linear List
- `AVLTreeBenchmark.java` - Benchmarks for AVL Tree
- `RBTBenchmark.java` - Benchmarks for RBT (includes TopKRecent)

### Utilities (`src/utils/`)
- `BenchmarkResult.java` - Data class for storing runtime metrics
- `PerformanceBenchmark.java` - Shared utilities (warmup, timing, data loading)
- `ResultsWriter.java` - CSV output utilities

### Main Runner (`src/`)
- `UnifiedBenchmarkRunner.java` - Orchestrates all benchmarks and generates reports

## Expected Results

### Time Complexity
- **Linear List**: O(1) insert, O(N) search
- **AVL Tree**: O(log N) for all operations
- **RBT**: O(log N) for most operations, O(k) for recent reviews

### Expected Winners
- **CREATE**: Linear List (fastest insertion)
- **READ**: AVL Tree or RBT (logarithmic search)
- **UPDATE**: AVL Tree or RBT (logarithmic search + update)
- **DELETE**: AVL Tree or RBT (logarithmic search + delete)
- **RBAR**: Depends on implementation efficiency
- **RANKINGS**: Depends on algorithm
- **TOPK**: RBT optimized for this
- **TOPK_RECENT**: RBT (specialized structure)

## Cleaning Up

To clean compiled files:

```bash
rm -rf classes/*
rm -rf results/*
```

To recompile from scratch:

```bash
rm -rf classes/*
./compile.sh
```

## Troubleshooting

### Compilation Errors

If you see package errors, ensure you're running from the project root and the directory structure is correct.

### Runtime Errors

If you see `ClassNotFoundException`:
- Ensure you're running from project root
- Check that all classes are in `experiments/classes/`
- Verify the classpath includes `experiments/classes`

If CSV loading fails:
- Verify `data/airline.csv` exists
- Check file permissions

## Customization

### Change Data Sizes

Edit `src/utils/PerformanceBenchmark.java`:
```java
public static final int[] TEST_SIZES = {1000, 5000, 10000, 25000, 41000};
```

### Change Iteration Counts

Edit individual benchmark methods in `src/benchmarks/*Benchmark.java` files.

### Add New Operations

1. Add benchmark method to each `*Benchmark.java` in `src/benchmarks/`
2. Update `runAllBenchmarks()` to include it
3. Recompile and run

## Author

CS201 Project - Performance Experiments
Clean, organized directory structure for maintainability
