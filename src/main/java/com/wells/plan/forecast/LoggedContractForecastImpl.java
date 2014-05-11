package com.wells.plan.forecast;

import com.wells.log.common.LogEntry;
import com.wells.plan.concept.ProductionContract;
import com.wells.plan.concept.LoggedContractPlanImpl;
import com.wells.plan.concept.PlanEntryLogType;

public class LoggedContractForecastImpl extends LoggedContractPlanImpl<ForecastEntry> {

	public LoggedContractForecastImpl(ProductionContract contract) {
		super(contract);
	}

	@Override
	public ForecastLog newPlanEntryLog(PlanEntryLogType logType,
			ForecastEntry planEntry, LogEntry logEntry) {
		return new ForecastLog(logType, planEntry, logEntry);
	}

}
