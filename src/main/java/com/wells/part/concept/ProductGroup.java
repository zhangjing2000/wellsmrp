package com.wells.part.concept;

import java.util.Map;
import java.util.SortedSet;
import java.util.UUID;

import com.wells.bom.concept.TagType;

public interface ProductGroup extends Part {
	UUID getGroupID();
	String getGroupName();
	GroupType getGroupType();
	SortedSet<ProductGroupMember> getGroupDetails();
	Map<TagType, String> getGroupTags();
	String getTagValue(TagType tagType);
}
