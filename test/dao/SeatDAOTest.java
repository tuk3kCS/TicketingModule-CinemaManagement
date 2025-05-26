package dao;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

import model.Seat;
import model.ScreeningRoom;

public class SeatDAOTest {
	SeatDAO seatDAO = new SeatDAO();
	
	@Test
	public void testGetSeatsByScreeningRoomException1() {
		ScreeningRoom invalidRoom = new ScreeningRoom();
		invalidRoom.setId(-999);
		
		List<Seat> seats = seatDAO.getSeatsByScreeningRoom(invalidRoom);
		Assert.assertNotNull(seats);
		Assert.assertEquals(0, seats.size());
		return;
	}
	
	@Test
	public void testGetSeatsByScreeningRoomException2() {
		ScreeningRoom invalidRoom = new ScreeningRoom();
		invalidRoom.setId(0);
		
		List<Seat> seats = seatDAO.getSeatsByScreeningRoom(invalidRoom);
		Assert.assertNotNull(seats);
		Assert.assertEquals(0, seats.size());
		return;
	}
	
	@Test
	public void testGetSeatsByScreeningRoomStandard1() {
		ScreeningRoom validRoom = new ScreeningRoom();
		validRoom.setId(1);
		
		List<Seat> seats = seatDAO.getSeatsByScreeningRoom(validRoom);
		Assert.assertNotNull(seats);
		Assert.assertTrue(seats.size() > 0);
		
		for(int i=0; i<seats.size(); i++) {
			Assert.assertTrue(seats.get(i).getId() > 0);
			Assert.assertNotNull(seats.get(i).getSeatNumber());
			Assert.assertEquals(validRoom.getId(), seats.get(i).getScreeningRoomId());
		}
		return;
	}
	
	@Test
	public void testGetSeatsByScreeningRoomStandard2() {
		ScreeningRoom validRoom = new ScreeningRoom();
		validRoom.setId(2);
		
		List<Seat> seats = seatDAO.getSeatsByScreeningRoom(validRoom);
		Assert.assertNotNull(seats);
		Assert.assertTrue(seats.size() > 0);
		
		for(int i=0; i<seats.size(); i++) {
			Assert.assertTrue(seats.get(i).getId() > 0);
			Assert.assertNotNull(seats.get(i).getSeatNumber());
			Assert.assertEquals(validRoom.getId(), seats.get(i).getScreeningRoomId());
		}
		return;
	}
} 