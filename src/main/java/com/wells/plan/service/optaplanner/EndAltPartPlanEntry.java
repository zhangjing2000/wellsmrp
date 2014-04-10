package com.wells.plan.service.optaplanner;

import java.util.Date;
import java.util.UUID;

import com.wells.bom.concept.HyveProductGroupMember;
import com.wells.bom.concept.MemberType;
import com.wells.plan.concept.HyvePlant;

public class EndAltPartPlanEntry extends FixedPlanEntry {
	
	public EndAltPartPlanEntry(HyveProductGroupMember planItem, FixedPlanEntry fulfilledParent, Date planDate, HyvePlant planLocation) {
		this(planItem.getMemberType(), fulfilledParent, planItem.getSubGroupID(), 
				planItem.getSkuNo(), planDate, planItem.getMinBOMQty(), planLocation);
	}

	public EndAltPartPlanEntry(MemberType itemType, FixedPlanEntry parent, UUID groupID,
			int skuNo, Date planDate, int planQty, HyvePlant planLocation) {
		super(itemType, parent, groupID, skuNo, planDate, planQty, planLocation);
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
				+ ", parent="  + (getFulfilledParent()==null?"Null":getFulfilledParent().getGroupID()) 
				+ ", groupID=" + getGroupID() 
				+ ", skuNo=" + getSkuNo()
				+ ", planDate=" + getPlanDate() 
				+ ", plant=" + getPlanLocation() 
				+ ", bomQty=" + getBomQty()
				+ ", planQty=" + getPlanQty() 
				+ ", fulfilledQty = " + getFulfilledQty()
				+ "]";
	}

}
