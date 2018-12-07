package io.github.alexbogovich.treevalidator;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NodeUtilsTest {

    @Test
    public void reverse() {
        Node root = Node.all("l0", 0, List.of(
                Node.all("l11", 1, List.of(
                        Node.of("l21", 2),
                        Node.of("l22", 2)
                )),
                Node.of("l12", 1)
        ));

        String reversedInlineValue = NodeUtils.getTreeNodesInReverseOrder(root);

        assertEquals("l0(l12,l11(l22,l21))", reversedInlineValue);
    }
}
