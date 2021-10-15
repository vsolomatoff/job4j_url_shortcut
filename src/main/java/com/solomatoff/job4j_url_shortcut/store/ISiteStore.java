package com.solomatoff.job4j_url_shortcut.store;

import com.solomatoff.job4j_url_shortcut.model.Site;

import java.util.Optional;

public interface ISiteStore extends IStore<Site> {

    Optional<Site> findByLogin(String login);

    Optional<Site> findByDomain(String domain);

}
