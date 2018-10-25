package org.deb.SFM.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "sfm")
public class ConfigurationComponent {

    @NotNull
    /**
     * Folder to check for files
     */
    private String folder;

    /**
     * File name pattern
     */
    private String pattern;

    /**
     * File extension
     */
    private String extension;

    /**
     * Word count, more than this count will return true for string validity
     */
    private int wordCount;


    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }
}
