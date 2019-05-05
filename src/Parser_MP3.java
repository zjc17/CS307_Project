import java.io.File;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;

public class Parser_MP3 extends Song_Parser {

  private String songName;
  private String album;
  private String artist;
  private String albumArtist;
  private String composer;
  private String genre;

  private String year;
  private int size;
  private int length;

  private String discOrder;
  private String discTotal;
  private String trackOrder;
  private String trackTotal;

  private String comments;

  private int sampleRate;
  private String channels;
  private boolean isVariableBitRate;
  private String bpm;
  private String grouping;


  private String lyric;
  private String lyricist;
//  private int

  public static void main(String[] args) {
    String path = "C:\\Users\\acezj\\Desktop\\test3.mp3";
    Parser_MP3 parser_mp3 = new Parser_MP3(path);
    System.out.println(parser_mp3);
  }

  public Parser_MP3(String filePath) {
    super(null);
    try {
      File file = new File(filePath);
      MP3File mp3File = (MP3File) AudioFileIO.read(file);
      MP3AudioHeader header = mp3File.getMP3AudioHeader();
      this.length = header.getTrackLength();
      this.sampleRate = header.getSampleRateAsNumber();
      this.channels = header.getChannels();
      this.isVariableBitRate = header.isVariableBitRate();

      /* 使用 ID3v2 进行解析 */
      AbstractID3v2Tag v2tag = mp3File.getID3v2Tag();
      /* Song Info. */
      this.songName = v2tag.getFirst(FieldKey.TITLE);
      this.artist = v2tag.getFirst(FieldKey.ARTIST);
      this.album = v2tag.getFirst(FieldKey.ALBUM);
      this.albumArtist = v2tag.getFirst(FieldKey.ALBUM_ARTIST);
      this.composer = v2tag.getFirst(FieldKey.COMPOSER);
      this.grouping = v2tag.getFirst(FieldKey.GROUPING);
      this.genre = v2tag.getFirst(FieldKey.GENRE);
      this.year = v2tag.getFirst(FieldKey.YEAR);
      this.trackOrder = v2tag.getFirst(FieldKey.TRACK);
      this.trackTotal = v2tag.getFirst(FieldKey.TRACK_TOTAL);
      this.discOrder = v2tag.getFirst(FieldKey.DISC_NO);
      this.discTotal = v2tag.getFirst(FieldKey.DISC_TOTAL);
      this.bpm = v2tag.getFirst(FieldKey.BPM);
      this.comments = v2tag.getFirst(FieldKey.COMMENT);

      /* Artwork */

      /* Lyrics */
      this.lyric = v2tag.getFirst(FieldKey.LYRICS);
      this.lyricist = v2tag.getFirst(FieldKey.LYRICIST);
      /* Option */
      /* **********************************************
       * SKIP
       ********************************************** */
      /* Sorting */


    } catch (Exception e) {
    }
  }

  @Override
  public String getArtist() {
    return this.artist;
  }

  @Override
  public String getAlbum() {
    return this.album;
  }

  @Override
  public String getGenre() {
    return this.genre;
  }

  @Override
  public int getLength() {
    return this.length;
  }

  @Override
  public int getSize() {
    return this.size;
  }

  @Override
  public String getBPM() {
    return this.bpm;
  }

  @Override
  public String getSongName() {
    return this.songName;
  }

  @Override
  public String getTrackOrder() {
    return this.trackOrder;
  }

  @Override
  public int getSampleRate() {
    return this.sampleRate;
  }

  @Override
  public String getChannels() {
    return this.channels;
  }

  @Override
  public boolean isVariableBitRate() {
    return isVariableBitRate;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append("\nartist: " + artist);
    s.append("\nalbum: " + album);
    s.append("\nalbum artist: " + albumArtist);
    s.append("\ncomposer: " + composer);
    s.append("\ngrouping: " + grouping);
    s.append("\ngenre: " + genre);
    s.append("\nyear: " + year);
    s.append("\ntrack: " + trackOrder + " of " + trackTotal);
    s.append("\ndisc: " + discOrder + " of " + discTotal);
    s.append("\nBPM: " + bpm);
    s.append("\ncomments: " + comments);
    return s.toString();
  }


}
