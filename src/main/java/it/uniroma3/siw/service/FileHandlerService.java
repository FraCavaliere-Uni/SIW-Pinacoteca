package it.uniroma3.siw.service;

import org.springframework.stereotype.Service;
import java.io.File;
import java.nio.file.Paths;


@Service
public class FileHandlerService {

	 public boolean deleteFileByUrl(String fileUrl) {
		 System.out.print(fileUrl);
	        if (fileUrl == null || fileUrl.isEmpty()) {
	            return false; 
	        }

	        String fullPath = Paths.get("").toAbsolutePath() + "/" + fileUrl;

	        File file = new File(fullPath);
	        System.out.println("Attempting to delete: " + fullPath);

	        return file.exists() && file.isFile() && file.delete();
    
	 }
}
