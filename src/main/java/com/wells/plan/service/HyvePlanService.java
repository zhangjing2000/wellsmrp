package com.wells.plan.service;

import com.wells.plan.concept.HyvePlan;
import com.wells.plan.forecast.ForecastEntry;
import com.wells.plan.mps.MPSEntry;
import com.wells.plan.mrp.MRPEntry;

public interface HyvePlanService {
	boolean isForecastSatisfiedByMPS(HyvePlan<ForecastEntry> forecast, HyvePlan<MPSEntry> mps);
	boolean isMPSSatisfiedByMRP(HyvePlan<MPSEntry> mps, HyvePlan<MRPEntry> mrp);
	void fromMPStoMRP(HyvePlan<MPSEntry> mps, HyvePlan<MRPEntry> mrp);
}
