package com.java.lingvo.common.event;

import java.time.Instant;
import java.util.UUID;

public record GoalAchievedEvent(
        UUID eventId,
        Instant timestamp,
        Long userId,
        String goalType
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
