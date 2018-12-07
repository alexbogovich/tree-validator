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

    public Node getNodeTree(String line) {
        Deque<Node> nodeStack = new ArrayDeque<>();
        StringBuilder sb = new StringBuilder();
        int bracketCount = 0;

        for (Character c: line.toCharArray()) {
            if(c == START_OF_SUBTREE) {
                bracketCount++;
                processHeadOfSubtree(nodeStack, sb);
                sb = new StringBuilder();
            } else if(c == END_OF_SUBTREE) {
                bracketCount--;
                processLastElementOfSubtree(nodeStack, sb);
                sb = new StringBuilder();
            } else if(c == NODE_SEPARATOR) {
                processSubtreeElement(nodeStack, sb);
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }

            if ((bracketCount < 0)) {
                throw new RuntimeException();
            }
        }

        if ((bracketCount > 0)) {
            throw new RuntimeException();
        }

        if (nodeStack.size() == 0) {
            return Node.of(sb.toString(), 0);
        }

        return nodeStack.peek();
    }

    public static void processSubtreeElement(Deque<Node> nodeStack, StringBuilder sb) {
        if (nodeStack.size() == 0) {
            throw new RuntimeException();
        }
        if (sb.length() > 0) {
            var node = Node.of(sb.toString(), nodeStack.size());
            nodeStack.peekLast().getChildes().add(node);
        }
    }

    public static void processHeadOfSubtree(Deque<Node> nodeStack, StringBuilder sb) {
        if (sb.length() > 0) {
            var node = Node.of(sb.toString(), nodeStack.size());
            if(nodeStack.size() != 0) {
                nodeStack.peekLast().getChildes().add(node);
            }
            nodeStack.add(node);
        } else {
            throw new RuntimeException();
        }
    }

    public static void processLastElementOfSubtree(Deque<Node> nodeStack, StringBuilder sb) {
        if (sb.length() > 0) {
            var node = Node.of(sb.toString(), nodeStack.size());
            nodeStack.peekLast().getChildes().add(node);
        }
        if (nodeStack.size() > 1) {
            nodeStack.pollLast();
        }
    }

}
