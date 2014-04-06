package com.wells.plan.mrp;

import java.util.Date;

import com.wells.plan.concept.HyvePlant;
import com.wells.plan.concept.PlanEntry;

public class MRPEntry implements PlanEntry {
	private final Date planRecDate;
	private final HyvePlant planLocation;
	private final int planRecQty;
	private final int skuNo;
	
	public MRPEntry(Date startDate, HyvePlant plantLocNo, int deliveryQty,
			int skuNo) {
		super();
		this.planRecDate = startDate;
		this.planLocation = plantLocNo;
		this.planRecQty = deliveryQty;
		this.skuNo = skuNo;
	}
	
	public Date getPlanDate() {
		return planRecDate;
	}
	public HyvePlant getPlanLocation() {
		return planLocation;
	}
	public int getPlanQty() {
		return planRecQty;
	}
	public int getSkuNo() {
		return skuNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + skuNo;
		result = prime * result
				+ ((planRecDate == null) ? 0 : planRecDate.hashCode());
		result = prime * result + planLocation.getLocNo();
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
		MRPEntry other = (MRPEntry) obj;
		if (skuNo != other.skuNo)
			return false;
		if (planRecDate == null) {
			if (other.planRecDate != null)
				return false;
		} else if (!planRecDate.equals(other.planRecDate))
			return false;
		if (planLocation == null) {
			if (other.planLocation != null)
				return false;
		} else if (!planLocation.equals(other.planLocation))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MRPEntry [planRecDate=" + planRecDate + ", planLocation="
				+ planLocation + ", planRecQty=" + planRecQty + ", skuNo="
				+ skuNo + "]";
	}
}
