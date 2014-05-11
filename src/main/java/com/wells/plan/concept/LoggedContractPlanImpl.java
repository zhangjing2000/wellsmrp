package com.wells.plan.concept;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.wells.log.common.LogEntry;

public abstract class LoggedContractPlanImpl<T extends PlanEntry> implements LoggedContractPlan<T>{

	private final ProductionContract productionContract;
	private final List<PlanEntryLog<T>> logs = new ArrayList<PlanEntryLog<T>>();

	public LoggedContractPlanImpl(ProductionContract productionContract) {
		super();
		this.productionContract = productionContract;
	}

	public ProductionContract getContract() {
		return productionContract;
	}

	public List<PlanEntryLog<T>> getPlanLogs() {
		return logs;
	}

	public List<T> getLatestPlan() {
		return getPlanSnapshot(null);
	}

	public List<T> getPlanSnapshot(Date snapshotDate) {
		List<T> result = new ArrayList<T>();
		Collections.sort(logs);
		for (PlanEntryLog<T> log:logs) {
			if (snapshotDate != null && log.getLogDate().after(snapshotDate)) break;
			result.add(log.getPlanEntry());
		}
		return result;
	}

	public void addPlanEntry(T planEntry, LogEntry logEntry) {
		PlanEntryLog<T> planEntryLog = newPlanEntryLog(PlanEntryLogType.ADD_ENTRY, planEntry, logEntry); 
		logs.add(planEntryLog);
	}

	public void deletePlanEntry(T planEntry, LogEntry logEntry) {
		PlanEntryLog<T> planEntryLog = newPlanEntryLog(PlanEntryLogType.DELETE_ENTRY, planEntry, logEntry); 
		logs.add(planEntryLog);
		
	}

	public void updatePlanEntry(T planEntry, LogEntry logEntry) {
		PlanEntryLog<T> planEntryLog = newPlanEntryLog(PlanEntryLogType.UPDATE_ENTRY, planEntry, logEntry); 
		logs.add(planEntryLog);
	}
	
	public  abstract PlanEntryLog<T> newPlanEntryLog(PlanEntryLogType logType, T planEntry, LogEntry logEntry);
}
