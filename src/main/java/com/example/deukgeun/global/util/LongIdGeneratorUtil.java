package com.example.deukgeun.global.util;

import java.time.ZonedDateTime;

public class LongIdGeneratorUtil {
    public static Long gen() {
        return ZonedDateTime.now().toEpochSecond();
    }
}
