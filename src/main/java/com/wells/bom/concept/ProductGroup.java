package com.wells.bom.concept;

import java.util.Map;
import java.util.SortedSet;
import java.util.UUID;

public interface ProductGroup {
	UUID getGroupID();
	String getGroupName();
	GroupType getGroupType();
	SortedSet<ProductGroupMember> getGroupDetails();
	Map<TagType, String> getGroupTags();
	String getTagValue(TagType tagType);
}
