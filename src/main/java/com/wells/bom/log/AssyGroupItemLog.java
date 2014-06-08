package com.wells.bom.log;

import java.util.Date;
import java.util.UUID;

import com.wells.part.concept.MemberType;
import com.wells.part.concept.PartRef;

public class AssyGroupItemLog extends ProdGroupItemLog {
	private static final long serialVersionUID = 1L;
	private final int minBOMQty;
	private final int maxBOMQty;

	AssyGroupItemLog(UUID logGroupID, int logUserID, Date logDate,
			GroupChangeLogType logType, String logComment, int line,
			String lineComment, PartRef part,
			int minBOMQty, int maxBOMQty) {
		super(logGroupID, logUserID, logDate, logType, logComment, line, 
				lineComment, part);
		this.maxBOMQty = maxBOMQty;
		this.minBOMQty = minBOMQty;
	}

	public int getMinBOMQty() {
		return minBOMQty;
	}

	public int getMaxBOMQty() {
		return maxBOMQty;
	}
}
