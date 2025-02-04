package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.ArtistValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Artwork;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.ArtworkRepository;
import it.uniroma3.siw.service.FileHandlerService;

@Controller
public class ArtistController {
	
	@Autowired 
	private ArtistRepository artistRepository;
	
	@Autowired
	private ArtistValidator artistValidator;

	@Autowired
	private ArtworkRepository artworkRepository;

	@Autowired
	private FileHandlerService fileHandlerService;
	

///////////////// GESTIONE AZIONI /////////////////

	@GetMapping("/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		model.addAttribute("action","create");
		return "admin/formNewArtist.html";
	}
	
	@GetMapping("/admin/manageArtist/{id}")
	public String manageArtist(@PathVariable("id") Long id, Model model) {
		Artist artist = artistRepository.findById(id).orElse(null);
		if(artist != null) {
			model.addAttribute("artist", artist);
			model.addAttribute("action","update");
				
		}
		
		return "admin/formNewArtist";
	}
	
	
///////////////// INSERIMENTO, MODIFICA E CANCELLAZIONE /////////////////

	@PostMapping(value = {"/admin/submitArtist", "/admin/submitArtist/{id}"})
	public String submitArtist(@PathVariable(value = "id", required = false) Long id, Artist artist, BindingResult bindingResult, Model model) {

	    model.addAttribute("artist", artist);
	    this.artistValidator.validate(artist, bindingResult);
	    
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("artist", artist);
	        return "admin/formNewArtist";
	    }
	
	    if (id != null) {
	        	    
	        Artist artistToUpdate = artistRepository.findById(id).orElse(null);
	        if (artistToUpdate != null) {
	        	artistToUpdate.setName(artist.getName());
	        	artistToUpdate.setSurname(artist.getSurname());
	        	artistToUpdate.setDateOfBirth(artist.getDateOfBirth());
	        	artistToUpdate.setPlaceOfBirth(artist.getPlaceOfBirth());
	        	artistToUpdate.setDateOfDeath(artist.getDateOfDeath());
	        	artistToUpdate.setUrlOfPicture(artist.getUrlOfPicture());

	           
	            artistRepository.save(artistToUpdate);
	            return  "redirect:/artist/" + artistToUpdate.getId();
	        }
	    }
	    artistRepository.save(artist);
	    return "redirect:/artist/" + artist.getId();
    
	}
	
	@GetMapping("/admin/deleteArtist/{id}")
    public String deleteArtist(@PathVariable Long id) {
		
		List<Artwork> artworksToDelete = artistRepository.findById(id).get().getMadeArtworks();
		for(Artwork artwork : artworksToDelete) {
			
			String urlToDelete = artworkRepository.findById(artwork.getId()).get().getUrlImage();
			fileHandlerService.deleteFileByUrl(urlToDelete);

		}
		
		String urlToDelete = artistRepository.findById(id).get().getUrlOfPicture();
		fileHandlerService.deleteFileByUrl(urlToDelete);
        artistRepository.deleteById(id);
        return ("/admin/indexArtist.html");
            
	}
	
///////////////// OPERAZIONI AGGIUNTA E RIMOZIONE OPERA AD ARTISTA /////////////////
///

	@PostMapping("/admin/artist/{artistId}/addArtwork")
	public String addArtworkToArtist(@PathVariable("artistId") Long artistId, Long artworkId, Model model) {
		
		Artwork artwork = artworkRepository.findById(artworkId).get();
		Artist artist = artistRepository.findById(artistId).get();
		
		artist.getMadeArtworks().add(artwork);
		artwork.setArtist(artist);
		
		artistRepository.save(artist);
		
		model.addAttribute("artwork", artwork);
		model.addAttribute("artist", artist);
 
		return "redirect:/artist/" + artistId;
	}
	
	@GetMapping("/admin/artist/{artistId}/removeArtwork/{artworkId}")
	public String removeArtworkFromArtist(@PathVariable("artistId") Long artistId, @PathVariable("artworkId")Long artworkId, Model model) {
		
		Artwork artwork = artworkRepository.findById(artworkId).get();
		Artist artist = artistRepository.findById(artistId).get();
		
		artist.getMadeArtworks().add(artwork);
		artwork.setArtist(null);
		
		artistRepository.save(artist);
		artworkRepository.save(artwork);
		
		model.addAttribute("artwork", artwork);
		model.addAttribute("artist", artist);


		return  "redirect:/artist/" + artistId;
	}
	
	
	
	
///////////////// MAPPING PAGINE WEB /////////////////

	
	@GetMapping("/admin/indexArtist")
	public String indexArtist() {
		return "admin/indexArtist.html";
	}
	
	@GetMapping("/artist/{id}")
	public String getArtistAdmin(@PathVariable("id") Long id, Model model) {
		
		Artist artist = this.artistRepository.findById(id).get();
		
		List<Artwork> artworks = artworkRepository.findOpereNonAssegnate();
		
		model.addAttribute("artist",artist );	
		model.addAttribute("artworks",artworks);
		return "artist.html";
	}

	@GetMapping("/artist")
	public String getArtists(Model model) {
		model.addAttribute("artists", this.artistRepository.findAll());
		return "artists.html";
	}
}
