package pl.kornijasz.pogodynka;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;
import pl.kornijasz.pogodynka.model.City;
import pl.kornijasz.pogodynka.model.WeatherModel;

@Controller
public class WeatherController {
    private City city = new City("Kraków");
    @Value("${apikey}")
    private String apikey;
    @Value("${language}")
    private String lang;
    @Value("${unit}")
    private String unit;
    private String url = "http://api.openweathermap.org/data/2.5/weather?";
    private String srcUrl = "https://openweathermap.org/img/wn/";

    @GetMapping
    public String get(Model model) {
        model.addAttribute("city", city);
        model.addAttribute("weather", getWeather(city));
        return "weather";
    }

    @PostMapping("/city")
    public String setCity(@ModelAttribute City city) {
        this.city.setName(city.getName());
        return "redirect:/";
    }

    private WeatherModel getWeather(City city) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            WeatherModel weather = restTemplate.getForObject(url + "q=" + city.getName() + "&lang=" + lang + "&units=" + unit + "&appid=" + apikey, WeatherModel.class);
            weather.setSrc(srcUrl + weather.getWeather().get(0).getIcon() + "@2x.png");
            System.out.println(weather.getSrc());
            return weather;
        } catch (HttpStatusCodeException e) {
            return null;
        } catch (UnknownContentTypeException e) {
            return null;
        }
    }

}
