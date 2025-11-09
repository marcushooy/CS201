#!/bin/bash

echo "================================================"
echo "  Compiling Performance Experiments"
echo "================================================"
echo ""

echo "Step 1: Compiling models..."
javac -d out models/*.java
if [ $? -eq 0 ]; then
    echo "✓ Models compiled successfully"
else
    echo "✗ Failed to compile models"
    exit 1
fi

echo ""
echo "Step 2: Compiling utilities..."
javac -d out -cp out utils/*.java
if [ $? -eq 0 ]; then
    echo "✓ Utilities compiled successfully"
else
    echo "✗ Failed to compile utilities"
    exit 1
fi

echo ""
echo "Step 3: Compiling data structures..."
javac -d out -cp out datastructures/Linear_List/*.java
javac -d out -cp out datastructures/AVL_Tree/*.java
javac -d out -cp out datastructures/Recency_Biased_Tree/*.java
if [ $? -eq 0 ]; then
    echo "✓ Data structures compiled successfully"
else
    echo "✗ Failed to compile data structures"
    exit 1
fi

echo ""
echo "Step 4: Compiling experiment utilities..."
javac -d out -cp out experiments/src/utils/*.java
if [ $? -eq 0 ]; then
    echo "✓ Experiment utilities compiled successfully"
else
    echo "✗ Failed to compile experiment utilities"
    exit 1
fi

echo ""
echo "Step 5: Compiling experiment benchmarks..."
javac -d out -cp out experiments/src/benchmarks/*.java
if [ $? -eq 0 ]; then
    echo "✓ Experiment benchmarks compiled successfully"
else
    echo "✗ Failed to compile experiment benchmarks"
    exit 1
fi

echo ""
echo "Step 6: Compiling unified runner..."
javac -d out -cp out experiments/src/UnifiedBenchmarkRunner.java
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
echo "All class files compiled to: out/"
echo ""
echo "To run the benchmarks:"
echo "  ./run.sh"
echo ""
