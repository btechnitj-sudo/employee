package com.example.employee.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;

@Column(nullable=false,unique=true)
private String token;

@Column(nullable=false)
private Instant expiryDate;

@ManyToOne
@JoinColumn(name = "user_id", nullable =false)
private User user;

}
