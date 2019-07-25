package com.cg.ticketcounter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.ticketcounter.model.Bookings;

public interface BookingsRepository extends JpaRepository<Bookings, Long> {

}
