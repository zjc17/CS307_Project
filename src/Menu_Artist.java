import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu_Artist {
  private final int numPerPage = 5; // 若修改需要更改DE_Reader
  private DB_Writer writter;
  private DB_Reader reader;
  private Scanner input;

  public Menu_Artist(DB_Writer writter, DB_Reader reader) {

    this.writter = writter;
    this.reader = reader;
    this.input = new Scanner(System.in);
    IN_THIS_MENU: // 使用标签来直接跳出循环
    while (true) {
      /* 写你需要的方法再进行调用
       * 输出某个值 break 即可，
       */
      System.out.println("Here is the list of all Artist:");
      showArtist();
      System.out.println("Now choose index of the PlayList you want\n"
          + "Input -1 to exit\n"
          + "Input 0 to go back to MainMenu");
      int next = input.nextInt();
      switch (next) {
        case -1:
          System.exit(0);
        case 0:
          break IN_THIS_MENU;
        default: //展示歌单
      }
    }
    System.out.println("退出PlaylistMenu");
    this.input.close();
  }

  private void showArtist() {
    int pageNum = 0;
    RUNNING:
    while (true) {
      System.out.println("这是表头，你自己添加");
      showArtist(pageNum * numPerPage);
      System.out.println("输入1下一页"
                        + " 输入-1退出程序"
                        + "输入0返回上一级菜单");
      int choose = input.nextInt();
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

  private void showArtist(int offset) {
    ResultSet resultSet = reader.getArtistInfo(offset);
    try {
      resultSet.next();
      if (resultSet.isClosed()) {
        return;
      } else {
        do {
          int id = resultSet.getInt("id");
          String name = resultSet.getString("name");
          System.out.printf("%2d\t\t%20s\n", id, name);
          resultSet.next();
        } while (!resultSet.isClosed());
      }
    } catch (SQLException e) {
      System.err.println("Error in printAlbumInfo");
    }
  }
}
