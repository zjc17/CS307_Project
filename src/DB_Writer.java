import java.sql.Connection;
import java.sql.PreparedStatement;

public class DB_Writer {

  private Connection connection;
  private DB_Reader db_reader;

  public DB_Writer(DB_Connector connector) {
    this.connection = connector.getConn();
    this.db_reader = new DB_Reader(connector, this);
  }

  // TODO:
  public void addPlaylistHasSong(int playlistId, int songId, int order) {

  }

  // TODO:
  public void addPicture(String path, int fileTypeId) {

  }

  // TODO:
  public void updateNameForSort(String datebaseName, String newNameForSort) {

  }

  public void addPlaylist(String playlistName, Integer folderId) {
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

  public void addFolder(String name) {
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

  protected void insertSong(String songName, String filePath, String album,
      Integer year, String artist, Double BPM, Integer sampleRate, Integer bitRate,
      String MPEG_Version,
      String MPEG_Layer, String channels, String comments, Integer fileSize, Integer length,
      Integer trackOrder, String albumArtist, String composer, String genre, Integer trackTotal) {

    int tmpInt = filePath.lastIndexOf(".");
    assert tmpInt != -1 : "Error: file path = " + filePath;
    String fileType = filePath.substring(tmpInt + 1);
    filePath = filePath.substring(0, tmpInt);
    int fileTypeId = db_reader.getFileTypeId(fileType);
    boolean isAlbum = !(album == null || album.equals(""));
    // 此处仅包含名字，默认文件处理后为 picPath.jpg
    String picPath = (isAlbum ? Constant.picPathForAlbum + Constant.fileSeparator + album
        : Constant.picPathForSong + Constant.fileSeparator + songName);
    insertPicture(picPath, 3);
    Integer pictureId = db_reader.getPictureId(picPath);

    int genreId = db_reader.getGenreId(genre);
    int peopleId = db_reader.getPeopleId(artist);
    int creditId = db_reader.getCreditId(peopleId, "A"); // Artist
    // process album and album artist
    Integer albumId = null;

    if (!album.equals("")) {
      albumId = db_reader.getAlbumId(album);
      if (albumId == null) {
        insertAlbum(album, trackTotal);
        albumId = db_reader.getAlbumId(album);
      }
      if (!(albumArtist == null || albumArtist.equals(""))) {
        peopleId = db_reader.getPeopleId(albumArtist);
        creditId = db_reader.getCreditId(peopleId, "M");
        insertCreditWithAlbum(creditId, albumId, 1);
      }
    }
    try {
      String sql;
      PreparedStatement pstmt;

      int songId = db_reader.getSongId(songName, fileTypeId);
      if (songId == -1) {
        sql =
            "INSERT OR IGNORE INTO song (name, file_path, file_type_id, picture_id, album_id, year,"
                + " bpm, sampleRate, bitRate, mpeg_version, mpeg_layer, channels, comments, size, length,"
                + " track_order) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, songName);
        pstmt.setString(2, filePath);
        pstmt.setInt(3, fileTypeId);
        pstmt.setObject(4, pictureId);
        pstmt.setObject(5, albumId);
        pstmt.setObject(6, year);
        pstmt.setObject(7, BPM);
        pstmt.setObject(8, sampleRate);
        pstmt.setObject(9, bitRate);
        pstmt.setString(10, MPEG_Version);
        pstmt.setString(11, MPEG_Layer);
        pstmt.setString(12, channels);
        pstmt.setString(13, comments);
        pstmt.setObject(14, fileSize);
        pstmt.setObject(15, length);
        pstmt.setObject(16, trackOrder);
        pstmt.executeUpdate();
        pstmt.close();
        songId = db_reader.getSongId(songName, fileTypeId);
      }
      insertSongHasGenre(songId, genreId, 1);
      insertCreditWithSong(creditId, songId, 1);
      // process composer
      if (!(composer == null || composer.equals(""))) {
        peopleId = db_reader.getPeopleId(composer);
        creditId = db_reader.getCreditId(peopleId, "C");
        insertCreditWithSong(creditId, songId, 1);
      }
    } catch (Exception e) {
      System.err.println(
          "DB_Writer::insertSong: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
  }

  private void insertPicture(String path, int fileTypeId) {
    try {
      String sql = "INSERT OR IGNORE INTO picture (path, file_type_id) VALUES (?, ?)";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, path);
      pstmt.setInt(2, fileTypeId);
      pstmt.executeUpdate();
      pstmt.close();
    } catch (Exception e) {
      System.err.println(
          "DB_Writer::insertPicture: " + e.getClass().getName() + ": " + e
              .getMessage());
    }
  }

  private void insertSongHasGenre(int songId, int genreId, int order) {
    assert (order > 0) : "Error: order = " + order;
    try {
      String sql = "INSERT OR IGNORE INTO song_has_genre (song_id, genre_id, \"order\") VALUES (?, ?, ?)";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setInt(1, songId);
      pstmt.setInt(2, genreId);
      pstmt.setInt(3, order);
      pstmt.executeUpdate();
      pstmt.close();
    } catch (Exception e) {
      System.err.println(
          "DB_Writer::insertSongHasGenre: " + this.getClass() + ": " + e.getClass().getName() + ": "
              + e
              .getMessage());
    }
  }

  private void insertCreditWithSong(int creditId, int songId, int order) {
    assert (order > 0) : "Error: order = " + order;
    try {
      String sql = "INSERT OR IGNORE INTO credit_with_song (credit_id, song_id, \"order\") VALUES (?, ?, ?)";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setInt(1, creditId);
      pstmt.setInt(2, songId);
      pstmt.setInt(3, order);
      pstmt.executeUpdate();
      pstmt.close();
    } catch (Exception e) {
      System.err.println(
          "DB_Writer::insertCreditWithSong: " + this.getClass() + ": " + e.getClass().getName()
              + ": " + e
              .getMessage());
    }
  }

  /**
   * @param creditId the id of the album artist. - user need to GUARANTEE that for this specific
   * credit id, the attribute credit_as = "M".
   * @param albumId the id of the album.
   * @param order the order of this album artist representing.
   */
  private void insertCreditWithAlbum(int creditId, int albumId, int order) {
    assert (order > 0) : "Error: order = " + order;

    try {
      String sql = "INSERT OR IGNORE INTO credit_with_album (credit_id, album_id, \"order\") VALUES (?, ?, ?)";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setInt(1, creditId);
      pstmt.setInt(2, albumId);
      pstmt.setInt(3, order);
      pstmt.executeUpdate();
      pstmt.close();
    } catch (Exception e) {
      System.err.println(
          "DB_Writer::insertCreditWithAlbum: " + this.getClass() + ": " + e.getClass().getName()
              + ": " + e
              .getMessage());
    }
  }

  /**
   * Add credit for a particular artist - For most case, the information of the artist has been
   * added into the database - no need to check if exist: INSERT OR IGNORE INTO
   *
   * @param peopleID people.id for the artist
   * @param credit A:artist M:album_artist C: composer
   */
  protected void insertCredit(int peopleID, String credit) {
    if (!(credit.equals(Constant.credit_as_album_artist) || credit.equals(Constant.credit_as_artist)
        || credit.equals(Constant.credit_as_composer))) {
      throw new IllegalArgumentException("DB_Writer::addCredit addCreditUnknown credit type");
    }
    try {
      String sql = "INSERT OR IGNORE INTO credit (people_id, credit_as) VALUES (?, ?)";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setInt(1, peopleID);
      pstmt.setObject(2, credit);
      pstmt.executeUpdate();
      pstmt.close();
    } catch (Exception e) {
      System.err.println(
          "DB_Writer::addCredit: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
  }

  /**
   * This function will only be called by getPeopleId.getPeopleId
   */
  protected void insertPeople(String peopleName) {
    if (!(new Throwable()).getStackTrace()[1].getMethodName().equals("getPeopleId")) {
      throw new IllegalCallerException(
          "Method DB_Writer::insertPeople can only be called by DB_Reader::getPeopleId");
    }
    if (peopleName == null || peopleName.equals("")) {
      throw new IllegalArgumentException("DB_Writer::insertPeople Argument is null or \"\"");
    }
    try {
      Integer pictureID = db_reader
          .getPictureId(Constant.picPathForPeople + Constant.fileSeparator + peopleName);
      String sql = "INSERT INTO people (name, picture_id) VALUES (?, ?)";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, peopleName);
      pstmt.setObject(2, pictureID);
      pstmt.executeUpdate();
      pstmt.close();
    } catch (Exception e) {
      System.err.println(
          "insertPeople: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
  }

  /**
   * Insert the name of the genre into the database - no need to check if exist: INSERT OR IGNORE
   * INTO
   *
   * @param genre the name of the genre
   */
  protected void insertGenre(String genre) {
    if (genre == null || genre.equals("")) {
      throw new IllegalArgumentException("DB_Writer::insertGenre Argument is null or \"\"");
    }
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

  /**
   * Insert the name, pictureId, trackTotalNumber of the genre into the database - no need to check
   * if exist: INSERT OR IGNORE INTO
   *
   * @param albumName name of this album
   * @param trackTotalNumber the number of the song belongs to this album (could be NULL)
   */
  private void insertAlbum(String albumName, Integer trackTotalNumber) {
    if (albumName == null || albumName.equals("")) {
      throw new IllegalArgumentException("DB_Writer::insertAlbum Argument is null or \"\"");
    }
    try {
      Integer pictureId = db_reader
          .getPictureId(Constant.picPathForAlbum + Constant.fileSeparator + albumName);
      /* INSERT */
      String sql = "INSERT OR IGNORE INTO album (name, picture_id, track_total_number) VALUES (?, ?, ?)";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, albumName);
      pstmt.setObject(2, pictureId);
      pstmt.setObject(3, trackTotalNumber);
      pstmt.executeUpdate();
      pstmt.close();
    } catch (Exception e) {
      System.err.println(
          "insertAlbum: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
  }
}
