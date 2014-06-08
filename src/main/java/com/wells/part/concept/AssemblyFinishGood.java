package com.wells.part.concept;

import java.util.Map;
import java.util.UUID;

import com.wells.bom.snapshot.ProductGroupSnapshot;

public class AssemblyFinishGood implements Part {
	private final UUID partNo;
	private final String partDesc;
	private final ProductGroupSnapshot bom;
	private final Map<UUID, Integer> components;
	public AssemblyFinishGood(UUID partNo, String partDesc, 
			ProductGroupSnapshot bom, Map<UUID, Integer> components) {
		super();
		this.partNo = partNo;
		this.partDesc = partDesc;
		this.bom = bom;
		this.components = components;
	}
	
	public UUID getPartNo() {
		return partNo;
	}
	
	public String getPartDesc() {
		return partDesc;
	}
	
	public ProductGroupSnapshot getBOM() {
		return bom;
	}
	
	public Map<UUID, Integer> getComponents() {
		return components;
	}
}
