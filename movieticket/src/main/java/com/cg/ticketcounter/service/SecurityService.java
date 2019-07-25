package com.cg.ticketcounter.service;

import com.cg.ticketcounter.exception.TicketCounterException;

public interface SecurityService {
	
	String findLoggedInUsername()throws TicketCounterException ;

	void autologin(String username, String password)throws TicketCounterException ;
}
