package com.wellls.plan.service.optaplanner;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.XmlSolverFactory;

import com.wells.bom.log.LoggedProductGroup;
import com.wells.bom.service.ProductGroupService;
import com.wells.bom.service.ProductGroupServiceImpl;
import com.wells.log.common.LogEntry;
import com.wells.part.concept.GroupType;
import com.wells.part.concept.MemberType;
import com.wells.part.concept.ProductGroup;
import com.wells.plan.concept.ProductionContract;
import com.wells.plan.concept.ContractPlan;
import com.wells.plan.concept.ProductionPlant;
import com.wells.plan.mps.LoggedContractMPSImpl;
import com.wells.plan.mps.MPSEntry;
import com.wells.plan.mrp.LoggedContractMRPImpl;
import com.wells.plan.mrp.MRPEntry;
import com.wells.plan.service.optaplanner.MPSReadinessCheckSolution;

public class ContractPlanTest {
	
	private static String rackGroupName = "Ash13Rack";
	private static String serverGroupName = "Ash13Server";
	private static String chassisGroupName ="Chassis";
	private static String motherboardGroupName ="Motherboard";
	private static String ssdGroupName = "SSD";
	private static String harddriveGroupName = "HardDrive";
	private static String processorGroupName = "Processor";
	private static String memoryGroupName = "Memory";
	private static String cableGroupName = "Cable";
	private static String powerGroupName = "Power";
	private static String screwsGroupName = "Screws";
	private static UUID processorPart = UUID.randomUUID();
	private static UUID harddrivePart = UUID.randomUUID();
	private static UUID motherboardPart = UUID.randomUUID();
	private static UUID ssdPart = UUID.randomUUID();
	private static UUID memoryPart1 = UUID.randomUUID();
	private static UUID memoryPart2 = UUID.randomUUID();
	private static UUID memoryPart3 = UUID.randomUUID();
	private static UUID cableAltPart1 = UUID.randomUUID();
	private static UUID cableAltPart2 = UUID.randomUUID();
	private static UUID powerPart = UUID.randomUUID();
	private static UUID screwAltPart1 = UUID.randomUUID();
	private static UUID screwAltPart2 = UUID.randomUUID();
	private static int entryID = 4739;
	private static DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	private static Date entryDate; 
	
	private ProductionContract contract;
	private Solver solver;
	private ProductGroupService bomService;
	private LogEntry logEntry;
	
