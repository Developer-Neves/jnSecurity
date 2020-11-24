package com.jdnevesti.security.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jdnevesti.security.domain.Especialidade;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long>{

	@Query("SELECT e FROM Especialidade e WHERE e.titulo LIKE :search%")
	Page<Especialidade> findAllByTitulo(String search, Pageable pageable);
	
}
