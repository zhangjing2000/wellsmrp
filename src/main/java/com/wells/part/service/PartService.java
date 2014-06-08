package com.wells.part.service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.wells.bom.snapshot.ProductGroupSnapshot;
import com.wells.part.concept.AssemblyFinishGood;
import com.wells.part.concept.MaterialItem;
import com.wells.part.concept.Part;

public interface PartService {
	Part findPart(UUID partNo);
	MaterialItem createMaterialItem(String partDesc);
	AssemblyFinishGood createFinishGood(String partDesc, 
			ProductGroupSnapshot bom, Map<UUID, Integer> components);
	Set<AssemblyFinishGood> getAllFinishGoodsForBOM(ProductGroupSnapshot bom);
}
