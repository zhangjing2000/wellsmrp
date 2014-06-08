package com.wells.bom.log;

import java.util.Date;
import java.util.UUID;

import com.wells.part.concept.MemberType;
import com.wells.part.concept.ProductGroupMember;

class ProductGroupLineLog extends ProductGroupChangeLog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int line;
	private MemberType logMemberType;
	private String lineComment;
	private UUID memberID;
	private int minBOMQty;
	private int maxBOMQty;
	
	ProductGroupLineLog(UUID logGroupID,  int logUserID, Date logDate,
			GroupChangeLogType logType, String logComment,
			int line, MemberType logMemberType, String lineComment,
			UUID memberID, int minBOMQty, int maxBOMQty) {
		super(logGroupID, logUserID, logDate, logType, logComment);
		this.line = line;
		this.logMemberType = logMemberType;
		this.lineComment = lineComment;
		this.memberID = memberID;
		this.minBOMQty = minBOMQty;
		this.maxBOMQty = maxBOMQty;
	}
	int getLine() {
		return line;
	}
	
	MemberType getLogMemberType() {
		return logMemberType;
	}
	
	String getLineComment() {
		return lineComment;
	}
	
	UUID getSubGroupID() {
		return memberID;
	}
	
	int getMinBOMQty() {
		return minBOMQty;
	}
	
	int getMaxBOMQty() {
		return maxBOMQty;
	}

	@Override
	public String toString() {
		return new StringBuilder("LineChangeLog")
			.append(super.toString())
			.append(",line#:").append(line)
			.append(",memberType:").append(logMemberType)
			.append(",lineComment:").append(lineComment)
			.append(",memberID:").append(memberID)
			.append(",minBOMQty:").append(minBOMQty)
			.append(",maxBOMQty:").append(maxBOMQty)
			.toString();
	}
	
	ProductGroupMember toProductGroupDetail() {
		return new ProductGroupMember(getLogGroupID(), line, lineComment, logMemberType, memberID, minBOMQty, maxBOMQty);
	}
}
