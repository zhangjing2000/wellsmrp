package com.wells.bom.service;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedSet;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.wells.bom.concept.TagType;
import com.wells.bom.log.LoggedProductGroup;
import com.wells.bom.service.ProductGroupService;
import com.wells.bom.service.ProductGroupServiceImpl;
import com.wells.bom.snapshot.SnapshotedProductGroup;
import com.wells.part.concept.GroupType;
import com.wells.part.concept.MemberType;
import com.wells.part.concept.ProductGroup;
import com.wells.part.concept.ProductGroupMember;

public class ProductGroupServiceTest {
	private LoggedProductGroup ash13ServerLog;
	private SnapshotedProductGroup ash13ServerSnapshot;
	private ProductGroupService service;
	private DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	private static int entryID = 4739;
	private Date entryDate;
	private static String serverGroupName = "Ash13Server";
	private static String chassisGroupName ="Chassis";
	private static String motherboardGroupName ="Motherboard";
	private static String harddriveGroupName = "HardDrive";
	private static String processorGroupName = "Processor";
	private static String memoryGroupName = "Memory";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		entryDate = sdf.parse("01/01/2014");
		service = new ProductGroupServiceImpl();
		ash13ServerLog = service.createProductGroupLog(entryID, entryDate, "Init Test",  GroupType.ASSEMBLY, serverGroupName);
	}

	@After
	public void tearDown() throws Exception {
  	}

	private void assertFirstMemberAsChassis(SortedSet<ProductGroupMember> details) {
		assertAssemblyGroupMember(details, 1, chassisGroupName);
	}
	
	private void assertAssemblyGroupMember(SortedSet<ProductGroupMember> details, int memberIndex, String groupName) {
		int arrayIdx = memberIndex -1;
		ProductGroupMember subGroupMember = (ProductGroupMember)details.toArray()[arrayIdx];
		assertEquals(MemberType.SUB_GROUP, subGroupMember.getMemberType());
		SnapshotedProductGroup snapshotSubGroup = service.getProductGroupSnapshot(subGroupMember.getMemberID());	
		assertNotNull(snapshotSubGroup);
		assertEquals(snapshotSubGroup.getGroupType(), GroupType.ASSEMBLY);
		assertEquals(snapshotSubGroup.getGroupName(), groupName);
	}

	private void assertAlternativeGroupMember(SortedSet<ProductGroupMember> details, int memberIndex, String groupName) {
		int arrayIdx = memberIndex -1;
		ProductGroupMember subGroupMember = (ProductGroupMember)details.toArray()[arrayIdx];
		assertEquals(MemberType.SUB_GROUP, subGroupMember.getMemberType());
		SnapshotedProductGroup snapshotSubGroup = service.getProductGroupSnapshot(subGroupMember.getMemberID());	
		assertNotNull(snapshotSubGroup);
		assertEquals(snapshotSubGroup.getGroupType(), GroupType.ALTERNATIVE);
		assertEquals(snapshotSubGroup.getGroupName(), groupName);
	}
	
	private void assertSecondMemberAsMotherboard(SortedSet<ProductGroupMember> details) {
		assertAlternativeGroupMember(details, 2, motherboardGroupName);
	}
	
	private void assertThirdMemberAsProcessor(SortedSet<ProductGroupMember> details) {
		assertAlternativeGroupMember(details, 3, processorGroupName);
	}
	
	private void assertFourthMemberAsMemory(SortedSet<ProductGroupMember> details) {
		assertAlternativeGroupMember(details, 4, memoryGroupName);
	}
	
	private void assertAssembleGroupMemberBOMQty(SortedSet<ProductGroupMember> details, int memberIndex, int minBOMQty, int maxBOMQty) {
		int arrayIdx = memberIndex -1;
		ProductGroupMember subGroupMember = (ProductGroupMember)details.toArray()[arrayIdx];
		assertEquals(MemberType.SUB_GROUP, subGroupMember.getMemberType());
		assertEquals(subGroupMember.getMinBOMQty(), minBOMQty);
		assertEquals(subGroupMember.getMaxBOMQty(), maxBOMQty);
	}
	
	@Test
	public void testCreateSnapshot() throws ParseException {
		int revision;
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = sdf.parse("01/01/2014");
		Date date2 = sdf.parse("01/02/2014");
		Date date3 = sdf.parse("01/03/2014");
		Date date4 = sdf.parse("01/04/2014");
		Date date5 = sdf.parse("01/05/2014");
		Date date6 = sdf.parse("01/06/2014");
		Date date7 = sdf.parse("01/07/2014");
		Date date8 = sdf.parse("01/08/2014");
		
		revision = 1;
		ash13ServerLog.addTag(TagType.FAMILY, "ASH", entryID, date1, "add family");
		ash13ServerLog.addTag(TagType.MODEL, "13", entryID, date1, "add model");
		ash13ServerSnapshot = service.createProductGroupSnapshot(ash13ServerLog, date1, "revision" + revision);
		assertNotNull(ash13ServerSnapshot);
		assertEquals(ash13ServerSnapshot.getLoggedGroupID(), ash13ServerLog.getGroupID());
		assertEquals(ash13ServerSnapshot.getGroupName(), ash13ServerLog.getGroupName());
		
		revision++;
		addGroupDetailToAshServer(entryID, date2, "add chassis",
				GroupType.ASSEMBLY, chassisGroupName, MemberType.SUB_GROUP,
				"chassis sub bom", 1, 1);
		ash13ServerSnapshot = service.createProductGroupSnapshot(ash13ServerLog, date2, "revision" + revision);
		assertNotNull(ash13ServerSnapshot);
		SortedSet<ProductGroupMember> details = ash13ServerSnapshot.getGroupDetails(); 
		assertTrue(!details.isEmpty());
		assertEquals(details.size(), 1);
		assertFirstMemberAsChassis(details);
		
		revision++;
		addGroupDetailToAshServer(entryID, date3, "add motherboard",
				GroupType.ALTERNATIVE, motherboardGroupName, MemberType.SUB_GROUP,
				"motherboard sub bom", 1, 1);
		ash13ServerSnapshot = service.createProductGroupSnapshot(ash13ServerLog, date3, "revision" + revision);
		assertNotNull(ash13ServerSnapshot);
		details = ash13ServerSnapshot.getGroupDetails(); 
		assertTrue(!details.isEmpty());
		assertEquals(details.size(), 2);
		assertFirstMemberAsChassis(details);
		assertSecondMemberAsMotherboard(details);
		
		revision++;
		ProductGroup processor = addGroupDetailToAshServer(entryID, date3, "add processor",
				GroupType.ALTERNATIVE, processorGroupName, MemberType.SUB_GROUP,
				"processor sub bom", 1, 1);
		assertEquals(ash13ServerLog.getGroupDetails().size(), 3);
		ash13ServerSnapshot = service.createProductGroupSnapshot(ash13ServerLog, date3, "revision" + revision);
		assertNotNull(ash13ServerSnapshot);
		details = ash13ServerSnapshot.getGroupDetails(); 
		assertTrue(!details.isEmpty());
		assertEquals(details.size(), 3);
		assertFirstMemberAsChassis(details);
		assertSecondMemberAsMotherboard(details);
		assertThirdMemberAsProcessor(details);
		
		revision++;
		int lineNo = 3;
		
		updateGroupDetailToAshServer(entryID, date4, "update processor",
				lineNo, processor.getGroupID(), MemberType.SUB_GROUP,
				"processor sub bom", 2, 2);

		assertEquals(ash13ServerLog.getGroupDetails().size(), 3);
		ash13ServerSnapshot = service.createProductGroupSnapshot(ash13ServerLog, date4, "revision" + revision);
		assertNotNull(ash13ServerSnapshot);
		details = ash13ServerSnapshot.getGroupDetails(); 
		assertTrue(!details.isEmpty());
		assertEquals(details.size(), 3);
		assertFirstMemberAsChassis(details);
		assertSecondMemberAsMotherboard(details);
		assertThirdMemberAsProcessor(details);

		revision++;
		ProductGroup memory = addGroupDetailToAshServer(entryID, date5, "add memory",
				GroupType.ALTERNATIVE, memoryGroupName, MemberType.SUB_GROUP,
				"memory sub bom", 2, 4);
		assertEquals(ash13ServerLog.getGroupDetails().size(),4);
		ash13ServerSnapshot = service.createProductGroupSnapshot(ash13ServerLog, date5, "revision" + revision);
		assertNotNull(ash13ServerSnapshot);
		details = ash13ServerSnapshot.getGroupDetails(); 
		assertTrue(!details.isEmpty());
		assertEquals(details.size(), 4);
		assertFirstMemberAsChassis(details);
		assertSecondMemberAsMotherboard(details);
		assertThirdMemberAsProcessor(details);
		assertFourthMemberAsMemory(details);

		ash13ServerSnapshot = service.getProductGroupSnapshot("revision" + (revision-1));
		assertNotNull(ash13ServerSnapshot);
		details = ash13ServerSnapshot.getGroupDetails(); 
		assertTrue(!details.isEmpty());
		assertEquals(details.size(), 3);
		assertFirstMemberAsChassis(details);
		assertSecondMemberAsMotherboard(details);
		assertThirdMemberAsProcessor(details);

		revision++;
		lineNo = 4;
		
		updateGroupDetailToAshServer(entryID, date6, "update memory",
				lineNo, memory.getGroupID(), MemberType.SUB_GROUP,
				"memory sub bom", 2, 2);
		ash13ServerSnapshot = service.createProductGroupSnapshot(ash13ServerLog, date6, "revision" + revision);

		revision++;
		lineNo = 3;
		
		updateGroupDetailToAshServer(entryID, date7, "update processor",
				lineNo, processor.getGroupID(), MemberType.SUB_GROUP,
				"processor sub bom", 1, 2);
		ash13ServerSnapshot = service.createProductGroupSnapshot(ash13ServerLog, date7, "revision" + revision);

		revision++;
		ProductGroup harddrive = insertGroupDetailToAshServer(entryID, date8, "add hard drive",
				3, GroupType.ALTERNATIVE, harddriveGroupName, MemberType.SUB_GROUP,
				"hard drive sub bom", 4, 4);

		assertNotNull(harddrive);
		assertEquals(ash13ServerLog.getGroupDetails().size(),5);
		ash13ServerSnapshot = service.createProductGroupSnapshot(ash13ServerLog, date5, "revision" + revision);
		assertNotNull(ash13ServerSnapshot);
		details = ash13ServerSnapshot.getGroupDetails(); 
		assertTrue(!details.isEmpty());
		assertEquals(details.size(), 4);
		assertFirstMemberAsChassis(details);
		assertSecondMemberAsMotherboard(details);
		assertAlternativeGroupMember(details, 3, processorGroupName);
		assertAlternativeGroupMember(details, 4, memoryGroupName);
		
		revision++;
		ash13ServerSnapshot = service.createProductGroupSnapshot(ash13ServerLog, date8, "revision" + revision);
		assertNotNull(ash13ServerSnapshot);
		details = ash13ServerSnapshot.getGroupDetails(); 
		assertTrue(!details.isEmpty());
		assertEquals(details.size(), 5);
		assertFirstMemberAsChassis(details);
		assertSecondMemberAsMotherboard(details);
		assertAlternativeGroupMember(details, 3, harddriveGroupName);
		assertAlternativeGroupMember(details, 4, processorGroupName);
		assertAlternativeGroupMember(details, 5, memoryGroupName);
		
	}

	private ProductGroup addGroupDetailToAshServer(int entryID, Date entryDate, String entryComment, 
			GroupType groupType, String groupName, MemberType memberType, 
			String lineComment, int minBOMQty, int maxBOMQty) {
		ProductGroup prodGroup = service.createProductGroupLog(entryID, entryDate, entryComment, groupType, groupName);
		ash13ServerLog.addGroupDetail(entryID, entryDate, entryComment, 0, memberType, lineComment, 
				prodGroup.getGroupID(),  minBOMQty, maxBOMQty);
		return prodGroup;
	}
	
	private ProductGroup insertGroupDetailToAshServer(int entryID, Date entryDate, String entryComment, 
			int lineNo, GroupType groupType, String groupName, MemberType memberType, 
			String lineComment, int minBOMQty, int maxBOMQty) {
		ProductGroup prodGroup = service.createProductGroupLog(entryID, entryDate, entryComment, groupType, groupName);
		ash13ServerLog.addGroupDetail(entryID, entryDate, entryComment, lineNo, memberType, lineComment, 
				prodGroup.getGroupID(), minBOMQty, maxBOMQty);
		return prodGroup;
	}
	
	private void updateGroupDetailToAshServer(int entryID, Date entryDate, String entryComment, 
			int lineNo, UUID subGroupID, MemberType memberType, 
			String lineComment, int minBOMQty, int maxBOMQty) {
		ash13ServerLog.updateGroupDetail(entryID, entryDate, entryComment, lineNo, memberType, lineComment, 
				subGroupID, minBOMQty, maxBOMQty);
	}

	private int getLineNo(SortedSet<ProductGroupMember> details,
			ProductGroup detailGroup) {
		int lineNo;
		lineNo = 0;
		for (ProductGroupMember detail:details) {
			if (detail.getMemberID() != null  && detail.getMemberID().equals(detailGroup.getGroupID())) {
				lineNo = detail.getLineNo();
				break;
			}
		}
		return lineNo;
	}

}
