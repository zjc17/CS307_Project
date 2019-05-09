BEGIN TRANSACTION;

DROP VIEW IF EXISTS "artist";
CREATE VIEW IF NOT EXISTS "artist" AS
SELECT credit.id            AS id,
       people.name          AS name,
       credit.name_for_sort AS name_for_sort,
       people.picture_id    AS picture_id
FROM credit
       INNER JOIN credit ON people.id = credit.people_id
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
INNER JOIN album  on credit_with_album.album_id = album.id



