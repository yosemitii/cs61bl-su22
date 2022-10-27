package RBTree;

public class RedBlackTree<T extends Comparable<T>> {

    /* Root of the tree. */
    RBTreeNode<T> root;

    static class RBTreeNode<T> {

        final T item;
        boolean isBlack;
        RBTreeNode<T> left;
        RBTreeNode<T> right;

        /* Creates a RBTreeNode with item ITEM and color depending on ISBLACK
           value. */
        RBTreeNode(boolean isBlack, T item) {
            this(isBlack, item, null, null);
        }

        /* Creates a RBTreeNode with item ITEM, color depending on ISBLACK
           value, left child LEFT, and right child RIGHT. */
        RBTreeNode(boolean isBlack, T item, RBTreeNode<T> left,
                   RBTreeNode<T> right) {
            this.isBlack = isBlack;
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }

    /* Creates an empty RBTree.RedBlackTree. */
    public RedBlackTree() {
        root = null;
    }

    /* Creates a RBTree.RedBlackTree from a given 2-3 TREE. */
    public RedBlackTree(TwoThreeTree<T> tree) {
        Node<T> ttTreeRoot = tree.root;
        root = buildRedBlackTree(ttTreeRoot);
    }

    /* Builds a RBTree.RedBlackTree that has isometry with given 2-3 tree rooted at
       given node R, and returns the root node. */
    RBTreeNode<T> buildRedBlackTree(Node<T> r) {
        if (r == null) {
            return null;
        }

        if (r.getItemCount() == 1) {
            // TODO: Replace with code to create a 2-node equivalent
            RBTreeNode<T> temp = new RBTreeNode<>(true, r.getItemAt(0),
                    buildRedBlackTree(r.getChildAt(0)), buildRedBlackTree(r.getChildAt(1)));
            return temp;
//            root = new RBTreeNode<>(true, r.getItemAt(0),
//                    buildRedBlackTree(r.getChildAt(0)), buildRedBlackTree(r.getChildAt(1)));
//            return root;
        } else {
            // TODO: Replace with code to create a 3-node equivalent
            RBTreeNode<T> temp = new RBTreeNode<>(true, r.getItemAt(1),
                    new RBTreeNode<>(false, r.getItemAt(0), buildRedBlackTree(r.getChildAt(0)), buildRedBlackTree(r.getChildAt(1))),
                    buildRedBlackTree(r.getChildAt(2)));
            return temp;
        }
    }

    /* Flips the color of NODE and its children. Assume that NODE has both left
       and right children. */
    void flipColors(RBTreeNode<T> node) {
        // TODO: YOUR CODE HERE
        if (node.isBlack)
        {
            node.isBlack = false;
            node.left.isBlack = true;
            node.right.isBlack = true;
        }
        else{
            node.isBlack = true;
            node.left.isBlack = false;
            node.right.isBlack = false;
        }
    }

    /* Rotates the given node NODE to the right. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateRight(RBTreeNode<T> node) {
        // TODO: YOUR CODE HERE
//        return new RBTreeNode<T>(true, node.left.item, node.left.left,
//                new RBTreeNode<T>(false, node.item, node.left.right, node.right));
        boolean leftCorlor = node.left.isBlack;
        T oldLeftItem = node.left.item;
        T oldRootItem = node.item;
        //new root node
        RBTreeNode<T> temp1 = new RBTreeNode<T>(node.isBlack, oldLeftItem);
        temp1.left = node.left.left;
        //new right node
        RBTreeNode<T> temp2 = new RBTreeNode<>(leftCorlor, oldRootItem);
        temp2.left = node.left.right;
        temp2.right = node.right;

        temp1.right = temp2;
        return temp1;
    }

    /* Rotates the given node NODE to the left. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateLeft(RBTreeNode<T> node) {
        // TODO: YOUR CODE HERE
//        return new RBTreeNode<T>(true, node.right.item,
//                new RBTreeNode<T>(false, node.item, node.left, node.right.left),
//                node.right.right);
        boolean oldRightColor = node.right.isBlack;
        boolean oldRootColor = node.isBlack;
        T oldRightItem = node.right.item;
        T oldLeftItem = node.item;
        //new root node
        RBTreeNode<T> newRoot = new RBTreeNode<T>(oldRootColor, oldRightItem);
        newRoot.right = node.right.right;
        //new left node
        RBTreeNode<T> temp2 = new RBTreeNode<>(oldRightColor, oldLeftItem);
        temp2.left = node.left;
        temp2.right = node.right.left;

        newRoot.left = temp2;
        return newRoot;


    }

    public void insert(T item) {
        root = insert(root, item);
        root.isBlack = true;
    }

    /* Inserts the given node into this Red Black Tree*/
    private RBTreeNode<T> insert(RBTreeNode<T> node, T item) {
        // Insert (return) new red leaf node.
        if (node == null) {
            return new RBTreeNode<>(false, item);
        }
        // Handle normal binary search tree insertion.
        int comp = item.compareTo(node.item);
        if (comp == 0) {
            return node; // do nothing.
        } else if (comp < 0) {
            node.left = insert(node.left, item);
        } else {
            node.right = insert(node.right, item);
        }
        // hanlde "Only Child of a Black RBTree.Node"
        if ((node.left == null) && (isRed(node.right))){
            node = rotateLeft(node);
        }

        // handle "middle of three" and "right-leaning red" structures
        if ((isRed(node) && isRed(node.right))){
            node = rotateLeft(node);
        }

        // handle "smallest of three" structure
        if (node.left != null){
            if (node.left.left != null){
//                if ((node.isBlack) && (isRed(node.left) && isRed(node.left.left))){
                if (isRed(node.left.left) && isRed(node.left)){
                    node = rotateRight(node);
                }
            }
        }
        
        // handle "largest of three" structure
        if (node.left != null){
            if (node.right != null){
                if ((isRed(node.left) && isRed(node.right))){
                    flipColors(node);
                }
            }
        }

        return node; //fix this return statement
    }



    /* Returns whether the given node NODE is red. Null nodes (children of leaf
       nodes are automatically considered black. */
    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }

    public void toString(RBTreeNode node){
        toStringHelper(node, 0);
    }

    public void toStringHelper(RBTreeNode<T> node, Integer n){
        System.out.print(n);
        System.out.print(node.item);
        if (node.left != null){
            toStringHelper(node.left, n+1);
        }
        if (node.right != null){
            toStringHelper(node.right, n+1);
        }
    }

//    public static void main(String[] args) {
//        RBTreeNode<Integer> node = new RBTreeNode<Integer>(true, 5);
//        RBTree.RedBlackTree<Integer> tree = new RBTree.RedBlackTree();
//        tree.insert(5);
//        tree.toString();
//
//    }

}
