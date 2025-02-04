package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Sala {
	
	 // INFO:
	 // Per quale motivo esiste Sala? Coerenza e retrocompatibilità, se in un eventuale update vogliamo aggiungere ad esempio una capienza alla sala o gestirne collegamenti,
	 // abbiamo già l'entità pronta senza dover andare a modificare altre classi.
	
	 // Al momento Location non ha metodi per la gestione delle varie Sale in quanto non richiesto, pertanto viene importato un preset di Sale direttamente nel database.
	
	 // Per quanto le operazioni su aggiunta, modifica o rimozione sale richiederebbero accesso diretto al database, ritengo plausibile considerare che non ci siano 
	 // ristrutturazioni talmente frequenti da necessitarne l'implementazione al momento.
	
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
    
        private String name;
        
        
        @OneToMany(mappedBy = "sala")
        private List<Artwork> artworks = new ArrayList<>();
        
        
        @ManyToOne
        @JoinColumn(name = "curator_id", nullable = true)
        @Cascade(CascadeType.PERSIST)
        private Curator curator;
        
      
        
        
        public Long getId() {
            return id;
        }
    
        public void setId(Long id) {
            this.id = id;
        }
    
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    
        public List<Artwork> getArtworks() {
            return artworks;
        }

        public void addArtwork(Artwork artwork) {
            this.artworks.add(artwork);
        }
        
        public Curator getCurator() {
            return curator;
        }
    
        public void setCurator(Curator curator) {
            this.curator = curator;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Sala other = (Sala) obj;
            return Objects.equals(name, other.name);
        }

		
    }
