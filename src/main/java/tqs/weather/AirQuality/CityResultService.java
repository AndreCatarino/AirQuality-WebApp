package tqs.weather.AirQuality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityResultService {

    @Autowired
    private CityResultRepository repository;


    public List<CityResult> getAllCityData(){
        List<CityResult> cityResults = new ArrayList<>();
                repository.findAll()
                .forEach(cityResults::add);
                return cityResults;

    }

    public CityResult getCityInfoById(String id){

        return repository.findById(id).orElse(null);
    }


    public void add(CityResult cityResult) {
        repository.save(cityResult);
    }




    public List<CityResult> getCityInfoByName(String name){
        List<CityResult> cityResults;
        cityResults = repository.findAll();
        List<CityResult> list_cityResults = new ArrayList<>();
        for (CityResult cr : cityResults) {
            if (cr.getName().toLowerCase().contains(name.toLowerCase())) {
                list_cityResults.add(cr);
            }
        }
        return list_cityResults;
    }




}
