package com.solomatoff.job4jurlshortcut.service;

import com.solomatoff.job4jurlshortcut.model.Site;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Фактически получается, что пользователи хранятся в таблице site
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final ISiteService siteService;

    public UserDetailsServiceImpl(ISiteService siteService) {
        this.siteService = siteService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var site = siteService.findByLogin(login);
        if (site.isEmpty()) {
            throw new UsernameNotFoundException(login);
        }
        Site site1 = site.get();
        return new User(site1.getLogin(), site1.getPassword(), Collections.emptySet());
    }

}
