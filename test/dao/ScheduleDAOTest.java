package dao;

import org.junit.Assert;
import org.junit.Test;

import model.Schedule;
import model.Showtime;

public class ScheduleDAOTest {
	ScheduleDAO scheduleDAO = new ScheduleDAO();
	
	@Test
	public void testGetScheduleByIdException1() {
		Showtime invalidShowtime = new Showtime();
		invalidShowtime.setId(-999);
		
		Schedule schedule = scheduleDAO.getScheduleById(invalidShowtime);
		Assert.assertNull(schedule);
		return;
	}
	
	@Test
	public void testGetScheduleByIdException2() {
		Showtime invalidShowtime = new Showtime();
		invalidShowtime.setId(0);

		Schedule schedule = scheduleDAO.getScheduleById(invalidShowtime);
		Assert.assertNull(schedule);
		return;
	}
	
	@Test
	public void testGetScheduleByIdStandard1() {
		Showtime validShowtime = new Showtime();
		validShowtime.setId(1);
		
		Schedule schedule = scheduleDAO.getScheduleById(validShowtime);
		Assert.assertNotNull(schedule);
		Assert.assertTrue(schedule.getId() > 0);
		Assert.assertNotNull(schedule.getDate());
		
		// Verify related objects
		Assert.assertNotNull(schedule.getMovie());
		Assert.assertTrue(schedule.getMovie().getId() > 0);
		
		Assert.assertNotNull(schedule.getShowtime());
		Assert.assertEquals(validShowtime.getId(), schedule.getShowtime().getId());
		
		Assert.assertNotNull(schedule.getScreeningRoom());
		Assert.assertTrue(schedule.getScreeningRoom().getId() > 0);
		return;
	}
	
	@Test
	public void testGetScheduleByIdStandard2() {
		Showtime validShowtime = new Showtime();
		validShowtime.setId(2);
		
		Schedule schedule = scheduleDAO.getScheduleById(validShowtime);
		if (schedule != null) {
			Assert.assertTrue(schedule.getId() > 0);
			Assert.assertNotNull(schedule.getDate());
			
			Assert.assertNotNull(schedule.getMovie());
			Assert.assertTrue(schedule.getMovie().getId() > 0);
			
			Assert.assertNotNull(schedule.getShowtime());
			Assert.assertEquals(validShowtime.getId(), schedule.getShowtime().getId());
			
			Assert.assertNotNull(schedule.getScreeningRoom());
			Assert.assertTrue(schedule.getScreeningRoom().getId() > 0);
		}
		return;
	}
} 