create table link (
    id  bigserial not null,
    challenge_count int4 not null,
    link_code varchar(255),
    link_url varchar(255),
    site_id int8, primary key (id)
                  );

create table site (
    id  bigserial not null,
    active boolean not null,
    current_token varchar(255),
    domain_url varchar(255) not null,
    login varchar(50) not null,
    password varchar(255) not null,
    registered timestamp, primary key (id)
                  );

alter table link
    add constraint link_fk
        foreign key (site_id)
            references site;

