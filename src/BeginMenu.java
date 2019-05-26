import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class BeginMenu {

  private final int MaxPerPage = 10;

  private DB_Connector connector;
  private DB_Writer writter;
  private DB_Reader reader;

  public BeginMenu() {
    this.connector = new DB_Connector();
    this.writter = new DB_Writer(connector);
    this.reader = new DB_Reader(connector, writter);
  }

  private Album[] albumView = new Album[MaxPerPage];

  static Scanner input = new Scanner(System.in);
  static ArrayList<Artist> singer = new ArrayList<Artist>();
  static ArrayList<Album> album = new ArrayList<Album>();
  static ArrayList<Song> song = new ArrayList<Song>();
  static ArrayList<PlayList> playlist = new ArrayList<PlayList>();

  public static void Menu() {
    System.out.println("\n1.Artist\n"
        + "2.Album\n"
        + "3.Songs\n"
        + "4.PlayList\n"
        + "5.Search\n"
        + "6.Exit\n");
    int next;
    next = input.nextInt();
    switch (next) {
      case 1:
        Singer();
        break;
      case 2:
        Album();
        break;
      case 3:
        Song();
        break;
      case 4:
        PlayList();
        break;
      case 5:
        Search();
        break;
      case 6:
        System.exit(233);
      default:

    }
  }

  public static void Singer() {
    System.out.println("Here is the list of all the artists:");
    //get artist
    singer.add(new Artist(1, "aaaaaa")); //example
    for (int i = 0; i < singer.size(); i++) {
      System.out.println(singer.get(i).id + " " + singer.get(i).name);
    }
    System.out.println(
        "Now choose the singer you want by input his/her number or input -1 to go back to MainMenu");
    int next = input.nextInt();
    switch (next) {
      case -1:
        Menu();
        break;
      default:
        showSinger(singer, next);

    }
  }

  public static void showSinger(ArrayList<Artist> now, int next) {
    if (next < now.size()) {
      //show artist(next) information
    } else {
      System.out.println("The artist you request is not valid.\nPlease choose again.");
      Singer();
    }
  }

  public static void Album() {
    System.out.println("Here is the list of all albums:");

    //get artist
    album.add(new Album(1, "aaaa", "aaa", 2)); //example
    for (int i = 0; i < album.size(); i++) {
      System.out.println(album.get(i).id + " " + album.get(i).name);
    }
    System.out.println("Now choose index of the album you want or input -1 to go back to MainMenu");
    int next = input.nextInt();
    switch (next) {
      case -1:
        Menu();
        break;
      default:
        showAlbumInformation(next);
        break;//展示专辑的信息以及其歌曲

    }
  }

  public static void showAlbumInformation(int id) {
    if (id <= album.size()) {
      System.out.println("Album name: " + album.get(id).name);
      System.out.println("Artist name: " + album.get(id).artist);
      System.out.println("Track:");
      for (int i = 0; i < album.get(id).trackNum; i++) {
        System.out.println(i + 1 + " " + album.get(id).track.get(i).name);
      }
      System.out.println("Now choose the song to play or\ninput -1 to get back");
      int next = input.nextInt();
      if (next == -1) {
        Album();
      } else {
        //play song
      }
    } else {
      System.out.println("The album does not exist");
      Album();
    }
  }

  public static void Song() {
    System.out.println("Here is the list of all songs:");
    //get song
    song.add(new Song(1, "aa", "aaa")); //example
    for (int i = 0; i < song.size(); i++) {
      System.out.println(song.get(i).id + " " + song.get(i).name);
    }
    System.out.println("Now choose index of the song you want or input -1 to go back to MainMenu");
    int next = input.nextInt();
    switch (next) {
      case -1:
        Menu();
        break;
      default:
        showSongInformation(song, next);
    }
  }

  public static void showSongInformation(ArrayList<Song> now, int next) {
    if (next < now.size()) {
      System.out.println(now.get(next).name);
      System.out.println("1.Play this song\n" + "2.More information\n" + "3.Back\n");
      int nextstep = input.nextInt();
      switch (nextstep) {
        case 1: //play
          break;
        case 2:
          //display
          break;
        case 3:
          Song();
      }
    } else {
      System.out.println("The song you request is not valid.\nPlease choose again.");
      Song();
    }

  }

  public static void PlayList() {
    System.out.println("Here is the list of all PlayLists:");

    //get song
    playlist.add(new PlayList(1, "favorite")); //example
    for (int i = 0; i < playlist.size(); i++) {
      System.out.println(playlist.get(i).id + " " + playlist.get(i).listname);
    }
    System.out.println(
        "Now choose index of the PlayList you want\n or input -1 to go back to MainMenu\n or input 0 to edit");
    int next = input.nextInt();
    switch (next) {
      case 0:
        EditPlayList();
        break;
      case -1:
        Menu();
        break;

      default: //展示歌单

    }
  }

  public static void EditPlayList() {
    System.out.println("1. Add new PlayList\n" + "2.Delete existing playList");
    int operation = input.nextInt();
    if (operation == 1) {
      //add new

    } else if (operation == 2) {
      int todelete = input.nextInt();
      if (todelete <= playlist.size()) {

      } else {
        System.out.println("The PlayList does not exist. Deletion failed");
        PlayList();
      }
    }
  }

  public static void Search() {
    String next = input.nextLine();
    //search result
    //input -1 to go back to Menu()
  }

  private void printAlbumInfo(int offset)   {
    ResultSet resultSet = reader.getAlbumInfo(offset);

    try {
      resultSet.next();
      if (resultSet.isClosed()) {
        return;
      } else {
        do {
          int id = resultSet.getInt("id");
          String name = resultSet.getString("name");
          int pic_id = resultSet.getInt("picture_id");
          System.out.printf("%2d\t\t%20s\t\t%2d\n", id, name, pic_id);
          resultSet.next();
        } while (!resultSet.isClosed());
      }

    } catch (SQLException e) {
      System.err.println("Error in printAlbumInfo");
    }
  }

  public static void main(String[] args) {
//    System.out.println("Welcome to use ***!");
//    Menu();
    BeginMenu beginMenu = new BeginMenu();

    beginMenu.printAlbumInfo(5);
  }
}
