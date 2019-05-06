import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DB_Reader {
  private Connection connection;
  private DB_Connector connector;

  public DB_Reader(DB_Connector connector) {
    this.connector = connector;
    this.connection = connector.getConn();
  }

  public Integer getPictureId(String songName, String albumName, String artistName) {
    return null;
  }

  public int getArtistId(String artistName) {
    try {
      String sql = "SELECT id FROM artist WHERE name = ?";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, artistName);
      ResultSet rs = pstmt.executeQuery();
      int artist_id = rs.getInt("id");
      rs.close();
      pstmt.close();
      return artist_id;
    } catch (Exception e) {
      System.err.println(
          "insertPlaylist: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
    return -1;
  }

  public int getComposerId(String composerName) {
    try {
      String sql = "SELECT id FROM composer WHERE name = ?";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, composerName);
      ResultSet rs = pstmt.executeQuery();
      int composerId = rs.getInt("id");
      rs.close();
      pstmt.close();
      return composerId;
    } catch (Exception e) {
      System.err.println(
          "insertPlaylist: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
    return -1;
  }

  public int getGenreId(String genreName) {
    try {
      String sql = "SELECT id FROM genre WHERE name = ?";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, genreName);
      ResultSet rs = pstmt.executeQuery();
      int genreId = rs.getInt("id");
      rs.close();
      pstmt.close();
      return genreId;
    } catch (Exception e) {
      System.err.println(
          "insertPlaylist: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
    return -1;
  }
}
