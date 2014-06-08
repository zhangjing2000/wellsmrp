package com.wells.inventory.concept;

import java.util.Date;
import java.util.UUID;

import com.wells.part.concept.AssemblyFinishGood;
import com.wells.part.concept.Part;

public class AssembleOrder implements InventoryOrder {
	private final UUID orderNo;
	private final int assembleLoc;
	private final AssemblyFinishGood finishGood;
	private Date recDate;
	private int recQty;
	
	public AssembleOrder(UUID orderNo, int assembleLoc, AssemblyFinishGood finishGood, Date recDate,
			int recQty) {
		super();
		this.orderNo = orderNo;
		this.assembleLoc = assembleLoc;
		this.finishGood = finishGood;
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
		return assembleLoc;
	}

	public Part getOrderPartNo() {
		return finishGood;
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

