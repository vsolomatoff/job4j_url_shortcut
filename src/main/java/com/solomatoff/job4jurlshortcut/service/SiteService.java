package com.solomatoff.job4jurlshortcut.service;

import com.solomatoff.job4jurlshortcut.exception.IllegalFieldException;
import com.solomatoff.job4jurlshortcut.model.Site;
import com.solomatoff.job4jurlshortcut.store.ISiteStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class SiteService implements ISiteService {
    @Autowired
    @Qualifier("siteStore")
    private ISiteStore siteStore;

    private final BCryptPasswordEncoder encoder;

    public SiteService(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public Optional<Site> saveOrUpdate(Site site) {
        return siteStore.saveOrUpdate(site);
    }

    @Override
    public Collection<Site> findAll() {
        return siteStore.findAll();
    }

    @Override
    public Optional<Site> findById(Long id) {
        return siteStore.findById(id);
    }

    @Override
    public void delete(Site site) {
        siteStore.delete(site);
    }

    @Override
    public Optional<Site> findByLogin(String login) {
        return siteStore.findByLogin(login);
    }

    @Override
    public Optional<Site> findByDomain(String domain) {
        return siteStore.findByDomain(domain);
    }

    @Override
    public Site create(Site site) {
        if (site != null) {
            if (site.getLinks().size() != 0) {
                throw new IllegalArgumentException("The new site should not contain links");
            }
            if (site.getId() == null) {
                // Проверим уникальность доменного имени сайта
                var ss = findByDomain(site.getDomainUrl());
                if (ss.isEmpty()) {
                    // Проверим уникальность login
                    ss = findByLogin(site.getLogin());
                    if (ss.isEmpty()) {
                        // Проверим корректность логина
                        if (site.getLogin().length() < 3 || site.getLogin().length() > 50) {
                            throw new IllegalFieldException("Invalid login");
                        }
                        // Проверим корректность пароля
                        checkPassword(site.getPassword());
                        // Сохраним и закодируем пароль
                        String password = site.getPassword();
                        site.setPassword(encoder.encode(site.getPassword()));
                        // Сохраняем
                        var optionalSite = saveOrUpdate(site);
                        if (optionalSite.isPresent()) {
                            Site site1 = optionalSite.get();
                            // Меняем пароль на незакодированный
                            site1.setPassword(password);
                            return site1;
                        } else {
                            throw new IllegalFieldException("Error saving to database");
                        }
                    } else {
                        throw new IllegalArgumentException("Site login is duplicated");
                    }
                } else {
                    throw new IllegalArgumentException("Site domain is duplicated");
                }
            } else {
                throw new IllegalArgumentException("Site ID must be empty");
            }
        } else {
            throw new IllegalArgumentException("Site mustn't be empty");
        }
    }

    @Override
    public void deactivate(Site site) {
        site.setActive(false);
        saveOrUpdate(site);
    }

    private void checkPassword(String password) {
        if (password.length() < 3 || password.length() > 255) {
            throw new IllegalFieldException("Invalid password");
        }
    }

}
