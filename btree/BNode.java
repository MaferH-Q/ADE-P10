package btree;

class BTreeNode {
    int[] keys;
    BTreeNode[] children;
    int numKeys;
    boolean isLeaf;

    public BTreeNode(int order, boolean isLeaf) {
        keys = new int[order - 1];
        children = new BTreeNode[order];
        this.isLeaf = isLeaf;
        this.numKeys = 0;
    }
}