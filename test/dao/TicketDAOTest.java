package dao;

import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

import model.Schedule;
import model.ScreeningRoom;
import model.Ticket;

public class TicketDAOTest {
	TicketDAO ticketDAO = new TicketDAO();
	
	@Test
	public void testGetBookedSeatsIdsException1() {
		Ticket ticket = new Ticket();
		Schedule schedule = new Schedule();
		schedule.setId(-999);
		ScreeningRoom screeningRoom = new ScreeningRoom();
		screeningRoom.setId(1);
		schedule.setScreeningRoom(screeningRoom);
		ticket.setSchedule(schedule);
		
		Map<Integer, Integer> seatStatusMap = ticketDAO.getBookedSeatsIds(ticket);
		Assert.assertNotNull(seatStatusMap);
		Assert.assertEquals(0, seatStatusMap.size());
		return;
	}
	
	@Test
	public void testGetBookedSeatsIdsException2() {
		Ticket ticket = new Ticket();
		Schedule schedule = new Schedule();
		schedule.setId(1);
		ScreeningRoom screeningRoom = new ScreeningRoom();
		screeningRoom.setId(-999);
		schedule.setScreeningRoom(screeningRoom);
		ticket.setSchedule(schedule);
		
		Map<Integer, Integer> seatStatusMap = ticketDAO.getBookedSeatsIds(ticket);
		Assert.assertNotNull(seatStatusMap);
		Assert.assertEquals(0, seatStatusMap.size());
		return;
	}
	
	@Test
	public void testGetBookedSeatsIdsStandard1() {
		Ticket ticket = new Ticket();
		Schedule schedule = new Schedule();
		schedule.setId(1);
		ScreeningRoom screeningRoom = new ScreeningRoom();
		screeningRoom.setId(1);
		schedule.setScreeningRoom(screeningRoom);
		ticket.setSchedule(schedule);
		
		Map<Integer, Integer> seatStatusMap = ticketDAO.getBookedSeatsIds(ticket);
		Assert.assertNotNull(seatStatusMap);
		Assert.assertTrue(seatStatusMap.size() > 0);
		
		for (Map.Entry<Integer, Integer> entry : seatStatusMap.entrySet()) {
			int seatId = entry.getKey();
			int status = entry.getValue();
			
			Assert.assertTrue(seatId > 0);
			Assert.assertTrue(status == -1 || status >= 0);
		}
		return;
	}
	
	@Test
	public void testGetBookedSeatsIdsStandard2() {
		Ticket ticket = new Ticket();
		Schedule schedule = new Schedule();
		schedule.setId(2);
		ScreeningRoom screeningRoom = new ScreeningRoom();
		screeningRoom.setId(2);
		schedule.setScreeningRoom(screeningRoom);
		ticket.setSchedule(schedule);
		
		Map<Integer, Integer> seatStatusMap = ticketDAO.getBookedSeatsIds(ticket);
		Assert.assertNotNull(seatStatusMap);
		
		if (!seatStatusMap.isEmpty()) {
			for (Map.Entry<Integer, Integer> entry : seatStatusMap.entrySet()) {
				int seatId = entry.getKey();
				int status = entry.getValue();
				
				Assert.assertTrue(seatId > 0);
				Assert.assertTrue(status == -1 || status >= 0);
			}
		}
		return;
	}
} 