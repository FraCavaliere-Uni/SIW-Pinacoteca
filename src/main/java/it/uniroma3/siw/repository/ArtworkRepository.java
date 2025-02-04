package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Artwork;

public interface ArtworkRepository extends CrudRepository<Artwork, Long> {

	public List<Artwork> findByYear(int year);

	public boolean existsByTitleAndYear(String title, int year);

	public List<Artwork> findByTechnique(String technique);	

	@Query(value = "SELECT * FROM Artwork WHERE artist_id = ?1 ORDER BY title ASC", nativeQuery = true)
	public List<Artwork> findByArtistId(Long artistId);

	@Query(value = "SELECT * FROM Artwork WHERE artist_id IS NULL ORDER BY title ASC", nativeQuery = true)
	public List<Artwork> findOpereNonAssegnate();
		
	
}