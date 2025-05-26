package dao;

import org.junit.Assert;
import org.junit.Test;

import model.ScreeningRoom;
import model.Schedule;

public class ScreeningRoomDAOTest {
	ScreeningRoomDAO screeningRoomDAO = new ScreeningRoomDAO();
	
	@Test
	public void testGetScreeningRoomByIdException1() {
		Schedule schedule = new Schedule();
		ScreeningRoom invalidRoom = new ScreeningRoom();
		invalidRoom.setId(-999);
		schedule.setScreeningRoom(invalidRoom);
		
		ScreeningRoom room = screeningRoomDAO.getScreeningRoomById(schedule);
		Assert.assertNull(room);
		return;
	}
	
	@Test
	public void testGetScreeningRoomByIdException2() {
		Schedule schedule = new Schedule();
		ScreeningRoom invalidRoom = new ScreeningRoom();
		invalidRoom.setId(0);
		schedule.setScreeningRoom(invalidRoom);
		
		ScreeningRoom room = screeningRoomDAO.getScreeningRoomById(schedule);
		Assert.assertNull(room);
		return;
	}
	
	@Test
	public void testGetScreeningRoomByIdStandard1() {
		Schedule schedule = new Schedule();
		ScreeningRoom validRoom = new ScreeningRoom();
		validRoom.setId(1);
		schedule.setScreeningRoom(validRoom);
		
		ScreeningRoom room = screeningRoomDAO.getScreeningRoomById(schedule);
		Assert.assertNotNull(room);
		Assert.assertEquals(1, room.getId());
		Assert.assertNotNull(room.getRoomName());
		Assert.assertTrue(room.getNumberOfSeats() > 0);
		Assert.assertNotNull(room.getRoomType());
		return;
	}
	
	@Test
	public void testGetScreeningRoomByIdStandard2() {
		Schedule schedule = new Schedule();
		ScreeningRoom validRoom = new ScreeningRoom();
		validRoom.setId(2);
		schedule.setScreeningRoom(validRoom);
		
		ScreeningRoom room = screeningRoomDAO.getScreeningRoomById(schedule);
		Assert.assertNotNull(room);
		Assert.assertEquals(2, room.getId());
		Assert.assertNotNull(room.getRoomName());
		Assert.assertTrue(room.getNumberOfSeats() > 0);
		Assert.assertNotNull(room.getRoomType());
		return;
	}
} 