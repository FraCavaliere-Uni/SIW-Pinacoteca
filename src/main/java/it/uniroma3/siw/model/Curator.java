package it.uniroma3.siw.model;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Curator extends Person{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String cf;

  	@OneToMany(mappedBy = "curator", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false)
	private List<Sala> managedSale = new ArrayList<>();;
	
  	
  	
  	public void addSala(Sala sala) {
         managedSale.add(sala);
         sala.setCurator(this);
     }

    public void removeSala(Sala sala) {
         if (managedSale != null) {
        	 managedSale.remove(sala);
             sala.setCurator(null); 
         }
     }
	
    
    
    
    // GETTER E SETTER
    
	
    public List<Sala> getManagedSale() {
         return managedSale;
     }

    public void setManagedSale(List<Sala> managedSale) {
         this.managedSale = managedSale;
     }

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

    public Long getId() {
	    return id;
    }

	public void setId(Long id) {
	    this.id = id;
    }

}