package com.wells.plan.concept;

import java.util.List;

public interface ContractPlan <T extends PlanEntry> {
	ProductionContract getContract();
	
	List<T> getLatestPlan(); 
}
