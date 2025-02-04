package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Artist extends Person{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate dateOfDeath;

	@OneToMany(mappedBy="artist", cascade = CascadeType.ALL)
	private List<Artwork> madeArtworks = new ArrayList<>();
	
	
	
	public List<Artwork> getMadeArtworks() {
		return madeArtworks;
	}

	public void setArtistOf(Artwork artwork) {
		this.madeArtworks.add(artwork);
	}

	public LocalDate getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(LocalDate dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
    
    