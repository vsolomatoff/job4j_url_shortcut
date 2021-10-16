package com.solomatoff.job4jurlshortcut.store;

import com.solomatoff.job4jurlshortcut.model.Site;

import java.util.Optional;

public interface ISiteStore extends IStore<Site> {

    Optional<Site> findByLogin(String login);

    Optional<Site> findByDomain(String domain);

}
