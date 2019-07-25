package com.cg.ticketcounter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.cg.ticketcounter.model.Movie;

public interface MovieRepository  extends JpaRepository<Movie, Long>{
	
	 Movie findByName(String name);
	 @Transactional
	 @Modifying
	 @Query("UPDATE Movie m SET m.numberOfTickets =:numberOfTickets where m.movieId =:movieId")
	  int updateNumberOfTickets(@Param("movieId") Long movieId,
	                                 @Param("numberOfTickets") int numberOfTickets);
	 
	
}


