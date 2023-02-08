package com.modugarden.domain.user.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.modugarden.domain.report.entity.enums.ReportType;

public enum UserAuthority {

    ROLE_CURATOR, ROLE_GENERAL, ROLE_BLOCKED;
}
