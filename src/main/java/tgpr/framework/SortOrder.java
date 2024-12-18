package tgpr.framework;

public enum SortOrder {
    Ascending, Descending;

    public SortOrder reversed() {
        return this == Ascending ? Descending : Ascending;
    }
}
