package tqs.weather.AirQuality;


import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.weather.AirQuality.AirQualityApp;

import java.io.IOException;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = AirQualityApp.class)
@AutoConfigureMockMvc

@AutoConfigureTestDatabase
public class RestControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CityResultRepository repo;


    @AfterEach
    public void resetDb() {
        repo.deleteAll();
    }

    @Test
    public void contextLoads() {

    }

    @Test
    public void givenResults_whenGetResults_thenStatus200() throws Exception {
        createTestCityInfos("2","New York");
        createTestCityInfos("3","Florida");

        mvc.perform(get("/cities").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].name", is("New York")))
                .andExpect(jsonPath("$[1].name", is("Florida")));
    }

    private void createTestCityInfos(String id,String name) {
        CityResult cityInfo = new CityResult(id,name);
        repo.saveAndFlush(cityInfo);

    }

}
