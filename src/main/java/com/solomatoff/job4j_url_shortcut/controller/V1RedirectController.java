package com.solomatoff.job4j_url_shortcut.controller;

import com.solomatoff.job4j_url_shortcut.model.Link;
import com.solomatoff.job4j_url_shortcut.service.LinkService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/redirect")
public class V1RedirectController {

    private final LinkService linkService;

    public V1RedirectController(LinkService linkService) {
        this.linkService = linkService;
    }

    @Transactional
    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable("code") String code) {
        Optional<Link> optionalLink = linkService.findByCode(code);
        if (optionalLink.isEmpty()) {
            throw new IllegalArgumentException("Url is illegal");
        }
        Link link = optionalLink.get();
        if (link.getId() == null) {
            throw new IllegalArgumentException("Code is illegal");
        }
        this.linkService.updateLinkStatistic(link.getId());
        HttpStatus statusResponse = HttpStatus.FOUND;
        return new ResponseEntity<>(buildHeaders(link, statusResponse), statusResponse);
    }


    private HttpHeaders buildHeaders(Link link, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("HTTP CODE", String.valueOf(status.value()));
        headers.add("REDIRECT", link.getLinkUrl());
        return headers;
    }
}
