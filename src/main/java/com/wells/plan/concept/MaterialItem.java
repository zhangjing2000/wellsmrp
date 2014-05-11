package com.wells.plan.concept;

import java.io.Serializable;

public class MaterialItem implements Serializable, Comparable<MaterialItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int skuNo;
	private final String partNo;
	
	public MaterialItem(int skuNo, String partNo) {
		super();
		this.skuNo = skuNo;
		this.partNo = partNo;
	}

	public int getSkuNo() {
		return skuNo;
	}

	public String getPartNo() {
		return partNo;
	}

	@Override
	public int hashCode() {
		return skuNo;
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
		if (skuNo != other.skuNo)
			return false;
		return true;
	}

	public int compareTo(MaterialItem o) {
		return skuNo - o.skuNo;
	}
	

}
