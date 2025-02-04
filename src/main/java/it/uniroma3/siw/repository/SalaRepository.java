package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Sala;

public interface SalaRepository extends CrudRepository<Sala, Long> {

	public List<Sala> findByName(String name);

	public boolean existsByName(String name);

	@Query(value = "SELECT * FROM Sala WHERE curator_id IS NULL ORDER BY name ASC", nativeQuery = true)
	public List<Sala> findSaleNonGestite();


	@Query("SELECT s FROM Sala s ORDER BY s.name ASC")
	public Iterable<Sala> findAllOrderByName();
	
	
	
	public Optional<Sala> findByCurator_Id(Long id);
	
}