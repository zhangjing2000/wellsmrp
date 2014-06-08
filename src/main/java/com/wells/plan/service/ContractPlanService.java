package com.wells.plan.service;

import java.util.Set;

import com.wells.inventory.concept.InventoryOrder;
import com.wells.plan.concept.ContractPlan;
import com.wells.plan.forecast.ForecastEntry;
import com.wells.plan.mps.MPSEntry;
import com.wells.plan.mrp.MRPEntry;

public interface ContractPlanService {
	boolean isForecastSatisfiedByMPS(ContractPlan<ForecastEntry> forecast, ContractPlan<MPSEntry> mps);
	boolean isMPSSatisfiedByMRP(ContractPlan<MPSEntry> mps, ContractPlan<MRPEntry> mrp);
	boolean isMRPSatisfiedByInventoryOrders(ContractPlan<MRPEntry> mrp, Set<InventoryOrder> orders);
	void fromMPStoMRP(ContractPlan<MPSEntry> mps, ContractPlan<MRPEntry> mrp);
}
