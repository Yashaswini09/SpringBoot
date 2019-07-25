package com.cg.ticketcounter.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.cg.ticketcounter.model.Shows;

@Entity
@Table(name = "movie")
public class Movie {

	private Long movieId;
	@NotNull(message = "Movie Name cannot be empty")
	@Size(min = 1, max = 200, message = "Movie Name must be between 1 and 200 characters")
	private String name;
	@Min(value = 1, message = "1-10 Halls are available")
	@Max(value = 10, message = "1-10 Halls are available")
	private int hallNumber;
	private int numberOfTickets;
//	private List<Bookings> bookings;
	private List<Shows> shows;

	@OneToMany(mappedBy = "movie")
	public List<Shows> getShows() {
		return shows;
	}

	public void setShows(List<Shows> shows) {
		this.shows = shows;
	}

	/*@OneToMany(mappedBy = "movie")
	public List<Bookings> getBookings() {
		return bookings;
	}

	public void setBookings(List<Bookings> bookings) {
		this.bookings = bookings;
	}*/

	public int getNumberOfTickets() {
		return numberOfTickets;
	}

	public void setNumberOfTickets(int numberOfTickets) {
		this.numberOfTickets = numberOfTickets;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHallNumber() {
		return hallNumber;
	}

	public void setHallNumber(int hallNumber) {
		this.hallNumber = hallNumber;
	}

	public Movie(Long movieId, String name, int hallNumber, int numberOfTickets) {
		super();
		this.movieId = movieId;
		this.name = name;
		this.hallNumber = hallNumber;
		this.numberOfTickets = numberOfTickets;
	}

	public Movie(String name, int hallNumber, int numberOfTickets, List<Shows> shows) {
		super();
		this.name = name;
		this.hallNumber = hallNumber;
		this.numberOfTickets = numberOfTickets;
		this.shows = shows;
	}

	public Movie() {
		super();
	}

}
