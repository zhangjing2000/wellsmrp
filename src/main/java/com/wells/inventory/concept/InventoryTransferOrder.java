package com.wells.inventory.concept;

import java.util.Date;
import java.util.UUID;

import com.wells.part.concept.Part;

public class InventoryTransferOrder implements InventoryOrder {
	private final UUID orderNo;
	private final int recLoc;
	private final int shipLoc;
	private final Part partNo;
	private Date recDate;
	private int recQty;
	
	public InventoryTransferOrder(UUID orderNo, int recLoc, int shipLoc, Part partNo, Date recDate,
			int recQty) {
		super();
		this.orderNo = orderNo;
		this.recLoc = recLoc;
		this.shipLoc = shipLoc;
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

	public int getOrderShipLoc() {
		return shipLoc;
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
