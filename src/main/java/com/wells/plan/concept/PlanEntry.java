package com.wells.plan.concept;

import java.util.Date;

public interface PlanEntry {
	Date getPlanDate();
	int getPlanQty();
	HyvePlant getPlanLocation();
}
