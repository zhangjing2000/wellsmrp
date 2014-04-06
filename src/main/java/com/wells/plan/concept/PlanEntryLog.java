package com.wells.plan.concept;

import java.util.UUID;

import com.wells.log.common.LogEntry;

public interface PlanEntryLog<T extends PlanEntry> extends LogEntry, PlanEntry, Comparable<PlanEntryLog<T>>{
	PlanEntryLogType getLogType();
	T getPlanEntry();
	UUID getLogID();
}
