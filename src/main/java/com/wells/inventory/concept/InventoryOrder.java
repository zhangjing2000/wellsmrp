package com.wells.inventory.concept;

import java.util.Date;
import java.util.UUID;

import com.wells.part.concept.Part;

public interface InventoryOrder {
	UUID getOrderNo();
	Date getOrderRecDate();
	int getOrderRecLoc();
	Part getOrderPartNo();
	int getOrderQty();
}
