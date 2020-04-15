package tqs.weather.AirQuality;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CityResultServiceIT {

    // lenient is required because we load some expectations in the setup
    // that are not used in all the tests.
    @Mock( lenient = true)
    private CityResultRepository repo;

    @InjectMocks
    private CityResultService service;

    @BeforeEach
    public void setUp() {
        CityResult lisbon = new CityResult("10","lisbon");
        //lisbon.setId("12");

        CityResult Madrid = new CityResult("3","madrid");
        CityResult Valencia = new CityResult("4","valencia");

        List<CityResult> allLocations = Arrays.asList(lisbon, Madrid, Valencia);

        Mockito.when(repo.findByName(lisbon.getName())).thenReturn(lisbon);
        Mockito.when(repo.findByName(Valencia.getName())).thenReturn(Valencia);
        Mockito.when(repo.findByName("wrong_name")).thenReturn(null);
        Mockito.when(repo.findById(Madrid.getId())).thenReturn(Optional.of(Madrid));
        Mockito.when(repo.findAll()).thenReturn(allLocations);
        Mockito.when(repo.findById("-65")).thenReturn(Optional.empty());
    }

    @Test
    public void contextLoads() {

    }

    //the 2 following test address the city repository
    @Test
    public void whenValidName_thenCityShouldBeFound() {
        String name = "lisbon";
        CityResult found = repo.findByName(name);

        assertThat(found.getName()).isEqualTo(name);
    }

    @Test
    public void whenInValidName_thenCityShouldNotBeFound() {
        CityResult db_val = repo.findByName("wrong_name");
        assertThat(db_val).isNull();

        verifyFindByNameIsCalledOnce("wrong_name");
    }
    /////////



    @Test
    public void whenNonExistingName_thenCityShouldntExist() {
        boolean City_exist = false;
        if (repo.findByName("randName") != null) { // it returns false once "randName" does not exist in the repository
            City_exist = true;
        }
        assertThat(City_exist).isEqualTo(false);

        verifyFindByNameIsCalledOnce("randName");
    }


    @Test
    public void whenValidName_thenCityShouldExist() {
        boolean City_exist = false;
        if (repo.findByName("lisbon") != null) { // it returns true once "lisbon"  exist in the repository
            City_exist = true;
        }
        assertThat(City_exist).isEqualTo(true);

        verifyFindByNameIsCalledOnce("lisbon");
    }

    @Test
    public void whenValidId_thenCityShouldBeFound() {
        CityResult db_val = service.getCityInfoById("3");
        System.out.println("db_val " + db_val.getName());
        assertThat(db_val.getName()).isEqualTo("madrid");

        verifyFindByIdIsCalledOnce();
    }

    @Test
    public void whenInValidId_thenCityShouldNotBeFound() {
        CityResult db_val = service.getCityInfoById("-65");
        verifyFindByIdIsCalledOnce();
        assertThat(db_val).isNull();
    }

    @Test
    public void given3Cities_thenReturn3Records() {
        CityResult lisbon = new CityResult("10","lisbon");
        CityResult madrid = new CityResult("2","madrid");
        CityResult valencia = new CityResult("3","valencia");

        List<CityResult> allCityInfos = service.getAllCityData();
        verifyFindAllCitiesIsCalledOnce();
        assertThat(allCityInfos).hasSize(3).extracting(CityResult::getName).contains(lisbon.getName(), madrid.getName(), valencia.getName());
    }

    @Test
    public void getAllCities(){
        List<CityResult> citiesList = service.getAllCityData();
        assertEquals(citiesList.size(), repo.findAll().size());
    }


    //////////////////////////////////////////////////////////////



    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(repo, VerificationModeFactory.times(1)).findById(Mockito.anyString());
        Mockito.reset(repo);
    }

    private void verifyFindByNameIsCalledOnce(String name) {
        Mockito.verify(repo, VerificationModeFactory.times(1)).findByName(name);
        Mockito.reset(repo);
    }


    private void verifyFindAllCitiesIsCalledOnce() {
        Mockito.verify(repo, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(repo);
    }
}
