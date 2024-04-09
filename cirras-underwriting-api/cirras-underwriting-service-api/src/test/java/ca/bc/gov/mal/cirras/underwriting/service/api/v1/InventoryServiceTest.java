package ca.bc.gov.mal.cirras.underwriting.service.api.v1;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.impl.CirrasInventoryServiceImpl;


public class InventoryServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(InventoryServiceTest.class);
	
	private enum inventoryType {
		GrainUnseeded,
		ForageSeeded
	}

	@Test
	public void testHandleDelete() throws Exception {
		logger.debug("<testHandleDelete");

		CirrasInventoryServiceImpl invService = new CirrasInventoryServiceImpl();
		AnnualFieldRsrc field = new AnnualFieldRsrc();
		
		InventoryField planting1 = getInventoryFieldAndPlanting(1, false, inventoryType.GrainUnseeded);
		InventoryField planting2 = getInventoryFieldAndPlanting(2, true, inventoryType.GrainUnseeded);
		InventoryField planting3 = getInventoryFieldAndPlanting(3, false, inventoryType.GrainUnseeded);
		InventoryField planting4 = getInventoryFieldAndPlanting(4, true, inventoryType.GrainUnseeded);
		InventoryField planting5 = getInventoryFieldAndPlanting(5, null, inventoryType.GrainUnseeded);
		
		List<InventoryField> plantings = new ArrayList<InventoryField>();
		plantings.add(planting1);
		plantings.add(planting2);
		plantings.add(planting3);
		plantings.add(planting4);
		plantings.add(planting5);
		
		field.setPlantings(plantings);

		invService.handleDeletedPlantings(field);
		
		Integer i = 1;
		
		for (InventoryField inventoryField : field.getPlantings()) {

			logger.debug(inventoryField.getInventoryFieldGuid() + " is deleted: " + inventoryField.getInventoryUnseeded().getDeletedByUserInd() + " has planting number: " + inventoryField.getPlantingNumber());

			//Test if the planting number of the remaining is in sequence
			if(inventoryField.getInventoryUnseeded().getDeletedByUserInd() == null || inventoryField.getInventoryUnseeded().getDeletedByUserInd() == false) {
				Assert.assertEquals("Wrong planting number for: " + inventoryField.getInventoryFieldGuid(), i, inventoryField.getPlantingNumber());
				i += 1;
			}
		}
		
		logger.debug(">testHandleDelete");
		 		
	}
	
	@Test
	public void testHandleDeleteForageSeeded() throws Exception {
		logger.debug("<testHandleDeleteForageSeeded");

		CirrasInventoryServiceImpl invService = new CirrasInventoryServiceImpl();
		AnnualFieldRsrc field = new AnnualFieldRsrc();
		
		InventoryField planting1 = getInventoryFieldAndPlanting(1, false, inventoryType.ForageSeeded);
		InventoryField planting2 = getInventoryFieldAndPlanting(2, true, inventoryType.ForageSeeded);
		InventoryField planting3 = getInventoryFieldAndPlanting(3, false, inventoryType.ForageSeeded);
		InventoryField planting4 = getInventoryFieldAndPlanting(4, true, inventoryType.ForageSeeded);
		InventoryField planting5 = getInventoryFieldAndPlanting(5, null, inventoryType.ForageSeeded);
		
		List<InventoryField> plantings = new ArrayList<InventoryField>();
		plantings.add(planting1);
		plantings.add(planting2);
		plantings.add(planting3);
		plantings.add(planting4);
		plantings.add(planting5);
		
		field.setPlantings(plantings);

		invService.handleDeletedPlantings(field);
		
		Integer i = 1;
		
		for (InventoryField inventoryField : field.getPlantings()) {

			logger.debug(inventoryField.getInventoryFieldGuid() + " is deleted: " + inventoryField.getInventorySeededForages().get(0).getDeletedByUserInd() + " has planting number: " + inventoryField.getPlantingNumber());

			//Test if the planting number of the remaining is in sequence
			if(inventoryField.getInventorySeededForages().get(0).getDeletedByUserInd() == null || inventoryField.getInventorySeededForages().get(0).getDeletedByUserInd() == false) {
				Assert.assertEquals("Wrong planting number for: " + inventoryField.getInventoryFieldGuid(), i, inventoryField.getPlantingNumber());
				i += 1;
			}
		}
		
		logger.debug(">testHandleDeleteForageSeeded");
		 		
	}
	
	private InventoryField getInventoryFieldAndPlanting(Integer plantingNumber, Boolean deletedByUserInd, inventoryType invType) {
		InventoryField planting = new InventoryField();
		planting.setPlantingNumber(plantingNumber);
		planting.setInventoryFieldGuid("planting" + plantingNumber);
		
		switch (invType) {
		case GrainUnseeded:
			planting.setInventoryUnseeded(getInventoryGrainUnseeded(deletedByUserInd));
			break;
		case ForageSeeded:
			InventorySeededForage invSeededForage = getInventoryForageSeeded(deletedByUserInd);
			List<InventorySeededForage> inventorySeededForages = new ArrayList<InventorySeededForage>();
			inventorySeededForages.add(invSeededForage);
			planting.setInventorySeededForages(inventorySeededForages);
			break;
		}
		
		return planting;
	}
	
	private InventoryUnseeded getInventoryGrainUnseeded(Boolean deletedByUserInd) {
		InventoryUnseeded invUnseeded = new InventoryUnseeded();
		invUnseeded.setDeletedByUserInd(deletedByUserInd);
		
		return invUnseeded;
	}
	
	private InventorySeededForage getInventoryForageSeeded(Boolean deletedByUserInd) {
		InventorySeededForage invSeeded = new InventorySeededForage();
		invSeeded.setDeletedByUserInd(deletedByUserInd);
		
		return invSeeded;
	}
	
	 
}
