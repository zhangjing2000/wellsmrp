package com.wells.plan.mps;

import com.wells.log.common.LogEntry;
import com.wells.plan.concept.HyveContract;
import com.wells.plan.concept.LoggedHyvePlanImpl;
import com.wells.plan.concept.PlanEntryLog;
import com.wells.plan.concept.PlanEntryLogType;

public class LoggedContractMPSImpl extends  LoggedHyvePlanImpl<MPSEntry> {

	
	public LoggedContractMPSImpl(HyveContract contract) {
		super(contract);
	}

	@Override
	public PlanEntryLog<MPSEntry> newPlanEntryLog(PlanEntryLogType logType,
			MPSEntry planEntry, LogEntry logEntry) {
		return new MPSEntryLog(logType, planEntry, logEntry);
	}

}
