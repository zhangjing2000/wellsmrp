package com.wells.plan.mrp;

import java.util.Date;
import java.util.UUID;

import com.wells.log.common.LogEntry;
import com.wells.plan.concept.ProductionPlant;
import com.wells.plan.concept.PlanEntryLog;
import com.wells.plan.concept.PlanEntryLogType;

public class MRPEntryLog extends MRPEntry implements PlanEntryLog<MRPEntry> {

	private final UUID logID;
	private final PlanEntryLogType logType;
	private final int logUserID;
	private final Date logDate;
	private final String logComment;
	
	public MRPEntryLog(PlanEntryLogType logType, MRPEntry mrpEntry, LogEntry logEntry) {
		this(logType, mrpEntry.getPlanDate(), 
				mrpEntry.getPlanLocation(), 
				mrpEntry.getPlanQty(), mrpEntry.getSkuNo(), 
				logEntry.getLogUserID(), logEntry.getLogDate(), logEntry.getLogComment());
	}
	public MRPEntryLog(PlanEntryLogType logType, Date startDate, ProductionPlant plant, int deliveryQty,
			int skuNo, int logUserID, Date logDate, String logComment) {
		super(startDate, plant, deliveryQty, skuNo);
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
	public int compareTo(PlanEntryLog<MRPEntry> o) {
		if (logDate.equals(o.getPlanDate())) {
			return logID.compareTo(o.getLogID());
		} else {
			return logDate.compareTo(o.getLogDate());
		}
	}
	public MRPEntry getPlanEntry() {
		return this;
	}
}
