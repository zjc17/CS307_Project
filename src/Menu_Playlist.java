import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu_Playlist {

  private final int numPerPage = 5; // 若修改需要更改DE_Reader
  private DB_Writer writter;
  private DB_Reader reader;
  private Scanner input;
  private PlayList[] curPlylist = new PlayList[numPerPage];
  public static ArrayList all=new  ArrayList<Integer>();
  public static int point=0;
  public Menu_Playlist(DB_Writer writter, DB_Reader reader) {

    this.writter = writter;
    this.reader = reader;
    this.input = new Scanner(System.in);
    IN_THIS_MENU: // 使用标签来直接跳出循环
    while (true) {
      /* 写你需要的方法再进行调用
       * 输出某个值 break 即可，
       */
      showPlaylist();
      System.out.println("Input the Id of the PlayList you want\n"
          + "Input -1 to exit\n"
          + "Input 0 to go back to MainMenu\n"
          +"Input 2 to edit playList");
      int next = input.nextInt();
      switch (next) {
        case -1:
          System.exit(0);
        case 0:
          Menu_Main menuMain = new Menu_Main();
          menuMain.Menu();
          break IN_THIS_MENU;
        case 2:
          editPlayList();
          break IN_THIS_MENU;
        case 3:

        default: //展示歌单
          showSongInPlaylist(next,0);
      }
    }
    System.out.println("退出PlaylistMenu");
    this.input.close();
  }


  /***********************************************************
   * 关于 playlist的操作
   *  - addSongToPlaylist(int songId, int playListId)
   *  - showPlaylist()
   *  - showSongInPlaylist(int playlistId, int offset)
   *  - addPlaylist(String playlistName)
   *  - editPlayList()
   *
   * *********************************************************/

  public void editPlayList() {
    System.out.println("1. Add new PlayList\n" + "2. Delete existing playList");
    int operation =input.nextInt();
    if (operation == 1) {
      //add new
      System.out.println("Input the name: ");
      String newname = input.next();
      //all.add(newname);
      addPlaylist(newname);
    } else if (operation == 2) {
      System.out.println("Input the id of PlayList you want to delete");
      if (false) { // check number
        System.err.println("The PlayList does not exist. Deletion failed");
      }
      int todelete = (Integer)all.get(input.nextInt()-1);
      System.out.println(todelete);
      deletePlaylist(todelete);



    }
    //  showPlaylist();
    Menu_Playlist menuPlaylist = new Menu_Playlist(writter, reader);
  }

  /**
   * 需要先查询，保证传参合法进行插入操作
   * @param songId 需要添加的歌曲id
   * @param playListId 需要添加的播放列表id
   */
  private void addSongToPlaylist(int songId, int playListId) {
    writter.insertPlaylistHasSong(playListId, songId);
  }

  private void showPlaylist() {
    System.out.println("PlayList: ");
    ResultSet resultSet = reader.getPlaylistInfo();
    try {
      resultSet.next();
      if (resultSet.isClosed()) {
        return;
      } else {
        point=0;
        do {
          //  point=0;
          point++;
          int id = resultSet.getInt("id");
          all.add(id);
          String playlistName = resultSet.getString("name");
          System.out.printf("%2d\t%30s\n", point, playlistName);
          resultSet.next();
        } while (!resultSet.isClosed());
      }
    } catch (SQLException e) {
      System.err.println("Error in printAlbumInfo");
    }
  }


  /**
   * 默认每页展示十首歌
   * @param playlistId 需要展示的播放列表id
   * @param offset 10*(page-1)
   */

  private void showSongInPlaylist(int playlistId, int offset) {
    ResultSet resultSet = reader.getSongInPlaylist(playlistId, offset);
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
   * 添加新的播放列表
   * @param playlistName 播放列表名字，若重名会添加失败并抛出异常
   */
  private void addPlaylist(String playlistName) {
    writter.insertPlaylist(playlistName);
  }

  private void deletePlaylist(int playlistId) {
    writter.deletePlaylist(playlistId);
  }

  private void deleteSongFromPlaylist(int songId, int playlistID) {
    writter.deletePlaylistHasSong(songId, playlistID);
  }

  // 内部类即可
  public class PlayList {
    int id;
    String listname;

    public PlayList(int id, String name){
      this.id = id;
      this.listname = name;
    }
  }
}
