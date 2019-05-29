import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    System.out.println("Database Connecting");
    try {
      Class.forName("org.sqlite.JDBC");
      this.conn = DriverManager.getConnection(url);
      stateCode = 0;
      System.out.println("Database Connectted");
    } catch (Exception e) {
      stateCode = 1;
      System.err.println(this.getClass() + ": " + e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
  }

  public void setAutoCommit(boolean isAutoCommit) {
   try {
     this.conn.setAutoCommit(isAutoCommit);
   } catch (SQLException e) {
     System.err.println("Error in DB_Connector::setAutoCommit");
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
    DB_Writer db_writer = new DB_Writer(connector);
    String name = "artist_1";
    Integer trackNumber = 10;
//    db_writer.insertArtistInfo(name);
//    db_writer.insertAlbum("album_1", 10);
    connector.close();
  }

}
