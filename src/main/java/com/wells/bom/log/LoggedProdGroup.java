package com.wells.bom.log;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wells.bom.concept.TagType;
import com.wells.log.common.LogEntry;
import com.wells.part.concept.ProdGroup;

public interface LoggedProdGroup<T extends ProdGroupItemLog> extends ProdGroup<T> {
	LoggedProdGroup<T> getGroupLogAtGivenTime(Date timeStamp);	
	LoggedProdGroup<T> getGroupLogWithGivenTag(TagType tagType, String tagValue);
	LoggedProdGroup<T> getGroupLogWithGivenTimeAndTag(Date timeStamp, TagType tagType, String tagValue);

	String getTagValueAtGivenTime(TagType tagType, Date timeStamp);
	Map<TagType, String> getGroupTagsAtGivenTime(Date timeStamp);
	//Date getRevisonDate(String revision);
	
	void setGroupName(String groupName, int entryID, Date entryDate, String comment);
	//void newRevision(String revision, int entryID, Date entryDate, String comment);
	void archiveGroup(int entryID, Date entryDate, String comment);
	
	void addGroupItem(LogEntry entry, T itemLog);
	void updateGroupItem(LogEntry entry, T itemLog);
	void deleteGroupItem(LogEntry entry, int line);
	
	void addTag(TagType tagType, String tagValue, int entryID, Date entryDate, String comment);
	void deleteTag(TagType tagType, int entryID, Date entryDate, String comment);	
	
	List<String> getDetailLogs();
	List<String> getTagLogs();
	
}
