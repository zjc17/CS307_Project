import java.util.Scanner;

public class Menu_Main {

  private final int MaxPerPage = 10;

  private DB_Writer writter;
  private DB_Reader reader;

  public Menu_Main() {
    DB_Connector connector = new DB_Connector();
    this.writter = new DB_Writer(connector);
    this.reader = new DB_Reader(connector, writter);
//    Menu();
  }


  public void Menu() {
    Scanner input = new Scanner(System.in);
    RUNNING:
    while (true) {
      System.out.println("1.Artist\n"
                        + "2.Album\n"
                        + "3.Songs\n"
                        + "4.PlayList\n"
                        + "5.Search\n"
                        + "6.添加新文件（文件路径或文件夹路径）\n"
                        + "0.Exit\n");
      int next = input.nextInt();
      switch (next) {
        case 0:
          break RUNNING;
        case 1:
          Menu_Artist menuArtist = new Menu_Artist(writter, reader);
          break;
        case 2:
          Menu_Album menuAlbum = new Menu_Album(writter, reader);
          break;
        case 3:
          Menu_Song menuSong = new Menu_Song(writter, reader);
          break;
        case 4:
          Menu_Playlist menuPlaylist = new Menu_Playlist(writter, reader);
          break;
        case 5:
          // Search
          break;
        case 6:
          String path = "F:\\Music";
          AddFiles addFiles = new AddFiles(path);
          break;
        default:
      }
    }
    System.out.println("Bye");
  }

  /**
   * 用于播放音乐
   * @param musicPath 需要播放的音乐路径
   */
  private void playSong(String musicPath) {
    Scanner keyboard = new Scanner(System.in);
    Music_Player music_player = new Music_Player(musicPath);
    music_player.start();
    System.out.println("Music is playing! Press 0 to stop it.");
    int off = -1;
    while(off == -1){
      off = keyboard.nextInt();
      if (off == 0) {
        music_player.stop();
        System.out.println("Music is stopped.");
      }
      else {
        System.out.println("Invalid input! Try again.");
        off = -1;
      }
    }
  }

  public static void main(String[] args) {
    System.out.println("Welcome to use ***!");
    Menu_Main menuMain = new Menu_Main();
    menuMain.Menu();

  }
}
