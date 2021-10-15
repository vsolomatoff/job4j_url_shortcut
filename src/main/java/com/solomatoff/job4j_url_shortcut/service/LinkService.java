package com.solomatoff.job4j_url_shortcut.service;

import com.solomatoff.job4j_url_shortcut.model.Link;
import com.solomatoff.job4j_url_shortcut.model.Site;
import com.solomatoff.job4j_url_shortcut.store.ILinkStore;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class LinkService implements ILinkService {
    @Autowired
    @Qualifier("linkStore")
    private ILinkStore linkStore;

    private static final int CODE_LENGTH = 10;

    @Override
    public Optional<Link> saveOrUpdate(Link link) {
        return linkStore.saveOrUpdate(link);
    }

    @Override
    public Collection<Link> findAll() {
        return linkStore.findAll();
    }

    @Override
    public Optional<Link> findById(Long id) {
        return linkStore.findById(id);
    }

    @Override
    public void delete(Link link) {
        linkStore.delete(link);
    }


    @Override
    public Link create(Link link, Site site) {
        site.addLink(link);
        return saveAndGenerateCode(link).orElse(link);
    }

    @Override
    public Optional<Link> saveAndGenerateCode(Link link) {
        Optional<Link> optionalLink = linkStore.findByLinkUrl(link.getLinkUrl());
        if (optionalLink.isPresent()) {
            return optionalLink;
        }
        link.setLinkCode(RandomStringUtils.random(CODE_LENGTH, true, true));
        return linkStore.saveOrUpdate(link);
    }

    @Override
    public Optional<Link> findByCode(String code) {
        return linkStore.findByLinkCode(code);
    }


    @Override
    public void updateLinkStatistic(long linkId) {
        linkStore.updateLinkStatistic(linkId);
    }

}
