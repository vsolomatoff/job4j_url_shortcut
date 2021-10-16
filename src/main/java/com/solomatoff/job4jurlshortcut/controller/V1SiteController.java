package com.solomatoff.job4jurlshortcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solomatoff.job4jurlshortcut.dto.SiteDTO;
import com.solomatoff.job4jurlshortcut.exception.IllegalFieldException;
import com.solomatoff.job4jurlshortcut.model.Site;
import com.solomatoff.job4jurlshortcut.service.ISiteService;
import com.solomatoff.job4jurlshortcut.validation.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class V1SiteController {

    private final ISiteService siteService;

    private static final Logger LOGGER = LoggerFactory.getLogger(V1SiteController.class.getSimpleName());

    private final ObjectMapper objectMapper;

    public V1SiteController(final ISiteService siteService,
                            ObjectMapper objectMapper) {
        this.siteService = siteService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/site/")
    public List<Site> findAll() {
        return new ArrayList<>(siteService.findAll());
    }

    @GetMapping("/site/{id}")
    public ResponseEntity<Site> findById(@PathVariable long id) {
        Optional<Site> optionalSite = siteService.findById(id);
        return optionalSite
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Site is not found. Please, check ID.")
                );
    }

    @PostMapping("/site/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<SiteDTO.Response.SiteResponse> newSite(
            @Valid @RequestBody SiteDTO.Request.SiteRequest siteRequest
    ) {
        Site site = siteService.transferSiteRequestToSite(siteRequest);
        return new ResponseEntity<>(
                siteService.transferSiteToSiteResponse(siteService.create(site)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/registration")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<SiteDTO.Response.RegResponse> registration(
            @Valid @RequestBody SiteDTO.Request.RegRequest regRequest
    ) {
        Site site = siteService.transferRegRequestToSite(regRequest);
        return new ResponseEntity<>(
                siteService.transferSiteToRegResponse(siteService.create(site)),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/site/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        var optionalSite = siteService.findById(id);
        if (optionalSite.isPresent()) {
            Site site = optionalSite.get();
            siteService.deactivate(site);
            return ResponseEntity.ok().build();
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Site is not found. Please, check ID."
            );
        }
    }

    @ExceptionHandler(value = { IllegalFieldException.class })
    public void exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
        LOGGER.error(e.getLocalizedMessage());
    }

}