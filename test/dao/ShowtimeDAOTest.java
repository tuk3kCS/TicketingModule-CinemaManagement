package dao;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

import dao.ShowtimeDAO;
import model.Showtime;
import model.Movie;

public class ShowtimeDAOTest {
	ShowtimeDAO showtimeDAO = new ShowtimeDAO();
	
	@Test
	public void testSearchAvailableShowtimeException1() {
		Movie invalidMovie = new Movie();
		invalidMovie.setId(-999);
		
		List<Showtime> showtimes = showtimeDAO.searchAvailableShowtime(invalidMovie);
		Assert.assertNotNull(showtimes);
		Assert.assertEquals(0, showtimes.size());
		return;
	}
	
	@Test
	public void testSearchAvailableShowtimeException2() {
		Movie invalidMovie = new Movie();
		invalidMovie.setId(0);
		
		List<Showtime> showtimes = showtimeDAO.searchAvailableShowtime(invalidMovie);
		Assert.assertNotNull(showtimes);
		Assert.assertEquals(0, showtimes.size());
		return;
	}
	
	@Test
	public void testSearchAvailableShowtimeStandard1() {
		Movie validMovie = new Movie();
		validMovie.setId(1);
		
		List<Showtime> showtimes = showtimeDAO.searchAvailableShowtime(validMovie);
		Assert.assertNotNull(showtimes);
		Assert.assertTrue(showtimes.size() > 0);
		
		for(int i=0; i<showtimes.size(); i++) {
			Assert.assertTrue(showtimes.get(i).getId() > 0);
			Assert.assertNotNull(showtimes.get(i).getTime());
		}
		return;
	}
	
	@Test
	public void testSearchAvailableShowtimeStandard2() {
		Movie validMovie = new Movie();
		validMovie.setId(2);
		
		List<Showtime> showtimes = showtimeDAO.searchAvailableShowtime(validMovie);
		
		Assert.assertNotNull(showtimes);
		
		if (showtimes.size() > 0) {
			for(int i=0; i<showtimes.size(); i++) {
				Assert.assertTrue(showtimes.get(i).getId() > 0);
				Assert.assertNotNull(showtimes.get(i).getTime());
			}
		}
		return;
	}
} 