package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.repository.ArtistRepository;

@Component
public class ArtistValidator implements Validator {
	@Autowired
	private ArtistRepository artistRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Artist artist = (Artist)o;
		
		
		if (artist.getId() == null) {
			 if (artistRepository.existsByNameAndSurnameAndDateOfBirth(artist.getName(),artist.getSurname(),artist.getDateOfBirth())) {
		            errors.rejectValue("name", "duplicate", "Esiste già!");
		            
		            if(artist.getDateOfDeath()!=null && artist.getDateOfBirth().isAfter(artist.getDateOfDeath())) {
		    			errors.rejectValue("dateOfDeath","not valid", "Un artista non può morire prima di nascere!");
		    		}
			
			 }
		} else {
	        Artist existingArtist = artistRepository.findById(artist.getId()).orElse(null);
	        if 	(existingArtist != null && 
	        		
	            (!existingArtist.getName().equals(artist.getName()) || 
	            !existingArtist.getSurname().equals(artist.getSurname()) ||
	            !existingArtist.getDateOfBirth().equals(artist.getDateOfBirth()))  &&
	            
	            artistRepository.existsByNameAndSurnameAndDateOfBirth(artist.getName(),artist.getSurname(),artist.getDateOfBirth()))
	            {
	
	            errors.rejectValue("name", "duplicate", "Esiste già!");
	        }
		
	     if(artist.getDateOfDeath()!=null && artist.getDateOfBirth().isAfter(artist.getDateOfDeath())) {
			errors.rejectValue("dateOfDeath","not valid", "Un artista non può morire prima di nascere!");
		}
	}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Artist.class.equals(aClass);
	}
}