package com.solomatoff.job4jurlshortcut.service;


import com.solomatoff.job4jurlshortcut.dto.SiteDTO;
import com.solomatoff.job4jurlshortcut.model.Site;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Optional;
import java.util.UUID;

public interface ISiteService extends IService<Site, Long> {

    int PASSWORD_LENGTH = 6;

    Optional<Site> findByLogin(String login);

    Optional<Site> findByDomain(String login);

    Site create(Site model);

    void deactivate(Site model);

    default Site transferRegRequestToSite(SiteDTO.Request.RegRequest regRequest) {
        Site site = new Site();
        site.setDomainUrl(regRequest.getSite());
        site.setActive(true);
        site.setLogin(UUID.randomUUID().toString());
        site.setPassword(RandomStringUtils.random(PASSWORD_LENGTH, true, true));
        return site;
    }

    default Site transferSiteRequestToSite(SiteDTO.Request.SiteRequest siteRequest) {
        Site site = new Site();
        site.setDomainUrl(siteRequest.getSite());
        site.setActive(true);
        site.setLogin(siteRequest.getLogin());
        site.setPassword(siteRequest.getPassword());
        return site;
    }

    default SiteDTO.Response.RegResponse transferSiteToRegResponse(Site site) {
        String registration = site.isActive()
                + ", login: " + site.getLogin()
                + ", password: " + site.getPassword();
        return new SiteDTO.Response.RegResponse(registration);
    }

    default SiteDTO.Response.SiteResponse transferSiteToSiteResponse(Site site) {
        return new SiteDTO.Response.SiteResponse(
                site.getId(),
                site.getDomainUrl(),
                site.getLogin(),
                site.getPassword(),
                site.getCurrentToken(),
                site.isActive(),
                site.getRegistered(),
                site.getLinks()
        );
    }

}
