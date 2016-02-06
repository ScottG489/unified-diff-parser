package udp.strategy

interface LineHandlingStrategy {
    // TODO: model used to be a UnifiedDiff but to decouple it was made an object. This means
    // TODO:    handler implementation has to cast it to what it likes. Is there a better
    // TODO:    way to do this?
    void handle(String line, Object model)
}
