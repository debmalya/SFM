package org.deb.SFM;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.deb.SFM.config.ConfigurationComponent;
import org.deb.SFM.repository.WordRegister24Hours;
import org.deb.SFM.service.SFMService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = SFMApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class SfmApplicationTests {

    private static String existingWord = "ExistingWord";

    private static String nonExistingWord = "NonExistingWord";


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

    }

    @After
    public void clean(){

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
