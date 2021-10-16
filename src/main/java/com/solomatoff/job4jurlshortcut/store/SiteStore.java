package com.solomatoff.job4jurlshortcut.store;

import com.solomatoff.job4jurlshortcut.model.Site;
import com.solomatoff.job4jurlshortcut.repository.SiteRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class SiteStore implements ISiteStore {

    private final SiteRepository siteRepository;

    public SiteStore(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    @Override
    public Collection<Site> findAll() {
        return (Collection<Site>) siteRepository.findAll();
    }

    @Override
    public Optional<Site> findById(long id) {
        return siteRepository.findById(id);
    }

    @Override
    public Optional<Site> saveOrUpdate(Site site) {
        return Optional.of(siteRepository.save(site));
    }

    @Override
    public void delete(Site site) {
        siteRepository.delete(site);
    }

    @Override
    public Optional<Site> findByLogin(String login) {
        return siteRepository.findByLogin(login);
    }

    @Override
    public Optional<Site> findByDomain(String domain) {
        return siteRepository.findByDomain(domain);
    }
}