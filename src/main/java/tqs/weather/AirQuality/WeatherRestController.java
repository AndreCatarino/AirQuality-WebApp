package tqs.weather.AirQuality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WeatherRestController {


    @Autowired
    private CityResultService service;
    @Autowired
    private CityResultRepository repo;


    @PostMapping ("/city")
    public void addCityInfo(@RequestBody CityResult cityResult) {

        service.add(cityResult);
    }


    @GetMapping("/cities")
    public List<CityResult> getAllResults() {
        return repo.findAll();
    }

/*
    @GetMapping("/City/{id}")
    public ResponseEntity<CityResult> getResultById(@PathVariable(value = "id") String cityID) throws ResourceNotFoundException {

        CityResult result =  repo.findById(cityID)
                .orElseThrow(() -> new ResourceNotFoundException("City not found for this id :: " + cityID));

        return CityResult.ok().body(result);
    }
*/


    @PutMapping("/City/{id}")
    public ResponseEntity<CityResult> updateResult(@PathVariable(value = "id") String ResultID,
                                                   @Valid @RequestBody CityResult ResultDetails) throws ResourceNotFoundException {
        CityResult result = (CityResult) repo.findById(ResultID)

                .orElseThrow(() -> new ResourceNotFoundException("city not found for this id :: " + ResultID));


        result.setName(ResultDetails.getName());

        final CityResult updatedResult = repo.save(result);
        return ResponseEntity.ok(updatedResult);
    }


    @DeleteMapping("/City/{id}")
    public Map<String, Boolean> deleteResult(@PathVariable(value = "id") String cityID)
            throws ResourceNotFoundException {
        CityResult result = repo.findById(cityID)
                .orElseThrow(() -> new ResourceNotFoundException("city not found for this id :: " + cityID));

        repo.delete(result);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


}
