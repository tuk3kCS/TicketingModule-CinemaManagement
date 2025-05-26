package dao;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

import model.FoodItem;

public class FoodItemDAOTest {
	FoodItemDAO foodItemDAO = new FoodItemDAO();
	
	@Test
	public void testSearchFoodItemByKeywordException1() {
		String keyword = "xxxxxxxxxx";
		List<FoodItem> items = foodItemDAO.searchFoodItemByKeyword(keyword);
		Assert.assertNotNull(items);
		Assert.assertEquals(0, items.size());
		return;
	}
	
	@Test
	public void testSearchFoodItemByKeywordException2() {
		String keyword = "";
		List<FoodItem> items = foodItemDAO.searchFoodItemByKeyword(keyword);
		Assert.assertNotNull(items);
		Assert.assertTrue(items.size() > 0);
		return;
	}
	
	@Test
	public void testSearchFoodItemByKeywordStandard1() {
		String keyword = "popcorn";
		List<FoodItem> items = foodItemDAO.searchFoodItemByKeyword(keyword);
		Assert.assertNotNull(items);
		Assert.assertTrue(items.size() > 0);
		for(int i=0; i<items.size(); i++){
			Assert.assertTrue(items.get(i).getName().toLowerCase().contains(keyword.toLowerCase()));
			Assert.assertTrue(items.get(i).getId() > 0);
            Assert.assertNotNull(items.get(i).getSize());
            Assert.assertTrue(items.get(i).getUnitPrice() >= 0);
		}
		return;
	}
	
	@Test
	public void testSearchFoodItemByKeywordStandard2() {
		String keyword = "coke";
		List<FoodItem> items = foodItemDAO.searchFoodItemByKeyword(keyword);
		Assert.assertNotNull(items);
		Assert.assertTrue(items.size() > 0);
		for(int i=0; i<items.size(); i++){
			Assert.assertTrue(items.get(i).getName().toLowerCase().contains(keyword.toLowerCase()));
		}
		return;
	}
} 