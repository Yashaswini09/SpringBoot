package com.cg.ticketcounter.model;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class Shows {

	private long dateTimeId;
	private String movieDate;
	@Min(value = 1, message = "1-10 Halls are available")
	@Max(value = 10, message = "1-10 Halls are available")
	private int hallNumber;
	@Max(value = 600, message = "Maximum of 600 Seats are available")
	private int numberOfTickets;
	private String time;
	@Autowired
	private Movie movie;

	private List<Bookings> bookings;
	
	@OneToMany(mappedBy="shows")
	public List<Bookings> getBookings() {
		return bookings;
	}

	public void setBookings(List<Bookings> bookings) {
		this.bookings = bookings;
	}
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "movieId")
	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	
	public Shows(String movieDate, int hallNumber, int numberOfTickets, String time, Movie movie) {
		super();
		this.movieDate = movieDate;
		this.hallNumber = hallNumber;
		this.numberOfTickets = numberOfTickets;
		this.time = time;
		this.movie = movie;
	}

	public int getHallNumber() {
		return hallNumber;
	}

	public void setHallNumber(int hallNumber) {
		this.hallNumber = hallNumber;
	}

	public int getNumberOfTickets() {
		return numberOfTickets;
	}

	public void setNumberOfTickets(int numberOfTickets) {
		this.numberOfTickets = numberOfTickets;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getDateTimeId() {
		return dateTimeId;
	}

	public void setDateTimeId(long dateTimeId) {
		this.dateTimeId = dateTimeId;
	}

	public Shows() {
	}

	public String getMovieDate() {
		return movieDate;
	}

	public void setMovieDate(String movieDate) {
		this.movieDate = movieDate;
	}

	public Shows(String movieDate, String time, Movie movie) {
		super();
		this.movieDate = movieDate;
		this.time = time;
		this.movie = movie;
	}
	
}
