import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DB_Connector {

  private Connection conn;
  private final String DATABASE_PATH = "test.db";
  private int stateCode;  // 0: OK  1: ERROR
  private String sql;
  private ResultSet rs;
  private PreparedStatement pstmt;

  public DB_Connector() {
    this.conn = null;
  }

  public void getConnection() {
    String url = "jdbc:sqlite:" + DATABASE_PATH;
    try {
      Class.forName("org.sqlite.JDBC");
      this.conn = DriverManager.getConnection(url);
      stateCode = 0;
//      connection.setAutoCommit(false);
    } catch (Exception e) {
      stateCode = 1;
      System.err.println(this.getClass() + ": " + e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
  }

  public Connection getConn() {
    return conn;
  }

  public void commit() {
    try {
      if (conn.getAutoCommit()) {
        System.out.println("database in auto-commit mode");
        return;
      }
      this.conn.commit();
    } catch (Exception e) {
      System.err.println(this.getClass() + ": " + e.getClass().getName() + ": " + e.getMessage());
    }
  }

  public void close() {
    try {
      this.conn.close();
    } catch (Exception e) {
      System.err.println(this.getClass() + ": " + e.getClass().getName() + ": " + e.getMessage());
    }
  }

  public void test() {
    try {
      String picPath = "test";
      sql = "SELECT id FROM picture WHERE path = ?";
      pstmt = conn.prepareStatement(sql);
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
    DB_Connector connector = new DB_Connector();
    connector.getConnection();
    DB_Writter db_writter = new DB_Writter(connector);
    String name = "artist_1";
    Integer trackNumber = 10;
//    db_writter.insertArtistInfo(name);
    db_writter.insertAlbumInfo("album_1", 10, "artist_1");
    connector.close();
  }

}
