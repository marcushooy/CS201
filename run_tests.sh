#!/bin/bash

# CS201 Project - Run All Tests

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║         CS201 Project - Running All Tests                     ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Compile all files
echo "🔨 Compiling..."
javac models/*.java utils/*.java datastructures/linear_list/*.java datastructures/avl_tree/*.java datastructures/rbt_tree/*.java

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
java -cp . datastructures.linear_list.LinearListTest 2>&1 | grep -v "Invalid CSV line"

echo ""
echo "════════════════════════════════════════════════════════════════"
echo "TEST 2: AVL Tree"
echo "════════════════════════════════════════════════════════════════"
java -cp . datastructures.avl_tree.AVLTest 2>&1 | grep -v "Invalid CSV line"

echo ""
echo "════════════════════════════════════════════════════════════════"
echo "TEST 3: RBT Tree"
echo "════════════════════════════════════════════════════════════════"
java -cp . datastructures.rbt_tree.RBTTest 2>&1 | grep -v "Invalid CSV line"

echo ""
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║         ✅ ALL TESTS COMPLETE!                                ║"
echo "╚════════════════════════════════════════════════════════════════╝"

