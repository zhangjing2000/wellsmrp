package com.wells.bom.snapshot;

import java.util.Date;
import java.util.UUID;

import com.wells.bom.concept.ProductGroup;

public interface SnapshotedProductGroup extends ProductGroup {
	UUID getLoggedGroupID();
	Date getSnapshotDateTime();
}
