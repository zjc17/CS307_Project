BEGIN TRANSACTION;
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
  "rating"         INTEGER DEFAULT 0,
  "bpm"            NUMERIC,
  "sampleRate"     INTEGER,
  "bitRate"        INTEGER,
  "mpeg_version"   TEXT,
  "mpeg_layer"     TEXT,
  "channels"       TEXT,
  "comments"       TEXT,
  "size"           INTEGER NOT NULL,
  "length"         INTEGER NOT NULL,
  "date_modified"  TEXT NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')),
  "date_added"     TEXT NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')),
  "track_order"    INTEGER DEFAULT 0,
  "play_count"     INTEGER DEFAULT 0,
  "last_play_time" TEXT,
  "skip_count"     INTEGER DEFAULT 0,
  "last_skip_time" TEXT,
  FOREIGN KEY ("album_id") REFERENCES "album" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY ("picture_id") REFERENCES "picture" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);
DROP TABLE IF EXISTS "song_has_artist";
CREATE TABLE IF NOT EXISTS "song_has_artist"
(
  "artist_id" INTEGER NOT NULL,
  "song_id"   INTEGER NOT NULL,
  "order"     INTEGER NOT NULL DEFAULT 1 CHECK ("order" > 0),
  FOREIGN KEY ("artist_id") REFERENCES "song" ("id") ON UPDATE CASCADE ON DELETE CASCADE,
  PRIMARY KEY ("artist_id", "song_id"),
  FOREIGN KEY ("song_id") REFERENCES "song" ("id") ON UPDATE CASCADE ON DELETE CASCADE
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
DROP TABLE IF EXISTS "song_has_composer";
CREATE TABLE IF NOT EXISTS "song_has_composer"
(
  "composer_id" INTEGER NOT NULL,
  "song_id"     INTEGER NOT NULL,
  "order"       INTEGER NOT NULL DEFAULT 1 CHECK ("order" > 0),
  PRIMARY KEY ("composer_id", "song_id"),
  FOREIGN KEY ("composer_id") REFERENCES "composer" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY ("song_id") REFERENCES "song" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);
DROP TABLE IF EXISTS "album";
CREATE TABLE IF NOT EXISTS "album"
(
  "id"            INTEGER PRIMARY KEY AUTOINCREMENT,
  "name"          TEXT NOT NULL,
  "artist_id"     INTEGER,
  "name_for_sort" TEXT,
  "picture_id"    INTEGER,
  "track_number"  INTEGER CHECK (track_number > 0),
  UNIQUE ("name", "artist_id"),
  CONSTRAINT "fk_album_artist" FOREIGN KEY ("artist_id") REFERENCES "artist" ("id"),
  CONSTRAINT "fk_album_picture" FOREIGN KEY ("picture_id") REFERENCES "picture" ("id")
);
DROP TABLE IF EXISTS "folder";
CREATE TABLE IF NOT EXISTS "folder"
(
  "id"   INTEGER PRIMARY KEY AUTOINCREMENT,
  "name" TEXT UNIQUE NOT NULL
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
DROP TABLE IF EXISTS "artist";
CREATE TABLE IF NOT EXISTS "artist"
(
  "id"            INTEGER PRIMARY KEY AUTOINCREMENT,
  "name"          TEXT NOT NULL UNIQUE,
  "name_for_sort" TEXT,
  "picture_id"    INTEGER,
  CONSTRAINT "fk_artist_picture" FOREIGN KEY ("picture_id") REFERENCES "picture" ("id")
);
DROP TABLE IF EXISTS "playlist";
CREATE TABLE IF NOT EXISTS "playlist"
(
  "id"        INTEGER PRIMARY KEY AUTOINCREMENT,
  "name"      TEXT UNIQUE NOT NULL,
  "folder_id" INTEGER,
  CONSTRAINT "fk_playlist_folder" FOREIGN KEY ("folder_id") REFERENCES "folder" ("id")
);
DROP TABLE IF EXISTS "picture";
CREATE TABLE IF NOT EXISTS "picture"
(
  "id"   INTEGER PRIMARY KEY AUTOINCREMENT,
  "path" TEXT UNIQUE NOT NULL
);
DROP TABLE IF EXISTS "composer";
CREATE TABLE IF NOT EXISTS "composer"
(
  "id"            INTEGER PRIMARY KEY AUTOINCREMENT,
  "name"          TEXT NOT NULL UNIQUE,
  "name_for_sort" TEXT
);
DROP TABLE IF EXISTS "genre";
CREATE TABLE IF NOT EXISTS "genre"
(
  "id"   INTEGER PRIMARY KEY AUTOINCREMENT,
  "name" TEXT NOT NULL UNIQUE
);
COMMIT;
