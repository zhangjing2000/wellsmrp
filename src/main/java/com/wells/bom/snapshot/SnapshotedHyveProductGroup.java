package com.wells.bom.snapshot;

import java.util.Date;
import java.util.UUID;

import com.wells.bom.concept.HyveProductGroup;

public interface SnapshotedHyveProductGroup extends HyveProductGroup {
	UUID getLoggedGroupID();
	Date getSnapshotDateTime();
}
