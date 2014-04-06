package com.wells.plan.forecast;

import com.wells.log.common.LogEntry;
import com.wells.plan.concept.HyveContract;
import com.wells.plan.concept.LoggedHyvePlanImpl;
import com.wells.plan.concept.PlanEntryLogType;

public class LoggedContractForecastImpl extends LoggedHyvePlanImpl<ForecastEntry> {

	public LoggedContractForecastImpl(HyveContract contract) {
		super(contract);
	}

	@Override
	public ForecastLog newPlanEntryLog(PlanEntryLogType logType,
			ForecastEntry planEntry, LogEntry logEntry) {
		return new ForecastLog(logType, planEntry, logEntry);
	}

}
