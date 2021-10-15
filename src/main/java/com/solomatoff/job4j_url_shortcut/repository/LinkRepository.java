package com.solomatoff.job4j_url_shortcut.repository;

import com.solomatoff.job4j_url_shortcut.model.Link;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LinkRepository extends CrudRepository<Link, Long> {

    Optional<Link> findByLinkUrl(String url);

    Optional<Link> findByLinkCode(String code);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("update Link a set a.challengeCount = a.challengeCount + 1 where a.id = ?1")
    void incrementChallengeCount(long id);

}
