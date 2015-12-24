package udp

class UnifiedDiff {
    private String rawDiff
    private String fromFile
    private String toFile
    private FileStatus fileStatus
    private String mode
    private String oldMode
    private boolean isBinary
    private String similarityIndex
    private String dissimilarityIndex
    private String checksumBefore
    private String checksumAfter

    UnifiedDiff(String rawDiff) {
        this.rawDiff = rawDiff
    }

    String getMode() {
        return mode
    }

    void setMode(String mode) {
        this.mode = mode
    }

    String getOldMode() {
        return oldMode
    }

    void setOldMode(String oldMode) {
        this.oldMode = oldMode
    }

    FileStatus getFileStatus() {
        return fileStatus
    }

    void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus
    }

    String getRawDiff() {
        return rawDiff
    }

    String getToFile() {
        return toFile
    }

    void setToFile(String toFile) {
        this.toFile = toFile
    }

    String getFromFile() {
        return fromFile
    }

    void setFromFile(String fromFile) {
        this.fromFile = fromFile
    }

    void setSimilarityIndex(String similarityIndex) {
        this.similarityIndex = similarityIndex
    }

    String getSimilarityIndex() {
        return similarityIndex
    }

    // TODO: Still need to find an example of this field
    void setDissimilarityIndex(String dissimilarityIndex) {
        this.dissimilarityIndex = dissimilarityIndex
    }

    String getDisimilarityIndex() {
        return dissimilarityIndex
    }

    boolean isBinary() {
        return isBinary
    }

    void setIsBinary(boolean isBinary) {
        this.isBinary = isBinary
    }

    String getChecksumBefore() {
        return checksumBefore
    }

    void setChecksumBefore(String checksumBefore) {
        this.checksumBefore = checksumBefore
    }

    String getChecksumAfter() {
        return checksumAfter
    }

    void setChecksumAfter(String checksumAfter) {
        this.checksumAfter = checksumAfter
    }

    boolean isAddedFile() {
        return getFileStatus().equals(FileStatus.Added)
    }
    boolean isModifiedFile() {
        return getFileStatus().equals(FileStatus.Modified)
    }
    boolean isRemovedFile() {
        return getFileStatus().equals(FileStatus.Removed)
    }
    boolean isRenamed() {
        return getFileStatus().equals(FileStatus.Renamed)
    }
    boolean isCopied() {
        return getFileStatus().equals(FileStatus.Copied)
    }

    enum FileStatus {
        Added, Modified, Removed, Copied, Renamed
    }
}
