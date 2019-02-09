package udp.parse;

public class UnifiedDiff {
    public UnifiedDiff(String rawDiff) {
        this.rawDiff = rawDiff;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getOldMode() {
        return oldMode;
    }

    public void setOldMode(String oldMode) {
        this.oldMode = oldMode;
    }

    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getRawDiff() {
        return rawDiff;
    }

    public String getToFile() {
        return toFile;
    }

    public void setToFile(String toFile) {
        this.toFile = toFile;
    }

    public String getFromFile() {
        return fromFile;
    }

    public void setFromFile(String fromFile) {
        this.fromFile = fromFile;
    }

    public void setSimilarityIndex(String similarityIndex) {
        this.similarityIndex = similarityIndex;
    }

    public String getSimilarityIndex() {
        return similarityIndex;
    }

    // TODO: Still need to find an example of this field
    public void setDissimilarityIndex(String dissimilarityIndex) {
        this.dissimilarityIndex = dissimilarityIndex;
    }

    public String getDisimilarityIndex() {
        return dissimilarityIndex;
    }

    public boolean isBinary() {
        return isBinary;
    }

    public void setIsBinary(boolean isBinary) {
        this.isBinary = isBinary;
    }

    public String getChecksumBefore() {
        return checksumBefore;
    }

    public void setChecksumBefore(String checksumBefore) {
        this.checksumBefore = checksumBefore;
    }

    public String getChecksumAfter() {
        return checksumAfter;
    }

    public void setChecksumAfter(String checksumAfter) {
        this.checksumAfter = checksumAfter;
    }

    public boolean isAddedFile() {
        return getFileStatus().equals(FileStatus.Added);
    }

    public boolean isModifiedFile() {
        return getFileStatus().equals(FileStatus.Modified);
    }

    public boolean isRemovedFile() {
        return getFileStatus().equals(FileStatus.Removed);
    }

    public boolean isRenamed() {
        return getFileStatus().equals(FileStatus.Renamed);
    }

    public boolean isCopied() {
        return getFileStatus().equals(FileStatus.Copied);
    }

    private String rawDiff;
    private String fromFile;
    private String toFile;
    private FileStatus fileStatus;
    private String mode;
    private String oldMode;
    private boolean isBinary;
    private String similarityIndex;
    private String dissimilarityIndex;
    private String checksumBefore;
    private String checksumAfter;

    public static enum FileStatus {
        Added, Modified, Removed, Copied, Renamed;
    }
}
