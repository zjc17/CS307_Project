public class Main {

  private static long startTime, endTime;

  public static void main(String[] args) {
    String songName = "Song_1";
    String filePath = "filePath_1";
    String picPath = "picPath_1";
    String album = "";
    Integer year = 1999;
    String artist = "Artist";
    Double BPM = 123.2;
    Integer sampleRate = 44;
    Integer bitRate = 180;
    String MPEG_Version = "MPEG-1";
    String MPEG_Layer = "MPEG-1";

    String channels = "联合立体声";
    String comments = "No Comments";
    Integer fileSize = 3010;
    Integer length = 180;
    Integer trackOrder = 2;

    String albumArtist = "";
    String composer = "Composer";
    String genre = "Blues";
    Integer trackTotal = 10;

    DB_Connector connector = new DB_Connector();
    connector.getConnection();
    DB_Writter writter = new DB_Writter(connector);
    writter
        .insertSongInfo(songName, filePath, picPath, album, year, artist, BPM, sampleRate, bitRate,
            MPEG_Version, MPEG_Layer, channels, comments, fileSize, length, trackOrder, albumArtist,
            composer, genre, trackTotal);
    connector.close();
  }

}
