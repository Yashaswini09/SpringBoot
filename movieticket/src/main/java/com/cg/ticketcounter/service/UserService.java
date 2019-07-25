package com.cg.ticketcounter.service;

import com.cg.ticketcounter.exception.TicketCounterException;
import com.cg.ticketcounter.model.User;

public interface UserService {
    void save(User user)throws TicketCounterException;

    User findByUsername(String username)throws TicketCounterException;
    
    
}
