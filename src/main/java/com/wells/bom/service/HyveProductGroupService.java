package com.wells.bom.service;

import java.util.Date;
import java.util.UUID;

import com.wells.bom.concept.GroupType;
import com.wells.bom.log.LoggedHyveProductGroup;
import com.wells.bom.snapshot.SnapshotedHyveProductGroup;

public interface HyveProductGroupService {
	LoggedHyveProductGroup createHyveProductGroupLog(int entryID, Date entryDate, String comment, GroupType groupType, String groupName);
	SnapshotedHyveProductGroup createHyveProductGroupSnapshot(LoggedHyveProductGroup groupLog, Date snapshotDatetime, String revision);
	SnapshotedHyveProductGroup getHyveProductGroupSnapshot(String revision);
	SnapshotedHyveProductGroup getHyveProductGroupSnapshot(UUID groupID);
}
