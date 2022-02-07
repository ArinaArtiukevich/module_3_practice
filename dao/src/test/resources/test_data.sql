INSERT INTO tags(tag_id, tag_name) VALUES (1, 'tag_winter');
INSERT INTO tags(tag_id, tag_name) VALUES (2, 'tag_relax');
INSERT INTO tags(tag_id, tag_name) VALUES (3, 'tag_snow');

INSERT INTO gift_certificates(id, name, description, duration, creation_date, price) VALUES (1, 'skiing', 'skiing school', 12, '2022-01-24T15:35:04.072', 1000);

INSERT INTO gift_certificates(id, name, description, duration, creation_date, price) VALUES (2, 'massage', 'massage center', 2, '2022-01-24T15:36:18.987', 100);


INSERT INTO certificates_tags(certificate_id, tag_id) VALUES (1,1);
INSERT INTO certificates_tags(certificate_id, tag_id) VALUES (1,3);
INSERT INTO certificates_tags(certificate_id, tag_id) VALUES (2,1);
INSERT INTO certificates_tags(certificate_id, tag_id) VALUES (2,2);

