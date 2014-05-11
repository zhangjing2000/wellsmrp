package com.wells.plan.service;

import com.wells.plan.concept.ContractPlan;
import com.wells.plan.forecast.ForecastEntry;
import com.wells.plan.mps.MPSEntry;
import com.wells.plan.mrp.MRPEntry;

public interface ContractPlanService {
	boolean isForecastSatisfiedByMPS(ContractPlan<ForecastEntry> forecast, ContractPlan<MPSEntry> mps);
	boolean isMPSSatisfiedByMRP(ContractPlan<MPSEntry> mps, ContractPlan<MRPEntry> mrp);
	void fromMPStoMRP(ContractPlan<MPSEntry> mps, ContractPlan<MRPEntry> mrp);
}
