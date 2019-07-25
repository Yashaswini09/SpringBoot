package com.cg.ticketcounter.service;

import java.util.List;

import com.cg.ticketcounter.exception.TicketCounterException;
import com.cg.ticketcounter.model.Bookings;



public interface BookingService {

	public Bookings save(Bookings booking)throws TicketCounterException;
	
	public List<Bookings> getBookings()throws TicketCounterException;
	
	public void deleteBookings(long bookingId)throws TicketCounterException;
}
