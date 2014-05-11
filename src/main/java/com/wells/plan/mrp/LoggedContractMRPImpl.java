package com.wells.plan.mrp;

import com.wells.log.common.LogEntry;
import com.wells.plan.concept.ProductionContract;
import com.wells.plan.concept.LoggedContractPlanImpl;
import com.wells.plan.concept.PlanEntryLog;
import com.wells.plan.concept.PlanEntryLogType;

public class LoggedContractMRPImpl extends  LoggedContractPlanImpl<MRPEntry> {
	
	public LoggedContractMRPImpl(ProductionContract contract) {
		super(contract);
	}

	@Override
	public PlanEntryLog<MRPEntry> newPlanEntryLog(PlanEntryLogType logType,
			MRPEntry planEntry, LogEntry logEntry) {
		return new MRPEntryLog(logType, planEntry, logEntry);
	}

}
