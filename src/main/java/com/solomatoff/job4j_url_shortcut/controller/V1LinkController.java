package com.solomatoff.job4j_url_shortcut.controller;

import com.solomatoff.job4j_url_shortcut.dto.LinkDTO;
import com.solomatoff.job4j_url_shortcut.model.Link;
import com.solomatoff.job4j_url_shortcut.model.Site;
import com.solomatoff.job4j_url_shortcut.service.ILinkService;
import com.solomatoff.job4j_url_shortcut.service.ISiteService;
import com.solomatoff.job4j_url_shortcut.validation.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1")
public class V1LinkController {

    private final ISiteService siteService;
    private final ILinkService linkService;

    public V1LinkController(ISiteService siteService, ILinkService linkService) {
        this.siteService = siteService;
        this.linkService = linkService;
    }


    @GetMapping("/site/{idSite}/link/")
    public List<Link> findLinksAll(@PathVariable long idSite) {
        var site = siteService.findById(idSite);
        return site
                .map(value -> new ArrayList<>(value.getLinks()))
                .orElseGet(ArrayList::new);
    }

    @GetMapping("/site/{idSite}/link/{id}")
    public ResponseEntity<Link> findLinkById(@PathVariable long idSite, @PathVariable long id) {
        var site = siteService.findById(idSite);
        if (site.isPresent()) {
            Link link = site.get().readLink(id);
            if (link != null) {
                return new ResponseEntity<>(link, HttpStatus.OK);
            } else {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Link is not found. Please, check ID."
                );
            }
        } else {
            throw new IllegalArgumentException("Site ID is illegal");
        }
    }

    @PostMapping("/site/{idSite}/link/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<LinkDTO.Response.LinkResponse> newLink(
            @Valid @RequestBody LinkDTO.Request.LinkRequest siteRequest
    ) {
        Link link = linkService.transferLinkRequestToLink(siteRequest);
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var optionalSite = siteService.findByLogin(authUser.getUsername());
        if (optionalSite.isPresent()) {
            Site site = optionalSite.get();
            return new ResponseEntity<>(
                    linkService.transferLinkToLinkResponse(linkService.create(link, site)),
                    HttpStatus.CREATED
            );
        } else {
            throw new IllegalArgumentException("Site ID is illegal");
        }
    }

    @PostMapping("/convert")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<LinkDTO.Response.CodeResponse> registration(
            @Valid @RequestBody LinkDTO.Request.LinkRequest regRequest
    ) {
        Link link = linkService.transferLinkRequestToLink(regRequest);
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var optionalSite = siteService.findByLogin(authUser.getUsername());
        if (optionalSite.isPresent()) {
            Site site = optionalSite.get();
            return new ResponseEntity<>(
                    linkService.transferLinkToRegResponse(linkService.create(link, site)),
                    HttpStatus.CREATED
            );
        } else {
            throw new IllegalArgumentException("Current user is illegal");
        }
    }

    @DeleteMapping("/site/{idSite}/link/{id}")
    public ResponseEntity<Void> deleteLink(@PathVariable long idSite, @PathVariable long id) {
        var optionalSite = siteService.findById(idSite);
        if (optionalSite.isPresent()) {
            Site site = optionalSite.get();
            Link link = site.readLink(id);
            if (link != null) {
                // Удалим сущность через метод сущности Site
                site.deleteLink(link);
                // Сохраняем сущность Site
                siteService.saveOrUpdate(site);
                return ResponseEntity.ok().build();
            } else {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Link is not found. Please, check ID."
                );
            }
        } else {
            throw new IllegalArgumentException("Site ID is illegal");
        }
    }

}