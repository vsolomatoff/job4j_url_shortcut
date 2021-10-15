package com.solomatoff.job4j_url_shortcut.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String linkUrl;

    private String linkCode;

    private int challengeCount;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;

    public static Link of(String linkUrl, int challengeCount) {
        Link result = new Link();
        result.setLinkUrl(linkUrl);
        result.setChallengeCount(challengeCount);
        return result;
    }

    public static Link of(String url) {
        return of(url, 0);
    }
}
