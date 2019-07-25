package com.cg.ticketcounter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.ticketcounter.exception.TicketCounterException;
import com.cg.ticketcounter.model.Shows;
import com.cg.ticketcounter.repository.ShowsRepository;

@Service
public class ShowsServiceImpl implements ShowsService {

	@Autowired
	ShowsRepository showsRepository;

	@Override
	public Shows getShows(long showsId) {

		return showsRepository.findOne(showsId);
	}

	@Override
	public Shows setShows(Shows shows) {
		
		return showsRepository.save(shows);
	}
	
	@Override
	public int updateNumberOfTickets(Long movieId, int numberOfTickets) throws TicketCounterException{
	
		
		return showsRepository.updateNumberOfTickets(movieId, numberOfTickets);
	}  

}
