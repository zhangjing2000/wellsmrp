package com.wells.bom.log;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.UUID;

import com.wells.bom.concept.TagType;
import com.wells.part.concept.GroupType;
import com.wells.part.concept.MemberType;
import com.wells.part.concept.ProductGroup;
import com.wells.part.concept.ProductGroupMember;

public interface LoggedProductGroup extends ProductGroup {
	SortedSet<ProductGroupMember> getGroupDetailsAtGivenTime(Date timeStamp);	
	SortedSet<ProductGroupMember> getGroupDetailsWithGivenTag(TagType tagType, String tagValue);
	SortedSet<ProductGroupMember> getGroupDetailsWithGivenTimeAndTag(Date timeStamp, TagType tagType, String tagValue);

	String getTagValueAtGivenTime(TagType tagType, Date timeStamp);
	Map<TagType, String> getGroupTagsAtGivenTime(Date timeStamp);
	//Date getRevisonDate(String revision);
	
	void setGroupName(String groupName, int entryID, Date entryDate, String comment);
	void setGroupType(GroupType groupType, int entryID, Date entryDate, String comment);
	//void newRevision(String revision, int entryID, Date entryDate, String comment);
	void archiveGroup(int entryID, Date entryDate, String comment);
	
	void addGroupDetail(int entryID, Date entryDate, String comment, int lineNo, 
			MemberType logMemberType, String lineComment,
			UUID memberID, int minBOMQty, int maxBOMQty);
	void updateGroupDetail(int entryID, Date entryDate, String comment, int line, 
			MemberType logMemberType, String lineComment,
			UUID memberID, int minBOMQty, int maxBOMQty);
	void deleteGroupDetail(int entryID, Date entryDate, String comment, int line);
	
	void addTag(TagType tagType, String tagValue, int entryID, Date entryDate, String comment);
	void deleteTag(TagType tagType, int entryID, Date entryDate, String comment);	
	
	List<String> getDetailLogs();
	List<String> getTagLogs();
	
}
