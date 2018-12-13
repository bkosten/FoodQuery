import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayDeque;

//GOD BLESS THIS STACK OVERFLOW POST
//https://stackoverflow.com/questions/33139425/concurrentmodificationexception-is-thrown-when-it-should-not

/**
 * Implementation of a B+ tree to allow efficient access to
 * many different indexes of a large data set.
 * BPTree objects are created for each type of index
 * needed by the program.  BPTrees provide an efficient
 * range search as compared to other types of data structures
 * due to the ability to perform log_m N lookups and
 * linear in-order traversals of the data items.
 *
 * @author sapan (sapan@cs.wisc.edu)
 *
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

    // Root of the tree
    private Node root;

    // Branching factor is the number of children nodes
    // for internal nodes of the tree
    private int branchingFactor;


    /**
     * Public constructor
     *
     * @param branchingFactor
     */
    public BPTree(int branchingFactor) {
        if (branchingFactor <= 2) throw new IllegalArgumentException("Illegal branching factor: " + branchingFactor);
        this.branchingFactor = branchingFactor;
    }


    /*
     * (non-Javadoc)
     * @see BPTreeADT#insert(java.lang.Object, java.lang.Object)
     */
    @Override
    public void insert(K key, V value) {
        if (root == null) root = new LeafNode();

        root.insert(key, value);

        if (root.isOverflow()) {
            root = root.split();
        }
    }


    /*
     * (non-Javadoc)
     * @see BPTreeADT#rangeSearch(java.lang.Object, java.lang.String)
     */
    @Override
    public List<V> rangeSearch(K key, String comparator) {
        if (!comparator.contentEquals(">=") &&
                !comparator.contentEquals("==") &&
                !comparator.contentEquals("<=") ) {
            ArrayList<V> values = new ArrayList<>();
            return values;
        }
        return root.rangeSearch(key, comparator);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        Queue<List<Node>> queue = new LinkedList<List<Node>>();
        queue.add(Arrays.asList(root));
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
            while (!queue.isEmpty()) {
                List<Node> nodes = queue.remove();
                sb.append('{');
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    sb.append(node.toString());
                    if (it.hasNext())
                        sb.append(", ");
                    if (node instanceof BPTree.InternalNode)
                        nextQueue.add(((InternalNode) node).children);
                }
                sb.append('}');
                if (!queue.isEmpty())
                    sb.append(", ");
                else {
                    sb.append('\n');
                }
            }
            queue = nextQueue;
        }
        return sb.toString();
    }

    private boolean compare(K key1, K key2, String comparator) {
        switch (comparator) {
            case "<=":
                return key1.compareTo(key2) <= 0;
            case ">=":
                return key1.compareTo(key2) >= 0;
            case "==":
                return key1.equals(key2);
            default:
                return false;
        }
    }

    /**
     * This abstract class represents any type of node in the tree
     * This class is a super class of the LeafNode and InternalNode types.
     *
     * @author sapan
     */
    private abstract class Node {

        // List of keys
        List<K> keys;

        /**
         * Package constructor
         */
        Node() {
            this.keys = new ArrayList<>();
        }

        /**
         * Inserts key and value in the appropriate leaf node
         * and balances the tree if required by splitting
         *
         * @param key
         * @param value
         */
        abstract void insert(K key, V value);

        /**
         * Gets the first leaf key of the tree
         *
         * @return key
         */
        abstract K getFirstLeafKey();

        /**
         * Gets the new sibling created after splitting the node
         *
         * @return Node
         */
        abstract Node split();

        /*
         * (non-Javadoc)
         * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
         */
        abstract List<V> rangeSearch(K key, String comparator);

        /**
         *
         * @return boolean
         */
        abstract boolean isOverflow();

        public String toString() {
            return keys.toString();
        }

    } // End of abstract class Node

    /**
     * This class represents an internal node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations
     * required for internal (non-leaf) nodes.
     *
     * @author sapan
     */
    private class InternalNode extends Node {

        // List of children nodes
        List<Node> children;

        /**
         * Package constructor
         */
        InternalNode() {
            super();
            this.children = new ArrayList<>();

        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            return children.get(0).getFirstLeafKey();
        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            return keys.size() >= branchingFactor;
        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */
        void insert(K key, V value) {

            int i;
            //find position to insert using insertion sort
            for (i = 0; i < keys.size(); i++) {
                if (key.compareTo(keys.get(i)) < 0) break;
            }

            children.get(i).insert(key, value);

            if (children.get(i).isOverflow()) {
                InternalNode splitNode = (InternalNode) children.get(i).split();

                //merge with splitNode
                keys.add(i, splitNode.keys.get(0));
                children.set(i, splitNode.children.get(0));
                children.add(i + 1, splitNode.children.get(1));
            }
        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        Node split() {
            InternalNode internalNode = new InternalNode();
            InternalNode left = new InternalNode();
            InternalNode right = new InternalNode();

            int pivot = branchingFactor/2;

            left.keys = new ArrayList<>(keys.subList(0, pivot));
            left.children = new ArrayList<>(children.subList(0, pivot + 1));

            right.keys = new ArrayList<>(keys.subList(pivot + 1, keys.size()));
            right.children = new ArrayList<>(children.subList(pivot+1, children.size()));

            internalNode.keys.add(keys.get(pivot));
            internalNode.children.add(left);
            internalNode.children.add(right);

            return internalNode;
        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
         */
        List<V> rangeSearch(K key, String comparator) {
            ArrayList<V> out = new ArrayList<>();

            //binary search
            int mid = 0;
            int high = keys.size() - 1;
            int low = 0;

            while (low <= high) {
                mid = low + (high - low)/2;

                if (keys.get(mid).equals(key)) {
                    break;
                }
                else if (keys.get(mid).compareTo(key) < 0) {
                    low = mid + 1;
                }

                else if (keys.get(mid).compareTo(key) > 0) {
                    high = mid - 1;
                }
            }

            switch (comparator) {
                case "<=":
                    if (keys.get(mid).compareTo(key) <= 0) mid = mid + 1;
                    for (int i = 0; i <= mid; i++) {
                        out.addAll(children.get(i).rangeSearch(key, comparator));
                    }
                    break;
                case ">=":
                    if (keys.get(mid).compareTo(key) >= 0) mid = mid;
                    for (int i = mid; i < children.size(); i++) {
                        out.addAll(children.get(i).rangeSearch(key, comparator));
                    }
                    break;
                case "==":
                    //either mid or mid + 1
                    if (keys.get(mid).equals(key)) mid = mid + 1;
                    out = (ArrayList<V>) children.get(mid).rangeSearch(key, comparator);
                break;
            }

            return out;
        }

    } // End of class InternalNode


    /**
     * This class represents a leaf node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations that
     * required for leaf nodes.
     *
     * @author sapan
     */
    private class LeafNode extends Node {

        // List of values
        List<V> values;

        // Reference to the next leaf node
        LeafNode next;

        // Reference to the previous leaf node
        LeafNode previous;

        /**
         * Package constructor
         */
        LeafNode() {
            super();
            values = new ArrayList<>();
        }


        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            return keys.get(0);
        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            return values.size() == branchingFactor;
        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(Comparable, Object)
         */
        void insert(K key, V value) {
            int i;

            //insertion sort O(N)
            for (i = 0; i < keys.size(); i++) {
                if (key.compareTo(keys.get(i)) < 0) {
                    break;
                }
            }

            keys.add(i, key);
            values.add(i, value);
        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        Node split() {
            InternalNode internalNode = new InternalNode();
            LeafNode left = new LeafNode();
            LeafNode right = new LeafNode();

            int pivot = branchingFactor/2;

            for (int i = 0; i < branchingFactor; i++) {
                if (i < pivot) {
                    left.insert(keys.get(i), values.get(i));
                }
                else {
                    right.insert(keys.get(i), values.get(i));
                }
            }

            if (previous != null) previous.next = left;
            left.previous = previous;
            left.next = right;

            if (next != null) next.previous = right;
            right.previous = left;
            right.next = next;

            internalNode.keys.add(keys.get(pivot));
            internalNode.children.add(left);
            internalNode.children.add(right);

            return internalNode;
        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(Comparable, String)
         */
        List<V> rangeSearch(K key, String comparator) {
            ArrayList<V> out = new ArrayList<>();
            for (int i = 0; i < keys.size(); i++) {
                if (compare(keys.get(i), key, comparator)) {
                    out.add(values.get(i));
                }
            }

            return out;
        }

    } // End of class LeafNode
} // End of class BPTree
