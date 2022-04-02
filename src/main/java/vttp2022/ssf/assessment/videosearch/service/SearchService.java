package vttp2022.ssf.assessment.videosearch.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp2022.ssf.assessment.videosearch.models.Game;

@Service
public class SearchService {

    private static Logger logger = Logger.getLogger(SearchService.class.getName());

    @Value("${rawg.api.key}")
    private String apiKey;

    private String url = "https://api.rawg.io/api/games";

    public List<Game> search(String searchString, Integer count) {
        url = UriComponentsBuilder
                .fromUriString(url)
                .queryParam("search", searchString)
                .queryParam("page_size", count)
                .queryParam("key", apiKey)
                .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);

        JsonObject data = null;
        try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())) {
            JsonReader reader = Json.createReader(is);
            data = reader.readObject();
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }

        List<Game> gamesList = new LinkedList<>();

        JsonArray results = data.getJsonArray("results");
        //System.out.println(">>>>> data: " + data.toString());   
        for (JsonValue uniqueGame: results) {
            Game game = new Game();
            game.setName(uniqueGame.asJsonObject().getString("name"));
            game.setBackgroundImage(uniqueGame.asJsonObject().getString("background_image"));
            JsonValue rating = uniqueGame.asJsonObject().get("rating");
            //System.out.println(">>>>>>>> Rating: " + rating.toString());
            game.setRating(Float.parseFloat(rating.toString()));
            //System.out.println(">>>> GameToString: " + game.toString());
            gamesList.add(game);
        }
        return gamesList;
        
    }
}
