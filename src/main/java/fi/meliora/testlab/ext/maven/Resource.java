package fi.meliora.testlab.ext.maven;

import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.Arrays;

/**
 * A configured resource.
 *
 * @author Meliora Ltd
 */
public class Resource {
    @Parameter
    private File directory;
    @Parameter
    private String[] includes;
    @Parameter
    private String[] excludes;

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public String[] getExcludes() {
        return excludes;
    }

    public void setExcludes(String[] excludes) {
        this.excludes = excludes;
    }

    public String[] getIncludes() {
        return includes;
    }

    public void setIncludes(String[] includes) {
        this.includes = includes;
    }

    @Override
    public String toString() {
        return "fi.meliora.testlab.ext.maven.Resource{" +
                "directory=" + directory +
                ", includes=" + Arrays.toString(includes) +
                ", excludes=" + Arrays.toString(excludes) +
                '}';
    }
}
