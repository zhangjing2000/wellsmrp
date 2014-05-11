package com.wells.plan.mps;

import java.util.Date;
import java.util.UUID;

import com.wells.log.common.LogEntry;
import com.wells.plan.concept.ProductionPlant;
import com.wells.plan.concept.PlanEntryLog;
import com.wells.plan.concept.PlanEntryLogType;

public class MPSEntryLog extends MPSEntry implements PlanEntryLog<MPSEntry> {

	private final UUID logID;
	private final PlanEntryLogType logType;
	private final int logUserID;
	private final Date logDate;
	private final String logComment;
	
	public MPSEntryLog(PlanEntryLogType logType, MPSEntry mpsEntry, LogEntry logEntry) {
		this(logType, mpsEntry.getPlanDate(), mpsEntry.getDeliveryDate(),
				mpsEntry.getPlanLocation(), 
				mpsEntry.getPlanQty(), mpsEntry.getCustBOM(), 
				logEntry.getLogUserID(), logEntry.getLogDate(), logEntry.getLogComment());
	}
	public MPSEntryLog(PlanEntryLogType logType, Date startDate, Date deliveryDate, ProductionPlant plant, int deliveryQty,
			UUID custBOM, int logUserID, Date logDate, String logComment) {
		super(startDate, deliveryDate, plant, deliveryQty, custBOM);
		this.logID = UUID.randomUUID();
		this.logType = logType;
		this.logUserID = logUserID;
		this.logDate = logDate;
		this.logComment = logComment;
	}

	public UUID getLogID() {
		return logID;
	}

	public PlanEntryLogType getLogType() {
		return logType;
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
	public int compareTo(PlanEntryLog<MPSEntry> o) {
		if (logDate.equals(o.getPlanDate())) {
			return logID.compareTo(o.getLogID());
		} else {
			return logDate.compareTo(o.getLogDate());
		}
	}
	public MPSEntry getPlanEntry() {
		return this;
	}
}
