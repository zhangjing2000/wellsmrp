package com.wells.inventory.concept;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.wells.part.concept.Part;

public class CustReserveInventoryOrder implements InventoryOrder {
	private final Part part;
	private final int locNo;
	private int reserveQty;
	private final int custNo;
	private final UUID reserveOrderNo;
	
	public CustReserveInventoryOrder(Part part, int locNo, int reserveQty,
			int custNo, UUID reserveOrderNo) {
		super();
		this.part = part;
		this.locNo = locNo;
		this.reserveQty = reserveQty;
		this.custNo = custNo;
		this.reserveOrderNo = reserveOrderNo;
	}

	public UUID getOrderNo() {
		return reserveOrderNo;
	}
	
	public Date getOrderRecDate() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.clear();
		calendar.set(year, month, day_of_month);
		date = calendar.getTime();
		return date;
	}
	
	public int getOrderRecLoc() {
		return locNo;
	}
	
	public Part getOrderPartNo() {
		return part;
	}
	
	public int getOrderQty() {
		return reserveQty;
	}
	
	public void setOrderQty(int orderQty) {
		this.reserveQty = orderQty;
	}
	
	public int custNo() {
		return custNo;
	}
}
