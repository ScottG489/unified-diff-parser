package udp

class UnifiedDiff {
    private String rawDiff
    private String fromFile
    private String toFile
    private FileStatus fileStatus
    private String mode
    private boolean isBinary
    private String diffBody
    private String diffHeader
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

    String getDiffBody() {
        return diffBody
    }

    void setDiffBody(String diffBody) {
        this.diffBody = diffBody
    }
    String getDiffHeader() {
        return diffHeader
    }

    void setDiffHeader(String diffHeader) {
        this.diffHeader = diffHeader
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
