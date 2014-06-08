package com.wells.inventory.concept;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.wells.part.concept.AssemblyFinishGood;
import com.wells.part.concept.Part;

public class DismantleOrder implements MultiPartInventoryOrder {
	private final UUID orderNo;
	private final int dismantleLoc;
	private final AssemblyFinishGood dismantleGood;
	private final int dismantleQty;
	private final Map<Part, Integer> components;
	private Date recDate;
	
	public DismantleOrder(UUID orderNo, int dismantleLoc, AssemblyFinishGood dismantleGood, Date recDate,
			int dismantleQty) {
		super();
		this.orderNo = orderNo;
		this.dismantleLoc = dismantleLoc;
		this.dismantleGood = dismantleGood;
		this.recDate = recDate;
		this.dismantleQty = dismantleQty;
		this.components = new HashMap<Part, Integer>();
	}

	public Date getOrderRecDate() {
		return recDate;
	}


	public void setOrderRecDate(Date recDate) {
		this.recDate = recDate;
	}


	public UUID getOrderNo() {
		return orderNo;
	}


	public int getOrderRecLoc() {
		return dismantleLoc;
	}


	public Map<Part, Integer> getOrderRecParts() {
		return components;
	}

	public Set<InventoryOrder> getPartOrders() {
		Set<InventoryOrder> orders = new HashSet<InventoryOrder>();
		for(Map.Entry<Part, Integer> component: components.entrySet()) {
			orders.add(new PurchaseOrder(orderNo,dismantleLoc, component.getKey(),recDate, component.getValue()));
		}
		return orders;
	}


	public AssemblyFinishGood getDismantleGood() {
		return dismantleGood;
	}


	public int getDismantleQty() {
		return dismantleQty;
	}
}
