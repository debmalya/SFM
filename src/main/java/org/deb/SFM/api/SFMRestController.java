package org.deb.SFM.api;

import org.deb.SFM.model.SFMResponse;
import org.deb.SFM.service.SFMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class SFMRestController {

    private static final Logger logger = Logger.getLogger("SFMRestController");

    @Autowired
    private SFMService sfmService;

    @RequestMapping(value = "/isStringValid",produces = "application/json")
    public ResponseEntity<SFMResponse> isValid(@RequestParam(value = "string", required = true)String word){
        ResponseEntity<SFMResponse> response = null;
        try {
           boolean result =  sfmService.isValidString(word);
            SFMResponse sfmResponse = new SFMResponse();
           if (result){
               sfmResponse.setResponse("true");
           }else{
               sfmResponse.setResponse("false");
           }
           response = new ResponseEntity<>(sfmResponse,HttpStatus.OK);
        }catch(Throwable th){
            logger.log(Level.SEVERE,th.getMessage(),th);
            throw new RuntimeException(th.getMessage());
        }
        return response;
    }



}
