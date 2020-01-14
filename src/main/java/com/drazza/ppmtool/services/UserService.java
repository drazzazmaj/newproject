package com.drazza.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.drazza.ppmtool.domain.User;
import com.drazza.ppmtool.exceptions.UsernameAlreadyExistsException;
import com.drazza.ppmtool.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired //bean je ppmtoolApplication
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser (User newUser) {
	   try {
		   newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
		   //user name has to be unique (exception)
		   newUser.setUsername(newUser.getUsername());//ovo i nije potrebno
		   //make sure that password and confirmed password match
		   //we don't persist or show the confirmed password
		   newUser.setConfirmPassword("");
		   return userRepository.save(newUser);
	   } catch(Exception e) {
		   throw new UsernameAlreadyExistsException("Username '"+newUser.getUsername()+"'already exists");
	   }
	 
	}
}
