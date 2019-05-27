import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu_Song {
  private final int numPerPage = 10; // 若修改需要更改DE_Reader
  private DB_Writer writter;
  private DB_Reader reader;
  private int pageNum;
  private Scanner input;

  public Menu_Song(DB_Writer writter, DB_Reader reader) {
    this.writter = writter;
    this.reader = reader;
    this.input = new Scanner(System.in);
    IN_THIS_MENU: // 使用标签来直接跳出循环
    while (true) {
      /* 写你需要的方法再进行调用
       * 输出某个值 break 即可，
       */
      System.out.println("Here is the list of all songs:");
      showSong();
      System.out.println("Now choose index of the song you want or input 0 to go back to MainMenu");
      int next = input.nextInt();
      switch (next) {
        case -1:
          System.exit(0);
        case 0:
          break IN_THIS_MENU;
        default:
      }
    }
    System.out.println("exit Menu_Song");
    input.close();
  }

  private void showSong() {
    this.pageNum = 0;

    while (true) {
      System.out.println("这是表头，你自己添加");
      showSong(pageNum * numPerPage);
      System.out.println("输入1下一页"
          + " 输入-1退出程序"
          + "输入0返回上一级菜单");
      int choose = input.nextInt();
      RUNNING:
      switch (choose) {
        case -1:
          System.exit(0);
        case 0:
          break RUNNING;
        case 1:
          pageNum++;
          break;
      }
    }
  }

  private void showSong(int offset) {
    ResultSet resultSet = reader.getSongInfo(offset);
    try {
      resultSet.next();
      if (resultSet.isClosed()) {
        return;
      } else {
        do {
          int id = resultSet.getInt("id");
          String songName = resultSet.getString("name");
          int length = resultSet.getInt("length");
          String lengthInMinute = resultSet.getString("length_in_minute");
          String albumName = resultSet.getString("album_name");
          String genreName = resultSet.getString("genre_name");
          int rating = resultSet.getInt("rating");
          String filePath = resultSet.getString("file_path");
          int pic_id = resultSet.getInt("picture_id");
          System.out.printf("%2d\t%30s\t%5s\t%20s\t%10s\t%d\t\t%60s\t%2d\n", id, songName, lengthInMinute, albumName, genreName, rating, filePath, pic_id);
          resultSet.next();
        } while (!resultSet.isClosed());
      }
    } catch (SQLException e) {
      System.err.println("Error in printAlbumInfo");
    }
  }

  /**
   * 更新歌曲评分
   * @param songId 歌曲id
   * @param rating 更新的评分，数值为1，2，3，4，5
   */
  private void ratingForSong(int songId, int rating) {
    if (0 <= rating && rating <= 5) {
      writter.updateSongRate(songId, rating);
    } else{
      System.err.println("rate should be between 1 and 5");
    }
  }
}
