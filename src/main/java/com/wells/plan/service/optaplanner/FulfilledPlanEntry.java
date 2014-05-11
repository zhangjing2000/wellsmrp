package com.wells.plan.service.optaplanner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.wells.bom.concept.ProductGroupMember;
import com.wells.bom.concept.MemberType;
import com.wells.plan.concept.ProductionPlant;

@PlanningEntity(difficultyComparatorClass = PlanEntryDifficultyComparator.class)
public class FulfilledPlanEntry extends FixedPlanEntry {
	
	private int fulfilledQty = 0;
	
	public FulfilledPlanEntry(ProductGroupMember planItem, FixedPlanEntry fulfilledParent, Date planDate, ProductionPlant planLocation) {
		this(planItem.getMemberType(), fulfilledParent, planItem.getSubGroupID(), 
				planItem.getSkuNo(), planDate, planItem.getMinBOMQty(), planLocation);
	}

	public FulfilledPlanEntry(MemberType itemType, FixedPlanEntry parent, UUID groupID,
			int skuNo, Date planDate, int planQty, ProductionPlant planLocation) {
		super(itemType, parent, groupID, skuNo, planDate, planQty, planLocation);
	}
	
	public int getPlanQty() {
		return getFulfilledParent().getBomQty() * getFulfilledQty();
	}
	
	@PlanningVariable(valueRangeProviderRefs = {"fulfilledQtyList"})
	public int getFulfilledQty() {
		return fulfilledQty;
	}
	
	public void setFulfilledQty(int fulfilledQty) {
		System.out.println("setFulfilledQty:" + fulfilledQty + "sku:" + this.getSkuNo() + "bom:" + getFulfilledParent().getBomQty());
		this.fulfilledQty = fulfilledQty;
	}
	
	@ValueRangeProvider(id = "fulfilledQtyList")
	public List<Integer> getFulfilledQtyList() {
		List<Integer> result = new ArrayList<Integer>();
		for (int i=0;i<=getFulfilledParent().getFulfilledParent().getPlanQty();i++) {
	    	result.add(i);
	    }
	    return result;
    }

	@Override 
	public String toString() {
		return "FulfilledPlanEntry [fulfilledQty=" + fulfilledQty
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
