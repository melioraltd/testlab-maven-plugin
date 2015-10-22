package fi.meliora.testlab.ext.maven;

import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Locates resources to publish the results from.
 *
 * @author Meliora Ltd
 */
public class ResourceDirectoryScanner {
    private DirectoryScanner directoryScanner;

    /**
     * Constructs a new ResourceDirectoryScanner for a directory.
     *
     * @param baseDir base directory to scan
     * @param includes include patterns
     * @param excludes exclude patterns
     */
    public ResourceDirectoryScanner(File baseDir, String[] includes, String[] excludes) {
        directoryScanner = new DirectoryScanner();
        directoryScanner.setFollowSymlinks(true);
        directoryScanner.setBasedir(baseDir);
        if(includes != null)
            directoryScanner.setIncludes(includes);
        if(excludes != null)
            directoryScanner.setExcludes(excludes);
        directoryScanner.addDefaultExcludes();
    }

    /**
     * @return all files included from this scanner
     */
    public List<File> getIncludedFiles() {
        List<File> includedFiles = new ArrayList<File>();
        directoryScanner.scan();
        String[] files = directoryScanner.getIncludedFiles();
        if(files.length > 0) {
            for(String file : files) {
                includedFiles.add(new File(directoryScanner.getBasedir(), file));
            }
        }
        return includedFiles;
    }

}
