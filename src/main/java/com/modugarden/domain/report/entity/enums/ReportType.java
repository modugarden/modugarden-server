package com.modugarden.domain.report.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ReportType {
    ABUSE, TERROR, SEXUAL, FISHING, INAPPROPRIATE;

    @JsonCreator
    public static ReportType from(String s) {
        return ReportType.valueOf(s.toUpperCase());
    }
}
