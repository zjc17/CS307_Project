import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DB_Writter {

  private final String SEPARATOR = ";";
  private Connection connection;
  private DB_Connector connector;
  private DB_Reader db_reader;

  public DB_Writter(DB_Connector connector) {
    this.connector = connector;
    this.connection = connector.getConn();
    this.db_reader = new DB_Reader(connector);
  }

  public void insertPictureInfo(String picPath) {
    try {
      String sql = "INSERT OR IGNORE INTO picture (path) VALUES (?)";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, picPath);
      pstmt.executeUpdate();
    } catch (Exception e) {
      System.err.println(
          "insertPictureInfo: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
  }

  public void insertAlbumInfo(String name, Integer track_number, String albumArtist) {
    if (name == null || name.equals("")) {
      return;
    }
    try {
      /* picture_id */
      DB_Reader dbReader = new DB_Reader(connector);
      Integer picture_id = dbReader.getPictureId(null, null, null);
      /* artist_id */
      insertArtistInfo(albumArtist);
      String sql = "SELECT id FROM artist WHERE name = ?";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, albumArtist);
      ResultSet rs = pstmt.executeQuery();
      int artist_id = rs.getInt("id");
      /* INSERT */
      sql = "INSERT OR IGNORE INTO album (name, artist_id, picture_id, track_number) VALUES (?, ?, ?, ?)";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, name);
      //noinspection JpaQueryApiInspection
      pstmt.setInt(2, artist_id);
      //noinspection JpaQueryApiInspection
      pstmt.setObject(3, picture_id);
      //noinspection JpaQueryApiInspection
      pstmt.setObject(4, track_number);
      pstmt.executeUpdate();
      pstmt.close();
    } catch (Exception e) {
      System.err.println(
          "insertPictureInfo: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
  }

  public void insertArtistInfo(String name) {
    try {
      DB_Reader dbReader = new DB_Reader(connector);
      Integer picture_id = dbReader.getPictureId(null, null, null);
      String sql = "INSERT OR IGNORE INTO artist (name, picture_id) VALUES (?, ?)";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, name);
      pstmt.setObject(2, picture_id);
      pstmt.executeUpdate();
      pstmt.close();
    } catch (Exception e) {
      System.err.println(
          "insertArtistInfo: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
  }

  public void insertComposerInfo(String name) {
    try {
      String sql = "INSERT OR IGNORE INTO composer (name) VALUES (?)";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, name);
      pstmt.executeUpdate();
      pstmt.close();
    } catch (Exception e) {
      System.err.println(
          "insertComposerInfo: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
  }

  public void insertGenreInfo(String genre) {
    try {
      String sql = "INSERT OR IGNORE INTO genre (name) VALUES (?)";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, genre);
      pstmt.executeUpdate();
      pstmt.close();
    } catch (Exception e) {
      System.err.println(
          "insertGenreInfo: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
  }

  public void createPlaylist(String playlistName, Integer folderId) {
    try {
      String sql = "INSERT OR IGNORE INTO playlist (name, folder_id) VALUES (?, ?)";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, playlistName);
      pstmt.setObject(2, folderId);
      pstmt.executeUpdate();
      pstmt.close();
    } catch (Exception e) {
      System.err.println(
          "insertPlaylist: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
  }

  public void createFolder(String name) {
    try {
      String sql = "INSERT OR IGNORE INTO playlist (name) VALUES (?)";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, name);
      pstmt.executeUpdate();
      pstmt.close();
    } catch (Exception e) {
      System.err.println(
          "insertPlaylist: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
  }

  private void songHasGenre(int songId, String genre) {
    try {
      if (genre == null) {
        return;
      }
      String[] genres = genre.split(SEPARATOR);
      for (int i = 1, len = genres.length; i <= len; i++) {
        String genreName = genres[i - 1];
        if (genreName.equals("")) {
          return;
        }
        insertGenreInfo(genreName);
        int genreId = db_reader.getGenreId(genreName);

        String sql = "INSERT OR IGNORE INTO song_has_genre (genre_id, song_id)"
            + "VALUES (?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, genreId);
        pstmt.setInt(2, songId);
        pstmt.executeUpdate();
        pstmt.close();
      }
    } catch (Exception e) {
      System.err.println(
          "insertPlaylist: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
  }

  private void songHasComposer(int songId, String composer) {
    try {
      if (composer == null || composer.equals("")) {
        return;
      }
      String[] composers = composer.split(SEPARATOR);
      for (int i = 1, len = composers.length; i <= len; i++) {
        String composerName = composers[i - 1];
        if (composerName == null || composerName.equals("")) {
          return;
        }
        insertComposerInfo(composerName);
        int composerId = db_reader.getComposerId(composerName);

        String sql = "INSERT OR IGNORE INTO song_has_composer (composer_id, song_id)"
            + "VALUES (?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, composerId);
        pstmt.setInt(2, songId);
        pstmt.executeUpdate();
        pstmt.close();
      }
    } catch (Exception e) {
      System.err.println(
          "insertPlaylist: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
  }

  private void songHasArtist(int songId, String artist) {
    try {
      if (artist == null) {
        return;
      }
      String[] artists = artist.split(SEPARATOR);
      for (int i = 1, len = artists.length; i <= len; i++) {
        String artistName = artists[i - 1];
        if (artistName == null || artistName.equals("")) {
          return;
        }
        insertArtistInfo(artistName);

        int artist_id = db_reader.getArtistId(artistName);
        String sql = "INSERT OR IGNORE INTO song_has_artist (artist_id, song_id)"
            + "VALUES (?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, artist_id);
        pstmt.setInt(2, songId);
        pstmt.executeUpdate();
        pstmt.close();
      }
    } catch (Exception e) {
      System.err.println(
          "insertPlaylist: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
  }

  @SuppressWarnings("JpaQueryApiInspection")
  public void insertSongInfo(String songName, String filePath, String picPath, String album,
      Integer year,
      String artist, Double BPM, Integer sampleRate, Integer bitRate, String MPEG_Version,
      String MPEG_Layer,
      String channels, String comments, Integer fileSize, Integer length, Integer trackOrder,
      String albumArtist, String composer,
      String genre, Integer trackTotal
  ) {
    Integer pic_id = null;
    Integer album_id = null;
    try {

      if (picPath != null && !picPath.equals("")) {
        /* pic_id */
        insertPictureInfo(picPath);
        /* SELECT pic_id: Need to add the pic file before */
        String sql = "SELECT id FROM picture WHERE path = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, picPath);
        ResultSet rs = pstmt.executeQuery();
        pic_id = rs.getInt("id");
        pstmt.close();
        rs.close();
      }
      /* album_id */
      if (album != null && !album.equals("")) {
        insertAlbumInfo(album, trackTotal, albumArtist);
        /* SELECT album_id: Need to add the pic file before */
        String sql = "SELECT id FROM album WHERE name = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, album);
        ResultSet rs = pstmt.executeQuery();
        album_id = rs.getInt("id");
        pstmt.close();
        rs.close();
      }

      /* INSERT song */
      String sql =
          "INSERT OR IGNORE INTO song (name, file_path, picture_id, album_id, year, bpm, sampleRate,"
              + "bitRate, mpeg_version, mpeg_layer, channels, comments, size,"
              + "length, track_order)"
              + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, songName); // name
      pstmt.setString(2, filePath); // file_path
      pstmt.setObject(3, pic_id);   // picture_id
      pstmt.setObject(4, album_id); // album_id
      pstmt.setInt(5, year);        // year
      pstmt.setObject(6, BPM);      // bpm
      pstmt.setObject(7, sampleRate); // sampleRate
      pstmt.setObject(8, bitRate);    // bitRate
      pstmt.setString(9, MPEG_Version); // mpeg_version
      pstmt.setString(10, MPEG_Layer);  // mpeg_layer
      pstmt.setString(11, channels);    // channels
      pstmt.setString(12, comments);    // comments
      pstmt.setInt(13, fileSize); // size
      pstmt.setInt(14, length);   // length
      pstmt.setObject(15, trackOrder);  // track_order
      pstmt.executeUpdate();
      pstmt.close();
      int songId;
      sql = "SELECT id FROM song WHERE name = ?";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, songName);
      ResultSet rs = pstmt.executeQuery();
      songId = rs.getInt("id");
      rs.close();
      songHasArtist(songId, artist);
      songHasComposer(songId, composer);
      songHasGenre(songId, genre);
    } catch (Exception e) {
      System.err.println(
          "insertSongInfo: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }

  }
}
