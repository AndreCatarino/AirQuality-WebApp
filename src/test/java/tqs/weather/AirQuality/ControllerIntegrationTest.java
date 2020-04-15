package tqs.weather.AirQuality;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = AirQualityApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void contextLoads() {

    }

    @Test
    public void testGetAllResult() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/cities",
                HttpMethod.GET, entity, String.class);

        assertNotNull(response.getBody());
    }

    @Test
    public void testCreateResult() {
        CityResult result = new CityResult("5", "Miami");

        //result.setName("Miami");
        //result.setId("5");

        ResponseEntity<CityResult> postResponse = restTemplate.postForEntity(getRootUrl() + "/city", result, CityResult.class);
        assertNotNull(postResponse);

    /*
    @Test
    public void testGetResultById() {
        CityResult res = new CityResult ("3","LA");
        CityResult result = restTemplate.getForObject(getRootUrl() + "/City/3", CityResult.class);
        System.out.println(result.getName());
        assertNotNull(result);
    }



    }

    @Test
    public void testUpdateResult() {
        String id = "1";
        CityResult result = restTemplate.getForObject(getRootUrl() + "/City/" + id, CityResult.class);


        result.setName("MiamiVice");
        restTemplate.put(getRootUrl() + "/City/" + id, result);

        CityResult updatedResult = restTemplate.getForObject(getRootUrl() + "/City/" + id, CityResult.class);
        assertNotNull(updatedResult);
    }

    @Test
    public void testDeleteResult() {
        String id = "2";
        CityResult result = restTemplate.getForObject(getRootUrl() + "/City/" + id, CityResult.class);
        //assertNotNull(result);

        restTemplate.delete(getRootUrl() + "/City/" + id);

        try {
            result = restTemplate.getForObject(getRootUrl() + "/City/" + id, CityResult.class);
        } catch (final HttpClientErrorException e) {
            assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }
    */
    }
}
