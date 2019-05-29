
import java.io.File;

public class AddFiles {

  private DB_Writer writter;
  private DB_Connector connector;
  private final int batch_size = 10;

  public AddFiles(String path, DB_Connector connector, DB_Writer writter) {
    this.connector = connector;
    this.writter = writter;
    getFileName(path);
  }

  public static void main(String[] args) {
    DB_Connector connector = new DB_Connector();
    connector.getConnection();
    DB_Writer writter = new DB_Writer(connector);

    System.out.println("main");
    String dirPath = "F:\\Music";
    long startTime = System.currentTimeMillis();
    AddFiles addFiles = new AddFiles(dirPath, connector, writter);
    long endTime = System.currentTimeMillis();
    System.out.printf("耗时：%.3fs\n",(endTime - startTime)/1000.0);
    connector.close();
  }

  private  void getFileName(String dirPath) {
    connector.setAutoCommit(false);   // 35 - 1.571
    File file = new File(dirPath);    //获取其file对象
    func(file);
    connector.setAutoCommit(true);
  }

  private  void func(File file) {
    File[] fs = file.listFiles();
    int cnt = 0;
    for (File f : fs) {
      if (f.isDirectory()) {
        func(f);
      }
      if (f.isFile()) {
        String fileName = f.getName();
        String filePath = f.getPath();
        if (filePath.endsWith("mp3")) {
          System.out.println(fileName);
          parseMP3(filePath);
          cnt++;
        }
      }
      if (cnt == batch_size) {
        connector.commit();
        cnt = 0;
      }

    }
    if (cnt > 0) {
      connector.commit();
    }
  }

  private  void parseMP3(String filePath) {
    Parser_MP3 parser_mp3 = new Parser_MP3(filePath);

    String songName = parser_mp3.getSongName().trim();
    String album = parser_mp3.getAlbum().trim();
    Integer year = strToInteger(parser_mp3.getYear());
    String artist = parser_mp3.getArtist().trim();
    Double BPM = strToDouble(parser_mp3.getBPM());
    Integer sampleRate = parser_mp3.getSampleRate();
    Integer bitRate = parser_mp3.getBitRate();
    String MPEG_Version = parser_mp3.getMpegVersion().trim();
    String MPEG_Layer = parser_mp3.getMpegLayer().trim();

    String channels = parser_mp3.getChannels().trim();
    String comments = parser_mp3.getComments().trim();
    Integer fileSize = parser_mp3.getSize();
    Integer length = parser_mp3.getLength();
    Integer trackOrder = strToInteger(parser_mp3.getTrackTotal());
    String albumArtist = parser_mp3.getAlbumArtist().trim();
    String composer = parser_mp3.getComposer().trim();
    String genre = parser_mp3.getGenre().trim();
    Integer trackTotal = strToInteger(parser_mp3.getTrackTotal());

    this.writter.insertSong(songName, filePath, album, year, artist, BPM, sampleRate, bitRate,
        MPEG_Version, MPEG_Layer, channels, comments, fileSize, length,
        trackOrder, albumArtist, composer, genre, trackTotal);
  }

  private  Integer strToInteger(String str) {
    if (str.equals("")) {
      return null;
    }
    return Integer.parseInt(str);
  }

  private  Double strToDouble(String str) {
    if (str.equals("")) {
      return null;
    }
    return Double.parseDouble(str);
  }

}
