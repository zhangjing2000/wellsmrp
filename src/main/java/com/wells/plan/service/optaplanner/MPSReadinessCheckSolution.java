package com.wells.plan.service.optaplanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.solution.Solution;

import com.wells.bom.concept.GroupType;
import com.wells.bom.concept.ProductGroup;
import com.wells.bom.concept.ProductGroupMember;
import com.wells.bom.concept.MemberType;
import com.wells.bom.service.ProductGroupService;
import com.wells.plan.concept.ContractPlan;
import com.wells.plan.concept.ProductionPlant;
import com.wells.plan.mps.MPSEntry;
import com.wells.plan.mrp.MRPEntry;

@PlanningSolution
public class MPSReadinessCheckSolution implements Solution<HardSoftScore> {

	private ContractPlan<MPSEntry> mps;
	private  ContractPlan<MRPEntry> mrp;
	private ProductGroupService bomService;
	
	private final Map<ProductionPlant, Map<Date, List<FixedPlanEntry>>> fulfilledMPS;
	private final Map<PlanEntryIndex, MRPEntry> accumulatedMRP;
	
	private HardSoftScore score;
	
	public  MPSReadinessCheckSolution() {
		this.fulfilledMPS = new HashMap<ProductionPlant, Map<Date, List<FixedPlanEntry>>>();
		this.accumulatedMRP = new HashMap<PlanEntryIndex, MRPEntry>();
	}
	
	public MPSReadinessCheckSolution(ContractPlan<MPSEntry> mps, ContractPlan<MRPEntry> mrp, ProductGroupService bomService) {
		this.mps = mps;
		this.mrp = mrp;
		this.bomService = bomService;
		this.fulfilledMPS = new HashMap<ProductionPlant, Map<Date, List<FixedPlanEntry>>>();
		this.accumulatedMRP = new HashMap<PlanEntryIndex, MRPEntry>();
		explodeMPS();
		accumulateMRP();
	}
	
	private void explodeMPS() {
		for (MPSEntry mpsEntry:mps.getLatestPlan()) {
			FixedPlanEntry fixedPlanEntry = new FixedPlanEntry(MemberType.SUB_GROUP, null, mpsEntry.getCustBOM(), 0, 
									mpsEntry.getPlanDate(), mpsEntry.getPlanQty(), mpsEntry.getPlanLocation());
			explodeFixedPlanEntry(fixedPlanEntry);
		}
	}

	private void explodeFixedPlanEntry(FixedPlanEntry fixedPlanEntry) {
		//System.out.println("explodeFixedPlanEntry:"+ fixedPlanEntry);
		ProductionPlant plant = fixedPlanEntry.getPlanLocation();
		Map<Date, List<FixedPlanEntry>> fulfilledByPlant = fulfilledMPS.get(plant);
		if (fulfilledByPlant == null) {
			fulfilledByPlant = new HashMap<Date, List<FixedPlanEntry>>();
			fulfilledMPS.put(plant,  fulfilledByPlant);
		}
		Date fulfilledDate = fixedPlanEntry.getPlanDate();
		List<FixedPlanEntry> fulfilledByDate = fulfilledByPlant.get(fulfilledDate);
		if (fulfilledByDate == null) {
			fulfilledByDate = new ArrayList<FixedPlanEntry>();
			fulfilledByPlant.put(fulfilledDate, fulfilledByDate);
		}
		// each node in a bom should have a record in the fulfilledByDate. 
		// Two nodes with same sku or sub bom but different parent are treated as different node, each should have a record 
		if (!fulfilledByDate.contains(fixedPlanEntry)) {
			fulfilledByDate.add(fixedPlanEntry);
		}
		UUID groupID = fixedPlanEntry.getGroupID();
		if (groupID == null) return;
		ProductGroup group = bomService.getProductGroupSnapshot(groupID);
		int groupSize = group.getGroupDetails().size();
		int i=0;
		for (ProductGroupMember detail:group.getGroupDetails()) {
			FixedPlanEntry entry = null;
			if (group.getGroupType() == GroupType.ALTERNATIVE) {
				if (groupSize == 1) {
					entry = new FixedPlanEntry(detail, fixedPlanEntry, fulfilledDate, plant);
				} else if (++i == groupSize) {
					entry = new EndAltPartPlanEntry(detail, fixedPlanEntry, fulfilledDate, plant);
				} else {
					entry = new FulfilledPlanEntry(detail, fixedPlanEntry, fulfilledDate, plant);
				}
			} else {
				entry = new FixedPlanEntry(detail, fixedPlanEntry, fulfilledDate, plant); 
			}
			if (entry != null) explodeFixedPlanEntry(entry);
		}
	}
	
	private void accumulateMRP() {
		for (MRPEntry entry:mrp.getLatestPlan()) {
			PlanEntryIndex idx = new PlanEntryIndex(entry.getPlanLocation(), entry.getPlanDate(), entry.getSkuNo());
			MRPEntry accEntry = accumulatedMRP.get(idx);
			if (accEntry == null) {
				accEntry = entry;
			} else {
				accEntry = new MRPEntry(idx.getPlanDate(), idx.getPlanLocation(), idx.getSkuNo(), entry.getPlanQty() + accEntry.getPlanQty());
			}
			accumulatedMRP.put(idx, entry);
		}
	}
	
	public ContractPlan<MPSEntry> getMps() {
		return mps;
	}

	public ContractPlan<MRPEntry> getMrp() {
		return mrp;
	}

	@PlanningEntityCollectionProperty
	public List<FulfilledPlanEntry> getFulfilledPlanEntryList() {
		List<FulfilledPlanEntry> list = new ArrayList<FulfilledPlanEntry>();
		for (Map<Date, List<FixedPlanEntry>> fulfilledByLoc: fulfilledMPS.values()) {
			for (List<? extends FixedPlanEntry> fulfilledByDate:fulfilledByLoc.values()) {
				for (FixedPlanEntry fulfilledEntry:fulfilledByDate) {
					if (fulfilledEntry instanceof FulfilledPlanEntry) {
						list.add((FulfilledPlanEntry)fulfilledEntry);
					}
				}
			}
		}
		
		return list;
	}
	
	public Map<ProductionPlant, Map<Date, List<FixedPlanEntry>>> getFulfilledMPS() {
		return fulfilledMPS;
	}

	public Map<PlanEntryIndex, MRPEntry> getAccumulatedMRP() {
		return accumulatedMRP;
	}

	public Collection<? extends Object> getProblemFacts() {
		return null;
	}

	public HardSoftScore getScore() {
		return score;
	}

	public void setScore(HardSoftScore score) {
		this.score = score;
	}
}
