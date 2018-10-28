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
		String onlyFileName = "";
		int arrayLength = config.getWordCount();
		String fileExtension = config.getExtension();
		pattern = Pattern.compile(config.getPattern());

		try {

			Path dir = Paths.get(config.getFolder());

			if (dir != null) {
				WatchKey key = dir.register(watcher, ENTRY_CREATE);

				if (null != (key = watcher.take())) {

					long startTime = System.currentTimeMillis();

					for (WatchEvent<?> event : key.pollEvents()) {
						logger.log(Level.INFO,
								String.format("Event kind: %s , file affected: %s", event.kind(), event.context()));
						onlyFileName = event.context().toString();
						processEachFile(arrayLength, onlyFileName, fileExtension);
					}
					key.reset();
					logger.log(Level.INFO, String.format("Completed processing, took %d milliseconds.",
							(System.currentTimeMillis() - startTime)));
				}
			}
		} catch (IOException | InterruptedException e) {
			int retryCount = 0;
			int retryLimit = 3;
			boolean isOK = false;
			logger.log(Level.SEVERE, e.getMessage(), e);
			while (retryCount < retryLimit) {
				try {
					if (!"".equals(onlyFileName)) {
						processEachFile(arrayLength, onlyFileName, fileExtension);
					}
					isOK = true;
					break;
				} catch (IOException e1) {
					logger.log(Level.SEVERE, String.format("Retrying... %s", e.getMessage()), e);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e2) {
						logger.log(Level.SEVERE, String.format("Retrying... %s", e.getMessage()), e);
					}
				}
				retryCount++;
			}
			if (!isOK) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}

	}

	/**
	 * This will process each file and update word frequency.
	 * 
	 * @param arrayLength
	 *            - this is word count.
	 * @param onlyFileName
	 *            - file name with extension, path not included.
	 * @param fileExtension
	 *            - file extension to be considered for processing.
	 * @throws IOException
	 *             - if file does not exist.
	 */
	private void processEachFile(int arrayLength, String onlyFileName, String fileExtension) throws IOException {
		if (pattern.matcher(onlyFileName).find() && onlyFileName.endsWith(fileExtension)) {
			try (BufferedReader bufferedLogReader = Files
					.newBufferedReader(Paths.get(config.getFolder() + File.separator + onlyFileName))) {

				bufferedLogReader.lines().forEach(e -> {

					String[] fieldValues = e.split(",");
					long epochTimeStampMillis = Long.parseLong(fieldValues[0]);

					String[] words = fieldValues[1].split("\\s");
					Arrays.stream(words).forEach(eachWord -> {
						if (eachWord.length() > 0) {
							long[] occurrences = wordRegister24Hours.getWordRegister24HoursMap().get(eachWord);

							for (int i = 0; i < occurrences.length - 1; i++) {
								occurrences[i] = occurrences[i + 1];
							}

							occurrences[arrayLength] = epochTimeStampMillis;

							wordRegister24Hours.getWordRegister24HoursMap().put(eachWord, occurrences);
							// logger.log(Level.INFO, String.format("Word %s and
							// occurrences %s", eachWord,
							// Arrays.toString(occurrences)));

						}
					});
				});

			}
		}
	}

}
