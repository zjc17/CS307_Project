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