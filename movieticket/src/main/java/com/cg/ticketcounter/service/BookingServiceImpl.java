package com.cg.ticketcounter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.ticketcounter.exception.TicketCounterException;
import com.cg.ticketcounter.model.Bookings;
import com.cg.ticketcounter.repository.BookingsRepository;

@Service
public class BookingServiceImpl implements BookingService {
	@Autowired
	BookingsRepository bookingRepository;

	@Override
	public Bookings save(Bookings booking) throws TicketCounterException {

		try {
			booking = bookingRepository.save(booking);
		} catch (IllegalArgumentException e) {

			throw new TicketCounterException(1, "Saving Booking details Failed(IllegalArgumentException)");
		} catch (ClassCastException e) {

			throw new TicketCounterException(1, "Saving Booking details Failed(ClassCastException)");
		} catch (Exception e) {

			throw new TicketCounterException(1, "Saving Booking details Failed(General Exception)");

		}

		return booking;
	}

	@Override
	public List<Bookings> getBookings() throws TicketCounterException {

		try {
			return bookingRepository.findAll();
		} catch (IllegalArgumentException e) {

			throw new TicketCounterException(1, "Getting Bookings detail Failed(IllegalArgumentException)");
		} catch (ClassCastException e) {

			throw new TicketCounterException(1, "Getting Bookings detail Failed(ClassCastException)");
		} catch (Exception e) {

			throw new TicketCounterException(1, "Getting Bookings detail Failed(General Exception)");

		}
	}

	@Override
	public void deleteBookings(long bookingId) throws TicketCounterException {
		try {
			bookingRepository.delete(bookingId);
		} catch (Exception e) {
			{
			}

		}
	}
}