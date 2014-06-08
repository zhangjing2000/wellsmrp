package com.wells.bom.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import com.wells.bom.concept.TagType;
import com.wells.bom.log.ProductGroupLog;
import com.wells.bom.log.LoggedProductGroup;
import com.wells.bom.snapshot.ProductGroupSnapshot;
import com.wells.bom.snapshot.SnapshotedProductGroup;
import com.wells.part.concept.GroupType;
import com.wells.part.concept.MemberType;
import com.wells.part.concept.ProductGroupMember;

public class ProductGroupServiceImpl implements ProductGroupService {

	private final Map<UUID, LoggedProductGroup> groupLogs = new HashMap<UUID, LoggedProductGroup>();
	private final Map<UUID, SnapshotedProductGroup> groupSnapshots = new HashMap<UUID, SnapshotedProductGroup>();
	private final Map<UUID, Map<Date, SnapshotedProductGroup>> logsToSnapshot
		= new HashMap<UUID, Map<Date, SnapshotedProductGroup>>();
	private final Map<String, SnapshotedProductGroup> groupRevisions = new HashMap<String, SnapshotedProductGroup>();
	
	//private boolean printTrace = false;
	public LoggedProductGroup createProductGroupLog(int entryID, Date entryDate, String comment,
			GroupType groupType, String groupName) {
		System.out.println("create new group" + groupName);
		LoggedProductGroup groupLog = new ProductGroupLog(entryID, entryDate, comment, groupType, groupName);
		groupLogs.put(groupLog.getGroupID(), groupLog);
		return groupLog;
	}

	private UUID getSnapshotGroupID(UUID logGroupID, Date snapshotDateTime) {
		Map<Date, SnapshotedProductGroup> snapshots = logsToSnapshot.get(logGroupID);
		if (snapshots == null) return null;
		SnapshotedProductGroup snapshot = snapshots.get(snapshotDateTime);
		return (snapshot == null?null:snapshot.getGroupID());
	}
	
	private void putSnapshotGroup(UUID logGroupID, Date snapshotDateTime, SnapshotedProductGroup snapshot) {
		groupSnapshots.put(snapshot.getGroupID(), snapshot);
		Map<Date, SnapshotedProductGroup> snapshots = logsToSnapshot.get(logGroupID);
		if (snapshots == null) {
			snapshots = new HashMap<Date, SnapshotedProductGroup>();
			logsToSnapshot.put(logGroupID, snapshots);
		}
		snapshots.put(snapshotDateTime, snapshot);
	}
	
	private SnapshotedProductGroup createProductGroupSnapshot(LoggedProductGroup groupLog, Date snapshotDateTime) {
		UUID snapshotGroupID = UUID.randomUUID();
		UUID logUUID = groupLog.getGroupID();
		Map<TagType, String> tags = groupLog.getGroupTagsAtGivenTime(snapshotDateTime);
		SortedSet<ProductGroupMember> logDetails = groupLog.getGroupDetailsAtGivenTime(snapshotDateTime);
		SortedSet<ProductGroupMember> snapshotDetails = new TreeSet<ProductGroupMember>();
		for (ProductGroupMember logDetail:logDetails) {
			ProductGroupMember snapshotDetail = null;
			UUID logMemberID = logDetail.getMemberID();
			int minBOMQty = logDetail.getMinBOMQty();
			int maxBOMQty = logDetail.getMaxBOMQty();
			UUID snapshotSubGroupID = null;
			if (logDetail.getMemberType() == MemberType.SUB_GROUP) {
				SnapshotedProductGroup subGroupSnapshot = null;
				snapshotSubGroupID = getSnapshotGroupID(logMemberID, snapshotDateTime);
				if (snapshotSubGroupID == null) {
					LoggedProductGroup logSubGroup = groupLogs.get(logMemberID);
					if (logSubGroup == null) 
						throw new GroupNotFoundException("Group:" + logMemberID + " not found");
					subGroupSnapshot = createProductGroupSnapshot(logSubGroup, snapshotDateTime);
					snapshotSubGroupID = subGroupSnapshot.getGroupID();
				}
			} else {
				snapshotSubGroupID = logMemberID;
			}
			snapshotDetail = new ProductGroupMember(snapshotGroupID, logDetail.getLineNo(), 
					logDetail.getLineComment(), logDetail.getMemberType(), 
					snapshotSubGroupID, minBOMQty, maxBOMQty);
			if (snapshotDetail != null) snapshotDetails.add(snapshotDetail);
		}
		/*
		if (printTrace) {
			System.out.println("snapshot date:" + snapshotDateTime);
			System.out.println("GroupName:" + groupLog.getGroupName());
			for (Map.Entry<TagType, String> entry: tags.entrySet()) {
				System.out.println("tags: key:" + entry.getKey() + ",value:" + entry.getValue());
			}
		}
		*/
		SnapshotedProductGroup snapshot = new ProductGroupSnapshot(logUUID, snapshotDateTime, snapshotDetails, tags);
		putSnapshotGroup(logUUID, snapshotDateTime, snapshot);
		return snapshot;
	}

	public SnapshotedProductGroup createProductGroupSnapshot(
			LoggedProductGroup groupLog, Date snapshotDateTime, String revision) {
		if (groupRevisions.get(revision) != null) 
			throw new UpdateRevisionException("Revision " + revision + " has been used");
		SnapshotedProductGroup snapshot = createProductGroupSnapshot(groupLog, snapshotDateTime);
		groupRevisions.put(revision, snapshot);
		return snapshot;
	}

	public SnapshotedProductGroup getProductGroupSnapshot(String revision) {
		return groupRevisions.get(revision);
	}

	public SnapshotedProductGroup getProductGroupSnapshot(UUID groupID) {
		return groupSnapshots.get(groupID);
	}
}
