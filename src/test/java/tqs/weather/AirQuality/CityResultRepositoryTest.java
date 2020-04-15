package tqs.weather.AirQuality;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.weather.AirQuality.CityResult;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CityResultRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CityResultRepository ResultRepo;

    @Test
    public void contextLoads() {

    }



    @Test
    public void whenFindById_thenReturnResult() {
        CityResult res = new CityResult("1","test");
        entityManager.persistAndFlush(res);

        CityResult db_val = ResultRepo.findById(res.getId()).orElse(null);
        assertThat(db_val.getName()).isEqualTo(res.getName());
    }

    @Test
    public void whenInvalidId_thenReturnNull() {
        CityResult db_val = ResultRepo.findById("-11").orElse(null);
        assertThat(db_val).isNull();
    }


    @Test
    public void whenValidName_thenCityShouldBeFound() {
        CityResult res = new CityResult("1","test");
        entityManager.persistAndFlush(res);
        String name = "test";
        CityResult found = ResultRepo.findByName(name);

        assertThat(found.getName()).isEqualTo(name);
    }

    @Test
    public void whenInValidName_thenCityShouldNotBeFound() {
        CityResult db_val = ResultRepo.findByName("wrong_name");
        assertThat(db_val).isNull();

    }

    @Test
    public void whenFindByName_thenReturnResult() {
        CityResult miami = new CityResult("5","Miami");
        entityManager.persistAndFlush(miami);

        CityResult found = ResultRepo.findByName(miami.getName());
        assertThat(found.getName()).isEqualTo(miami.getName());
    }


    @Test
    public void whenInvalidName_thenReturnNull() {
        CityResult Db_val = ResultRepo.findByName("doesNotExist");
        assertThat(Db_val).isNull();
    }

    @Test
    public void givenSetOfLocations_whenFindAll_thenReturnAllLocations() {
        CityResult Miami = new CityResult("2","Miami");
        CityResult Vegas = new CityResult("3","Vegas");
        CityResult LA = new CityResult("4","LA");

        entityManager.persist(Miami);
        entityManager.persist(Vegas);
        entityManager.persist(LA);
        entityManager.flush();

        List<CityResult> allResults = ResultRepo.findAll();

        assertThat(allResults).hasSize(3).extracting(CityResult::getName).containsOnly(Miami.getName(), Vegas.getName(), LA.getName());
    }
}
