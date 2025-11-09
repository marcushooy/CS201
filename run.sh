#!/bin/bash

echo "================================================"
echo "  Running Performance Benchmarks"
echo "================================================"
echo ""

# Run the unified benchmark runner
java -cp out experiments.UnifiedBenchmarkRunner

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
