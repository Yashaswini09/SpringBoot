package com.cg.ticketcounter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.ticketcounter.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
