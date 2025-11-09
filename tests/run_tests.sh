#!/bin/bash

# CS201 Project - Run All Tests

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║         CS201 Project - Running All Tests                      ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Move to project root
cd "$(dirname "$0")/.."

# Compile all files (models, utils, datastructures, and test files)
echo "🔨 Compiling..."
javac -d tests/out models/*.java utils/*.java datastructures/Linear_List/*.java datastructures/AVL_Tree/*.java datastructures/Recency_Biased_Tree/*.java tests/*.java

if [ $? -ne 0 ]; then
    echo "❌ Compilation failed!"
    exit 1
fi

echo "✅ Compilation successful!"
echo ""

# Run tests
echo "════════════════════════════════════════════════════════════════"
echo "TEST 1: Linear List"
echo "════════════════════════════════════════════════════════════════"
java -cp tests/out LinearListTest 2>&1 | grep -v "Invalid CSV line"

echo ""
echo "════════════════════════════════════════════════════════════════"
echo "TEST 2: AVL Tree"
echo "════════════════════════════════════════════════════════════════"
java -cp tests/out AVLTest 2>&1 | grep -v "Invalid CSV line"

echo ""
echo "════════════════════════════════════════════════════════════════"
echo "TEST 3: RBT Tree"
echo "════════════════════════════════════════════════════════════════"
java -cp tests/out RBTTest 2>&1 | grep -v "Invalid CSV line"

echo ""
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║         ✅ ALL TESTS COMPLETE!                                 ║"
echo "╚════════════════════════════════════════════════════════════════╝"

