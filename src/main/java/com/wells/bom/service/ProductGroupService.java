package com.wells.bom.service;

import java.util.Date;
import java.util.UUID;

import com.wells.bom.log.LoggedProductGroup;
import com.wells.bom.snapshot.SnapshotedProductGroup;
import com.wells.part.concept.GroupType;

public interface ProductGroupService {
	LoggedProductGroup createProductGroupLog(int entryID, Date entryDate, String comment, GroupType groupType, String groupName);
	SnapshotedProductGroup createProductGroupSnapshot(LoggedProductGroup groupLog, Date snapshotDatetime, String revision);
	SnapshotedProductGroup getProductGroupSnapshot(String revision);
	SnapshotedProductGroup getProductGroupSnapshot(UUID groupID);
}
