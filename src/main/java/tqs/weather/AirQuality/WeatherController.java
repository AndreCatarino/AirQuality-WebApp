package tqs.weather.AirQuality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import java.util.Timer;
import java.util.TimerTask;

@Controller
public class WeatherController {


    @Autowired
    private CityResultRepository repo;

    @Autowired
    private CityResultService service;




    @GetMapping("/City/{id}")
    public String CityInfo(@PathVariable(value="id") String id, Model model) {
        model.addAttribute("id", id);
        CityResult c_res  = service.getCityInfoById(id);
        model.addAttribute("City", c_res);
        return "ShowResult";
    }

    @GetMapping("/City/ByName/{name}")
    public String getAllCityInfoByName(@PathVariable(value = "name") String name, Model model){
        model.addAttribute("cityDataRepositoryByName", service.getCityInfoByName(name));
        return "CitiesResultsByName";
    }
/*
    @GetMapping("/City/ID/{id}")
    public String getAllCityInfoByID(@PathVariable(value = "id") String id, Model model){
        assertNotNull(service.getCityInfoById(id));
        model.addAttribute("cityDataRepositoryById", repo.findById(id));
        return "CitiesResultsById";
    }
*/

    @GetMapping("/Cities")
    public String CityInfo(Model model) {
        model.addAttribute("cityDataRepository", repo.findAll());
        return "AllInfo";
    }


    public void ClearCache (int seconds){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                repo.deleteAll();
                ClearCache(10000);
            }
        }, seconds * 1000);
    }


}
