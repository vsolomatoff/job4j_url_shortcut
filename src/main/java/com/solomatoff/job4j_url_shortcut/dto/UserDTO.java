package com.solomatoff.job4j_url_shortcut.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/*
 Вспомогательный DTO для регистрации сайта с указанием пользовательских данных
 Соответствующей модели данных не существует
 */
public enum UserDTO {
    /* enum без значений */;

    private interface Login { @NotBlank String getLogin(); }
    private interface Password { @NotBlank String getPassword(); }

    public enum Request {
        /* enum без значений */;

        @Data
        public static class User implements Login, Password {
            String login;
            String password;
        }
    }

}