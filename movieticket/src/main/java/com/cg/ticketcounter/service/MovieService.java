package com.cg.ticketcounter.service;

import java.util.List;

import com.cg.ticketcounter.exception.TicketCounterException;
import com.cg.ticketcounter.model.Movie;

public interface MovieService {

	public void save(Movie movie)throws TicketCounterException;

	public Movie findByName(Long id)throws TicketCounterException;

	public List<Movie> getAllMovies()throws TicketCounterException;
	
	public Boolean deleteMovie(Long id)throws TicketCounterException;
	
	public int updateNumberOfTickets(Long movieId,int numberOfTickets)throws TicketCounterException;

}
