package com.wells.part.concept;

import java.io.Serializable;
import java.util.UUID;

public class MaterialItem implements Part, Serializable, Comparable<MaterialItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final UUID partNo;
	private final String partDesc;
	public MaterialItem(UUID partNo, String partDesc) {
		super();
		this.partNo = partNo;
		this.partDesc = partDesc;
	}
	public UUID getPartNo() {
		return partNo;
	}
	public String getPartDesc() {
		return partDesc;
	}
	@Override
	public int hashCode() {
		return partNo.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MaterialItem other = (MaterialItem) obj;
		if (!partNo.equals(other.partNo))
			return false;
		return true;
	}

	public int compareTo(MaterialItem o) {
		return partNo.compareTo(o.partNo);
	}
	

}
