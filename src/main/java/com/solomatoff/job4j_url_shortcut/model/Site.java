package com.solomatoff.job4j_url_shortcut.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
public class Site implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Domain Url must be non null")
    private String domainUrl;

    @NotNull(message = "Login must be non null")
    @Size(
            min = 3,
            max = 50,
            message = "Login is required, minimum 3, maximum 50 characters."
    )
    private String login;

    @NotNull(message = "Password must be non null")
    @Size(
            min = 3,
            max = 255,
            message = "Password is required, minimum 3, maximum 255 characters."
    )
    private String password;

    private String currentToken;

    private boolean active;

    @CreationTimestamp
    private LocalDateTime registered;

    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    protected Set<Link> links = new HashSet<>();


    public static Optional<Site> of(String domainUrl, String login) {
        Site site = new Site();
        site.setDomainUrl(domainUrl);
        return Optional.of(site);
    }

    /*
        Methods for link
     */
    public void addLink(Link link) {
        if (link == null) {
            throw new NullPointerException("Can't add null Link");
        }
        if (link.getLinkUrl() == null || link.getLinkUrl().isEmpty()) {
            throw new NullPointerException("Can't add Link with null url");
        }
        links.add(link);
        link.setSite(this);
    }

    public Link readLink(Long id) {
        List<Link> listLinks = this.links.stream().filter(person -> person.getId().equals(id)).collect(Collectors.toList());
        return (listLinks.size() == 1) ? listLinks.get(0) : null;
    }

    public void deleteLink(Link link) {
        this.links.remove(link);
    }

}
