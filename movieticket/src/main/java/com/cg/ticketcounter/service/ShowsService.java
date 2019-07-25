package com.cg.ticketcounter.service;


import com.cg.ticketcounter.exception.TicketCounterException;
import com.cg.ticketcounter.model.Shows;


public interface ShowsService {
	
	 Shows getShows(long showsId);
	
	 Shows setShows(Shows shows);
	
	 public int updateNumberOfTickets(Long showId,int numberOfTickets)throws TicketCounterException;

}
