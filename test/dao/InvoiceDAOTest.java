package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

import dao.DAO;
import dao.InvoiceDAO;
import model.Invoice;
import model.Ticket;
import model.User;
import model.Schedule;
import model.Seat;

public class InvoiceDAOTest {
	InvoiceDAO invoiceDAO = new InvoiceDAO();
	
	@Test
	public void testAddInvoiceException1() {
		Invoice invoice = new Invoice();
		invoice.setDateTime(new Date());
		
		boolean result = invoiceDAO.addInvoice(invoice);
		Assert.assertFalse(result);
		return;
	}
	
	@Test
	public void testAddInvoiceException2() {
		Invoice invoice = new Invoice();
		invoice.setDateTime(new Date());
		
		User invalidUser = new User();
		invalidUser.setId(-999);
		invoice.setUser(invalidUser);
		
		boolean result = invoiceDAO.addInvoice(invoice);
		Assert.assertFalse(result);
		return;
	}
	
	@Test
	public void testAddInvoiceStandard1() {
		Connection con = DAO.con;
		try {
			con.setAutoCommit(false);
			
			Invoice invoice = new Invoice();
			invoice.setDateTime(new Date());
			
			User user = new User();
			user.setId(1);
			invoice.setUser(user);
			
			invoice.setTicketList(new ArrayList<>());
			invoice.setFoodList(new ArrayList<>());
			
			boolean result = invoiceDAO.addInvoice(invoice);
			Assert.assertTrue(result);
			Assert.assertTrue(invoice.getId() > 0);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
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
	public void testAddInvoiceStandard2() {
		Connection con = DAO.con;
		try {
			con.setAutoCommit(false);
			
			Invoice invoice = new Invoice();
			invoice.setDateTime(new Date());
			
			User user = new User();
			user.setId(1);
			invoice.setUser(user);
			
			List<Ticket> tickets = new ArrayList<>();
			Ticket ticket = new Ticket();
			
			Schedule schedule = new Schedule();
			schedule.setId(1);
			
			Seat seat = new Seat();
			seat.setId(1);
			
			ticket.setSchedule(schedule);
			ticket.setSeat(seat);
			ticket.setPrice(100);
			
			tickets.add(ticket);
			invoice.setTicketList(tickets);
			
			boolean result = invoiceDAO.addInvoice(invoice);
			Assert.assertTrue(result);
			Assert.assertTrue(invoice.getId() > 0);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
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