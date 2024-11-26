package com.example.HolaSpring_1.dao;

import com.example.HolaSpring_1.domain.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaDao extends JpaRepository<Persona, Long>{
    
}
