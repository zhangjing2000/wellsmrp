package com.wells.plan.concept;

import java.util.List;

public interface HyvePlan <T extends PlanEntry> {
	HyveContract getContract();
	
	List<T> getLatestPlan(); 
}
