package com.solomatoff.job4j_url_shortcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solomatoff.job4j_url_shortcut.Job4jUrlShortcutApplication;
import com.solomatoff.job4j_url_shortcut.dto.LinkDTO;
import com.solomatoff.job4j_url_shortcut.model.Link;
import com.solomatoff.job4j_url_shortcut.model.Site;
import com.solomatoff.job4j_url_shortcut.service.SiteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Job4jUrlShortcutApplication.class)
@AutoConfigureMockMvc
public class StatisticControllerTest {

    private final static String USERNAME = "username";

    @MockBean
    private SiteService siteService;

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = USERNAME)
    @Test
    public void whenStatisticThenStatusOk() throws Exception {
        Link linkFirst = Link.of("https://job4j.ru/profile/exercise/106/task-view/531", 10);
        Link linkSecond = Link.of("https://job4j.ru/profile/exercise/106/task-view/532", 20);
        String respJson = new ObjectMapper().writeValueAsString(List.of(
                new LinkDTO.Response.StatisticResponse(linkFirst.getLinkUrl(), linkFirst.getChallengeCount()),
                new LinkDTO.Response.StatisticResponse(linkSecond.getLinkUrl(), linkSecond.getChallengeCount()))
        );
        var optionalSite = Site.of("job4j.ru", USERNAME);
        Site site = optionalSite.get();
        site.setLinks(Set.of(linkFirst, linkSecond));
        when(siteService.findByLogin(USERNAME)).thenReturn(Optional.of(site));
        mockMvc.perform(get("/api/v1/statistic"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(respJson));
    }
}