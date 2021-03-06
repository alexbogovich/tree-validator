package io.github.alexbogovich.treevalidator;

import java.util.Set;

public enum TreeOperation {
    INIT,
    APPEND,
    OPEN_BRACKET,
    CLOSE_BRACKET,
    COMMA,
    END;

    public boolean isInvalidPreviousOperation(TreeOperation previous) {
        return this.getPreviousInvalidOperation().contains(previous);
    }

    public Set<TreeOperation> getPreviousInvalidOperation() {
        switch (this) {
            case OPEN_BRACKET:
                return Set.of(CLOSE_BRACKET);
            case CLOSE_BRACKET:
                return Set.of(COMMA, INIT, OPEN_BRACKET);
            case COMMA:
                return Set.of(COMMA, INIT, OPEN_BRACKET);
            case END:
                return Set.of(COMMA, INIT, OPEN_BRACKET);
            default:
                return Set.of();
        }
    }
}
