package com.wells.log.common;

import java.util.Date;

public interface LogEntry {
	int getLogUserID();
	Date getLogDate();
	String getLogComment();
}
