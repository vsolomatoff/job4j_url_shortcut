package com.solomatoff.job4jurlshortcut.service;


import com.solomatoff.job4jurlshortcut.dto.LinkDTO;
import com.solomatoff.job4jurlshortcut.model.Link;
import com.solomatoff.job4jurlshortcut.model.Site;

import java.util.Optional;

public interface ILinkService extends IService<Link, Long> {

    Link create(Link link, Site site);

    Optional<Link> findByCode(String code);

    Optional<Link> saveAndGenerateCode(Link link);

    void updateLinkStatistic(long linkId);

    default Link transferLinkRequestToLink(LinkDTO.Request.LinkRequest linkRequest) {
        Link link = new Link();
        link.setLinkUrl(linkRequest.getUrl());
        return link;
    }

    default LinkDTO.Response.CodeResponse transferLinkToRegResponse(Link link) {
        String code = link.getLinkCode();
        return new LinkDTO.Response.CodeResponse(code);
    }

    default LinkDTO.Response.LinkResponse transferLinkToLinkResponse(Link link) {
        return new LinkDTO.Response.LinkResponse(
                link.getId(),
                link.getLinkUrl(),
                link.getLinkCode(),
                link.getChallengeCount()
        );
    }

}
