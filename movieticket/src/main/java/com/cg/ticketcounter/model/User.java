package com.cg.ticketcounter.model;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "user")
public class User {
	private Long id;

	@Size(min = 3, max = 30, message = "Length between 3-30 characters")
	private String username;
	@Size(min = 8, message = "Length between 8-30 characters")
	private String password;
	@Size(min = 8, message = "Length between 8-30 characters")
	private String passwordConfirm;
	// @NotNull
	private String role;
	 @Size(min=10 ,max=11, message="Not a valid Phone number")
	private String phoneNo;
	 @Email(message="Not a valid Email Address")
	private String email;
	
	
	private List<Bookings> bookings;
	
	@OneToMany(mappedBy="user")
	public List<Bookings> getBookings() {
		return bookings;
	}

	public void setBookings(List<Bookings> bookings) {
		this.bookings = bookings;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Transient
	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}


	public User(Long id, String username, String password, String passwordConfirm, String role, String phoneNo,
			String email) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.passwordConfirm = passwordConfirm;
		this.email = email;
		this.role = role;
		this.phoneNo = phoneNo;
	}

	public User() {
		super();
	}
}
