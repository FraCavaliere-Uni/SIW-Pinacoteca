package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Artwork;
import it.uniroma3.siw.repository.ArtworkRepository;

@Component
public class ArtworkValidator implements Validator {
	@Autowired
	private ArtworkRepository artworkRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Artwork artwork = (Artwork)o;
	
	    if (artwork.getId() == null) { 
	        // Logica per aggiungere una nuova opera
	        if (artworkRepository.existsByTitleAndYear(artwork.getTitle(), artwork.getYear())) {
	            errors.rejectValue("title", "duplicate", "Esiste già un'opera con stesso nome e anno.");
	        }
	    } else {
	        // Logica per modificare un'opera
	        Artwork existingArtwork = artworkRepository.findById(artwork.getId()).orElse(null);
	        if 	(existingArtwork != null && 
	            (!existingArtwork.getTitle().equals(artwork.getTitle()) || 
	             !existingArtwork.getYear().equals(artwork.getYear())) &&
	            artworkRepository.existsByTitleAndYear(artwork.getTitle(), artwork.getYear())
	            ) {
	
	            errors.rejectValue("title", "duplicate", "Esiste già un'opera con stesso nome e anno.");
	        }
	    }
		
		
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Artwork.class.equals(aClass);
	}
}