package com.bytehonor.sdk.server.bytehonor.scheduler.time;

import java.time.LocalDateTime;

public interface TimeCron {
    public static final int ANY = -1;

    public boolean match(LocalDateTime ldt);
}
