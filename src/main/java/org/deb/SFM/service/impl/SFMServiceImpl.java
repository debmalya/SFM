package org.deb.SFM.service.impl;

import org.deb.SFM.repository.WordRegister24Hours;
import org.deb.SFM.service.SFMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class SFMServiceImpl implements SFMService {

    @Autowired
    private WordRegister24Hours wordRegister24Hours;

    /**
     *
     * @param word to be checked.
     * @return
     */
    @Override
    public boolean isValidString(String word) {
        long[] timestamps = wordRegister24Hours.getWordRegister24HoursMap().get(word);
        long currentTimeStamp = System.currentTimeMillis();
        long oneDayBefore = currentTimeStamp - 24 * 60 * 600000;

        return (Arrays.stream(timestamps).anyMatch(timestamp -> timestamp < oneDayBefore));

    }
}
