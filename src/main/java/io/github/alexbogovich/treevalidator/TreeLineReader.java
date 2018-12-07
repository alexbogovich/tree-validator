package io.github.alexbogovich.treevalidator;

import lombok.experimental.UtilityClass;

import java.util.ArrayDeque;
import java.util.Deque;


@SuppressWarnings("WeakerAccess")
@UtilityClass
public class TreeLineReader {

    private static final char OPEN_BRACKET = '(';
    private static final char CLOSE_BRACKET = ')';
    private static final char COMMA = ',';

    public String getReversedInlineNodeTree(String line) {
        return NodeUtils.getTreeNodesInReverseOrder(getNodeTree(line));
    }

    public Node getNodeTree(String line) {
        Deque<Node> nodeStack = new ArrayDeque<>();
        StringBuilder sb = new StringBuilder();
        int bracketCount = 0;
        TreeOperation lastOperation = TreeOperation.INIT;

        for (Character c: line.toCharArray()) {
            if(c == OPEN_BRACKET) {
                bracketCount++;
                lastOperation = processHeadOfSubtree(nodeStack, sb.toString(), lastOperation);
                sb = new StringBuilder();
            } else if(c == CLOSE_BRACKET) {
                bracketCount--;
                lastOperation = processLastElementOfSubtree(nodeStack, sb.toString(), lastOperation);
                sb = new StringBuilder();
            } else if(c == COMMA) {
                lastOperation = processSubtreeElement(nodeStack, sb.toString(), lastOperation);
                sb = new StringBuilder();
            } else {
                sb.append(c);
                lastOperation = TreeOperation.APPEND;
            }

            if ((bracketCount < 0)) {
                throw new RuntimeException("Invalid close bracket placement");
            }
        }

        if ((bracketCount > 0)) {
            throw new RuntimeException("Missing close bracket");
        }

        if (TreeOperation.END.isInvalidPreviousOperation(lastOperation)) {
            throw new RuntimeException("Invalid last operation");
        }

        if (nodeStack.size() == 0) {
            return Node.of(sb.toString(), 0);
        }

        return nodeStack.peek();
    }

    public static TreeOperation processSubtreeElement(Deque<Node> nodeStack, String nodeName, TreeOperation previousOperation) {
        if (TreeOperation.COMMA.isInvalidPreviousOperation(previousOperation)) {
            throw new RuntimeException("Invalid operation order");
        }
        if (nodeStack.size() == 0) {
            throw new RuntimeException("More than 1 root element");
        }
        if (nodeName.length() > 0) {
            Node node = Node.of(nodeName, nodeStack.size());
            nodeStack.peekLast().getChildes().add(node);
        }
        return TreeOperation.COMMA;
    }

    public static TreeOperation processHeadOfSubtree(Deque<Node> nodeStack, String nodeName, TreeOperation previousOperation) {
        if (TreeOperation.OPEN_BRACKET.isInvalidPreviousOperation(previousOperation)) {
            throw new RuntimeException("Invalid operation order");
        }
        if (nodeName.length() > 0) {
            Node node = Node.of(nodeName, nodeStack.size());
            if(nodeStack.size() != 0) {
                nodeStack.peekLast().getChildes().add(node);
            }
            nodeStack.add(node);
        } else {
            throw new RuntimeException("Missing or empty node name");
        }
        return TreeOperation.OPEN_BRACKET;
    }

    public static TreeOperation processLastElementOfSubtree(Deque<Node> nodeStack, String nodeName, TreeOperation previousOperation) {
        if (TreeOperation.CLOSE_BRACKET.isInvalidPreviousOperation(previousOperation)) {
            throw new RuntimeException("Invalid operation order");
        }
        if (nodeName.length() > 0) {
            Node node = Node.of(nodeName, nodeStack.size());
            nodeStack.peekLast().getChildes().add(node);
        }
        if (nodeStack.size() > 1) {
            nodeStack.pollLast();
        }
        return TreeOperation.CLOSE_BRACKET;
    }

}
