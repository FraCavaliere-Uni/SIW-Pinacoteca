package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.CuratorValidator;
import it.uniroma3.siw.model.Curator;
import it.uniroma3.siw.model.Sala;
import it.uniroma3.siw.repository.CuratorRepository;
import it.uniroma3.siw.repository.SalaRepository;
import it.uniroma3.siw.service.FileHandlerService;
import jakarta.transaction.Transactional;

@Controller
public class CuratorController {
	
	@Autowired 
	private CuratorRepository curatorRepository;
		
	@Autowired 
	private SalaRepository salaRepository;
	
	@Autowired
	private CuratorValidator curatorValidator;

	@Autowired
	private FileHandlerService fileHandlerService;
	
///////////////// GESTIONE AZIONI /////////////////


	@GetMapping("/admin/formNewCurator")
	public String formNewCurator(Model model) {
		
		model.addAttribute("curator", new Curator());
		model.addAttribute("action","create");
		
		return "admin/formNewCurator.html";
	}
	
	@GetMapping("/admin/manageCurator/{id}")
	public String manageCurator(@PathVariable("id") Long id, Model model) {
		
		Curator curator = curatorRepository.findById(id).orElse(null);
		
		model.addAttribute("curator", curator);
		model.addAttribute("action","update");
			
		return "admin/formNewCurator";
	}
		
///////////////// INSERIMENTO, MODIFICA E CANCELLAZIONE /////////////////

	@Transactional
	@PostMapping({"/admin/submitCurator", "/admin/submitCurator/{id}"})
	public String submitCurator(@PathVariable(value = "id", required = false) Long id,  @ModelAttribute("curator") Curator curator, BindingResult bindingResult, Model model) {

		
		List<Sala> sale = salaRepository.findSaleNonGestite();
	    
		model.addAttribute("curator", curator);
	    model.addAttribute("sale", sale);

	    	    
	    this.curatorValidator.validate(curator, bindingResult);
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("curator", curator);
		    model.addAttribute("sale", sale);
	        return "admin/formNewCurator";
	    }
	    
	 	    
	    if (id != null) {
        	// Aggiorniamo il curatore se viene passato il suo id
	    	
	    	Curator curatorToUpdate = curatorRepository.findById(id).orElse(null);
	    	
	        if (curatorToUpdate != null) {
	        	
	        	curatorToUpdate.setName(curator.getName());
	        	curatorToUpdate.setSurname(curator.getSurname());
	        	curatorToUpdate.setDateOfBirth(curator.getDateOfBirth());
	        	curatorToUpdate.setPlaceOfBirth(curator.getPlaceOfBirth());
	        	curatorToUpdate.setCf(curator.getCf());
	        	curatorToUpdate.setUrlOfPicture(curator.getUrlOfPicture());
	        	
	        		            
	            curatorRepository.save(curatorToUpdate);
	            return "redirect:/admin/curator/" + curatorToUpdate.getId();
	        }
	    }
	    
	    //Creo il nuovo curatore
	    curatorRepository.save(curator);
	    return "redirect:/admin/curator/" + curator.getId();
    
	}
	
	@GetMapping("/admin/deleteCurator/{id}")
    public String deleteCurator(@PathVariable Long id) {

		Curator curatorToDelete = curatorRepository.findById(id).orElse(null);

	    List<Sala> managedSale = curatorToDelete.getManagedSale();
	        if (managedSale != null) {
	        	for(Sala sala : managedSale) {
		            sala.setCurator(null); 
		            salaRepository.save(sala); 
	        	}
	        }
	        
	      String urlToDelete = curatorToDelete.getUrlOfPicture();
	      fileHandlerService.deleteFileByUrl(urlToDelete);
	      curatorRepository.delete(curatorToDelete);
	    
          return ("/admin/indexCurator.html");
            
	}
	
///////////////// OPERAZIONI AGGIUNTA E RIMOZIONE CURATORE A SALA /////////////////

	@PostMapping("/admin/curator/{curatorId}/addSala")
	public String addSalaToCurator(@PathVariable("curatorId") Long curatorId, Long salaId, Model model) {
		
		Sala sala = salaRepository.findById(salaId).get();
		Curator curator = curatorRepository.findById(curatorId).get();
		
		curator.getManagedSale().add(sala);
		sala.setCurator(curator);
		curatorRepository.save(curator);
		
		model.addAttribute("sala", sala);
		model.addAttribute("curator", curator);
 
		return "redirect:/admin/curator/" + curatorId;
	}
	
	@GetMapping("/admin/curator/{curatorId}/removeSala/{salaId}")
	public String removeSalaFromCurator(@PathVariable("curatorId") Long curatorId, @PathVariable("salaId")Long salaId, Model model) {
		
		Sala sala = this.salaRepository.findById(salaId).get();
		Curator curator = this.curatorRepository.findById(curatorId).get();
		curator.getManagedSale().remove(sala);
		sala.setCurator(null);
		
		this.curatorRepository.save(curator);
		this.salaRepository.save(sala);
		
		model.addAttribute("sala", sala);
		model.addAttribute("curator", curator);


		return  "redirect:/admin/curator/" + curatorId;
	}
	
	
	
///////////////// MAPPING PAGINE WEB /////////////////

	
	
	@GetMapping("/admin/indexCurator")
	public String indexCurator() {
		return "admin/indexCurator.html";
	}
	
	@GetMapping("/admin/manageCurator")
	public String manageCurators(Model model) {
		
		Iterable<Curator> curatorsBySurame = curatorRepository.findAllOrderBySurname();
		model.addAttribute("curators", curatorsBySurame );
		return "admin/manageCurators.html";
	}

	@GetMapping("admin/curator/{id}")
	public String getCurator(@PathVariable("id") Long id, Model model) {
		
		Curator curator = this.curatorRepository.findById(id).get();
		
		List<Sala> sale = salaRepository.findSaleNonGestite();
		
		model.addAttribute("curator", curator );	
		model.addAttribute("sale",sale);
		return "admin/curator.html";
	}

	
	
}
