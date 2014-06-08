package com.wells.part.concept;

import java.util.UUID;

public class ProductGroupMember implements Comparable<ProductGroupMember>{
	private final UUID groupID;
	private final int lineNo;
	private final String lineComment;
	private final MemberType memberType;
	private final UUID memberID;
	private final int minBOMQty;
	private final int maxBOMQty;
	
	public ProductGroupMember(UUID groupID, int lineNo, String lineComment,
			MemberType memberType, UUID memberID, int minBOMQty, int maxBOMQty) {
		super();
		this.groupID = groupID;
		this.lineNo = lineNo;
		this.lineComment = lineComment;
		this.memberType = memberType;
		this.memberID = memberID;
		this.minBOMQty = minBOMQty;
		this.maxBOMQty = maxBOMQty;
	}
	public UUID getGroupID() {
		return groupID;
	}
	public int getLineNo() {
		return lineNo;
	}
	public String getLineComment() {
		return lineComment;
	}
	public MemberType getMemberType() {
		return memberType;
	}
	public UUID getMemberID() {
		return memberID;
	}
	public int getMinBOMQty() {
		return minBOMQty;
	}
	public int getMaxBOMQty() {
		return maxBOMQty;
	}
	public int compareTo(ProductGroupMember m1) {
		return lineNo - m1.getLineNo();
	}
	public String toString() {
		return new StringBuilder("")
		.append("line#:").append(lineNo)
		.append(",member type:").append(memberType)
		.append(",lineComment:").append(lineComment)
		.append(",member ID:").append(memberID)
		.append(",min BOM qty:").append(minBOMQty)
		.append(",max BOM qty:").append(maxBOMQty)
		.toString();
	}
}
