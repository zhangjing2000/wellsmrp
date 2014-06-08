package com.wells.plan.service.optaplanner;

import java.util.Comparator;

public class PlanEntryDifficultyComparator implements Comparator<FulfilledPlanEntry> {

	public int compare(FulfilledPlanEntry arg0, FulfilledPlanEntry arg1) {
		if (arg0.getItemID() != null) {
			return arg1.getItemID() != null? arg0.getItemID().compareTo(arg1.getItemID()):1;
		} else {
			return arg1.getItemID() != null? 1:0;
		}
	}

}
