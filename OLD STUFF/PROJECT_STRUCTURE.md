# 📁 Project Structure

## Quick Overview

```
CS201/
├── compile.sh                    ← Compile everything
├── run.sh                        ← Run benchmarks
├── CS201_Analysis.ipynb          ← Jupyter notebook for charts
├── QUICK_START.md                ← Start here!
├── README.md                     ← Full documentation
│
├── data/
│   └── airline.csv               ← 41,350 real reviews
│
├── results/                      ← ALL results go here
│   ├── *.csv                     ← Import to Excel
│   ├── *.png                     ← Generated charts
│   ├── PERFORMANCE_ANALYSIS.md   ← Read this!
│   └── README.md                 ← Results guide
│
└── src/main/java/
    └── com/reviews/
        ├── Models/               ← Review data models
        ├── datastructures/       ← Linear List, AVL, RBT
        ├── utils/                ← CSV loader
        └── experiments/          ← Benchmarks
            ├── experiment1/      ← Linear List tests
            ├── experiment2/      ← AVL Tree tests
            ├── experiment3/      ← RBT tests
            ├── MasterRunner.java ← Main entry point
            └── UnifiedBenchmarkRunner.java
```

## How It Works

### 1. Data Flow
```
airline.csv (41,350 reviews)
    ↓
CSVLoader.java (reads data)
    ↓
BenchmarkUtils.java (distributes to experiments)
    ↓
Linear List / AVL Tree / RBT (performance tests)
    ↓
UnifiedBenchmarkRunner.java (collects results)
    ↓
results/ folder (CSV files)
    ↓
ChartGenerator.java (creates chart-ready CSVs)
ResultsAnalyzer.java (generates analysis)
    ↓
CS201_Analysis.ipynb (visualizes)
```

### 2. Key Files

**Main Entry Point:**
- `MasterRunner.java` - Runs everything

**Benchmarks:**
- `LinearListPerformanceBenchmark.java` - Test Linear List
- `AVLPerformanceBenchmark.java` - Test AVL Tree
- `RBTPerformanceBenchmark.java` - Test RBT

**Results:**
- `UnifiedBenchmarkRunner.java` - Orchestrates all tests
- `ChartGenerator.java` - Creates Excel-ready CSVs
- `ResultsAnalyzer.java` - Generates analysis reports

### 3. What Gets Tested

**Operations:**
1. **Search** - Find a specific review (by ID)
2. **Insertion** - Add all reviews to data structure
3. **RBAR** - Calculate Recency-Biased Average Rating
4. **Top-K** - Retrieve top K reviews (K=5, 10, 25, 50)

**Data Sizes Tested:**
- 1,000 reviews
- 5,000 reviews
- 10,000 reviews
- 20,000 reviews
- 41,457 reviews (full dataset)

## Simple Usage

```bash
# Step 1: Compile
./compile.sh

# Step 2: Run
./run.sh

# Step 3: View results
open results/PERFORMANCE_ANALYSIS.md
# OR
jupyter notebook CS201_Analysis.ipynb
```

## That's It!

Everything is automated. Just compile, run, and view results.

For detailed instructions, see `QUICK_START.md` or `README.md`.

