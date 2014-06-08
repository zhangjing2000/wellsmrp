package com.wells.log.common;

import java.util.Date;

public class LogEntryVo implements LogEntry {
	private final int logUserID;
	private final Date logDate;
	private final String logComment;
	
	public LogEntryVo(int logUserID, Date logDate, String logComment) {
	super();
	this.logUserID = logUserID;
	this.logDate = logDate;
	this.logComment = logComment;
	}
	public int getLogUserID() {
		return logUserID;
	}

	public Date getLogDate() {
		return logDate;
	}

	public String getLogComment() {
		return logComment;
	}

}
