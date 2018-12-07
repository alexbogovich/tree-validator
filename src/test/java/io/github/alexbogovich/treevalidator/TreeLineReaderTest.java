package io.github.alexbogovich.treevalidator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.slf4j.LoggerFactory.getLogger;

class TreeLineReaderTest {
    private static final Logger log = getLogger(TreeLineReaderTest.class);

    @Test
    public void single() {
        String line = "single";
        Node node = TreeLineReader.getNodeTree(line);
        assertEquals(Node.of(line, 0), node);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "single(",
            "single)",
            "single,",
            ",single",
            ")single",
            "(single"
    })
    void singleErrorCases(String candidate) {
        assertThrows(RuntimeException.class, () -> TreeLineReader.getNodeTree(candidate));
    }

    @Test
    public void oneLevelNesting() {
        String line = "root(nest1,nest2)";
        Node node = TreeLineReader.getNodeTree(line);
        assertEquals(Node.all("root", 0,
                List.of(
                        Node.of("nest1", 1),
                        Node.of("nest2", 1))
                ),
                node);
    }

    @Test
    public void forLevelNesting() {
        String line = "root(nest1,nest2(nest3(nest4),nest5))";
        Node node = TreeLineReader.getNodeTree(line);
        assertEquals(Node.all("root", 0,
                List.of(
                        Node.of("nest1", 1),
                        Node.all("nest2", 1,
                                List.of(
                                        Node.all("nest3", 2,
                                                List.of(Node.of("nest4", 3))
                                        ),
                                        Node.of("nest5", 2)
                                )
                        ))
                ),
                node);
    }

    @Test
    void getReversionInlineNodeTree() {
        String reversionInlineNodeTree = TreeLineReader.getReversedInlineNodeTree("aaa(bbb(sdf(1,2),ooo(4,5,fgh(6))),456)");
        assertEquals("aaa(456,bbb(ooo(fgh(6),5,4),sdf(2,1)))", reversionInlineNodeTree);
    }
}
