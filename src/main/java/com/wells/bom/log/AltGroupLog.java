package com.wells.bom.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import com.wells.bom.concept.TagType;
import com.wells.bom.log.exception.InvalidGroupTypeException;
import com.wells.bom.log.exception.NullGroupTypeException;
import com.wells.log.common.LogEntry;
import com.wells.part.concept.AltGroupItem;
import com.wells.part.concept.GroupType;
import com.wells.part.concept.PartRef;

public class AltGroupLog extends TreeSet<AltGroupItemLog> implements LoggedProdGroup<AltGroupItemLog> {

	private static final long serialVersionUID = 1L;
	private final UUID groupID;
	private final SortedSet<ProductGroupTagLog> tags;
	
	public AltGroupLog(int entryID, Date entryDate, String comment, GroupType groupType, String groupName) {
		this.groupID = UUID.randomUUID();
		tags = new TreeSet<ProductGroupTagLog>();
		addTag(TagType.NEW_GROUP, groupID.toString(), entryID, entryDate, comment);
		setGroupName(groupName, entryID, entryDate, comment);
	}
	
	private AltGroupLog(AltGroupLog log) {
		this.groupID = log.getGroupID();
		tags = new TreeSet<ProductGroupTagLog>();
		Date entryDate = new Date();
		int entryID = 0;
		addTag(TagType.NEW_GROUP, groupID.toString(), entryID, entryDate, "clone");
		setGroupName(log.getGroupName(), entryID, entryDate, "clone");
	}
	
	public UUID getGroupID() {
		return groupID;
	}

	public String getGroupName() {
		return getTagValue(TagType.GROUP_NAME);
	}

	public GroupType getGroupType() {
		return GroupType.valueOf(getTagValue(TagType.GROUP_TYPE));
	}

	public LoggedProdGroup<AltGroupItemLog> getGroupLogAtGivenTime(Date timeStamp) {
		AltGroupLog result = new AltGroupLog(this);
		for (AltGroupItem item: this) {
			AltGroupItemLog log = (AltGroupItemLog)item;
			if (timeStamp != null && log.getLogDate().after(timeStamp)) break;
			if (log.getLogType() == GroupChangeLogType.DELETE_GROUP_LINE) {
				result.remove(log);
			} else {
				result.add(log);
			}
		}
		return result;
	}

	public Map<TagType, String> getGroupTags() {
		return getGroupTagsAtGivenTime(null);
	}
	
	public String getTagValue(TagType tagType) {
		return getTagValueAtGivenTime(tagType, null);
	}
	
	public String getTagValueAtGivenTime(TagType tagType, Date timeStamp) {
		String returnValue = null;
		for (ProductGroupTagLog tagLog: tags) {
			if (timeStamp!= null && tagLog.getLogDate().after(timeStamp)) break;
			if (tagLog.getTagType() != tagType) continue;
			if (tagLog.getLogType() == GroupChangeLogType.NEW_TAG) {
				returnValue = tagLog.getTagValue();
			} else if (tagLog.getLogType() == GroupChangeLogType.DELETE_TAG) {  
				returnValue = null;
			}
		}
		return returnValue;
	}
	
	public Map<TagType, String> getGroupTagsAtGivenTime(Date timeStamp) {
		Map<TagType, String> map = new HashMap<TagType, String>();
		for (ProductGroupTagLog tagLog: tags) {
			if (timeStamp!= null && tagLog.getLogDate().after(timeStamp)) break;
			if (tagLog.getLogType() == GroupChangeLogType.NEW_TAG) {
				map.put(tagLog.getTagType(), tagLog.getTagValue());
			} else if (tagLog.getLogType() == GroupChangeLogType.DELETE_TAG) {  
				map.remove(tagLog.getLogType());
			}
		}
		return map;
	}
	
	public LoggedProdGroup<AltGroupItemLog> getGroupLogWithGivenTag(TagType tagType, String tagValue) {
		return getGroupLogWithGivenTimeAndTag(null, tagType, tagValue);
	}
	
	public LoggedProdGroup<AltGroupItemLog> getGroupLogWithGivenTimeAndTag(Date timeStamp, 
			TagType tagType, String tagValue) {
		// find tag time before timestamp
		Date tagTime = null; 
		for (ProductGroupTagLog tagLog: tags) {
			if (timeStamp != null && tagLog.getLogDate().after(timeStamp)) break;
			if (tagLog.getTagType() != tagType) continue;
			if (!tagLog.getTagValue().equals(tagValue)) continue;
			if (tagLog.getLogType() == GroupChangeLogType.NEW_TAG) {
				tagTime = tagLog.getLogDate();
			} else if (tagLog.getLogType() == GroupChangeLogType.DELETE_TAG) {  
				tagTime = null;
			}
		}
		
		if (tagTime != null) {
			return getGroupLogAtGivenTime(tagTime);
		} else {
			return new AltGroupLog(this);
		}
	}

	public void setGroupName(String groupName, int entryID, Date entryDate, String comment) {
		addTag(TagType.GROUP_NAME, groupName, entryID, entryDate, comment);
	}
	
	/*
	public void newRevision(String revision, int entryID, Date entryDate, String comment) {
		addTag(TagType.REVISION, revision, entryID, entryDate, comment);
	}
	*/
	public void archiveGroup(int entryID, Date entryDate, String comment) {
		addTag(TagType.ARCHIVE_GROUP, groupID.toString(), entryID, entryDate, comment);
	}
	
