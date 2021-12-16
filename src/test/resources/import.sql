drop table if exists members;

create table members(group_id integer not null, artist_id integer not null, primary key(group_id, artist_id))
alter table members add constraint fk_members_groups foreign key(group_id) references groups
alter table members add constraint fk_members_artists foreign key(artist_id) references artists

INSERT INTO artists(name, real_name) VALUES('Onyx', null);
INSERT INTO artists(name, real_name) VALUES('Sticky Fingaz', 'Kirk Jones');
INSERT INTO artists(name, real_name) VALUES('Fredro Starr', 'Fred Lee Scrugs');
INSERT INTO artists(name, real_name) VALUES('Das Efx', null);
INSERT INTO artists(name, real_name) VALUES('Krazy Drayz', 'Andre Weston');
INSERT INTO artists(name, real_name) VALUES('Skoob', 'William Hines');

INSERT INTO music_groups(name) VALUES('Onyx');
INSERT INTO music_groups(name) VALUES('Das Efx');

INSERT INTO members(group_id, artist_id) VALUES(1, 2);
INSERT INTO members(group_id, artist_id) VALUES(1, 3);
INSERT INTO members(group_id, artist_id) VALUES(2, 5);
INSERT INTO members(group_id, artist_id) VALUES(2, 6);