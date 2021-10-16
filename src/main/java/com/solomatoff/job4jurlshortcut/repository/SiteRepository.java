package com.solomatoff.job4jurlshortcut.repository;

import com.solomatoff.job4jurlshortcut.model.Site;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SiteRepository extends CrudRepository<Site, Long> {

    @NotNull
    @Query("SELECT distinct a FROM Site a "
                + "LEFT JOIN FETCH a.links b "
            + "ORDER BY a.id")
    Iterable<Site> findAll();

    @Query("SELECT distinct a FROM Site a "
                + "LEFT JOIN FETCH a.links b "
            + "WHERE a.id = ?1")
    Optional<Site> findById(long id);

    @Query("SELECT distinct a FROM Site a "
            + "LEFT JOIN FETCH a.links b "
            + "WHERE a.login = ?1 and a.active = TRUE")
    Optional<Site> findByLogin(String login);

    @Query("SELECT distinct a FROM Site a "
            + "LEFT JOIN FETCH a.links b "
            + "WHERE a.domainUrl = ?1 and a.active = TRUE")
    Optional<Site> findByDomain(String login);
}