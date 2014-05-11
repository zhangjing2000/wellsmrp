package com.wells.plan.concept;

import java.util.Date;
import java.util.List;

import com.wells.log.common.LogEntry;

public interface LoggedContractPlan<T extends PlanEntry> extends ContractPlan<T> {
	List<T> getPlanSnapshot(Date snapshotDate);
	List<PlanEntryLog<T>> getPlanLogs();
	void addPlanEntry(T planEntry, LogEntry logEntry);
	void deletePlanEntry(T planEntry, LogEntry logEntry);
	void updatePlanEntry(T planEntry, LogEntry logEntry);
}
