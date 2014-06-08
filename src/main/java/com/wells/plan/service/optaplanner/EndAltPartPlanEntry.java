package com.wells.plan.service.optaplanner;

import java.util.Date;
import java.util.UUID;

import com.wells.part.concept.MemberType;
import com.wells.part.concept.ProductGroupMember;
import com.wells.plan.concept.ProductionPlant;

public class EndAltPartPlanEntry extends FixedPlanEntry {
	
	public EndAltPartPlanEntry(ProductGroupMember planItem, FixedPlanEntry fulfilledParent, Date planDate, ProductionPlant planLocation) {
		this(planItem.getMemberType(), fulfilledParent, planItem.getMemberID(), 
				 planDate, planItem.getMinBOMQty(), planLocation);
	}

	public EndAltPartPlanEntry(MemberType itemType, FixedPlanEntry parent, UUID itemID,
			 Date planDate, int planQty, ProductionPlant planLocation) {
		super(itemType, parent, itemID, planDate, planQty, planLocation);
	}
	
	public int getFulfilledQty() {
		return getPlanQty() / getFulfilledParent().getBomQty();
	}

	public int getPlanQty() {
		if (getFulfilledParent() == null) return 0;
		if (getFulfilledParent().getFulfilledChildren() == null) return 0;
		int totalPlanQty = 0;
		for (FixedPlanEntry sibling: getFulfilledParent().getFulfilledChildren()) {
			if (sibling.equals(this)) continue;
			totalPlanQty += sibling.getPlanQty();
		}
		return getFulfilledParent().getPlanQty() - totalPlanQty;
	}

	@Override
	public String toString() {
		return "EndAltPartPlanEntry [fulfilledQty=" + getFulfilledQty()
				+ ", itemType=" + getItemType() 
				+ ", parent="  + (getFulfilledParent()==null?"Null":getFulfilledParent().getItemID()) 
				+ ", groupID=" + getItemID() 
				+ ", planDate=" + getPlanDate() 
				+ ", plant=" + getPlanLocation() 
				+ ", bomQty=" + getBomQty()
				+ ", planQty=" + getPlanQty() 
				+ ", fulfilledQty = " + getFulfilledQty()
				+ "]";
	}

}
