package com.wells.plan.mrp;

import com.wells.log.common.LogEntry;
import com.wells.plan.concept.HyveContract;
import com.wells.plan.concept.LoggedHyvePlanImpl;
import com.wells.plan.concept.PlanEntryLog;
import com.wells.plan.concept.PlanEntryLogType;

public class LoggedContractMRPImpl extends  LoggedHyvePlanImpl<MRPEntry> {
	
	public LoggedContractMRPImpl(HyveContract contract) {
		super(contract);
	}

	@Override
	public PlanEntryLog<MRPEntry> newPlanEntryLog(PlanEntryLogType logType,
			MRPEntry planEntry, LogEntry logEntry) {
		return new MRPEntryLog(logType, planEntry, logEntry);
	}

}
