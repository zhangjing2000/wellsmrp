package com.wells.bom.snapshot;

import java.util.Date;
import java.util.Map;
import java.util.SortedSet;
import java.util.UUID;

import com.wells.bom.concept.GroupType;
import com.wells.bom.concept.ProductGroupMember;
import com.wells.bom.concept.TagType;

public class ProductGroupSnapshot implements SnapshotedProductGroup {

	private final UUID snapshotGroupID;
	private final Date snapshotDateTime;
	private final String groupName;
	private final GroupType groupType; 
	private final UUID loggedGroupID;
	private final SortedSet<ProductGroupMember> groupDetails; 
	private final Map<TagType, String> groupTags;
	
	public ProductGroupSnapshot(UUID loggedGroupID, Date snapshotDateTime,
			SortedSet<ProductGroupMember> groupDetails,
			Map<TagType, String> groupTags) {
		super();
		this.snapshotGroupID = UUID.randomUUID();
		this.loggedGroupID = loggedGroupID;
		this.groupTags = groupTags;
		this.snapshotDateTime = snapshotDateTime;
		this.groupName = groupTags.get(TagType.GROUP_NAME);
		this.groupType = GroupType.valueOf(groupTags.get(TagType.GROUP_TYPE));
		this.groupDetails = groupDetails;
	}

	public UUID getGroupID() {
		return snapshotGroupID;
	}

	public String getGroupName() {
		return groupName;
	}

	public GroupType getGroupType() {
		return groupType;
	}

	public SortedSet<ProductGroupMember> getGroupDetails() {
		return groupDetails;
	}

	public Map<TagType, String> getGroupTags() {
		return groupTags;
	}

	public String getTagValue(TagType tagType) {
		return groupTags.get(tagType);
	}

	public UUID getLoggedGroupID() {
		return loggedGroupID;
	}

	public Date getSnapshotDateTime() {
		return snapshotDateTime;
	}
}
