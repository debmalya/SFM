package org.deb.SFM.service.impl;

import org.deb.SFM.repository.WordRegister24Hours;
import org.deb.SFM.service.SFMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class SFMServiceImpl implements SFMService {

    private static Logger logger = Logger.getLogger("SFMServiceImpl");
    
    /**
     * Number of milliseconds in a day.
     */
    private static final long ONE_DAY_BEFORE = 86400000L;

    @Autowired
    private WordRegister24Hours wordRegister24Hours;

    /**
     *
     * @param word to be checked.
     * @return
     */
    @Override
    public boolean isValidString(String word) {
    	long startTime = System.currentTimeMillis();
        long[] timestamps = wordRegister24Hours.getWordRegister24HoursMap().get(word);
        logger.log(Level.INFO, String.format("Time taken %d milliseconds", System.currentTimeMillis() - startTime));
        long currentTimeStamp = System.currentTimeMillis();
        long oneDayBefore = currentTimeStamp - ONE_DAY_BEFORE;

        logger.log(Level.INFO,String.format("Before 24 hours %d", oneDayBefore));
        logger.log(Level.INFO,String.format("Word : %s and occurrences %s",word,Arrays.toString(timestamps)));
        

        return (Arrays.stream(timestamps).parallel().anyMatch(timestamp -> timestamp < oneDayBefore));

    }
}
