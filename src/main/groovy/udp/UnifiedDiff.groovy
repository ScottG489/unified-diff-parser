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

    boolean isBinary() {
        return isBinary
    }

    void setIsBinary(boolean isBinary) {
        this.isBinary = isBinary
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

    enum FileStatus {
        Added, Modified, Removed
    }
}
