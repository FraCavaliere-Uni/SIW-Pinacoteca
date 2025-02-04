package it.uniroma3.siw.model;

import java.util.Objects;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Artwork {
    
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
    
        @NotBlank
        private String title;
        
        @NotNull(message = "Inserire un anno")
        @Min(0)
        private Integer year;
        
        
        private String urlImage;
        private String technique;
        
        @ManyToOne
        @JoinColumn(name = "sala_id", nullable = true)
        private Sala sala;
        
        @ManyToOne 
        @JoinColumn(name = "artist_id", nullable = true)
        private Artist artist;
           
        public Long getId() {
            return id;
        }
    
        public void setId(Long id) {
            this.id = id;
        }
    
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
    
        public Integer getYear() {
            return year;
        }
    
        public void setYear(Integer year) {
            this.year = year;
        }
        
        public String getUrlImage() {
            return urlImage;
        }
    
        public void setUrlImage(String urlImage) {
            this.urlImage = urlImage;
        }
    
        public Artist getArtist() {
            return artist;
        }
    
        public void setArtist(Artist artist) {
            this.artist = artist;
        }
        
        public String getTechnique() {
			return technique;
		}

		public void setTechnique(String technique) {
			this.technique = technique;
		}
      
		
		public Sala getSala() {
			return sala;
		}
		
		public void setSala(Sala sala) {
			this.sala = sala;
		}
        @Override
        public int hashCode() {
            return Objects.hash(title, year);
        }
    
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Artwork other = (Artwork) obj;
            return Objects.equals(title, other.title) && year.equals(other.year);
        }

		
    }
