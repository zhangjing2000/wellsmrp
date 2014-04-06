package com.wells.plan.service;

import com.wells.plan.concept.HyvePlan;
import com.wells.plan.forecast.ForecastEntry;
import com.wells.plan.mps.MPSEntry;
import com.wells.plan.mrp.MRPEntry;

public class HyvePlanServiceImpl implements HyvePlanService {

	public boolean isForecastSatisfiedByMPS(HyvePlan<ForecastEntry> forecasts,
			HyvePlan<MPSEntry> mps) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isMPSSatisfiedByMRP(HyvePlan<MPSEntry> mps,
			HyvePlan<MRPEntry> mrp) {
		// TODO Auto-generated method stub
		return false;
	}

	public void fromMPStoMRP(HyvePlan<MPSEntry> mps, HyvePlan<MRPEntry> mrp) {
		// TODO Auto-generated method stub
		
	}

}
