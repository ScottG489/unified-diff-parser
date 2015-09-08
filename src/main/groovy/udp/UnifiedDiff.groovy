package udp

class UnifiedDiff {
    private String rawDiff

    private UnifiedDiff(String rawDiff) {
        this.rawDiff = rawDiff
    }

    static UnifiedDiff fromRawDiff(String rawDiff) {
        UnifiedDiff unifiedDiff = new UnifiedDiff(rawDiff)
        return unifiedDiff
    }

    String getRawDiff() {
        return rawDiff
    }
}
