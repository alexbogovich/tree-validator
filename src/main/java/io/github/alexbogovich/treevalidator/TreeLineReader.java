package io.github.alexbogovich.treevalidator;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;


@SuppressWarnings("WeakerAccess")
@UtilityClass
@Slf4j
public class TreeLineReader {

    private static final char OPEN_BRACKET = '(';
    private static final char CLOSE_BRACKET = ')';
    private static final char COMMA = ',';

    public String getReversedInlineNodeTree(String line) {
        String output = "ERROR";
        try {
            output = NodeUtils.getTreeNodesInReverseOrder(getNodeTree(line));
        } catch (Exception e) {
            log.error("Exception:" + e.getMessage() + ". Line: " + line);
        }
        return output;
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

    public TreeOperation processSubtreeElement(Deque<Node> nodeStack, String nodeName, TreeOperation previousOperation) {
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

    public TreeOperation processHeadOfSubtree(Deque<Node> nodeStack, String nodeName, TreeOperation previousOperation) {
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

    public TreeOperation processLastElementOfSubtree(Deque<Node> nodeStack, String nodeName, TreeOperation previousOperation) {
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
