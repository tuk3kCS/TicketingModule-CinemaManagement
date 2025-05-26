package dao;

import java.sql.Connection;
import org.junit.Assert;
import org.junit.Test;

import model.User;

public class UserDAOTest {
	UserDAO userDAO = new UserDAO();
	
	@Test
	public void testCheckLoginException1() {
		User user = new User();
		user.setUsername("invaliduser");
		user.setPassword("invalidpassword");
		
		boolean result = userDAO.checkLogin(user);
		Assert.assertFalse(result);
		Assert.assertEquals(0, user.getId());
		return;
	}
	
	@Test
	public void testCheckLoginException2() {
		User user = new User();
		user.setUsername("");
		user.setPassword("");
		
		boolean result = userDAO.checkLogin(user);
		Assert.assertFalse(result);
		Assert.assertEquals(0, user.getId());
		return;
	}
	
	@Test
	public void testCheckLoginStandard1() {
		Connection con = DAO.con;
		try {
			con.setAutoCommit(false);
			
			User user = new User();
			user.setUsername("admin");
			user.setPassword("admin");
			
			boolean result = userDAO.checkLogin(user);
			Assert.assertTrue(result);
			Assert.assertTrue(user.getId() > 0);
			Assert.assertNotNull(user.getFullName());
			Assert.assertNotNull(user.getRole());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Test failed with exception: " + e.getMessage());
		} finally {
			try {
				con.rollback();
				con.setAutoCommit(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return;
	}
	
	@Test
	public void testCheckLoginStandard2() {
		Connection con = DAO.con;
		try {
			con.setAutoCommit(false);
			
			User user = new User();
			user.setUsername("user");
			user.setPassword("user");
			
			boolean result = userDAO.checkLogin(user);
			
			if (result) {
				Assert.assertTrue(user.getId() > 0);
				Assert.assertNotNull(user.getFullName());
				Assert.assertNotNull(user.getRole());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Test failed with exception: " + e.getMessage());
		} finally {
			try {
				con.rollback();
				con.setAutoCommit(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return;
	}
} 