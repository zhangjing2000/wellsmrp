package com.wells.plan.service.optaplanner;

import java.util.Date;
import java.util.UUID;

import com.wells.plan.concept.ProductionPlant;

public class PlanEntryIndex {
	private final ProductionPlant planLocation;
	private final Date planDate; 
	private final UUID skuNo;
	public PlanEntryIndex(ProductionPlant planLocation, Date planDate, UUID skuNo) {
		super();
		this.planLocation = planLocation;
		this.planDate = planDate;
		this.skuNo = skuNo;
	}
	public ProductionPlant getPlanLocation() {
		return planLocation;
	}
	public Date getPlanDate() {
		return planDate;
	}
	public UUID getSkuNo() {
		return skuNo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((planDate == null) ? 0 : planDate.hashCode());
		result = prime * result
				+ ((planLocation == null) ? 0 : planLocation.hashCode());
		result = prime * result + skuNo.hashCode();
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlanEntryIndex other = (PlanEntryIndex) obj;
		if (planDate == null) {
			if (other.planDate != null)
				return false;
		} else if (!planDate.equals(other.planDate))
			return false;
		if (planLocation == null) {
			if (other.planLocation != null)
				return false;
		} else if (!planLocation.equals(other.planLocation))
			return false;
		if (skuNo.equals(other.skuNo))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "PlanEntryIndex [planLocation=" + planLocation + ", planDate="
				+ planDate + ", skuNo=" + skuNo + "]";
	}
	
}
