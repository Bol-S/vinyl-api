-- drop table if exists members;

-- create table members(group_id integer not null, artist_id integer not null, primary key(group_id, artist_id))
-- alter table members add constraint fk_members_groups foreign key(group_id) references groups
-- alter table members add constraint fk_members_artists foreign key(artist_id) references artists

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

INSERT INTO albums(name, release_year, label, artist_id, condition) VALUES ('Bacdafucup', 1993, 'JMJ', 1, 'Near Mint');
INSERT INTO albums(name, release_year, label, artist_id, condition) VALUES ('All We Got Iz Us', 1995, 'JMJ', 1, 'Very Good Plus');
INSERT INTO albums(name, release_year, label, artist_id, condition) VALUES ('Shut ''Em Down', 1998, 'JMJ', 1, 'Near Mint');
INSERT INTO albums(name, release_year, label, artist_id, condition) VALUES ('Dead Serious', 1991, 'EastWest Records America', 4, 'Near Mint');
INSERT INTO albums(name, release_year, label, artist_id, condition) VALUES ('Straight Up Sewaside', 1993, 'EastWest Records America', 4, 'Mint');
INSERT INTO albums(name, release_year, label, artist_id, condition) VALUES ('Decade... But Wait It Gets Worse', 1993, 'Universal Records', 2, 'Very Good Plus');
INSERT INTO albums(name, release_year, label, artist_id, condition) VALUES ('Firestarr', 2001, 'Koch Records', 3, 'Near Mint');
INSERT INTO albums(name, release_year, label, artist_id, condition) VALUES ('Don''t Get Mad Get Money', 2003, 'D3 Entertainment', 3, 'Good');
INSERT INTO albums(name, release_year, label, artist_id, condition) VALUES ('Back Fot That A$$', 2006, null, 5, 'Very Good Plus');
INSERT INTO albums(name, release_year, label, artist_id, condition) VALUES ('Back Fot That A$$ pt. 2', 2006, null, 5, 'Good');