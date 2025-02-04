package it.uniroma3.siw.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Curator;

public interface CuratorRepository extends CrudRepository<Curator, Long> {

	public boolean existsByNameAndSurname(String name, String surname);	


	public boolean existsByCf(String cf);

	@Query("SELECT c FROM Curator c ORDER BY c.surname ASC")
	public Iterable<Curator> findAllOrderBySurname();



	


}