drop table certificates_tags if exists;
drop table tags if exists;
drop table gift_certificates if exists;

CREATE TABLE gift_certificates
(
    id integer,
    name varchar(100),
    description varchar(100),
    duration integer ,
    creation_date varchar(100),
    last_update_date varchar(100),
    price integer
);
CREATE TABLE tags
(
    tag_id integer,
    tag_name varchar(100)
);
CREATE TABLE certificates_tags
(
    certificate_id integer,
    tag_id integer
);
