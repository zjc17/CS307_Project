BEGIN TRANSACTION;
DROP TABLE IF EXISTS "song";
CREATE TABLE IF NOT EXISTS "song" (
	"id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"name"	TEXT NOT NULL,
	"name_for_sort"	TEXT,
	"file_path"	TEXT NOT NULL,
	"picture_id"	NUMERIC NOT NULL,
	"album_id"	INTEGER,
	"year"	INTEGER,
	"rating"	INTEGER,
	"bpm"	INTEGER,
	"comments"	TEXT,
	"size"	INTEGER NOT NULL,
	"length"	INTEGER NOT NULL,
	"date_modified"	TEXT NOT NULL,
	"date_added"	TEXT,
	"track_order"	INTEGER NOT NULL,
	"play_count"	INTEGER,
	"last_play_time"	INTEGER,
	"skip_count"	INTEGER,
	"last_skip_time"	TEXT,
	FOREIGN KEY("album_id") REFERENCES "album"("id") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY("picture_id") REFERENCES "picture"("id") ON DELETE CASCADE ON UPDATE CASCADE
);
DROP TABLE IF EXISTS "song_has_artist";
CREATE TABLE IF NOT EXISTS "song_has_artist" (
	"artist_id"	INTEGER NOT NULL,
	"song_id"	INTEGER NOT NULL,
	"order"	INTEGER NOT NULL DEFAULT 1 CHECK("order">0),
	FOREIGN KEY("artist_id") REFERENCES "song"("id") ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY("artist_id","song_id"),
	FOREIGN KEY("song_id") REFERENCES "song"("id") ON UPDATE CASCADE ON DELETE CASCADE
);
DROP TABLE IF EXISTS "song_has_genre";
CREATE TABLE IF NOT EXISTS "song_has_genre" (
	"song_id"	INTEGER NOT NULL,
	"genre_id"	INTEGER NOT NULL,
	"order"	INTEGER NOT NULL DEFAULT 1 CHECK("order">0),
	FOREIGN KEY("song_id") REFERENCES "song"("id") ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY("song_id","genre_id")
);
DROP TABLE IF EXISTS "song_has_composer";
CREATE TABLE IF NOT EXISTS "song_has_composer" (
	"composer_id"	INTEGER NOT NULL,
	"song_id"	INTEGER NOT NULL,
	"order"	INTEGER NOT NULL DEFAULT 1 CHECK("order">0),
	PRIMARY KEY("composer_id","song_id"),
	FOREIGN KEY("composer_id") REFERENCES "composer"("id") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY("song_id") REFERENCES "song"("id") ON DELETE CASCADE ON UPDATE CASCADE
);
DROP TABLE IF EXISTS "album";
CREATE TABLE IF NOT EXISTS "album" (
	"id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"name"	TEXT NOT NULL,
	"name_for_sort"	TEXT,
	"picture_id"	INTEGER,
	"track_number"	INTEGER CHECK(track_number>0),
	CONSTRAINT "fk_album_picture" FOREIGN KEY("picture_id") REFERENCES "picture"("id")
);
DROP TABLE IF EXISTS "folder";
CREATE TABLE IF NOT EXISTS "folder" (
	"id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"name"	TEXT NOT NULL,
	"playlist_id"	INTEGER,
	CONSTRAINT "fk_folder_playlist" FOREIGN KEY("playlist_id") REFERENCES "playlist"("id")
);
DROP TABLE IF EXISTS "playlist_has_song";
CREATE TABLE IF NOT EXISTS "playlist_has_song" (
	"playlist_id"	INTEGER,
	"song_id"	INTEGER,
	"order"	INTEGER NOT NULL,
	PRIMARY KEY("playlist_id","song_id"),
	CONSTRAINT "fk_song" FOREIGN KEY("song_id") REFERENCES "song"("id"),
	CONSTRAINT "fk_playlist" FOREIGN KEY("playlist_id") REFERENCES "playlist"("id")
);
DROP TABLE IF EXISTS "artist";
CREATE TABLE IF NOT EXISTS "artist" (
	"id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"first_name"	TEXT,
	"surname"	TEXT NOT NULL,
	"name_for_sort"	TEXT,
	"picture_id"	INTEGER,
	CONSTRAINT "fk_artist_picture" FOREIGN KEY("picture_id") REFERENCES "picture"("id")
);
DROP TABLE IF EXISTS "playlist";
CREATE TABLE IF NOT EXISTS "playlist" (
	"id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"name"	TEXT UNIQUE
);
DROP TABLE IF EXISTS "picture";
CREATE TABLE IF NOT EXISTS "picture" (
	"id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"path"	TEXT UNIQUE
);
DROP TABLE IF EXISTS "composer";
CREATE TABLE IF NOT EXISTS "composer" (
	"id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"first_name"	TEXT,
	"surname"	TEXT NOT NULL,
	"name_for_sort"	TEXT
);
DROP TABLE IF EXISTS "genre";
CREATE TABLE IF NOT EXISTS "genre" (
	"id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"name"	TEXT NOT NULL UNIQUE
);
COMMIT;
