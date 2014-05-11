package com.wells.plan.service.optaplanner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.simple.SimpleScoreCalculator;

import com.wells.bom.concept.MemberType;
import com.wells.plan.concept.ProductionPlant;
import com.wells.plan.mrp.MRPEntry;

public class MPSReadinessScoreCalculator implements SimpleScoreCalculator<MPSReadinessCheckSolution>{

	public HardSoftScore calculateScore(MPSReadinessCheckSolution solution) {
		//System.out.println("start calculateScore Called, solution=" + solution);
		int  hardScore = 0, softScore = 0;
		Map<ProductionPlant, Map<Date, List<FixedPlanEntry>>> fulfilled = solution.getFulfilledMPS();
		Map<PlanEntryIndex, MRPEntry> mrp = solution.getAccumulatedMRP();
		Map<ProductionPlant, Map<Date, Map<Integer, Integer>>> aggregatecDemands = aggregateDemands(fulfilled);
		for (Map.Entry<ProductionPlant, Map<Date, Map<Integer, Integer>>> fulfilledByLocEntry: aggregatecDemands.entrySet()) {
			ProductionPlant fulfilledLoc = fulfilledByLocEntry.getKey();
			Map<Date, Map<Integer, Integer>> fulfilledByLoc = fulfilledByLocEntry.getValue();
			for (Map.Entry<Date, Map<Integer, Integer>> fulfilledByDateEntry: fulfilledByLoc.entrySet()) {
				Date fulfilledDate = fulfilledByDateEntry.getKey();
				Map<Integer, MRPEntry> mrpPlan = findMRPSupportByFulfilledDate(mrp, fulfilledLoc, fulfilledDate);
				if (mrpPlan == null) {
					hardScore = -10;
					//System.out.println("no mrp plan, date=" + fulfilledDate);
					break;
				}
				Map<Integer, Integer> skuDemands = fulfilledByDateEntry.getValue();
				for (Map.Entry<Integer, Integer> skuDemand: skuDemands.entrySet()) {
					System.out.println("skuDemand:" + skuDemand);
					int skuNo = skuDemand.getKey();
					int mpsQty = skuDemand.getValue();
					//System.out.println("calculateScore Called, skuNo=" + skuNo + ",mpsQty =" + mpsQty);
					if (mpsQty == 0) {
						hardScore++;
						//System.out.println("calculateScore no demand, hardscore:" + hardScore);
						continue;
					}
					MRPEntry mrpEntry = mrpPlan.get(skuNo);
					if (mrpEntry == null) {
						hardScore=-10;
						//System.out.println("calculateScore no mrp, hardscore:" + hardScore + ",sku=" + skuDemand.getKey());
						break;
					}
					
					if (mrpEntry.getPlanQty() >= mpsQty) {
						hardScore++;
						//System.out.println("calculateScore mrp meet demand, hardscore:" + hardScore + ",mrpEntry=" + mrpEntry);
					} else { 
						hardScore = -1;
						//System.out.println("calculateScore mrp less than demand, hardscore:" + hardScore + ",mrpEntry=" + mrpEntry);
						break;
					}
				}
				if (hardScore < 0) break;
			}
		}
		//System.out.println("end calculateScore Called, hardScore=" + hardScore + ",solution=" + solution);
		//System.out.println("");
		return HardSoftScore.valueOf(hardScore, softScore);
	}
	
	private Map<ProductionPlant, Map<Date, Map<Integer, Integer>>> aggregateDemands(
			Map<ProductionPlant, Map<Date, List<FixedPlanEntry>>> fulfilled) {
		Map <ProductionPlant, Map<Date, Map<Integer, Integer>>> demands = new HashMap<ProductionPlant, Map<Date, Map<Integer, Integer>>>();
		for (Map.Entry<ProductionPlant, Map<Date, List<FixedPlanEntry>>> fulfilledByLocEntry: fulfilled.entrySet()) {
			ProductionPlant fulfilledLoc = fulfilledByLocEntry.getKey();
			Map<Date, Map<Integer, Integer>> demandsByLoc = demands.get(fulfilledLoc);
			if (demandsByLoc == null) {
				demandsByLoc = new HashMap<Date, Map<Integer, Integer>>();
				demands.put(fulfilledLoc, demandsByLoc);
			}
			Map<Date, List<FixedPlanEntry>> fulfilledByLoc = fulfilledByLocEntry.getValue();
			for (Date fulfillDate : fulfilledByLoc.keySet()) {
				Map<Integer, Integer> skuDemand = demandsByLoc.get(fulfillDate);
				if (skuDemand == null) {
					skuDemand = new HashMap<Integer, Integer>();
					demandsByLoc.put(fulfillDate, skuDemand);
				}
			}
		}
		for (Map.Entry<ProductionPlant, Map<Date, Map<Integer, Integer>>> demandsByLocEntry: demands.entrySet()) {
			ProductionPlant plant = demandsByLocEntry.getKey();
			Map<Date, List<FixedPlanEntry>> fulfilledByLoc = fulfilled.get(plant);
			Map<Date, Map<Integer, Integer>> demandsByLoc = demands.get(plant);
			for (Map.Entry<Date, Map<Integer, Integer>> demandByDate: demandsByLoc.entrySet()) {
				Date demandDate = demandByDate.getKey();
				Map<Integer, Integer> skuDemand = demandByDate.getValue();
				for (Map.Entry<Date, List<FixedPlanEntry>> fulfilledByDateEntry: fulfilledByLoc.entrySet()) {
					Date fulfillDate = fulfilledByDateEntry.getKey();
					if (fulfillDate.after(demandDate)) continue;
					List<FixedPlanEntry> fulfilledPlan = fulfilledByDateEntry.getValue();
					for (FixedPlanEntry fixedPlanEntry: fulfilledPlan) {
						if (fixedPlanEntry.getItemType() == MemberType.MATERIAL) {
							int skuNo = fixedPlanEntry.getSkuNo();
							Integer planQty = skuDemand.get(skuNo);
							if (planQty == null) planQty = 0;
							planQty += fixedPlanEntry.getPlanQty();
							skuDemand.put(skuNo, planQty);
						}
					}
				}
			}
		}
		return demands;
	}
	
	private Map<Integer, MRPEntry> findMRPSupportByFulfilledDate(
			Map<PlanEntryIndex, MRPEntry> mrp, ProductionPlant plant, Date fulfilledDate) {
		Map<Integer, MRPEntry> mrpSkuMap = new HashMap<Integer, MRPEntry>();
		for (MRPEntry mrpEntry: mrp.values()) {
			ProductionPlant thisMRPPlant = mrpEntry.getPlanLocation();
			if (!thisMRPPlant.equals(plant)) continue;
			Date thisMRPDate = mrpEntry.getPlanDate();
			if (thisMRPDate.after(fulfilledDate)) continue;
			MRPEntry skuMRPEntry = mrpSkuMap.get(mrpEntry.getSkuNo());
			if (skuMRPEntry == null) {
				skuMRPEntry = mrpEntry;
			} else {
				skuMRPEntry = new MRPEntry(mrpEntry.getPlanDate(), mrpEntry.getPlanLocation(), mrpEntry.getPlanQty() + skuMRPEntry.getPlanQty(), mrpEntry.getSkuNo());
			}
			mrpSkuMap.put(mrpEntry.getSkuNo(),  skuMRPEntry);
		}
		return mrpSkuMap;
	}

}
