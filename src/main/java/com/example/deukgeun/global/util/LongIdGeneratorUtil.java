package com.example.deukgeun.global.util;

import java.time.ZonedDateTime;

public class LongIdGeneratorUtil {
    /**
     * 현재 시간을 기반으로 유닉스 타임스탬프를 생성합니다.
     *
     * @return 생성된 유닉스 타임스탬프입니다.
     */
    public static Long gen() {
        return ZonedDateTime.now().toEpochSecond();
    }
}
