package io.github.alexbogovich.treevalidator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(staticName = "all")
@RequiredArgsConstructor(staticName = "of")
@Data
class Node {
    private final String value;
    private final int depth;
    private List<Node> childes = new ArrayList<>();
}
