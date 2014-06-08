package com.wells.bom.log;

import java.util.Date;
import java.util.UUID;

import com.wells.part.concept.Part;
import com.wells.part.concept.PartRef;
import com.wells.part.concept.ProdGroupItem;

class ProdGroupItemLog extends ProductGroupChangeLog implements ProdGroupItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int lineNo;
	private String lineComment;
	private PartRef item;
	
	ProdGroupItemLog(UUID logGroupID,  int logUserID, Date logDate,
			GroupChangeLogType logType, String logComment,
			int lineNo, String lineComment,
			PartRef item) {
		super(logGroupID, logUserID, logDate, logType, logComment);
		this.lineNo = lineNo;
		this.lineComment = lineComment;
		this.item = item;
	}
	public int getLineNo() {
		return lineNo;
	}
	
	public String getLineComment() {
		return lineComment;
	}
	
	public Part getItem() {
		return item;
	}
	
	public PartRef getPart() {
		return item;
	}

	@Override
	public String toString() {
		return new StringBuilder("LineChangeLog")
			.append(super.toString())
			.append(",line#:").append(lineNo)
			.append(",lineComment:").append(lineComment)
			.append(",item:").append(item)
			.toString();
	}
	
}
