package datastructures.Recency_Biased_Tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

/**
 * RecencyBiasedTree<T>
 *
 * Implemented as a splay tree:
 *  - Tree is ordered by review date (most recent at root)
 *  - On insert or access, most recent is splayed to root
 */
public class RecencyBiasedTree<T> {

    private class Node {
        T value;
        Node left;
        Node right;
        Node parent;

        Node(T value, Node parent) {
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private final Comparator<? super T> comparator;

    public RecencyBiasedTree(Comparator<? super T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator must not be null");
        }
        this.comparator = comparator;
    }

    // =============== Public API ================

    public void insert(T value) {
        if (root == null) {
            root = new Node(value, null);
            return;
        }

        Node curr = root;
        Node parent = null;
        int cmp = 0;

        // standard BST insertion
        while (curr != null) {
            parent = curr;
            cmp = comparator.compare(value, curr.value);
            if (cmp < 0) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }

        Node newNode = new Node(value, parent);
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        // Ssplay global most recent (max) to root
        splayMax();
    }

    public List<T> inOrderTraversal() {
        List<T> result = new ArrayList<>();
        Deque<Node> stack = new ArrayDeque<>();
        Node curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            result.add(curr.value);
            curr = curr.right;
        }

        return result;
    }

    /**
     * Returns the k largest elements (by comparator),
     * in descending order (e.g. most recent dates first).
     */
    public List<T> reverseInOrderTakeK(int k) {
        List<T> result = new ArrayList<>();
        if (k <= 0) return result;

        Deque<Node> stack = new ArrayDeque<>();
        Node curr = root;

        while ((curr != null || !stack.isEmpty()) && result.size() < k) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.right;
            }
            curr = stack.pop();
            result.add(curr.value);
            curr = curr.left;
        }

        return result;
    }

    // =============== Internal helpers ================

    // find max element and splay it to root
    // newest review becomes root
    private void splayMax() {
        if (root == null) return;

        Node curr = root;
        while (curr.right != null) {
            curr = curr.right;
        }
        // curr is the maximum element
        splay(curr);
    }

    private void splay(Node x) {
        if (x == null) return;

        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;

            if (g == null) {
                // Zig
                if (x == p.left) {
                    rotateRight(p);
                } else {
                    rotateLeft(p);
                }
            } else if (x == p.left && p == g.left) {
                // Zig-zig (left-left)
                rotateRight(g);
                rotateRight(p);
            } else if (x == p.right && p == g.right) {
                // Zig-zig (right-right)
                rotateLeft(g);
                rotateLeft(p);
            } else if (x == p.right && p == g.left) {
                // Zig-zag (left-right)
                rotateLeft(p);
                rotateRight(g);
            } else {
                // Zig-zag (right-left)
                rotateRight(p);
                rotateLeft(g);
            }
        }

        root = x;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y == null) return;

        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }

        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y == null) return;

        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }

        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }

        y.right = x;
        x.parent = y;
    }
}
