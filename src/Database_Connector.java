import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database_Connector {

  private Connection connection;
  private final String DATABASE_PATH = "test.db";
  private int stateCode;  // 0: OK  1: ERROR
  private String sql;
  private ResultSet rs;
  private PreparedStatement pstmt;

  public Database_Connector() {
    this.connection = null;
  }

  public void getConnection() {
    String url = "jdbc:sqlite:" + DATABASE_PATH;
    try {
      Class.forName("org.sqlite.JDBC");
      this.connection = DriverManager.getConnection(url);
      stateCode = 0;
//      connection.setAutoCommit(false);
    } catch (Exception e) {
      stateCode = 1;
      System.err.println(this.getClass() + ": " + e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
  }

  private void insertSongInfo(String songName, String filePath, String album, String artist,
      String albumArtist, String composer,
      String genre, String year, int length, String grouping, String trackOrder, String trackTotal,
      String comments, String BPM) {
    try {
      /* INSERT picture: Need to add the pic file before */
      String picPath = "artWork" + File.separator + (album == "" ? songName : album) + ".jpg";
      insertPictureInfo(picPath);
      /* SELECT pic_id: Need to add the pic file before */
      sql = "SELECT id FROM picture WHERE path = ?";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, picPath);
      rs = pstmt.executeQuery();
      int pic_id = rs.getInt("id");

      sql = "INSERT INTO song (name, file_path, picture_id, album_id, year, rating, bpm, comments,"
          + " size, length, date_modified, date_added, track_order, play_count, last_play_time,"
          + " skip_count, last_skip_time)"
          + "VALUES (" + songName + "\",\"" + filePath + "\",\"";
      System.out.println("sql = \n" + sql);

    } catch (Exception e) {
      System.err.println(
          "insertSongInfo: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }

  }

  public void insertPictureInfo(String picPath) {
    try {
      sql = "INSERT OR IGNORE INTO picture (path) VALUES (?)";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, picPath);
      pstmt.executeUpdate();
    } catch (Exception e) {
      System.err.println(
          "insertPictureInfo: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
  }

  public void insertAlbumInfo(String name, Integer track_number) {
    try {
      Integer picture_id = getPictureId(null, null, null);
      PreparedStatement pstmt;
      String sql = "INSERT OR IGNORE INTO album (name, picture_id, track_number)" +
                    "VALUES (?, ?, ?)";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, name);
      pstmt.setObject(2, picture_id);
      pstmt.setObject(3, track_number);
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
      Integer picture_id = getPictureId(null, null, null);
      PreparedStatement pstmt;
      String sql = "INSERT OR IGNORE INTO artist (surname, picture_id) VALUES (?, ?)";
      pstmt = connection.prepareStatement(sql);
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

  public Integer getPictureId(String songName, String albumName, String artistName) {
    return null;
  }

  public void commit() {
    try {
      if (connection.getAutoCommit()) {
        System.out.println("database in auto-commit mode");
        return;
      }
      this.connection.commit();
    } catch (Exception e) {
      System.err.println(this.getClass() + ": " + e.getClass().getName() + ": " + e.getMessage());
    }
  }

  public void close() {
    try {
      this.connection.close();
    } catch (Exception e) {
      System.err.println(this.getClass() + ": " + e.getClass().getName() + ": " + e.getMessage());
    }
  }

  public void test() {
    try {
      String picPath = "test";
      sql = "SELECT id FROM picture WHERE path = ?";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, picPath);
      rs = pstmt.executeQuery();

      int pic_id = rs.getInt("id");
      while (rs.next()){

      }
    } catch (Exception e) {
      System.err.println(
          "test: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
  }

  public static void main(String[] args) {
    Database_Connector connector = new Database_Connector();
    connector.getConnection();
    String name = "artist_1";
    Integer trackNumber = 10;
    connector.insertArtistInfo(name);
    connector.close();
  }

}
