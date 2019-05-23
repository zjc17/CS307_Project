BEGIN TRANSACTION;

DROP TABLE IF EXISTS "people";
CREATE TABLE IF NOT EXISTS "people"
(
  "id"         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "name"       TEXT    NOT NULL UNIQUE,
  "picture_id" INTEGER,
  "name_for_sort" TEXT,
  FOREIGN KEY ("picture_id") REFERENCES "picture" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS "credit";
CREATE TABLE IF NOT EXISTS "credit"
(
  "id"            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "people_id"     INTEGER NOT NULL,
  "credit_as"     TEXT    NOT NULL,
  UNIQUE ("people_id", "credit_as"),
  FOREIGN KEY ("people_id") REFERENCES "people" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);


DROP TABLE IF EXISTS "song";
CREATE TABLE IF NOT EXISTS "song"
(
  "id"             INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "name"           TEXT    NOT NULL UNIQUE,
  "name_for_sort"  TEXT,
  "file_path"      TEXT    NOT NULL,
  "picture_id"     INTEGER,
  "album_id"       INTEGER,
  "year"           INTEGER,
  "rating"         INTEGER          DEFAULT 0,
  "bpm"            NUMERIC,
  "sampleRate"     INTEGER,
  "bitRate"        INTEGER,
  "mpeg_version"   TEXT,
  "mpeg_layer"     TEXT,
  "channels"       TEXT,
  "comments"       TEXT,
  "size"           INTEGER NOT NULL,
  "length"         INTEGER NOT NULL,
  "date_modified"  TEXT    NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')),
  "date_added"     TEXT    NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')),
  "track_order"    INTEGER          DEFAULT 0,
  "play_count"     INTEGER          DEFAULT 0,
  "last_play_time" TEXT,
  "skip_count"     INTEGER          DEFAULT 0,
  "last_skip_time" TEXT,
  FOREIGN KEY ("album_id") REFERENCES "album" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY ("picture_id") REFERENCES "picture" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS "credit_with_song";
CREATE TABLE IF NOT EXISTS "credit_with_song"
(
  "credit_id" INTEGER NOT NULL,
  "song_id"   INTEGER NOT NULL,
  "order"     INTEGER NOT NULL DEFAULT 1,
  FOREIGN KEY ("credit_id") REFERENCES "credit" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY ("song_id") REFERENCES "song" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS "picture";
CREATE TABLE IF NOT EXISTS "picture"
(
  "id"            INTEGER PRIMARY KEY AUTOINCREMENT,
  "path"          TEXT UNIQUE NOT NULL
);

DROP TABLE IF EXISTS "genre";
CREATE TABLE IF NOT EXISTS "genre"
(
  "id"   INTEGER PRIMARY KEY AUTOINCREMENT,
  "name" TEXT NOT NULL UNIQUE COLLATE NOCASE
);

DROP TABLE IF EXISTS "song_has_genre";
CREATE TABLE IF NOT EXISTS "song_has_genre"
(
  "song_id"  INTEGER NOT NULL,
  "genre_id" INTEGER NOT NULL,
  "order"    INTEGER NOT NULL DEFAULT 1 CHECK ("order" > 0),
  FOREIGN KEY ("song_id") REFERENCES "song" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
  PRIMARY KEY ("song_id", "genre_id")
);


DROP TABLE IF EXISTS "album";
CREATE TABLE IF NOT EXISTS "album"
(
  "id"                 INTEGER PRIMARY KEY AUTOINCREMENT,
  "name"               TEXT    NOT NULL UNIQUE COLLATE NOCASE,
  "name_for_sort"      TEXT,
  "picture_id"         INTEGER,
  "track_total_number" INTEGER CHECK (track_total_number > 0), -- Total number
  "compilation"        INTEGER NOT NULL DEFAULT 0,       -- 1 for true, and group by albumArtist otherwise group by artist
  CONSTRAINT "fk_album_picture" FOREIGN KEY ("picture_id") REFERENCES "picture" ("id")
);

DROP TABLE IF EXISTS "credit_with_album";
CREATE TABLE IF NOT EXISTS "credit_with_album"
(
  "credit_id" INTEGER NOT NULL,
  "album_id"  INTEGER NOT NULL,
  "order"     INTEGER NOT NULL DEFAULT 1,
  UNIQUE ("credit_id", "album_id"),
  FOREIGN KEY ("credit_id") REFERENCES "credit" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY ("album_id") REFERENCES "album" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS "playlist_has_song";
CREATE TABLE IF NOT EXISTS "playlist_has_song"
(
  "playlist_id" INTEGER,
  "song_id"     INTEGER,
  "order"       INTEGER NOT NULL,
  PRIMARY KEY ("playlist_id", "song_id"),
  CONSTRAINT "fk_song" FOREIGN KEY ("song_id") REFERENCES "song" ("id"),
  CONSTRAINT "fk_playlist" FOREIGN KEY ("playlist_id") REFERENCES "playlist" ("id")
);

DROP TABLE IF EXISTS "playlist";
CREATE TABLE IF NOT EXISTS "playlist"
(
  "id"        INTEGER PRIMARY KEY AUTOINCREMENT,
  "name"      TEXT UNIQUE NOT NULL,
  "folder_id" INTEGER,
  CONSTRAINT "fk_playlist_folder" FOREIGN KEY ("folder_id") REFERENCES "folder" ("id")
);

DROP TABLE IF EXISTS "folder";
CREATE TABLE IF NOT EXISTS "folder"
(
  "id"   INTEGER PRIMARY KEY AUTOINCREMENT,
  "name" TEXT UNIQUE NOT NULL
);

COMMIT;
