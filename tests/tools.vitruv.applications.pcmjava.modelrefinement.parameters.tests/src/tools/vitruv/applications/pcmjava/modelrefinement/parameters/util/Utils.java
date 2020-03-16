package tools.vitruv.applications.pcmjava.modelrefinement.parameters.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.*;

public class Utils {

    private static Path getNewestKiekerDir(String pathStr) {
        Path path = Paths.get(pathStr);
        
        Optional<File> mostRecentFolder =
                Arrays
                    .stream(path.toFile().listFiles())
                    .filter(f -> f.isDirectory())
                    .max(
                        (f1, f2) -> Long.compare(f1.lastModified(),
                            f2.lastModified()));
        
        if (mostRecentFolder.isPresent()) {
            File mostRecent = mostRecentFolder.get();
            return mostRecent.toPath();
        } else {
            return null;
        }   
        
    }
    
    public void deleteDirectoryRecursion(Path path, boolean contentsOnly) throws IOException {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
          try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
            for (Path entry : entries) {
              deleteDirectoryRecursion(entry, false);
            }
          }
        }
        if(!contentsOnly) Files.delete(path);
      }
    
    public void moveKiekerFiles(String pathStr) {
        Path source = Paths.get(pathStr);
        Path target = Paths.get(source + File.separator + ".." + File.separator);
        
        try (DirectoryStream<Path> entries = Files.newDirectoryStream(source)) {
            for (Path entry : entries) {
              Files.move(entry, target, REPLACE_EXISTING);
            }
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
