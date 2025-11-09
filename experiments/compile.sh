#!/bin/bash

echo "================================================"
echo "  Compiling Performance Experiments"
echo "================================================"
echo ""

# Navigate to project root
cd ..

echo "Step 1: Compiling models..."
javac models/*.java
if [ $? -eq 0 ]; then
    echo "✓ Models compiled successfully"
else
    echo "✗ Failed to compile models"
    exit 1
fi

echo ""
echo "Step 2: Compiling utilities..."
javac -cp . utils/*.java
if [ $? -eq 0 ]; then
    echo "✓ Utilities compiled successfully"
else
    echo "✗ Failed to compile utilities"
    exit 1
fi

echo ""
echo "Step 3: Compiling data structures..."
javac -cp .:models:utils datastructures/linear_list/*.java
javac -cp .:models:utils datastructures/avl_tree/*.java
javac -cp .:models:utils datastructures/rbt_tree/*.java
if [ $? -eq 0 ]; then
    echo "✓ Data structures compiled successfully"
else
    echo "✗ Failed to compile data structures"
    exit 1
fi

echo ""
echo "Step 4: Compiling experiment utilities..."
javac -d experiments/classes -cp .:models:utils experiments/src/utils/*.java
if [ $? -eq 0 ]; then
    echo "✓ Experiment utilities compiled successfully"
else
    echo "✗ Failed to compile experiment utilities"
    exit 1
fi

echo ""
echo "Step 5: Compiling experiment benchmarks..."
javac -d experiments/classes -cp .:models:utils:datastructures/linear_list:datastructures/avl_tree:datastructures/rbt_tree:experiments/classes experiments/src/benchmarks/*.java
if [ $? -eq 0 ]; then
    echo "✓ Experiment benchmarks compiled successfully"
else
    echo "✗ Failed to compile experiment benchmarks"
    exit 1
fi

echo ""
echo "Step 6: Compiling unified runner..."
javac -d experiments/classes -cp .:models:utils:datastructures/linear_list:datastructures/avl_tree:datastructures/rbt_tree:experiments/classes experiments/src/UnifiedBenchmarkRunner.java
if [ $? -eq 0 ]; then
    echo "✓ Unified runner compiled successfully"
else
    echo "✗ Failed to compile unified runner"
    exit 1
fi

echo ""
echo "================================================"
echo "  Compilation Complete!"
echo "================================================"
echo ""
echo "Directory structure:"
echo "  experiments/"
echo "    ├── src/"
echo "    │   ├── benchmarks/     (benchmark implementations)"
echo "    │   ├── utils/          (shared utilities)"
echo "    │   └── UnifiedBenchmarkRunner.java"
echo "    ├── classes/            (compiled .class files)"
echo "    ├── results/            (benchmark results - will be created on run)"
echo "    ├── compile.sh"
echo "    ├── run.sh"
echo "    └── README.md"
echo ""
echo "To run the benchmarks:"
echo "  cd experiments"
echo "  ./run.sh"
echo ""
