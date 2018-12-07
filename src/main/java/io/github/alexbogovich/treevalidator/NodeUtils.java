package io.github.alexbogovich.treevalidator;

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;

import java.util.stream.Collectors;

@UtilityClass
public class NodeUtils {
    public String getTreeNodesInReverseOrder(Node root) {
        if ((root.getChildes().size() == 0)) {
            return root.getValue();
        }
        String collect = Lists.reverse(root.getChildes())
                .stream()
                .map(NodeUtils::getTreeNodesInReverseOrder)
                .collect(Collectors.joining(","));
        return root.getValue() + "(" + collect + ")";
    }
}
