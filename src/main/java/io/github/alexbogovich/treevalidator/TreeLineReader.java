package io.github.alexbogovich.treevalidator;

import lombok.experimental.UtilityClass;

import java.util.ArrayDeque;
import java.util.Deque;


@SuppressWarnings("WeakerAccess")
@UtilityClass
public class TreeLineReader {

    private static final char START_OF_SUBTREE = '(';
    private static final char END_OF_SUBTREE = ')';
    private static final char NODE_SEPARATOR = ',';

    public String getReversedInlineNodeTree(String line) {
        return NodeUtils.getTreeNodesInReverseOrder(getNodeTree(line));
    }

    public Node getNodeTree(String line) {
        Deque<Node> nodeStack = new ArrayDeque<>();
        StringBuilder sb = new StringBuilder();
        int bracketCount = 0;

        for (Character c: line.toCharArray()) {
            if(c == START_OF_SUBTREE) {
                bracketCount++;
                processHeadOfSubtree(nodeStack, sb.toString());
                sb = new StringBuilder();
            } else if(c == END_OF_SUBTREE) {
                bracketCount--;
                processLastElementOfSubtree(nodeStack, sb.toString());
                sb = new StringBuilder();
            } else if(c == NODE_SEPARATOR) {
                processSubtreeElement(nodeStack, sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }

            if ((bracketCount < 0)) {
                throw new RuntimeException("Invalid close bracket");
            }
        }

        if ((bracketCount > 0)) {
            throw new RuntimeException("Missing close bracket");
        }

        if (nodeStack.size() == 0) {
            return Node.of(sb.toString(), 0);
        }

        return nodeStack.peek();
    }

    public static void processSubtreeElement(Deque<Node> nodeStack, String nodeName) {
        if (nodeStack.size() == 0) {
            throw new RuntimeException("More than 1 root element");
        }
        if (nodeName.length() > 0) {
            var node = Node.of(nodeName, nodeStack.size());
            nodeStack.peekLast().getChildes().add(node);
        }
    }

    public static void processHeadOfSubtree(Deque<Node> nodeStack, String nodeName) {
        if (nodeName.length() > 0) {
            var node = Node.of(nodeName, nodeStack.size());
            if(nodeStack.size() != 0) {
                nodeStack.peekLast().getChildes().add(node);
            }
            nodeStack.add(node);
        } else {
            throw new RuntimeException("Missing or empty node name");
        }
    }

    public static void processLastElementOfSubtree(Deque<Node> nodeStack, String nodeName) {
        if (nodeName.length() > 0) {
            var node = Node.of(nodeName, nodeStack.size());
            nodeStack.peekLast().getChildes().add(node);
        }
        if (nodeStack.size() > 1) {
            nodeStack.pollLast();
        }
    }

}
