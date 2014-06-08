package com.wells.bom.log;

import java.util.Date;
import java.util.UUID;

import com.wells.part.concept.AltGroupItem;
import com.wells.part.concept.Part;
import com.wells.part.concept.PartRef;

public class AltGroupItemLog extends ProdGroupItemLog implements AltGroupItem {
	private static final long serialVersionUID = 1L;

	AltGroupItemLog(UUID logGroupID, int logUserID, Date logDate,
			GroupChangeLogType logType, String logComment, int line,
			String lineComment, PartRef part) {
		super(logGroupID, logUserID, logDate, logType, logComment, line, lineComment, part);
	}
	
}
