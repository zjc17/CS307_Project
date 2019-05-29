import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu_Album {

  private DB_Writer writter;
  private DB_Reader reader;
  private int pageNum;
  private final int numPerPage = 5; // 若修改需要更改DE_Reader
  private Scanner input;
  static  int all[]=new int[6];
  public Menu_Album(DB_Writer writter, DB_Reader reader) {
    this.writter = writter;
    this.reader = reader;
    this.input = new Scanner(System.in);
    IN_THIS_MENU: // 使用标签来直接跳出循环
    while (true) {
      /* 写你需要的方法再进行调用
       * 输出某个值 break 即可，
       */
      showAlbum();
      System.out.println("选中的Album展示完了，然后");
      System.out.println("Now choose id of the song you want or input -1 to exit");
      int next = all[input.nextInt()];
      // System.out.println(next);
      switch (next) {
        case -1:
          System.exit(0);
        default:
          break IN_THIS_MENU;
      }
    }
    //  System.out.println("Menu_Album");
    input.close();
  }

  private void showAlbum() {
    this.pageNum = 0;
    RUNNING:
    while (true) {
      System.out.println("Page "+pageNum+"\nAlbum ID       Album Name          Rating");
      showAlbum(pageNum * numPerPage);
      System.out.println("Input 1 to next page\n"
          + "Input -1 to exit\n"
          + "Input 0 to choose album on this page\n"
          + "Input 2 to go back to main menu\n");
      System.out.println("Please choose: ");
      int choose = input.nextInt();
      switch (choose) {
        case 2:
          Menu_Main menuMain = new Menu_Main();
          menuMain.Menu();
          break RUNNING;
        case -1:
          System.exit(0);
        case 0:
          System.out.println("Please choose the album you want on this page: ");
          int album = input.nextInt();
          showSongInAlbum(album);
          break RUNNING;
        case 1:
          pageNum++;
          break;
      }
    }
  }

  private void showAlbum(int offset) {
    ResultSet resultSet = reader.getAlbumInfo(offset);
    try {
      resultSet.next();
      if (resultSet.isClosed()) {
        return;
      } else {
        int point=0;
        do {
          point++;
          int id = resultSet.getInt("id");
          all[point]=id;
          String name = resultSet.getString("name");
          int pic_id = resultSet.getInt("picture_id");
          System.out.printf("%2d\t\t%20s\t\t%2d\n", point, name, pic_id);
          resultSet.next();
        } while (!resultSet.isClosed());
      }
    } catch (SQLException e) {
      System.err.println("Error in printAlbumInfo");
    }
  }

  // TODO:
  private void showSongInAlbum(int albumId) {
    System.out.println("未开发");
  }

}
