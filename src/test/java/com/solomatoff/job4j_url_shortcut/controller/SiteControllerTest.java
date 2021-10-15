package com.solomatoff.job4j_url_shortcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solomatoff.job4j_url_shortcut.Job4jUrlShortcutApplication;
import com.solomatoff.job4j_url_shortcut.dto.SiteDTO;
import com.solomatoff.job4j_url_shortcut.model.Site;
import com.solomatoff.job4j_url_shortcut.service.SiteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Job4jUrlShortcutApplication.class)
@AutoConfigureMockMvc
public class SiteControllerTest {

    private final static String USERNAME = "username";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @MockBean
    private SiteService siteService;

    @Autowired
    private MockMvc mockMvc;


    private SiteDTO.Request.RegRequest createNewSiteReqRequest(String domainUrl) {
        // add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310"
        //MAPPER.registerModule(new JavaTimeModule());
        SiteDTO.Request.RegRequest regRequest = new SiteDTO.Request.RegRequest();
        regRequest.setSite(domainUrl);
        return regRequest;
    }

    @Test
    public void whenRegistrationSiteSuccessThenResponseStatusIsCreated() throws Exception {
        String domainUrl = "job4j.ru";
        SiteDTO.Request.RegRequest reqRequest = createNewSiteReqRequest(domainUrl);
        String reqJson = MAPPER.writeValueAsString(reqRequest);
        var optionalSite = Site.of(domainUrl, USERNAME);
        if (optionalSite.isPresent()) {
            Site site = optionalSite.get();
            when(siteService.saveOrUpdate(site)).thenReturn(optionalSite);
            mockMvc.perform(post(
                            "/api/v1//registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(reqJson)
                    )
                    .andDo(print())
                    .andExpect(status().isCreated());
        } else {
            throw new RuntimeException("Test failed");
        }
    }

    @Test
    public void whenInputRegistrationDataNotValidThenResponseStatusBadRequest() throws Exception {
        String domainUrl = "job4j.ru";
        // Задаем неверные входные данные, просто домен сайта, а не json
        String reqJson = MAPPER.writeValueAsString(domainUrl);
        var optionalSite = Site.of(domainUrl, USERNAME);
        if (optionalSite.isPresent()) {
            mockMvc.perform(post("/api/v1/registration").contentType(MediaType.APPLICATION_JSON)
                            .content(reqJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        } else {
            throw new RuntimeException("Test failed");
        }
    }
}