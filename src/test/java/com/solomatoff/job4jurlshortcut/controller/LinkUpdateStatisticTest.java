package com.solomatoff.job4jurlshortcut.controller;

import com.solomatoff.job4jurlshortcut.model.Link;
import com.solomatoff.job4jurlshortcut.service.LinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class LinkUpdateStatisticTest {

    @Autowired
    private LinkService linkService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenThereAreManyThreadForUpdateStatistic() throws Exception {
        String linkInput = "https://job4j.ru";
        var optionalLink = linkService.saveAndGenerateCode(Link.of(linkInput));
        int numThreads = 1000;
        Link link = optionalLink.get();
        long id = link.getId();
        String code = link.getLinkCode();
        for (int i = 0; i < numThreads; i++) {
            RunRedirectWithSleep runRedirectWithSleep = new RunRedirectWithSleep(i, id, code);
            Thread thread = new Thread(runRedirectWithSleep);
            thread.start();
        }
        for (int i = 0; i < numThreads; i++) {
            RunRedirect runRedirect = new RunRedirect(i, id, code);
            Thread thread = new Thread(runRedirect);
            thread.start();
        }
        Thread.sleep(3000);
        assertThat(linkService.findById(id).get().getChallengeCount(), is(numThreads * 2));
        linkService.delete(link);
    }

    private class RunRedirect implements Runnable {

        int i;
        long id;
        String code;

        public RunRedirect(int i, long id, String code) {
            this.i = i;
            this.id = id;
            this.code = code;
        }

        @Override
        public void run() {
            try {
                mockMvc.perform(get("/api/v1/redirect/{code}", code))
                        .andReturn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class RunRedirectWithSleep implements Runnable {

        int i;
        long id;
        String code;

        public RunRedirectWithSleep(int i, long id, String code) {
            this.i = i;
            this.id = id;
            this.code = code;
        }

        @Override
        public void run() {
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                mockMvc.perform(get("/api/v1/redirect/{code}", code))
                        .andReturn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
