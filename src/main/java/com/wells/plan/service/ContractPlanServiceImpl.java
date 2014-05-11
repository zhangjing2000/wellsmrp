package com.wells.plan.service;

import com.wells.plan.concept.ContractPlan;
import com.wells.plan.forecast.ForecastEntry;
import com.wells.plan.mps.MPSEntry;
import com.wells.plan.mrp.MRPEntry;

public class ContractPlanServiceImpl implements ContractPlanService {

	public boolean isForecastSatisfiedByMPS(ContractPlan<ForecastEntry> forecasts,
			ContractPlan<MPSEntry> mps) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isMPSSatisfiedByMRP(ContractPlan<MPSEntry> mps,
			ContractPlan<MRPEntry> mrp) {
		// TODO Auto-generated method stub
		return false;
	}

	public void fromMPStoMRP(ContractPlan<MPSEntry> mps, ContractPlan<MRPEntry> mrp) {
		// TODO Auto-generated method stub
		
	}

}
