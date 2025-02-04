package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Curator;
import it.uniroma3.siw.repository.CuratorRepository;

@Component
public class CuratorValidator implements Validator {
	@Autowired
	private CuratorRepository curatorRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Curator curator = (Curator)o;
		
		
		if (curator.getId() == null) {
			 // Logica per aggiungere un curatore
			 if (curatorRepository.existsByCf(curator.getCf())) {
		            errors.rejectValue("name", "duplicate", "Esiste già un curatore con questo CF!");
			
			
			 }
		} else {
	        // Logica per modificare un curatore
	        Curator existingCurator = curatorRepository.findById(curator.getId()).orElse(null);
	        if 	(existingCurator != null && 
	        		
	            (!existingCurator.getCf().equals(curator.getCf()) &&
	            
	            curatorRepository.existsByCf(curator.getCf()) )
	            ){
	        	
	
	            errors.rejectValue("name", "duplicate", "Esiste già un curatore con questo CF!");
	        }
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Curator.class.equals(aClass);
	}
}