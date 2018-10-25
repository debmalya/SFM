package org.deb.SFM.service;

import org.springframework.stereotype.Service;

@Service
public interface SFMService {
    /**
     *
     * @param word to be checked.
     * @return false if word appears more than 5 times in the last 24 hours, true otherwise.
     */
    public boolean isValidString(String word);
}
