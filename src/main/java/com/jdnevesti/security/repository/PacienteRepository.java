package com.jdnevesti.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jdnevesti.security.domain.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long>{

	@Query("SELECT p FROM Paciente p WHERE p.usuario.email LIKE :email")
	Optional<Paciente> findByUsuarioEmail(String email);
}
