#!/bin/bash

echo "================================================"
echo "  Running Performance Benchmarks"
echo "================================================"
echo ""

# Navigate to project root
cd ..

# Run the unified benchmark runner
java -cp .:models:utils:datastructures/linear_list:datastructures/avl_tree:datastructures/rbt_tree:experiments/classes experiments.UnifiedBenchmarkRunner

echo ""
echo "================================================"
echo "  Benchmark Run Complete"
echo "================================================"
echo ""
echo "Results saved to experiments/results/"
echo ""
echo "Generated files:"
echo "  - experiments/results/linear_list_results.csv"
echo "  - experiments/results/avl_tree_results.csv"
echo "  - experiments/results/rbt_results.csv"
echo "  - experiments/results/comparison_summary.csv"
echo ""
