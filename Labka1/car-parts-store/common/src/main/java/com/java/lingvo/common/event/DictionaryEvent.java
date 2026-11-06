package com.java.lingvo.common.event;

import java.time.Instant;
import java.util.UUID;

public interface DictionaryEvent {
    UUID getEventId();
    Instant getTimestamp();
}
