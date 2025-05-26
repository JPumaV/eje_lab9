
class Node {
    int key;
    Node left, right;
    int height;

    Node(int d) {
        key = d;
        height = 1;
    }
}

class BSTree {
    Node root;

    Node search(Node root, int key) {
        if (root == null || root.key == key)
            return root;
        if (key < root.key)
            return search(root.left, key);
        return search(root.right, key);
    }

    int height(Node node) {
        return node == null ? 0 : node.height;
    }

    void inOrder(Node root) {
        if (root != null) {
            inOrder(root.left);
            System.out.print(root.key + " ");
            inOrder(root.right);
        }
    }
}

class AVLTree extends BSTree {

    int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    Node insert(Node node, int key) {
        if (node == null)
            return new Node(key);

        if (key < node.key)
            node.left = insert(node.left, key);
        else if (key > node.key)
            node.right = insert(node.right, key);
        else
            return node;

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        // Rotaciones
        if (balance > 1 && key < node.left.key)
            return rotateRight(node); // Left Left
        if (balance < -1 && key > node.right.key)
            return rotateLeft(node); // Right Right
        if (balance > 1 && key > node.left.key) { // Left Right
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && key < node.right.key) { // Right Left
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    Node delete(Node root, int key) {
        if (root == null)
            return root;

        if (key < root.key)
            root.left = delete(root.left, key);
        else if (key > root.key)
            root.right = delete(root.right, key);
        else {
            if ((root.left == null) || (root.right == null)) {
                Node temp = null;
                temp = root.left != null ? root.left : root.right;
                if (temp == null)
                    return null;
                else
                    root = temp;
            } else {
                Node temp = minValueNode(root.right);
                root.key = temp.key;
                root.right = delete(root.right, temp.key);
            }
        }

        root.height = Math.max(height(root.left), height(root.right)) + 1;
        int balance = getBalance(root);

        // Rebalanceo
        if (balance > 1 && getBalance(root.left) >= 0)
            return rotateRight(root);
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = rotateLeft(root.left);
            return rotateRight(root);
        }
        if (balance < -1 && getBalance(root.right) <= 0)
            return rotateLeft(root);
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rotateRight(root.right);
            return rotateLeft(root);
        }

        return root;
    }

    void BFS(Node node) {
        int h = height(node);
        for (int i = 1; i <= h; i++) {
            printLevel(node, i);
        }
    }

    void printLevel(Node node, int level) {
        if (node == null)
            return;
        if (level == 1)
            System.out.print(node.key + " ");
        else if (level > 1) {
            printLevel(node.left, level - 1);
            printLevel(node.right, level - 1);
        }
    }

    void preOrder(Node node) {
        if (node != null) {
            System.out.print(node.key + " ");
            preOrder(node.left);
            preOrder(node.right);
        }
    }
}

public class AVLTreeLab {
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();

        int[] keys = { 50, 30, 70, 20, 40, 60, 80, 10, 25, 65 };
        for (int key : keys)
            tree.root = tree.insert(tree.root, key);

        System.out.println("Recorrido por niveles (BFS):");
        tree.BFS(tree.root);
        System.out.println("\n\nRecorrido Preorden:");
        tree.preOrder(tree.root);

        System.out.println("\n\nEliminando 30 (verificar balance):");
        tree.root = tree.delete(tree.root, 30);
        tree.BFS(tree.root);
    }
}
