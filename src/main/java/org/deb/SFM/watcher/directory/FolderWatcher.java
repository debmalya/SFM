package org.deb.SFM.watcher.directory;

import org.deb.SFM.config.ConfigurationComponent;
import org.deb.SFM.repository.WordRegister24Hours;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

@Service
@RefreshScope
@Configuration
public class FolderWatcher {


    private static final Logger logger = Logger.getLogger("FolderWatcher");

    /**
     * All the configuration resides here.
     */
    private ConfigurationComponent config;
    private WatchService watcher;

    private Pattern pattern;

    @Autowired
    private WordRegister24Hours wordRegister24Hours;


    public FolderWatcher(ConfigurationComponent configurationComponent) throws IOException, InterruptedException {

        this.config = configurationComponent;
        watcher = FileSystems.getDefault().newWatchService();
    }

    @Scheduled(fixedRate = 1000)
    public void watch() {

        try {

            Path dir = Paths.get(config.getFolder());
            if (dir != null) {
                WatchKey key = dir.register(watcher,
                        ENTRY_CREATE);

                if (null != (key = watcher.take())) {
                    pattern = Pattern.compile(config.getPattern());
                    for (WatchEvent<?> event : key.pollEvents()) {
                        logger.log(Level.INFO,
                                String.format("Event kind: %s , file affected: %s"  , event.kind(),event.context()));
                        String onlyFileName = event.context().toString();
                        if (pattern.matcher(onlyFileName).find() && onlyFileName.endsWith(".log")) {
                            try (BufferedReader bufferedLogReader = Files.newBufferedReader(Paths.get(config.getFolder() + File.separator + onlyFileName))) {
                                bufferedLogReader.lines().forEach(e -> {
                                    String[] fieldValues = e.split(",");
                                    long epochTimeStampMillis = Long.parseLong(fieldValues[0]);
                                    String[] words = fieldValues[1].split("\\s");
                                    Arrays.stream(words).forEach(eachWord -> {
                                        if (eachWord.length() > 0) {
                                            long[] occurrrences = wordRegister24Hours.getWordRegister24HoursMap().get(eachWord);

                                            for (int i = 0; i < occurrrences.length - 1; i++) {
                                                occurrrences[i] = occurrrences[i + 1];
                                            }
                                            occurrrences[config.getWordCount()] = epochTimeStampMillis;
                                            wordRegister24Hours.getWordRegister24HoursMap().put(eachWord, occurrrences);
                                            logger.log(Level.INFO, eachWord + " " + Arrays.toString(occurrrences));
                                        }
                                    });
                                });

                            }
                        }
                    }
                    key.reset();
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

    }

}
