drop table if exists beer_audit cascade;

create table beer_audit
(
    beer_style       smallint check (beer_style between 0 and 9),
    price            numeric(38, 2),
    quantity_on_hand integer,
    version          integer,
    created_at       timestamp(6),
    updated_at       timestamp(6),
    audit_created_at timestamp(6),
    audit_id         uuid not null,
    id               uuid,
    beer_name        varchar(50),
    upc              varchar(255),
    principal_name   varchar(255),
    audit_event_type varchar(255),
    primary key (audit_id)
);
