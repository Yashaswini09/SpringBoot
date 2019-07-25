package com.cg.ticketcounter.service;

import com.cg.ticketcounter.exception.TicketCounterException;
import com.cg.ticketcounter.model.User;
import com.cg.ticketcounter.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
 
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user)throws TicketCounterException {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username)throws TicketCounterException {
        return userRepository.findByUsername(username);
    }
}
