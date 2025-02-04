package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.ArtworkValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Artwork;
import it.uniroma3.siw.model.Sala;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.ArtworkRepository;
import it.uniroma3.siw.repository.SalaRepository;
import it.uniroma3.siw.service.FileHandlerService;

@Controller
public class ArtworkController {
	@Autowired 
	private ArtworkRepository artworkRepository;
	
	@Autowired 
	private ArtistRepository artistRepository;

	@Autowired 
	private ArtworkValidator artworkValidator;
	
	@Autowired 
	private SalaRepository salaRepository;
	
	@Autowired 
	private FileHandlerService fileHandlerService;

	
///////////////// CONTROLLER PER LA GESTIONE DELLE AZIONI /////////////////
	
	
	@GetMapping("/admin/formNewArtwork")
	public String formNewArtwork(Model model) {
		model.addAttribute("artwork", new Artwork());
		model.addAttribute("action","create");
		return "admin/formNewArtwork.html";
	}

	@GetMapping("/admin/manageArtwork/{id}")
	public String manageArtwork(@PathVariable("id") Long id, Model model) {
		Artwork artwork = artworkRepository.findById(id).orElse(null);
		if(artwork != null) {
			model.addAttribute("artwork", artwork);
			model.addAttribute("action","update");
				
		}
		return "admin/formNewArtwork";
	}

///////////////// INSERIMENTO, MODIFICA E CANCELLAZIONE /////////////////
	
	@PostMapping({"/admin/submitArtwork", "/admin/submitArtwork/{id}"})
	public String submitArtwork(@PathVariable(value = "id", required = false) Long id, Artwork artwork, BindingResult bindingResult, Model model) {
			    
	    this.artworkValidator.validate(artwork, bindingResult);
	    	    	    
	    if (bindingResult.hasErrors()) {		    
		    model.addAttribute("artwork", artwork);	    
	        return "admin/formNewArtwork";
	    }
	
	    if (id != null) {
	 	        	    
	        Artwork artworkToUpdate = artworkRepository.findById(id).orElse(null);
	        if (artworkToUpdate != null) {
	            artworkToUpdate.setTitle(artwork.getTitle());
	            artworkToUpdate.setYear(artwork.getYear());
	            artworkToUpdate.setTechnique(artwork.getTechnique());
	            
	            
	             if(artwork.getArtist().getId() != null) {
	            	 Artist artist = artistRepository.findById(artwork.getArtist().getId()).orElse(null);
	            	 artworkToUpdate.setArtist(artist);
	            	 artist.getMadeArtworks().add(artworkToUpdate);
	             }

	             if(artwork.getSala().getId() != null) {
	            	 Sala sala = salaRepository.findById(artwork.getSala().getId()).orElse(null);
	            	 artworkToUpdate.setSala(sala);
	            	sala.getArtworks().add(artworkToUpdate);
	             }
	             
	           
	            
	            artworkRepository.save(artworkToUpdate);
	           
	           return "redirect:/artwork/" + artwork.getId();
	        }
	    }
	    
	    if (id == null) {
	    artwork.setArtist(null); 
	    artwork.setSala(null);
	    artworkRepository.save(artwork);
	    return "redirect:/artwork/" + artwork.getId();
    
	}
		return "redirect:/";
}
	
	
	
	@GetMapping("/admin/deleteArtwork/{id}")
    public String deleteArtwork(@PathVariable Long id) {
		
				 
		String urlToDelete = artworkRepository.findById(id).get().getUrlImage();
		fileHandlerService.deleteFileByUrl(urlToDelete);
		
        artworkRepository.deleteById(id);
        return ("/admin/indexArtwork.html");
            
	}
///////////////// AGGIUNTA ARTISTA AD ARTWORK /////////////////
	
	@PostMapping("/admin/artwork/{artworkId}/addArtist")
	public String setArtistToArtwork(Long artistId, @PathVariable("artworkId") Long artworkId, Model model) {
		
		Artist artist = this.artistRepository.findById(artistId).get();
		Artwork artwork = this.artworkRepository.findById(artworkId).get();
		artwork.setArtist(artist);
		artist.setArtistOf(artwork);
		this.artworkRepository.save(artwork);
		
		model.addAttribute("artwork", artwork);
		model.addAttribute("artist", artist);

		return "redirect:/artwork/" + artworkId;
	}
	
	
///////////////// OPERAZIONI AGGIUNTA E RIMOZIONE ARTWORK A SALA /////////////////

	@PostMapping("/admin/artwork/{artworkId}/addSala")
	public String addArtworkToSala(@PathVariable("artworkId") Long artworkId, Long salaId, Model model) {

		Sala sala = salaRepository.findById(salaId).get();
		Artwork artwork = artworkRepository.findById(artworkId).get();

		artwork.setSala(sala);
		sala.getArtworks().add(artwork);
		artworkRepository.save(artwork);

		model.addAttribute("sala", sala);
		model.addAttribute("artwork", artwork);
		model.addAttribute("success", "L'opera si trova nella sala : " + sala.getName());

		return "redirect:/artwork/" + artworkId;
	}

	@GetMapping("/admin/artwork/{artworkId}/removeSala/{salaId}")
	public String removeArtworkFromSala(@PathVariable("artworkId") Long artworkId, @PathVariable("salaId")Long salaId, Model model) {

		Sala sala = this.salaRepository.findById(salaId).get();
		Artwork artwork = this.artworkRepository.findById(artworkId).get();
		artwork.setSala(null);
		sala.getArtworks().remove(artwork);

		artworkRepository.save(artwork);
		salaRepository.save(sala);

		model.addAttribute("sala", sala);
		model.addAttribute("artwork", artwork);
		model.addAttribute("success", "L'opera non si trova più in: " + sala.getName());


		return  "redirect:/artwork/" + artworkId;
	}


	
///////////////// RICERCA ARTWORKS  /////////////////
	
	@PostMapping("/searchArtworks")
	public String searchArtworks(Model model, 
			Integer year,
			String technique, 
			Long artistId) {
		
	 if(year != null) {
		model.addAttribute("artworks", this.artworkRepository.findByYear(year));
	 }
	 else if(technique != null) {
		model.addAttribute("artworks", this.artworkRepository.findByTechnique(technique));
	 }
	 else if(artistId != null) {
		 
		model.addAttribute("artworks", this.artworkRepository.findByArtistId(artistId));
	 }
	 else { 
		model.addAttribute("artworks", this.artworkRepository.findAll());
	 
	 }
		
		return "foundArtworks.html";
	}
	
	
///////////////// MAPPING PAGINE WEB /////////////////

	
	@GetMapping("/admin/indexArtwork")
	public String indexArtwork() {
		return "admin/indexArtwork.html";
	}
		
	
	@GetMapping("/artwork/{id}")
	public String getArtworkAdmin(@PathVariable("id") Long id, Model model) {
		
		Artwork artwork = artworkRepository.findById(id).get();
		
		Iterable<Artist> artistsByName = artistRepository.findAllOrderBySurname();
		Iterable<Sala> sale = salaRepository.findAllOrderByName();

		
		model.addAttribute("artists", artistsByName);
		model.addAttribute("artwork",artwork);
		model.addAttribute("sale", sale);

		return "artwork.html";
	}

	@GetMapping("/artwork")
	public String getArtworks(Model model) {		
		model.addAttribute("artworks", this.artworkRepository.findAll());
		return "artworks.html";
	}
	
	@GetMapping("/formSearchArtworks")
	public String formSearchArtworks(Model model) {
		model.addAttribute("artists",artistRepository.findAll());
		return "formSearchArtworks.html";
	}

}
