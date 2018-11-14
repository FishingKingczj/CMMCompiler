package struct;

import java.util.ArrayList;

public class TreeNode {
    private TreeNode parent;
    private ArrayList<TreeNode> children;

    public TreeNode() {
        parent = null;
        children = new ArrayList<>();
    }

    public TreeNode(TreeNode parent, ArrayList<TreeNode> children) {
        this.parent = parent;
        this.children = children;
        for (TreeNode node : children) {
            node.parent = this;
        }
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<TreeNode> children) {
        this.children = children;
        for (TreeNode node : children) {
            node.parent = this;
        }
    }

    public void addChildren(TreeNode child) {
        children.add(child);
        child.parent = this;
    }

    public void addChildren(int index, ArrayList<TreeNode> children) {
        this.children.addAll(index, children);
        for (TreeNode child : children) {
            child.parent = this;
        }
    }

    public void removeChildren(int index) {
        children.remove(index);
    }

    public void removeChildren(TreeNode child) {
        children.remove(child);
    }

    public void replaceChildren(TreeNode child, ArrayList<TreeNode> replace) {
        int index = children.indexOf(child);
        children.remove(child);
        addChildren(index, replace);
    }


    /**
     * 获取右节点
     *
     * @return 节点的右节点
     */
    public TreeNode getRight() {
        if (parent == null) return null;
        int pos = parent.children.indexOf(this);
        return pos + 1 == parent.children.size() ? null : parent.children.get(pos + 1);
    }

    /**
     * 获取左子节点
     *
     * @return 节点左子节点
     */
    public TreeNode getLeftChild() {
        if (children == null) return null;
        return children.isEmpty() ? null : children.get(0);
    }

    public static String print(TreeNode treeNode) {
        return subPrint(treeNode, "", 1);
    }

    private static String subPrint(TreeNode treeNode, String str, int layer) {
        //获取内容
        String type;
        if (treeNode.getClass() == NonTerminal.class) {
            type = ((NonTerminal) treeNode).getType().toString();
        } else if (treeNode.getClass() == Token.class) {
            type = ((Token) treeNode).getType().toString();
        } else return null;

        //打印自己
        for (int i = 1; i < layer; i++)
            str += "  ";
        str += "└ ";
        str += type;
        str += "\n";
        for (TreeNode child : treeNode.getChildren()) {
            str = subPrint(child, str, layer + 1);
        }
        return str;
    }
}
