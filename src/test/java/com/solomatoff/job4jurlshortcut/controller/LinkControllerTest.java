package com.solomatoff.job4jurlshortcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solomatoff.job4jurlshortcut.Job4jUrlShortcutApplication;
import com.solomatoff.job4jurlshortcut.dto.LinkDTO;
import com.solomatoff.job4jurlshortcut.jwt.TokenAuthenticationService;
import com.solomatoff.job4jurlshortcut.model.Link;
import com.solomatoff.job4jurlshortcut.model.Site;
import com.solomatoff.job4jurlshortcut.service.LinkService;
import com.solomatoff.job4jurlshortcut.service.SiteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Job4jUrlShortcutApplication.class)
@AutoConfigureMockMvc
public class LinkControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final static String USERNAME = "username";

    @MockBean
    private LinkService linkService;

    @MockBean
    private SiteService siteService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    private LinkDTO.Request.LinkRequest createNewLinkReqRequest(String domainUrl) {
        LinkDTO.Request.LinkRequest regRequest = new LinkDTO.Request.LinkRequest();
        regRequest.setUrl(domainUrl);
        return regRequest;
    }

    @WithMockUser(username = USERNAME)
    @Test
    public void whenConvertSuccessThenResponseStatusIsCreated() throws Exception {
        String domainUrl = "https://job4j.ru/profile/exercise/106/task-view/532";
        LinkDTO.Request.LinkRequest linkRequest = createNewLinkReqRequest(domainUrl);
        String reqJson = MAPPER.writeValueAsString(linkRequest);

        Link link = Link.of(domainUrl);

        Link linkWithCode = Link.of(link.getLinkUrl());
        String code = "abc";
        linkWithCode.setLinkCode(code);

        LinkDTO.Response.CodeResponse codeResponse = new LinkDTO.Response.CodeResponse(code);
        String respJson = new ObjectMapper().writeValueAsString(codeResponse);
        System.out.println("    respJson = " + respJson);

        var optionalSite = Site.of(domainUrl, USERNAME);
        when(siteService.findByLogin(USERNAME)).thenReturn(optionalSite);
        when(linkService.transferLinkRequestToLink(linkRequest)).thenReturn(link);
        when(linkService.saveAndGenerateCode(link)).thenReturn(Optional.of(linkWithCode));
        when(linkService.create(link, optionalSite.get())).thenReturn(linkWithCode);
        when(linkService.transferLinkToRegResponse(linkWithCode)).thenReturn(codeResponse);
        mockMvc.perform(
                    post("/api/v1/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson)
                )
                .andDo(print())
                .andExpect(content().json(respJson))
                .andExpect(status().isCreated());
    }

    @WithMockUser(username = USERNAME)
    @Test
    public void whenConvertNotSuccessThenResponseStatusBadRequest() throws Exception {
        String domainUrl = "https://job4j.ru/same_page.html";
        // Задаем неверные входные данные, пустой запрос
        LinkDTO.Request.LinkRequest linkRequest = new LinkDTO.Request.LinkRequest();
        String reqJson = new ObjectMapper().writeValueAsString(linkRequest);

        mockMvc.perform(post("/api/v1/convert").contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("must not be blank. Actual value: null")));
    }

}