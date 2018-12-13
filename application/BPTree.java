import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                !comparator.contentEquals("<=") )
            return new ArrayList<V>();
        // TODO : Complete
        return null;
    }


    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String out = "";

        if (root.getClass() == LeafNode.class) {
            LeafNode node = (LeafNode) root;

            do {
                for (V value : node.values) {
                    out += "|" + value.toString() + "| ";
                }

                out += "==";
                node = node.next;
            } while (node != null);
        }

        return out;
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
            this.children = new ArrayList<>(Collections.nCopies(branchingFactor, null));
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
            int count = (int) children.stream().filter((node) -> node != null).count();
            return count == branchingFactor;
        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */
        void insert(K key, V value) {
            //Insert only key into internal node
            if (value == null) keys.add(key);
            else {
                int i;
                //find position to insert using insertion sort
                for (i = 0; i < keys.size(); i++) { if (key.compareTo(keys.get(i)) < 0) break; }

                if (children.get(i) == null) {
                    LeafNode leafNode = new LeafNode();
                    leafNode.insert(key, value);

                    children.set(i, leafNode);
                }

                else {
                    children.get(i).insert(key, value);
                    /*
                    //Overflow check and split accordingly!
                    if (children.get(i).isOverflow()) {
                        //if type of node is internal
                        if (children.get(i).getClass() == InternalNode.class) {

                        }

                        else if (children.get(i).getClass() == LeafNode.class) {
                            InternalNode node = (InternalNode) children.get(i).split();
                            children.set(i, null);

                            for (int j = 0; j < node.; j++) {

                            }
                        }
                    }
                    */

                }
            }

        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        Node split() {
            return null;
        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
         */
        List<V> rangeSearch(K key, String comparator) {
            // TODO : Complete
            return null;
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

            internalNode.insert(keys.get(keys.size()/2), null);
            for (int i = 0; i < keys.size(); i++) {
                internalNode.insert(keys.get(i), values.get(i));
            }

            return internalNode;
        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(Comparable, String)
         */
        List<V> rangeSearch(K key, String comparator) {
            // TODO : Complete
            return null;
        }

    } // End of class LeafNode


    /**
     * Contains a basic test scenario for a BPTree instance.
     * It shows a simple example of the use of this class
     * and its related types.
     *
     * @param args
     */
    public static void main(String[] args) {
        BPTree<Integer, Integer> bpTree = new BPTree<>(3);
        bpTree.insert(1, 1);
        bpTree.insert(10, 10);
        bpTree.insert(20, 20);
        bpTree.insert(5, 5);
        bpTree.insert(7, 7);
        System.out.println(bpTree);
    }

} // End of class BPTree
