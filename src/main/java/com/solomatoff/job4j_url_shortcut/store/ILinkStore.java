package com.solomatoff.job4j_url_shortcut.store;

import com.solomatoff.job4j_url_shortcut.model.Link;

import java.util.Optional;

public interface ILinkStore extends IStore<Link> {

    Optional<Link> findByLinkUrl(String url);

    Optional<Link> findByLinkCode(String code);

    void updateLinkStatistic(long linkId);

}
