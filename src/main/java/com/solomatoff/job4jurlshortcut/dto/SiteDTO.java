package com.solomatoff.job4jurlshortcut.dto;

import com.solomatoff.job4jurlshortcut.model.Link;
import com.solomatoff.job4jurlshortcut.validation.Operation;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;

public enum SiteDTO {
    /* enum без значений */;

    private interface Id {
        @NotBlank(groups = {
                Operation.OnUpdate.class, Operation.OnDelete.class
        }) Long getId();
    }

    private interface RegSite { @NotBlank String getSite(); }

    private interface Login { @NotBlank String getLogin(); }

    private interface Password { @NotBlank String getPassword(); }

    private interface CurrentToken { String getCurrentToken(); }

    private interface Active { Boolean getActive(); }

    private interface Registered { LocalDateTime getRegistered(); }

    private interface Links { Set<Link> getLinks(); }

    private interface Registration { String getRegistration(); }

    public enum Request {
        /* enum без значений */;

        @Data
        public static class RegRequest implements RegSite {
            String site;
        }

        @Value
        public static class SiteRequest implements RegSite, Login, Password {
            String site;
            String login;
            String password;
        }

    }

    public enum Response {
        /* enum без значений */;

        @Value
        public static class RegResponse implements Registration {
            String registration;
        }

        @Value
        public static class SiteResponse
                implements Id, RegSite, Login, Password, CurrentToken, Active, Registered, Links {
            Long id;
            String site;
            String login;
            String password;
            String currentToken;
            Boolean active;
            LocalDateTime registered;
            Set<Link> links;
        }

    }

}