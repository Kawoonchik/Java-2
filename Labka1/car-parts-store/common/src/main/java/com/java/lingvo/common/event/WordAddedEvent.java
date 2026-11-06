package com.java.lingvo.common.event;

import java.time.Instant;
import java.util.UUID;

public record WordAddedEvent(
        UUID eventId,
        Instant timestamp,
        Long wordId,
        Long userId
) implements DictionaryEvent {
    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
