package udp.parse;

import java.util.regex.Pattern;

public class LineExpression {
    public static Pattern getDIFF_GIT() {
        return DIFF_GIT;
    }

    public static Pattern getNEW_FILE_MODE() {
        return NEW_FILE_MODE;
    }

    public static Pattern getDELETED_FILE_MODE() {
        return DELETED_FILE_MODE;
    }

    public static Pattern getOLD_MODE() {
        return OLD_MODE;
    }

    public static Pattern getNEW_MODE() {
        return NEW_MODE;
    }

    public static Pattern getSIMILARITY_INDEX() {
        return SIMILARITY_INDEX;
    }

    public static Pattern getCOPY_FROM() {
        return COPY_FROM;
    }

    public static Pattern getCOPY_TO() {
        return COPY_TO;
    }

    public static Pattern getRENAME_FROM() {
        return RENAME_FROM;
    }

    public static Pattern getRENAME_TO() {
        return RENAME_TO;
    }

    public static Pattern getDISSIMILARITY_INDEX() {
        return DISSIMILARITY_INDEX;
    }

    public static Pattern getINDEX() {
        return INDEX;
    }

    public static Pattern getBINARY() {
        return BINARY;
    }

    public static Pattern getGIT_BINARY() {
        return GIT_BINARY;
    }

    public static Pattern getFROM_FILE() {
        return FROM_FILE;
    }

    public static Pattern getTO_FILE() {
        return TO_FILE;
    }

    // Always first line
    private static final Pattern DIFF_GIT = Pattern.compile("diff --git a/(.*) b/(.*)");
    // New and deleted are mutually exclusive and exclusive from old and new mode below
    private static final Pattern NEW_FILE_MODE = Pattern.compile("new file mode (.*)");
    private static final Pattern DELETED_FILE_MODE = Pattern.compile("deleted file mode (.*)");
    // Modified file mode. Should always go together.
    private static final Pattern OLD_MODE = Pattern.compile("old mode (.*)");
    private static final Pattern NEW_MODE = Pattern.compile("new mode (.*)");
    // Displayed with copy and rename
    private static final Pattern SIMILARITY_INDEX = Pattern.compile("similarity index (.*)");
    // Both together. Exclusive from rename
    private static final Pattern COPY_FROM = Pattern.compile("copy from (.*)");
    private static final Pattern COPY_TO = Pattern.compile("copy to (.*)");
    // Both together. Exclusive from copy
    private static final Pattern RENAME_FROM = Pattern.compile("rename from (.*)");
    private static final Pattern RENAME_TO = Pattern.compile("rename to (.*)");
    // Not shown with copy or rename
    private static final Pattern DISSIMILARITY_INDEX = Pattern.compile("dissimilarity index (.*)");
    /*
     * "The index line includes the SHA-1 checksum before and after the
     * change. The <mode> is included if the file mode does not change;
     * otherwise, separate lines indicate the old and the new mode."
     */
    // In other words the last group will be empty if the mode changed.
    // TODO: Are there always 2 .'s here? Thought I saw something about them being
    // TODO:    used for padding which means that could change.
    private static final Pattern INDEX = Pattern.compile("index (.*)\\.\\.([^ ]*) *(.*)");
    // TODO: Not convinced these binary file name patters are 100% flawless
    private static final Pattern BINARY = Pattern.compile("Binary files (a/)*(.*) and (b/)*(.*) differ");
    private static final Pattern GIT_BINARY = Pattern.compile("GIT binary patch");
    private static final Pattern FROM_FILE = Pattern.compile("--- (a/)*(.*)");
    private static final Pattern TO_FILE = Pattern.compile("\\+\\+\\+ (b/)*(.*)");
}
