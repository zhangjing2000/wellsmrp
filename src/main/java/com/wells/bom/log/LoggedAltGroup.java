package com.wells.bom.log;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wells.bom.concept.TagType;
import com.wells.part.concept.AltProductGroup;
import com.wells.part.concept.PartRef;

public interface LoggedAltGroup extends AltProductGroup<AltGroupItemLog> {
	LoggedAltGroup getGroupLogAtGivenTime(Date timeStamp);	
	LoggedAltGroup getGroupLogWithGivenTag(TagType tagType, String tagValue);
	LoggedAltGroup getGroupLogWithGivenTimeAndTag(Date timeStamp, TagType tagType, String tagValue);

	String getTagValueAtGivenTime(TagType tagType, Date timeStamp);
	Map<TagType, String> getGroupTagsAtGivenTime(Date timeStamp);
	
	void setGroupName(String groupName, int entryID, Date entryDate, String comment);
	void archiveGroup(int entryID, Date entryDate, String comment);
	
	void addGroupItem(int entryID, Date entryDate, String comment, int lineNo, 
			String lineComment,	PartRef part);
	void updateGroupItem(int entryID, Date entryDate, String comment, int line, 
			String lineComment,	PartRef part);
	void deleteGroupItem(int entryID, Date entryDate, String comment, int line);
	
	void addTag(TagType tagType, String tagValue, int entryID, Date entryDate, String comment);
	void deleteTag(TagType tagType, int entryID, Date entryDate, String comment);	
	
	List<String> getDetailLogs();
	List<String> getTagLogs();
	
}
