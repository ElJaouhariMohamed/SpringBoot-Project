package utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
	
	

    public static void saveFile(String fileName, String directoryPath, MultipartFile file) throws IOException {
        Path path = Paths.get(directoryPath, fileName);
        Files.write(path, file.getBytes());
    }
    
    public static String findUser() {
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
		   username = ((UserDetails)principal).getUsername();
		} else {
		   username = principal.toString();
		}
		
		return username;
    }
}