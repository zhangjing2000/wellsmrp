package com.wells.plan.mps;

import com.wells.log.common.LogEntry;
import com.wells.plan.concept.ProductionContract;
import com.wells.plan.concept.LoggedContractPlanImpl;
import com.wells.plan.concept.PlanEntryLog;
import com.wells.plan.concept.PlanEntryLogType;

public class LoggedContractMPSImpl extends  LoggedContractPlanImpl<MPSEntry> {

	
	public LoggedContractMPSImpl(ProductionContract contract) {
		super(contract);
	}

	@Override
	public PlanEntryLog<MPSEntry> newPlanEntryLog(PlanEntryLogType logType,
			MPSEntry planEntry, LogEntry logEntry) {
		return new MPSEntryLog(logType, planEntry, logEntry);
	}

}
