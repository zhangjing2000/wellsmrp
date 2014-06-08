package com.wells.inventory.concept;

import java.util.Date;
import java.util.UUID;

import com.wells.part.concept.Part;

public class PurchaseOrder implements InventoryOrder {
	private final UUID orderNo;
	private final int recLoc;
	private final Part partNo;
	private Date recDate;
	private int recQty;
	
	public PurchaseOrder(UUID orderNo, int recLoc, Part partNo, Date recDate,
			int recQty) {
		super();
		this.orderNo = orderNo;
		this.recLoc = recLoc;
		this.partNo = partNo;
		this.recDate = recDate;
		this.recQty = recQty;
	}

	public UUID getOrderNo() {
		return orderNo;
	}

	public Date getOrderRecDate() {
		return recDate;
	}

	public int getOrderRecLoc() {
		return recLoc;
	}

	public Part getOrderPartNo() {
		return partNo;
	}

	public int getOrderQty() {
		return recQty;
	}

	public Date getRecDate() {
		return recDate;
	}

	public void setRecDate(Date recDate) {
		this.recDate = recDate;
	}

	public int getRecQty() {
		return recQty;
	}

	public void setRecQty(int recQty) {
		this.recQty = recQty;
	}

	
}
