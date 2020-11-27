package com.jdnevesti.security.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jdnevesti.security.domain.Especialidade;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long>{

	@Query("SELECT e FROM Especialidade e WHERE e.titulo LIKE :search%")
	Page<Especialidade> findAllByTitulo(String search, Pageable pageable);

	@Query("SELECT e.titulo FROM Especialidade e WHERE e.titulo LIKE :termo%")
	List<String> findEspecialidadeByTermo(String termo);

	@Query("SELECT e FROM Especialidade e WHERE e.titulo IN :titulos")
	Set<Especialidade> findByTitulos(String[] titulos);

	@Query("SELECT e FROM Especialidade e "
			+ "JOIN e.medicos m "
			+ "WHERE m.id = :id")
	Page<Especialidade> findByIdMedico(Long id, Pageable pageable);
	
}
