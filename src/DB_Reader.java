import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DB_Reader {
  private Connection connection;
  private DB_Writer writter;

  public DB_Reader(DB_Connector connector, DB_Writer db_writer) {
    this.writter = db_writer;
    this.connection = connector.getConn();
  }

  public Integer getPictureId(String picturePath) {
    return null;
  }

  public int getSongId(String songName) {
    try {
      String sql = "SELECT id FROM song WHERE name = ?";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, songName);
      ResultSet rs = pstmt.executeQuery();
      if (rs.isClosed()) {
        pstmt.close();
        return -1;
      } else{
        int songId = rs.getInt("id");
        pstmt.close();
        rs.close();
        return songId;
      }
    }catch (Exception e) {
      System.err.println(
          "getSongId: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
    throw new IllegalStateException("DB_Reader:: getCreditId:: IllegalStateException");
  }

  /**
   * must ensure the database hold this data.
   * @param peopleId
   * @param creditAs
   * @return
   */
  public int getCreditId(int peopleId, String creditAs) {
    try {
      String sql = "SELECT id FROM credit WHERE people_id = ? AND credit_as = ?";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setInt(1, peopleId);
      pstmt.setString(2, creditAs);
      ResultSet rs = pstmt.executeQuery();
      if (rs.isClosed()) { // no such people
        writter.insertCredit(peopleId, creditAs);
        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, peopleId);
        pstmt.setString(2, creditAs);
        rs = pstmt.executeQuery();
      }
      int people_id = rs.getInt("id");
      pstmt.close();
      rs.close();
      return people_id;
    } catch (Exception e) {
      System.err.println(
          "getCreditId: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
    throw new IllegalStateException("DB_Reader:: getCreditId:: IllegalStateException");
  }

  /**
   * search people.id for the person with given name
   * if not exist, call DB_Writer:: insertPeople
   * @param peopleName The name of the people
   * @return people.id for the given name
   */
  public int getPeopleId(String peopleName) {
    try {
      String sql = "SELECT id FROM people WHERE name = ?";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, peopleName);
      ResultSet rs = pstmt.executeQuery();
      if (rs.isClosed()) { // no such people
        writter.insertPeople(peopleName);
        pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, peopleName);
        rs = pstmt.executeQuery();
      }
      int people_id = rs.getInt("id");
      pstmt.close();
      rs.close();
      return people_id;
    } catch (Exception e) {
      System.err.println(
          "getPeopleId: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
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
      if (rs.isClosed()) {
        writter.insertGenre(genreName);
        sql = "SELECT id FROM genre WHERE name = ?";
        pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, genreName);
        rs = pstmt.executeQuery();
      }
      int genreId = rs.getInt("id");
      rs.close();
      pstmt.close();
      return genreId;
    } catch (Exception e) {
      System.err.println(
          "DB_Reader::getGenreId: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
    return -1;
  }

  /*
  protected int getFileTypeId(String typeName) {
    try {
      String sql = "SELECT id FROM file_type WHERE name = ?";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, typeName);
      ResultSet rs = pstmt.executeQuery();
      if (rs.isClosed()) {
        throw new IllegalAccessException("Unsupported file type: <" + typeName+">");
      }
      int fileTypeId = rs.getInt("id");
      pstmt.close();
      rs.close();
      return fileTypeId;
    } catch (Exception e) {
      System.err.println(
          "DB_Reader::getFileTypeId: " + this.getClass() + ": " + e.getMessage());
    }
    throw new IllegalAccessError("Unsupported file type");
  }
  */

  protected Integer getAlbumId(String albumName) {
    try {
      String sql = "SELECT id FROM album WHERE name = ?";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, albumName);
      ResultSet rs = pstmt.executeQuery();

      if (rs.isClosed()) {
        return null;
      }
      int albumId = rs.getInt("id");
      pstmt.close();
      rs.close();
      return albumId;
    } catch (Exception e) {
      System.err.println(
          "DB_Reader::getFileTypeId: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
    return null;
  }

  protected ResultSet getAlbumInfo(int offset) {
    try {
      String sql = "SELECT id, name, picture_id FROM album ORDER BY CASE WHEN name_for_sort IS NOT NULL THEN name_for_sort ELSE name END LIMIT 5 offset ?";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setInt(1, offset);
      ResultSet rs = pstmt.executeQuery();
      return rs;
    } catch (Exception e) {
      System.err.println(
          "DB_Reader::getAlbumInfo: " + this.getClass() + ": " + e.getClass().getName() + ": " + e
              .getMessage());
    }
    throw new IllegalStateException("Wrong: getAlbumInfo");

  }
}
