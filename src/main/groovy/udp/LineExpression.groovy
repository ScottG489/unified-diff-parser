package udp

import java.util.regex.Pattern

class LineExpression {
    // Always first line
    static final Pattern DIFF_GIT = Pattern.compile("diff --git a/(.*) b/(.*)")
    // New and deleted are mutually exclusive and exclusive from old and new mode below
    static final Pattern NEW_FILE_MODE = Pattern.compile("new file mode (.*)")
    static final Pattern DELETED_FILE_MODE = Pattern.compile("deleted file mode (.*)")
    // Modified file mode. Should always go together.
    static final Pattern OLD_MODE = Pattern.compile("old mode (.*)")
    static final Pattern NEW_MODE = Pattern.compile("new mode (.*)")
    // Displayed with copy and rename
    static final Pattern SIMILARITY_INDEX = Pattern.compile("similarity index (.*)")
    // Both together. Exclusive from rename
    static final Pattern COPY_FROM = Pattern.compile("copy from (.*)")
    static final Pattern COPY_TO = Pattern.compile("copy to (.*)")
    // Both together. Exclusive from copy
    static final Pattern RENAME_FROM = Pattern.compile("rename from (.*)")
    static final Pattern RENAME_TO = Pattern.compile("rename to (.*)")
    // Not shown with copy or rename
    static final Pattern DISSIMILARITY_INDEX = Pattern.compile("dissimilarity index (.*)")
    /*
    * "The index line includes the SHA-1 checksum before and after the
    * change. The <mode> is included if the file mode does not change;
    * otherwise, separate lines indicate the old and the new mode."
    */
    // In other words the last group will be empty if the mode changed.
    // TODO: Are there always 2 .'s here? Thought I saw something about them being
    // TODO:    used for padding which means that could change.
    static final Pattern INDEX = Pattern.compile("index (.*)\\.\\.([^ ]*) *(.*)")
    // TODO: Not convinced these binary file name patters are 100% flawless
    static final Pattern BINARY = Pattern.compile("Binary files (a/)*(.*) and (b/)*(.*) differ")
    static final Pattern GIT_BINARY = Pattern.compile("GIT binary patch")
    static final Pattern FROM_FILE = Pattern.compile("--- (a/)*(.*)")
    static final Pattern TO_FILE = Pattern.compile("\\+\\+\\+ (b/)*(.*)")
}

