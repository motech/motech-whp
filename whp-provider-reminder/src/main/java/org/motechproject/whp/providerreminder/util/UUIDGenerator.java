package org.motechproject.whp.providerreminder.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDGenerator {
    public String uuid() {
        return UUID.randomUUID().toString();
    }
}
