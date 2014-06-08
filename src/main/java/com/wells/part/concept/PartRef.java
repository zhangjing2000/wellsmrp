package com.wells.part.concept;

import java.util.UUID;

public class PartRef implements Part {
	private final UUID partNo;
	
	
	public PartRef(UUID partNo) {
		super();
		this.partNo = partNo;
	}

	public UUID getPartNo() {
		return partNo;
	}

	public String getPartDesc() {
		return null;
	}

}
