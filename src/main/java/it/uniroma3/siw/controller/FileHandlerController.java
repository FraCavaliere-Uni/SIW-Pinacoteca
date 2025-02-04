package it.uniroma3.siw.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Artwork;
import it.uniroma3.siw.model.Curator;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.ArtworkRepository;
import it.uniroma3.siw.repository.CuratorRepository;

@Controller
public class FileHandlerController {

    private static final Path UPLOAD_DIR =  Paths.get("uploads");

    
    @Autowired
    private ArtworkRepository artworkRepository;
    
    @Autowired
    private ArtistRepository artistRepository;   
    
    @Autowired
    private CuratorRepository curatorRepository; 

    @PostMapping("admin/upload/{entity}/{id}")
    public String handleFileUpload(@PathVariable String entity, @PathVariable Long id, 
                                    MultipartFile file) {
      
    	
    	String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);

        
        try {
            String newFileName = entity + "_" + id + "_image." + extension;

            Path path = UPLOAD_DIR.resolve(newFileName);
            Files.createDirectories(UPLOAD_DIR);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = "/uploads/" + newFileName;

            switch (entity) {
                case "artwork":
                    Artwork artwork = artworkRepository.findById(id).orElse(null);
                    artwork.setUrlImage(imageUrl);  
                    artworkRepository.save(artwork);
                    break;
                case "artist":
                    Artist artist = artistRepository.findById(id).orElse(null);
                    artist.setUrlOfPicture(imageUrl);  
                    artistRepository.save(artist);
                    break;
                case "curator":
                    Curator curator = curatorRepository.findById(id).orElse(null);
                    curator.setUrlOfPicture(imageUrl);
                    curatorRepository.save(curator);
                    return "redirect:/admin/"+ entity + "/" + id;
                   
                default:
                    return "redirect:/upload/" + entity + "/" + id;
            }

            return "redirect:/"+ entity + "/" + id;
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/"+ entity + "/" + id;
        }
    }
    
    @GetMapping("/uploads/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Path path = Paths.get("uploads").resolve(filename);
        Resource resource = new FileSystemResource(path);
        
        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG) 
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
    
    
    
    
   
    
}
