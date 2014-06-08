package com.wells.inventory.concept;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.wells.part.concept.Part;

public interface MultiPartInventoryOrder {
	UUID getOrderNo();
	Date getOrderRecDate();
	int getOrderRecLoc();
	Map<Part, Integer> getOrderRecParts();
	Set<InventoryOrder> getPartOrders();
}
