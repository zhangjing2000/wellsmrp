package com.wells.plan.concept;

import java.util.Date;

import com.wells.part.concept.Part;

public interface PlanEntry {
	Part getPlanPart();
	Date getPlanDate();
	int getPlanQty();
	ProductionPlant getPlanLocation();
}
