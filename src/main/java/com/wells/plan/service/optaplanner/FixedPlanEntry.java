package com.wells.plan.service.optaplanner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.wells.part.concept.MemberType;
import com.wells.part.concept.Part;
import com.wells.part.concept.PartRef;
import com.wells.part.concept.ProductGroupMember;
import com.wells.plan.concept.ProductionPlant;
import com.wells.plan.concept.PlanEntry;

public class FixedPlanEntry implements PlanEntry {
	
	private final MemberType itemType;
	private final FixedPlanEntry parent;
	private final List<FixedPlanEntry> children;
	private final UUID itemID;
	private final Date planDate;
	private final ProductionPlant planLocation;
	private int bomQty;
	
	public FixedPlanEntry(ProductGroupMember planItem, FixedPlanEntry fulfilledParent, Date planDate, ProductionPlant planLocation) {
		this(planItem.getMemberType(), fulfilledParent, planItem.getMemberID(), 
				planDate, planItem.getMinBOMQty(), planLocation);
	}

	public FixedPlanEntry(MemberType itemType, FixedPlanEntry fulfilledParent, UUID itemID,
			Date planDate, int bomQty, ProductionPlant planLocation) {
		super();
		this.itemType = itemType;
		this.parent = fulfilledParent;
		this.itemID = itemID;
		this.planDate = planDate;
		this.bomQty = bomQty;
		this.planLocation = planLocation;
		this.children = new ArrayList<FixedPlanEntry>();
		if (this.parent != null)
			this.parent.getFulfilledChildren().add(this);
	}
	
	public MemberType getItemType() {
		return itemType;
	}
	public FixedPlanEntry getFulfilledParent() {
		return parent;
	}
	public List<FixedPlanEntry> getFulfilledChildren() {
		return children;
	}
	
	public UUID getItemID() {
		return itemID;
	}
	public Date getPlanDate() {
		return planDate;
	}
	public ProductionPlant getPlanLocation() {
		return planLocation;
	} 
	public int getBomQty() {
		return bomQty;
	}
	public int getPlanQty() {
		return parent == null? bomQty:parent.getPlanQty() * bomQty;
	}
	public int getFulfilledQty() {
		return getBomQty();
	}
	public Part getPlanPart() {
		return new PartRef(itemID);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemID == null) ? 0 : itemID.hashCode());
		result = prime * result
				+ ((itemType == null) ? 0 : itemType.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result
				+ ((planDate == null) ? 0 : planDate.hashCode());
		result = prime * result
				+ ((planLocation == null) ? 0 : planLocation.hashCode());
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
		FixedPlanEntry other = (FixedPlanEntry) obj;
		if (itemID == null) {
			if (other.itemID != null)
				return false;
		} else if (!itemID.equals(other.itemID))
			return false;
		if (itemType != other.itemType)
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
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
		return true;
	}

	
	@Override
	public String toString() {
		return "FixedPlanEntry [itemType=" + itemType + ", parent="  + (parent==null?"Null":parent.itemID)
				+ ", children=" + children + ", itemID=" + itemID
				+ ", planDate=" + planDate
				+ ", planLocation=" + planLocation 
				+ ", bomQty=" + bomQty
				+ ", planQty=" + getPlanQty()
				+ ", fulfilledQty = " + getFulfilledQty()
				+ "]";
	}
	
}
