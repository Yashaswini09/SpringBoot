package com.cg.ticketcounter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.ticketcounter.exception.TicketCounterException;
import com.cg.ticketcounter.model.Movie;
import com.cg.ticketcounter.repository.MovieRepository;

@Service
public class MovieServiceImpl implements MovieService {

	@Autowired
	private MovieRepository movieRepository;

	@Override
	public void save(Movie movie) throws TicketCounterException{

		movieRepository.save(movie);
	}

	@Override
	public Movie findByName(Long id)throws TicketCounterException {

		return movieRepository.findOne(id);
	}
	
	 public List<Movie> getAllMovies(){  
	        List<Movie>movies = new ArrayList<>();  
	        movieRepository.findAll().forEach(movies::add);  
	        return movies;  
	    }

	@Override
	public Boolean deleteMovie(Long id)throws TicketCounterException {
		boolean check=true;
		movieRepository.delete(id);
		return check;
	}

	@Override
	public int updateNumberOfTickets(Long movieId, int numberOfTickets) throws TicketCounterException{
		// TODO Auto-generated method stub
		
		return movieRepository.updateNumberOfTickets(movieId, numberOfTickets);
	}  

}
