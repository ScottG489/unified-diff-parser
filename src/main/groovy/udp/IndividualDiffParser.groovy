        // TODO: This will likely be set incorrectly for binary files since we don't currently
        // TODO:    have detection on whether a binary file is being added, modified, removed, etc
    UnifiedDiff parseHeader() {
        // TODO: This likely doesn't need to loop since we step through each line until we
        // TODO:    get to the end of the header
        if (lineIter.hasNext()) {
                return unifiedDiff
            } else if (isGitBinaryFileLine(indexLine)) {
                unifiedDiff.setIsBinary(true)
                return unifiedDiff
        } else {
            // empty diff, malformed

        return unifiedDiff
    }
    // TODO: Inconsistency between added/deleted text and binary files:
    // TODO:        Text: To/From file respectively is '/dev/null' along with file being added/deleted
    // TODO:        Binary: To/From file respectively are both the same
    // TODO: This should be refactored using 'states' if only for readability
    // TODO: Should 'binary' 'header' lines be considered part of the header? Or are they
    // TODO:    really part of the body? Seems like they are more part of the body
    // TODO:    but since we want to know if the file is binary we parse them as though they are
    // TODO:    part of the header.
    UnifiedDiff parse() {
        unifiedDiff.setDiffHeader(extractDiffHeader(unifiedDiff.getRawDiff()))
        parseHeader()
        // TODO: Doesn't get body for binary patches
        // XXX: This is incorrect as the diff body won't match this for binary patches.
        // XXX: This is incorrect as the diff body won't match this for binary patches.