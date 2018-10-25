package org.deb.SFM;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.deb.SFM.config.ConfigurationComponent;
import org.deb.SFM.repository.WordRegister24Hours;
import org.deb.SFM.service.SFMService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = SFMApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class SfmApplicationTests {


    private static String fileName;

    private static String existingWord = "ExistingWord";

    private static String nonExistingWord = "NonExistingWord";

    private static final Logger logger = Logger.getLogger("SpringBootTest");

    private static final SimpleDateFormat yyyyMMddhh = new SimpleDateFormat("yyyyMMddhh");

    @Autowired
    private  ConfigurationComponent configurationComponent;

    @Autowired
    private WordRegister24Hours wordRegister24Hours;

    @LocalServerPort
    private int port;

    @Before
    public void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;


        long[] occurrences = new long[configurationComponent.getWordCount()+1];
        for (int i = 0; i < configurationComponent.getWordCount()+1; i++){
            occurrences[i] = System.currentTimeMillis();
        }
        wordRegister24Hours.getWordRegister24HoursMap().put(existingWord,occurrences);
        /*
        String fileSuffix = yyyyMMddhh.format(new Date());
        fileName = configurationComponent.getFolder()+File.separator+"string-generation-"+fileSuffix+".log";
        try(PrintWriter writer = new PrintWriter(new File(fileName))){
            int i = 0;
            StringBuilder sb = new StringBuilder(String.valueOf(epochTimeMilliSeconds));
            sb.append(",");
            while (i <= configurationComponent.getWordCount()){
                sb.append(existingWord);
                sb.append(" ");
                i++;
            }
            Thread.sleep(5000);


        }catch(IOException ioe){
            logger.log(Level.SEVERE,ioe.getMessage(),ioe);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE,e.getMessage(),e);
        }
         */
    }

    @After
    public void clean(){
        /*
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
            logger.log(Level.INFO,fileName+" deleted successfully");
        }
        */
    }


    @Autowired
    private SFMService sfmService;

	@Test
	public void contextLoads() {
	}

	/**
	 * Test for a non existing word. It should return true.
	 */
	@Test
    public void testLessThan5Times(){
	    Assert.assertNotNull(sfmService);
	    Assert.assertTrue(sfmService.isValidString(nonExistingWord));

    }

    /**
     * Test for a non existing word. It should return true.
     */
    @Test
    public void testMoreThan5Times(){
        Assert.assertNotNull(sfmService);
        Assert.assertFalse(sfmService.isValidString(existingWord));

    }

    @Test
    public void testNonExistingWordUsingApi() {
        when()
                .get("/isStringValid?string="+nonExistingWord)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(is(equalTo("{\"response\":\"true\"}")));
    }

    @Test
    public void testExistingWordUsingApi() {
        when()
                .get("/isStringValid?string="+existingWord)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(is(equalTo("{\"response\":\"false\"}")));
    }


}