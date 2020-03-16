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

public class KUtils {

    public static String getNewestKiekerDir(String pathStr) {
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
            System.out.println(mostRecent.getPath());
            return mostRecent.getPath();
        } else {
            return "";
        }   
        
    }
    
    public static void deleteDirectoryRecursion(Path path, boolean contentsOnly) throws IOException {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
          try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
            for (Path entry : entries) {
              deleteDirectoryRecursion(entry, false);
            }
          }
          catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
          }
          catch(NullPointerException e) {
              e.printStackTrace();
          }
        }
        if(!contentsOnly) Files.delete(path);
      }
    
    public static void moveKiekerFiles(String pathStr) throws IOException {
        Path source = Paths.get(getNewestKiekerDir(pathStr));
        if(source.toString().isEmpty()) {
            return; //nothing to do here
        }
        Path target = Paths.get(source + File.separator + ".." + File.separator);
        try (DirectoryStream<Path> entries = Files.newDirectoryStream(source)) {
            if(entries.equals(null)) return;
            for (Path entry : entries) {
                System.out.println(entry.toString());
              Files.move(entry, target, REPLACE_EXISTING);
            }
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }
        //deleteDirectoryRecursion(target, false);
        
    }
}
