package vttp2022.ssf.assessment.videosearch.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.ssf.assessment.videosearch.models.Game;
import vttp2022.ssf.assessment.videosearch.service.SearchService;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchSvc;

    @GetMapping("/search")
    public String showGames(
        @RequestParam(name="searchName") String searchName, 
        @RequestParam(name="noOfGames") Integer noOfGames,
        Model model) {
        
        List<Game> listOfGames = searchSvc.search(searchName, noOfGames);
        model.addAttribute("listOfGames", listOfGames);

        return "showGames";
    }


}
