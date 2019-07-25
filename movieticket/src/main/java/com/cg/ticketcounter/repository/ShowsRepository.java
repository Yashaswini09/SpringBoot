package com.cg.ticketcounter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cg.ticketcounter.model.Shows;

@Repository
public interface ShowsRepository  extends JpaRepository<Shows, Long> {

	
	 @Transactional
	 @Modifying
	 @Query("UPDATE Shows m SET m.numberOfTickets =:numberOfTickets where m.dateTimeId =:dateTimeId")
	  int updateNumberOfTickets(@Param("dateTimeId") Long showId,
	                                 @Param("numberOfTickets") int numberOfTickets);
}