	private ProductionPlant plant = new ProductionPlant(1, "MFG", "US");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		entryDate = sdf.parse("01/01/2014");
		contract = new ProductionContract(1,1);
		bomService = new ProductGroupServiceImpl();
		logEntry = new LogEntry() {
			public int getLogUserID() {
				return entryID;
			}
			public Date getLogDate() {
				return entryDate;
			}
			public String getLogComment() {
				return "test";
			}
		};
		initSolver();
	}

	private void initSolver() {
		 SolverFactory solverFactory = new XmlSolverFactory(
	                "/com/wells/plan/mps/MPSToMRPSolverConfig.xml");
		 solver = solverFactory.buildSolver();		
	}
	
	private ProductGroup initAFullRackBOM() {
		LoggedProductGroup power = initPowerAltGroup();
		LoggedProductGroup screws = initScrewsAltGroup();
		LoggedProductGroup rack = bomService.createProductGroupLog(entryID, entryDate, "Init Test", GroupType.ASSEMBLY, rackGroupName);
		LoggedProductGroup server = initAServerBOM(power, screws);
		LoggedProductGroup cables = bomService.createProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, cableGroupName);

		addSubGroupToBOM(rack, "Add Servers", 1, "Servers", server, 10, 20);
		addSubGroupToBOM(rack, "Add Cables", 2, "Cables", cables, 1, 1);
		addSubGroupToBOM(rack, "Add Power", 3, "Power", power, 1, 1);
		addSubGroupToBOM(rack, "Add Screws", 4, "Screws", screws, 10, 20);
		
		addPartToAltGroup(cables, "Add Cable1", 1, "cable1", cableAltPart1);
		addPartToAltGroup(cables, "Add Cable2", 2, "cable2", cableAltPart2);

		return bomService.createProductGroupSnapshot(rack, entryDate, "1.0");
	}

	private ProductGroup initAServerBOM() {
		LoggedProductGroup power = initPowerAltGroup();
		LoggedProductGroup screws = initScrewsAltGroup();

		LoggedProductGroup server = initAServerBOM(power, screws);
		
		return bomService.createProductGroupSnapshot(server, entryDate, "1.0");
	}

	private LoggedProductGroup initAServerBOM(LoggedProductGroup power,
			LoggedProductGroup screws) {
		LoggedProductGroup server = bomService.createProductGroupLog(entryID, entryDate, "Init Test", GroupType.ASSEMBLY, serverGroupName);
		LoggedProductGroup chassis = initAChassisBOM(power, screws);
		LoggedProductGroup motherboard = bomService.createProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, motherboardGroupName);
		LoggedProductGroup processor = bomService.createProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, processorGroupName);
		LoggedProductGroup memory = initMemoryAltGroup();
		LoggedProductGroup harddrive = bomService.createProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, harddriveGroupName);
		LoggedProductGroup ssd = bomService.createProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, ssdGroupName);

		addSubGroupToBOM(server, "Add Chassis", 1, "Chassis", chassis, 1, 1);
		addSubGroupToBOM(server, "Add Motherboard", 2, "motherboard", motherboard, 1, 1);
		addSubGroupToBOM(server, "Add Processor", 3, "Processor", processor, 1, 1);
		addSubGroupToBOM(server, "Add Memory", 4, "Memory", memory, 2, 2);
		addSubGroupToBOM(server, "Add Harddrive",5, "Harddrive", harddrive, 2, 2);
		addSubGroupToBOM(server, "Add SSD", 6, "SSD", ssd, 1, 1);
		
		addPartToAltGroup(processor, "Add Processor", 1, "Processor Part", processorPart);
		addPartToAltGroup(motherboard, "Add Motherboard part", 1, "Motherboard Part", motherboardPart);
		addPartToAltGroup(harddrive, "Add Hard drive part", 1, "HardDrive Part", harddrivePart);
		addPartToAltGroup(ssd, "Add SSD part", 1, "SSD Part", ssdPart);
		return server;
	}

	private ProductGroup initAChassisBOM() {
		LoggedProductGroup power = initPowerAltGroup();
		LoggedProductGroup screws = initScrewsAltGroup();

		LoggedProductGroup chassis =  initAChassisBOM(power, screws);
		return bomService.createProductGroupSnapshot(chassis, entryDate, "1.0");
	}

	private LoggedProductGroup initAChassisBOM(LoggedProductGroup power,
			LoggedProductGroup screws) {
		LoggedProductGroup chassis = bomService.createProductGroupLog(entryID, entryDate, "Init Test", GroupType.ASSEMBLY, chassisGroupName);

		addSubGroupToBOM(chassis, "Add Screw", 1, "Screw", screws, 5, 5);
		addSubGroupToBOM(chassis, "Add Power", 2, "Power", power, 1, 1);
		
		return chassis;
	}
	
	private LoggedProductGroup initMemoryAltGroup() {
		LoggedProductGroup memory = bomService.createProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, memoryGroupName);
		addPartToAltGroup(memory, "Add Memory part", 1, "Memory Part", memoryPart1);
		addPartToAltGroup(memory, "Add Memory part", 2, "Memory Part", memoryPart2);
		addPartToAltGroup(memory, "Add Memory part", 3, "Memory Part", memoryPart3);
		return memory;
	}
	
	private LoggedProductGroup initPowerAltGroup() {
		LoggedProductGroup power = bomService.createProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, powerGroupName);
		addPartToAltGroup(power, "Add Power", 1, "Power Part", powerPart);
		return power;
	}
	
	private LoggedProductGroup initScrewsAltGroup() {
		LoggedProductGroup screws = bomService.createProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, screwsGroupName);
		addPartToAltGroup(screws, "Add Screw1", 1, "screw1", screwAltPart1);
		addPartToAltGroup(screws, "Add Screw2", 2, "screw2", screwAltPart2);
		return screws;
	}

	private void addPartToAltGroup(LoggedProductGroup group, String comment, int lineNo,  String lineComment, UUID skuNo) {
		group.addGroupDetail(entryID, entryDate, comment, lineNo, MemberType.MATERIAL, lineComment, skuNo, 1, 1);
	}
	
	private void addSubGroupToBOM(LoggedProductGroup group, String comment, int lineNo,  String lineComment, LoggedProductGroup subGroup, int minBOMQty, int maxBOMQty) {
		group.addGroupDetail(entryID, entryDate, comment, lineNo, MemberType.SUB_GROUP, lineComment, subGroup.getGroupID(), minBOMQty, maxBOMQty);
	}
	
	@After
	public void tearDown() throws Exception {
	}

	private ContractPlan<MPSEntry> init1ChassisMPS() {
		LoggedContractMPSImpl mps = new LoggedContractMPSImpl(contract);
		ProductGroup chassis = initAChassisBOM();
		MPSEntry planEntry;
		try {
			planEntry = new MPSEntry(sdf.parse("01/02/2014"), sdf.parse("01/03/2014"), plant, 1, chassis.getGroupID());
			mps.addPlanEntry(planEntry, logEntry);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return mps;
	}
	
	private ContractPlan<MRPEntry> init1ChassisMRP() {
		LoggedContractMRPImpl mrp = new LoggedContractMRPImpl(contract);
		try {
			MRPEntry screwPlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant,5, screwAltPart1);
			MRPEntry powerPlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant, 10, powerPart);
			mrp.addPlanEntry(screwPlanEntry, logEntry);
			mrp.addPlanEntry(powerPlanEntry, logEntry);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return mrp;
	}
	
	@Test
	public void test1ChassisMPS() {
		ContractPlan<MPSEntry> mps = init1ChassisMPS();
		ContractPlan<MRPEntry> mrp = init1ChassisMRP();
		MPSReadinessCheckSolution unsolvedSolution = new MPSReadinessCheckSolution(mps, mrp, bomService);
		solver.solve(unsolvedSolution);
		MPSReadinessCheckSolution solvedSolution = (MPSReadinessCheckSolution)solver.getBestSolution();
		assertNotNull(solvedSolution);
		System.out.println("score=" + solvedSolution.getScore().getHardScore());
		assertTrue(solvedSolution.getScore().getHardScore() > 0);
	}

	private ContractPlan<MPSEntry> init2ChassisMPS() {
		LoggedContractMPSImpl mps = new LoggedContractMPSImpl(contract);
		ProductGroup chassis = initAChassisBOM();
		MPSEntry planEntry;
		try {
			planEntry = new MPSEntry(sdf.parse("01/02/2014"), sdf.parse("01/03/2014"), plant, 1, chassis.getGroupID());
			mps.addPlanEntry(planEntry, logEntry);
			planEntry = new MPSEntry(sdf.parse("01/05/2014"), sdf.parse("01/06/2014"), plant, 2, chassis.getGroupID());
			mps.addPlanEntry(planEntry, logEntry);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return mps;
	}
	
	private ContractPlan<MRPEntry> init2ChassisMRP() {
		LoggedContractMRPImpl mrp = new LoggedContractMRPImpl(contract);
		try {
			MRPEntry screwPlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant, 10, screwAltPart2);
			MRPEntry powerPlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant, 10, powerPart);
			mrp.addPlanEntry(screwPlanEntry, logEntry);
			mrp.addPlanEntry(powerPlanEntry, logEntry);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return mrp;
	}
	
	@Test
	public void test2ChassisMPS() {
		ContractPlan<MPSEntry> mps = init2ChassisMPS();
		ContractPlan<MRPEntry> mrp = init2ChassisMRP();
		MPSReadinessCheckSolution unsolvedSolution = new MPSReadinessCheckSolution(mps, mrp, bomService);
		solver.solve(unsolvedSolution);
		MPSReadinessCheckSolution solvedSolution = (MPSReadinessCheckSolution)solver.getBestSolution();
		assertNotNull(solvedSolution);
		System.out.println("score=" + solvedSolution.getScore().getHardScore());
		assertTrue(solvedSolution.getScore().getHardScore() > 0);
	}

	private ContractPlan<MPSEntry> init1ServerMPS() {
		LoggedContractMPSImpl mps = new LoggedContractMPSImpl(contract);
		ProductGroup server = initAServerBOM();
		MPSEntry planEntry;
		try {
			planEntry = new MPSEntry(sdf.parse("01/02/2014"), sdf.parse("01/03/2014"), plant, 1, server.getGroupID());
			mps.addPlanEntry(planEntry, logEntry);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return mps;
	}
	
	private ContractPlan<MRPEntry> init1ServerMRP() {
		LoggedContractMRPImpl mrp = new LoggedContractMRPImpl(contract);
		try {
			MRPEntry screwPlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant, 10, screwAltPart1);
			MRPEntry powerPlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant, 10, powerPart);
			MRPEntry motherboardPlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant, 1, motherboardPart);
			MRPEntry processorPlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant, 1, processorPart);
			//MRPEntry memoryPlanEntry1 = new MRPEntry(sdf.parse("01/02/2014"),  plant, 2, memoryPart1);
			MRPEntry memoryPlanEntry2 = new MRPEntry(sdf.parse("01/02/2014"),  plant, 2, memoryPart2);
			MRPEntry harddrivePlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant, 2, harddrivePart);
			MRPEntry ssdPlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant, 1, ssdPart);
			mrp.addPlanEntry(screwPlanEntry, logEntry);
			mrp.addPlanEntry(powerPlanEntry, logEntry);
			mrp.addPlanEntry(motherboardPlanEntry, logEntry);
			mrp.addPlanEntry(processorPlanEntry, logEntry);
			//mrp.addPlanEntry(memoryPlanEntry1, logEntry);
			mrp.addPlanEntry(memoryPlanEntry2, logEntry);
			mrp.addPlanEntry(harddrivePlanEntry, logEntry);
			mrp.addPlanEntry(ssdPlanEntry, logEntry);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return mrp;
	}
	
	@Test
	public void test1ServerMPS() {
		ContractPlan<MPSEntry> mps = init1ServerMPS();
		ContractPlan<MRPEntry> mrp = init1ServerMRP();
		MPSReadinessCheckSolution unsolvedSolution = new MPSReadinessCheckSolution(mps, mrp, bomService);
		solver.solve(unsolvedSolution);
		MPSReadinessCheckSolution solvedSolution = (MPSReadinessCheckSolution)solver.getBestSolution();
		assertNotNull(solvedSolution);
		System.out.println("score=" + solvedSolution.getScore().getHardScore());
		assertTrue(solvedSolution.getScore().getHardScore() > 0);
	}

	private ContractPlan<MPSEntry> init2ServerMPS() {
		LoggedContractMPSImpl mps = new LoggedContractMPSImpl(contract);
		ProductGroup server = initAServerBOM();
		MPSEntry planEntry;
		try {
			planEntry = new MPSEntry(sdf.parse("01/02/2014"), sdf.parse("01/03/2014"), plant, 2, server.getGroupID());
			mps.addPlanEntry(planEntry, logEntry);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return mps;
	}
	
	private ContractPlan<MRPEntry> init2ServerMRP() {
		LoggedContractMRPImpl mrp = new LoggedContractMRPImpl(contract);
		try {
			MRPEntry screw1PlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant,5, screwAltPart1);
			MRPEntry screw2PlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant, 5, screwAltPart2);
			MRPEntry powerPlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant, 10, powerPart);
			MRPEntry motherboardPlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant, 2, motherboardPart);
			MRPEntry processorPlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant, 2, processorPart);
			MRPEntry memoryPlanEntry1 = new MRPEntry(sdf.parse("01/02/2014"),  plant, 2, memoryPart1);
			MRPEntry memoryPlanEntry2 = new MRPEntry(sdf.parse("01/02/2014"),  plant, 2, memoryPart2);
			MRPEntry harddrivePlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant, 4, harddrivePart);
			MRPEntry ssdPlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant, 2, ssdPart);
			mrp.addPlanEntry(screw1PlanEntry, logEntry);
			mrp.addPlanEntry(screw2PlanEntry, logEntry);
			mrp.addPlanEntry(powerPlanEntry, logEntry);
			mrp.addPlanEntry(motherboardPlanEntry, logEntry);
			mrp.addPlanEntry(processorPlanEntry, logEntry);
			mrp.addPlanEntry(memoryPlanEntry1, logEntry);
			mrp.addPlanEntry(memoryPlanEntry2, logEntry);
			mrp.addPlanEntry(harddrivePlanEntry, logEntry);
			mrp.addPlanEntry(ssdPlanEntry, logEntry);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return mrp;
	}

	@Test
	public void test2ServerMPS() {
		ContractPlan<MPSEntry> mps = init2ServerMPS();
		ContractPlan<MRPEntry> mrp = init2ServerMRP();
		MPSReadinessCheckSolution unsolvedSolution = new MPSReadinessCheckSolution(mps, mrp, bomService);
		solver.solve(unsolvedSolution);
		MPSReadinessCheckSolution solvedSolution = (MPSReadinessCheckSolution)solver.getBestSolution();
		assertNotNull(solvedSolution);
		System.out.println("score=" + solvedSolution.getScore().getHardScore());
		assertTrue(solvedSolution.getScore().getHardScore() > 0);
	}
	
	@Test 
	public void testUUID() {
		UUID a = new UUID(12345678, 87654321);
		UUID b = new UUID(12345678, 87654321);
		UUID c = new UUID(11111111, 22222222);
		
		System.out.println("a=" + a);
		System.out.println("b=" + b);
		System.out.println("c=" + c);

		assertTrue(a == a); // returns true
		assertTrue(a.equals(a)); // returns true

		assertFalse(a == b); // returns false
		assertTrue(a.equals(b)); // returns true

		assertFalse(a == c); // returns false
		assertFalse(a.equals(c)); //return false
	}
}
