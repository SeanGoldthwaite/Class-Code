import java.util.ArrayList;
import java.util.Stack;

class Traversal<E> {

    // in-order recursive 
    static <E> List<E> inOrderRecursive(BinTree<E> tree){
        try {
            return inOrderRecursive(tree.getLeftT()).append(new Node<>(tree.getData(), inOrderRecursive(tree.getRightT())));
        }
        catch (EmptyTreeE e) {
            return new Empty<>();
        }
    }

    // in-order iterative
    static <E> List<E> inOrderIterative(BinTree<E> tree) {
        List<E> result = new Empty<>();
        Stack<Pair<BinTree<E>,E>> stack = new Stack<>();
        VISIT_NODE: while (true) {
            try {
                stack.push(new Pair<>(tree.getRightT(), tree.getData()));
                tree = tree.getLeftT();
            } catch (EmptyTreeE e) {
                while (!stack.isEmpty()) {
                    Pair<BinTree<E>,E> frame = stack.pop();
                    result = result.append(new Node<>(frame.getSecond(),new Empty<>()));
                    tree = frame.getFirst();
                    continue VISIT_NODE;
                }
                return result;
            }
        }
    }
    static <E> ArrayList<List<E>> inOrderMarked(BinTree<E> tree) {
        Stack<BinTree<E>> stack = new Stack<>();
        ArrayList<List<E>> result = new ArrayList();
        stack.push(tree);
        while (true) {
            int nodeCount = tree.size();
            if (nodeCount == 0)
                break;
            List<E> level = new Empty<>();
            while (nodeCount > 0) {
                try {
                    BinTree<E> current = stack.pop();
                    level = level.append(new Node<E>(current.getData(), new Empty<>()));
                    stack.push(current.getRightT());
                    stack.push(current.getLeftT());
                    nodeCount--;
                }
                catch (Exception e) {
                    break;
                }
            }
            result.add(level);
        }
        return result;
    }
    
    // pre-order recursive
    static <E> List<E> preOrderRecursive(BinTree<E> tree){
        try {
            return new Node<E>(tree.getData(), new Empty<>()).append(preOrderRecursive(tree.getLeftT())).append(preOrderRecursive(tree.getRightT()));
        }
        catch (EmptyTreeE e) {
            return new Empty<>();
        }
    }

    // pre-order iterative
    static <E> List<E> preOrderIterative(BinTree<E> tree) {
        Stack<BinTree<E>> stack = new Stack<>();

        stack.push(tree);
        List<E> result = new Empty<>();
        while (!stack.isEmpty()) {
            try {
                BinTree<E> temp = stack.pop();
                result = result.append(new Node<>(temp.getData(), new Empty<>()));
                stack.push(temp.getRightT());
                stack.push(temp.getLeftT());
            }
            catch (EmptyTreeE e) {

            }
        }
        return result;
    }

    // post-order recursive
    static <E> List<E> postOrderRecursive(BinTree<E> tree) {
        try {
            return postOrderRecursive(tree.getLeftT()).append(postOrderRecursive(tree.getRightT())).append(new Node<E>(tree.getData(), new Empty<E>()));
        }
        catch (EmptyTreeE e) {
            return new Empty<>();
        }
    }

    // post-order iterative
    static <E> List<E> postOrderIterative(BinTree<E> tree) {
        Stack<BinTree<E>> stack = new Stack<>();

        stack.push(tree);
        List<E> result = new Empty<>();
        while (!stack.isEmpty()) {
            try {
                BinTree<E> temp = stack.pop();
                result = new Node<>(temp.getData(), result);
                stack.push(temp.getLeftT());
                stack.push(temp.getRightT());
            }
            catch (EmptyTreeE e) {

            }
        }
        return result;
    }
}
