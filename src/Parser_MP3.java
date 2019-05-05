import java.io.File;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;

public class Parser_MP3 extends Song_Parser{

  private String song_name;
  private String artist_name;
  private String genre;
  private String size;
  private String length;
  private String track_order;

  public static void main(String[] args) {
    String path = "C:\\Users\\acezj\\Desktop\\test2.mp3";
    Parser_MP3 parser_mp3 = new Parser_MP3(path);
  }

  public Parser_MP3(String filePath) {
    super(null);
    try {
      File file = new File(filePath);
      MP3File mp3File = (MP3File) AudioFileIO.read(file);
      MP3AudioHeader audioHeader = (MP3AudioHeader) mp3File.getAudioHeader();
      this.length = audioHeader.getTrackLength();
      System.out.println("getTrackLength: " + );
      System.out.println("getSampleRateAsNumber: " + audioHeader.getSampleRateAsNumber());
      System.out.println("getChannels: " + audioHeader.getChannels());
      System.out.println("isVariableBitRate: " + audioHeader.isVariableBitRate());
      AbstractID3v2Tag v2tag = mp3File.getID3v2Tag();
      //String artist = v2tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST);
      //String album = v2tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM);
      String artist = v2tag.getFirst(FieldKey.ARTIST);
      String album = v2tag.getFirst(FieldKey.ALBUM);
      String songName = v2tag.getFirst(FieldKey.TITLE);
      System.out.println("album: " + album); // 专辑名
      System.out.println("singer: " + artist); // 歌手名
      System.out.println("songName: " + songName); // 歌名
      MP3AudioHeader header = mp3File.getMP3AudioHeader(); // mp3文件头部信息
      int length = header.getTrackLength();
      System.out.println("Length: " + length / 60 + ":" + length % 60 + "sec"); // 歌曲时长
    } catch (Exception e) {

    }
  }

  @Override
  public String getArtist_name() {
    return this.artist_name;
  }

  @Override
  public String getGenre() {
    return this.genre;
  }

  @Override
  public String getLength() {
    return this.length;
  }

  @Override
  public String getSize() {
    return this.size;
  }

  @Override
  public String getSong_name() {
    return this.song_name;
  }

  @Override
  public String getTrack_order() {
    return this.track_order;
  }

  @Override
  public String toString() {
    return super.toString();
  }

}
