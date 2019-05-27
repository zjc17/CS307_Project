BEGIN TRANSACTION;

DROP VIEW IF EXISTS "artist";
CREATE VIEW IF NOT EXISTS "artist" AS
SELECT p.id            AS id,
       p.name          AS name,
       p.name_for_sort AS name_for_sort,
       p.picture_id    AS picture_id,
       c.id            AS credit_id
FROM people p
       INNER JOIN credit c ON p.id = c.people_id
WHERE credit_as = 'A';

DROP VIEW IF EXISTS "album_artist";
CREATE VIEW IF NOT EXISTS "album_artist" AS
SELECT credit.id            AS id,
       people.name          AS name,
       credit.name_for_sort AS name_for_sort,
       people.picture_id    AS picture_id
FROM people
       INNER JOIN credit ON people.id = credit.people_id
WHERE credit_as = 'M';

DROP VIEW IF EXISTS "composer";
CREATE VIEW IF NOT EXISTS "composer" AS
SELECT credit.id            AS id,
       people.name          AS name,
       credit.name_for_sort AS name_for_sort,
       people.picture_id    AS picture_id
FROM people
       INNER JOIN credit ON people.id = credit.people_id
WHERE credit_as = 'C';

COMMIT;


SELECT credit.id            AS id,
       people.name          AS name,
       credit.credit_as     AS credit_as,
       credit.name_for_sort AS name_for_sort,
       people.picture_id    AS picture_id
FROM people
       INNER JOIN credit ON people.id = credit.people_id
ORDER BY name;

SELECT song.name, album.name
FROM song
       INNER JOIN album on song.album_id = album.id
ORDER BY album.name;



-- artist and album
SELECT DISTINCT artist.name, album.name
FROM artist
       INNER JOIN credit_with_song ON artist.id = credit_id
       INNER JOIN song on credit_with_song.song_id = song.id
       INNER JOIN album on song.album_id = album.id;

-- 需要多存储一个 artist 和 album 的关系 （credit_as 可以区分 artist 和 album_artist）
SELECT DISTINCT artist.name, album.name
FROM artist
       INNER JOIN credit_with_album ON artist.id = credit_with_album.credit_id
       INNER JOIN album on credit_with_album.album_id = album.id

-- View for menu: Album
SELECT a.name               AS album_name,
       aa.name              AS album_artist_name,
--        artist.name          AS artist_name,
       p.path||"."||ft.name AS picture_path,
       a.id                 AS album_id
FROM album a
       INNER JOIN credit_with_album cwa on a.id = cwa.album_id
       INNER JOIN album_artist aa ON cwa.credit_id = aa.id
       INNER JOIN picture p ON a.picture_id = p.id
       INNER JOIN file_type ft on p.file_type_id = ft.id;


create view people_and_credit as
select credit.id as credit_id, credit.people_id as people_id, people.name, people.name_for_sort, people.picture_id, credit_as
from credit join people  on credit.people_id = people.id;

CREATE VIEW IF NOT EXISTS credit_and_album as
select pac.credit_id, pac.people_id,pac.name as people_name,
       pac.picture_id as picture_id_people,
       a.id as album_id, a.name as album_name,
       a.picture_id as picture_id_album, track_total_number, compilation
from people_and_credit as pac
            left join credit_with_album cwa on cwa.credit_id = pac.credit_id
            left join album a on cwa.album_id = a.id;

create view credit_song as
select pac.*, cws.song_id as song_id
from people_and_credit pac join credit_with_song cws on pac.credit_id = cws.credit_id;





