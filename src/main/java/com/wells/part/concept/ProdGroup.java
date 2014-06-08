package com.wells.part.concept;

import java.util.Map;
import java.util.SortedSet;
import java.util.UUID;

import com.wells.bom.concept.TagType;

public interface ProdGroup<T extends ProdGroupItem> extends Part, SortedSet<T> {
	UUID getGroupID();
	String getGroupName();
	GroupType getGroupType();
	Map<TagType, String> getGroupTags();
	String getTagValue(TagType tagType);
}
