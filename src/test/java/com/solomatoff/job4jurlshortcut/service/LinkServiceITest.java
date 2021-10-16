package com.solomatoff.job4jurlshortcut.service;

import com.solomatoff.job4jurlshortcut.Job4jUrlShortcutApplication;
import com.solomatoff.job4jurlshortcut.model.Link;
import com.solomatoff.job4jurlshortcut.repository.LinkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Job4jUrlShortcutApplication.class)
public class LinkServiceITest {

    @MockBean
    private LinkRepository linkRepository;

    @Autowired
    private LinkService linkService;

    @Test
    public void whenSaveAndGenerateCodeForSavedLinkThenLinkIsPresentWithSavedCode() {
        Link linkInput = Link.of("https://job4j.ru");
        Link linkReturn = Link.of(linkInput.getLinkUrl());
        linkReturn.setLinkCode("abc");
        when(linkRepository.findByLinkUrl(linkInput.getLinkUrl())).thenReturn(Optional.of(linkReturn));
        Optional<Link> result = linkService.saveAndGenerateCode(linkInput);
        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getLinkCode(), is(linkReturn.getLinkCode()));
    }

    @Test
    public void whenSaveAndGenerateCodeForNewLinkThenLinkIsPresentWithCodeNotNull() {
        Link linkInput = Link.of("https://some-site.ru");
        when(linkRepository.findByLinkUrl(linkInput.getLinkUrl())).thenReturn(Optional.empty());
        when(linkRepository.save(linkInput)).thenReturn(linkInput);
        Optional<Link> result = linkService.saveAndGenerateCode(linkInput);
        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getLinkCode(), not(blankOrNullString()));
    }

    @Test
    public void whenFindByCodeThenLinkIsPresent() {
        String code = "abc";
        Link linkReturn = Link.of("https://job4j.ru");
        linkReturn.setLinkCode(code);
        when(linkRepository.findByLinkCode(code)).thenReturn(Optional.of(linkReturn));
        Optional<Link> result = linkService.findByCode(code);
        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getLinkCode(), is(linkReturn.getLinkCode()));
        assertThat(result.get().getLinkUrl(), is(linkReturn.getLinkUrl()));
    }
}