package it.uniroma3.siw.repository;



import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Artist;

public interface ArtistRepository extends CrudRepository<Artist, Long> {

	public boolean existsByNameAndSurname(String name, String surname);	
	

	public boolean existsByNameAndSurnameAndDateOfBirth(String name, String surname, LocalDate dateOfBirth);


	@Query("SELECT a FROM Artist a ORDER BY a.surname ASC")
	public Iterable<Artist> findAllOrderBySurname();



}