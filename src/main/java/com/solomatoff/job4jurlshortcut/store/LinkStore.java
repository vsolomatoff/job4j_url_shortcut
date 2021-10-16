package com.solomatoff.job4jurlshortcut.store;

import com.solomatoff.job4jurlshortcut.model.Link;
import com.solomatoff.job4jurlshortcut.repository.LinkRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class LinkStore implements ILinkStore {

    private final LinkRepository linkRepository;

    public LinkStore(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public Collection<Link> findAll() {
        return (Collection<Link>) linkRepository.findAll();
    }

    @Override
    public Optional<Link> findById(long id) {
        return linkRepository.findById(id);
    }

    @Override
    public Optional<Link> saveOrUpdate(Link link) {
        return Optional.of(linkRepository.save(link));
    }

    @Override
    public void delete(Link link) {
        linkRepository.delete(link);
    }

    @Override
    public Optional<Link> findByLinkUrl(String url) {
        return linkRepository.findByLinkUrl(url);
    }

    @Override
    public Optional<Link> findByLinkCode(String code) {
        return linkRepository.findByLinkCode(code);
    }

    @Override
    public void updateLinkStatistic(long linkId) {
        linkRepository.incrementChallengeCount(linkId);
    }
}