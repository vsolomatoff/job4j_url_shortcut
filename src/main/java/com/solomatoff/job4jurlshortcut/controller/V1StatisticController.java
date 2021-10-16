package com.solomatoff.job4jurlshortcut.controller;

import com.solomatoff.job4jurlshortcut.dto.LinkDTO;
import com.solomatoff.job4jurlshortcut.model.Site;
import com.solomatoff.job4jurlshortcut.service.SiteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/v1/statistic")
public class V1StatisticController {

    private final SiteService siteService;

    public V1StatisticController(SiteService siteService) {
        this.siteService = siteService;
    }

    @GetMapping
    public ResponseEntity<List<LinkDTO.Response.StatisticResponse>> statistic() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var optionalSite = siteService.findByLogin(principal.getUsername());
        if (optionalSite.isPresent()) {
            Site site = optionalSite.get();
            return new ResponseEntity<>(
                    site.getLinks()
                        .stream()
                        .map(link -> new LinkDTO.Response.StatisticResponse(
                                link.getLinkUrl(),
                                link.getChallengeCount()
                                )
                        )
                        .collect(toList()),
                    HttpStatus.OK
            );
        } else {
            throw new IllegalArgumentException("Current user is illegal");
        }
    }

}