	public void addGroupItem(LogEntry entry, AltGroupItemLog itemLog) {
		boolean isInsert = true;
		int lineNo = itemLog.getLineNo();
		if (isEmpty()) {
			if (lineNo<= 0) lineNo = 1;
			isInsert = false;
		} else {
			AltGroupItem lastLine = last();
			if (lineNo == 0) {
				lineNo = lastLine.getLineNo() + 1;
				isInsert = false;
			} else if (lineNo > lastLine.getLineNo()) {
				isInsert = false;
			}
		}
		AltGroupItemLog log = new AltGroupItemLog(groupID, entry.getLogUserID(), entry.getLogDate(),
				isInsert?GroupChangeLogType.UPDATE_GROUP_LINE:GroupChangeLogType.NEW_GROUP_LINE, 
				entry.getLogComment(),
				lineNo, itemLog.getLineComment(), itemLog.getPart());
		addGroupDetail(log);
		if (!isInsert) return;
		int insertLineNo = lineNo;
		for (AltGroupItem detail:this) {
			if (detail.getLineNo() < insertLineNo) continue;
			if (detail.getLineNo() == insertLineNo) {
				insertLineNo++;
				log = new AltGroupItemLog(groupID,  entry.getLogUserID(), entry.getLogDate(),
						GroupChangeLogType.UPDATE_GROUP_LINE, entry.getLogComment(),
						insertLineNo, 
						detail.getLineComment(),
						(PartRef)detail.getItem()); 
				addGroupDetail(log);
			}
			if (detail.getLineNo() > insertLineNo) break;
		}
	}
	
	public void updateGroupItem(LogEntry entry, AltGroupItemLog itemLog) {
		AltGroupItemLog log = new AltGroupItemLog(groupID,  entry.getLogUserID(), entry.getLogDate(),
			GroupChangeLogType.UPDATE_GROUP_LINE, entry.getLogComment(),
			itemLog.getLineNo(), itemLog.getLineComment(), itemLog.getPart());
		addGroupDetail(log);
	}
	
	public void deleteGroupItem(LogEntry entry, int line) {
		AltGroupItemLog log = new AltGroupItemLog(groupID, entry.getLogUserID(), entry.getLogDate(),
			GroupChangeLogType.DELETE_GROUP_LINE, entry.getLogComment(),
			line, null, null);
		addGroupDetail(log);
	}
	
	private void addGroupDetail(AltGroupItemLog lineLog) {
		add(lineLog);
	}
	
	private void checkGroupType(String groupTypeStr) {
		if (groupTypeStr == null) throw new NullGroupTypeException("cannot give a empty group type");
		try {
			if (GroupType.valueOf(groupTypeStr) == null) throw new InvalidGroupTypeException("wrong group type input:" + groupTypeStr);
		} catch (IllegalArgumentException e) {
			throw new InvalidGroupTypeException("wrong group type input:" + groupTypeStr);
		}
	}
	
	/*
	private void checkRevision(String revision) {
		for (GroupTagLog tagLog: tags) {
			//if (tagLog.getTagType() != TagType.REVISION) continue;
			if (tagLog.getTagValue().equals(revision)) throw new UpdateRevisionException("cannot add an exisitng revision");
		}
	}
	*/
	public void addTag(TagType tagType, String tagValue, int entryID, Date entryDate, String comment) {
		if (tagType == TagType.GROUP_TYPE) {
			checkGroupType(tagValue);
		}
		/*
		if (tagType == TagType.REVISION) {
			checkRevision(tagValue);
		}
		*/
		ProductGroupTagLog tagLog = new ProductGroupTagLog(groupID, entryID, entryDate, GroupChangeLogType.NEW_TAG, comment, tagType, tagValue);
		tags.add(tagLog);
	}

	public void deleteTag(TagType tagType, int entryID, Date entryDate, String comment) {
		//if (tagType == TagType.REVISION) throw new UpdateRevisionException("cannot remove an revision");
		ProductGroupTagLog tagLog = new ProductGroupTagLog(groupID, entryID, entryDate, GroupChangeLogType.DELETE_TAG, comment, tagType, null);
		tags.add(tagLog);
	}
	
	public List<String> getDetailLogs() {
		List<String> logs = new ArrayList<String>();
		for (AltGroupItem lineLog: this) {
			logs.add(lineLog.toString());
		}
		return logs;
	}

	public List<String> getTagLogs() {
		List<String> logs = new ArrayList<String>();
		for (ProductGroupTagLog tagLog: tags) {
			logs.add(tagLog.toString());
		}
		return logs;
	}
/*
	@Override
	public Date getRevisonDate(String revision) {
		Date tagTime = null;
		for (GroupTagLog tagLog: tags) {
			if (tagLog.getTagType() != TagType.REVISION) continue;
			if (!tagLog.getTagValue().equals(revision)) continue;
			if (tagLog.getLogType() == GroupChangeLogType.NEW_TAG) {
				tagTime = tagLog.getLogDate();
			} else if (tagLog.getLogType() == GroupChangeLogType.DELETE_TAG) {  
				tagTime = null;
			}
		}
		return tagTime;
	}
	*/

	public UUID getPartNo() {
		return getGroupID();
	}

	public String getPartDesc() {
		return getGroupName();
	}
}
