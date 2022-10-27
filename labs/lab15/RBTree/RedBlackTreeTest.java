package RBTree;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class RedBlackTreeTest {

    @Test
    public void buildRedBlackTreeTest() {
        RedBlackTree.RBTreeNode<Integer> node = new RedBlackTree.RBTreeNode(true, 2);
        RedBlackTree<Integer> tree = new RedBlackTree();
        tree.insert(1);
        assertEquals(tree.root.item, (Integer) 1);
        tree.insert(2);
        assertEquals(tree.root.right.item, (Integer) 3);

    }

    @Test
    public void flipColors() {
    }

    @Test
    public void rotateRight() {
    }

    @Test
    public void rotateLeft() {
    }

    @Test
    public void insert() {
    }
}