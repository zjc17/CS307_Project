BEGIN TRANSACTION;
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
DROP TABLE IF EXISTS "album";
CREATE TABLE IF NOT EXISTS "album" (
	"id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"name"	TEXT NOT NULL,
	"name_for_sort"	TEXT,
	"picture_id"	INTEGER,
	CONSTRAINT "fk_album_picture" FOREIGN KEY("picture_id") REFERENCES "picture"("id")
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
DROP TABLE IF EXISTS "song";
CREATE TABLE IF NOT EXISTS "song" (
	"id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"name"	TEXT NOT NULL,
	"name_for_sort"	TEXT,
	"file_path"	TEXT NOT NULL,
	"picture_id"	NUMERIC NOT NULL,
	"artist_id"	INTEGER NOT NULL,
	"album_id"	INTEGER,
	"composer_id"	INTEGER,
	"genre_id"	INTEGER NOT NULL,
	"size"	INTEGER NOT NULL,
	"length"	INTEGER NOT NULL,
	"date_modified"	TEXT NOT NULL,
	"date_added"	TEXT,
	"year"	INTEGER,
	"track_order"	INTEGER NOT NULL,
	"rating"	INTEGER,
	"bpm"	INTEGER,
	"play_count"	INTEGER,
	"last_play_time"	INTEGER,
	"skip_count"	INTEGER,
	"last_skip_time"	TEXT,
	"comments"	TEXT,
	CONSTRAINT "fk_song_album" FOREIGN KEY("album_id") REFERENCES "album"("id"),
	CONSTRAINT "fk_song_artist" FOREIGN KEY("artist_id") REFERENCES "artist"("id"),
	CONSTRAINT "fk_song_picture" FOREIGN KEY("picture_id") REFERENCES "picture"("id"),
	CONSTRAINT "fk_song_composer" FOREIGN KEY("composer_id") REFERENCES "composer"("id"),
	CONSTRAINT "fk_song_genre" FOREIGN KEY("genre_id") REFERENCES "genre"("id")
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
