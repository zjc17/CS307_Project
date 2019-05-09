
import java.io.File;

public class Main {

  private static long startTime, endTime;
  private static DB_Connector connector = new DB_Connector();

  public static void main(String[] args) {
//  check();

    connector.getConnection();
    DB_Writer writter = new DB_Writer(connector);
    DB_Reader reader = new DB_Reader(connector, writter);
    String dirPath = "F:\\Music";
//    parseMP3("F:\\Music\\Taylor Swift\\reputation\\03 I Did Something Bad.mp3");
//System.out.println(    reader.getFileTypeId("mp3"));
    getFileName(dirPath);
    connector.close();

  }

  private static void getFileName(String dirPath) {
    File file = new File(dirPath);    //获取其file对象
    func(file);
  }

  private static void func(File file) {
    File[] fs = file.listFiles();
    for (File f : fs) {
      if (f.isDirectory()) {
        func(f);
      }
      if (f.isFile()) {
        String fileName = f.getName();
        String filePath = f.getPath();
        if (!filePath.endsWith("mp3")) {
          continue;
        }
        System.out.println(fileName);
        parseMP3(filePath);

      }

    }
  }

  private static void parseMP3(String filePath) {
    Parser_MP3 parser_mp3 = new Parser_MP3(filePath);

    String songName = parser_mp3.getSongName();
    String album = parser_mp3.getAlbum();
    Integer year = strToInteger(parser_mp3.getYear());
    String artist = parser_mp3.getArtist();
    Double BPM = strToDouble(parser_mp3.getBPM());
    Integer sampleRate = parser_mp3.getSampleRate();
    Integer bitRate = parser_mp3.getBitRate();
    String MPEG_Version = parser_mp3.getMpegVersion();
    String MPEG_Layer = parser_mp3.getMpegLayer();

    String channels = parser_mp3.getChannels();
    String comments = parser_mp3.getComments();
    Integer fileSize = parser_mp3.getSize();
    Integer length = parser_mp3.getLength();
    Integer trackOrder = strToInteger(parser_mp3.getTrackTotal());
    String albumArtist = parser_mp3.getAlbumArtist();
    String composer = parser_mp3.getComposer();
    String genre = parser_mp3.getGenre();
    Integer trackTotal = strToInteger(parser_mp3.getTrackTotal());
    DB_Writer writter = new DB_Writer(connector);
    writter.insertSong(songName, filePath, album, year, artist, BPM, sampleRate, bitRate,
        MPEG_Version, MPEG_Layer, channels, comments, fileSize, length,
        trackOrder, albumArtist, composer, genre, trackTotal);
  }

  private static Integer strToInteger(String str) {
    if (str.equals("")) {
      return null;
    }
    return Integer.parseInt(str);
  }

  private static Double strToDouble(String str) {
    if (str.equals("")) {
      return null;
    }
    return Double.parseDouble(str);
  }

}
