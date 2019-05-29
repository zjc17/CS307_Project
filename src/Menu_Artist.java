import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu_Artist {
  private final int numPerPage = 5; // 若修改需要更改DE_Reader
  private DB_Writer writter;
  private DB_Reader reader;
  private Scanner input;
  static  int all[]=new int[6];
  public Menu_Artist(DB_Writer writter, DB_Reader reader) {

    this.writter = writter;
    this.reader = reader;
    this.input = new Scanner(System.in);
    showArtist();
    IN_THIS_MENU: // 使用标签来直接跳出循环
    while (true) {
      /* 写你需要的方法再进行调用
       * 输出某个值 break 即可，
       */
      System.out.println("Input -1 to exit\n"
          + "or choose the id of song");
      int tmp = input.nextInt();
      if(tmp==-1)
        System.exit(0);
      int next = all[tmp];
      switch (next) {
        case -1:
          System.exit(0);
        case 0:
          break IN_THIS_MENU;
      }
    }
    System.out.println("退出ArtistListMenu");
    this.input.close();
}

  private void showArtist() {
    int pageNum = 0;
    RUNNING:
    while (true) {
      System.out.println("Page "+pageNum+"\nArtist ID       Artist Name ");
      showArtist(pageNum * numPerPage);
      System.out.println("Input 1 to next page\n"
          + "Input -1 to exit\n"
          + "Input 0 to choose artist on this page\n"
          + "Input 2 to go back to main menu\n");
      System.out.println("Please choose: ");
      int choose = input.nextInt();
      switch (choose) {
        case -1:
          System.exit(0);
        case 0:
          System.out.println("Please choose the artist you want on this page: ");
          int artist = input.nextInt();
          ResultSet rs = reader.getSongFromArtistId(artist, 0);

          int temp = 0;
          try {
            rs.next();
            if (rs.isClosed()) {
              return;
            } else {
              while (!rs.isClosed()) {
                temp++;
                int id = rs.getInt("song_id");
                String name = rs.getString("song_name");
                System.out.printf("%2d\t\t%20s\n", temp, name);
                rs.next();
              }
            }
          } catch (SQLException e) {
            System.err.println("Error in printAlbumInfo");
          }

          break RUNNING;
        case 1:
          pageNum++;
          break;
        case 2:
          Menu_Main menuMain = new Menu_Main();
          menuMain.Menu();
          break RUNNING;
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
        int point=0;
        do {
          point++;
          int id = resultSet.getInt("id");
          all[point]=id;
          String name = resultSet.getString("name");
          System.out.printf("%2d\t\t%20s\n", point, name);
          resultSet.next();
        } while (!resultSet.isClosed());
      }
    } catch (SQLException e) {
      System.err.println("Error in printAlbumInfo");
    }
  }

  private void showSongofArtist(int artistId){
    //TO DO:
    //展示此歌手部分信息和他的歌曲列表
    System.out.println("这里是选中的歌手的信息和他的歌曲"); //

  }
}
