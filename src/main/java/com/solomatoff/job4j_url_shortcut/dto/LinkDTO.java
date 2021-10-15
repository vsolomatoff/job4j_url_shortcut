package com.solomatoff.job4j_url_shortcut.dto;

import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public enum LinkDTO {
    /* enum без значений */;

    private interface Id { @Positive Long getId(); }
    private interface LinkUrl { @NotNull @NotBlank String getUrl(); }
    private interface LinkCode { String getCode(); }
    private interface ChallengeCount { int getTotal(); }

    public enum Request {
        /* enum без значений */;

        @Data
        public static class LinkRequest implements LinkUrl {
            String url;
        }
    }

    public enum Response {
        /* enum без значений */;

        @Value
        public static class CodeResponse implements LinkCode {
            String code;
        }

        @Value
        public static class LinkResponse implements Id, LinkUrl, LinkCode, ChallengeCount {
            Long id;
            String url;
            String code;
            int total;
        }

        @Value
        public static class StatisticResponse implements LinkUrl, ChallengeCount {
            String url;
            int total;
        }

    }
}