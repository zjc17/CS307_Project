import java.io.File;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.AbstractID3v1Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v24Frames;

public class Parser_MP3 extends Song_Parser {

  /* Song Info. */
  private String songName;
  private String album;
  private String artist;
  private String albumArtist;
  private String composer;
  private String genre;
  private String year;
  private int length;
  private String grouping;
  private String discOrder;
  private String discTotal;
  private String trackOrder;
  private String trackTotal;
  private String comments;
  private String BPM;

  /* Sort Info. */
  private String artistSort;
  private String albumSort;
  private String albumArtistSort;
  private String composerSort;

  /* File Info. */
  private int sampleRate;
  private String channels;
  private boolean isVariableBitRate;
  private int size;
  private String identifier;
  private int bitRate;

  private String MPEG_Version;
  private String MPEG_Layer;

  private String lyric;
  private String lyricist;

  public Parser_MP3(String filePath) {
    try {
      File file = new File(filePath);
      MP3File mp3File = (MP3File) AudioFileIO.read(file);
      MP3AudioHeader header = mp3File.getMP3AudioHeader();
      this.length = header.getTrackLength();
      this.sampleRate = header.getSampleRateAsNumber();
      this.channels = header.getChannels();
      this.isVariableBitRate = header.isVariableBitRate();
      AbstractID3v1Tag v1tag = mp3File.getID3v1Tag();
      /* 使用 ID3v2 进行解析 */
      AbstractID3v2Tag v2tag = mp3File.getID3v2Tag();
      /* Song Info. */
      this.songName = v2tag.getFirst(FieldKey.TITLE);
      this.artist = v2tag.getFirst(FieldKey.ARTIST);
      this.album = v2tag.getFirst(FieldKey.ALBUM);
      this.albumArtist = v2tag.getFirst(FieldKey.ALBUM_ARTIST);
      this.composer = v2tag.getFirst(FieldKey.COMPOSER);
      this.grouping = v2tag.getFirst(FieldKey.GROUPING);
//      for (FieldKey fieldKey: FieldKey.values()) {
//        System.out.println(fieldKey+"\t\t\t" + v2tag.getFirst(fieldKey));
//      }
      System.out.println(header.getMpegLayer());
      this.genre = v2tag.getFirst(FieldKey.GENRE);
      this.year = v2tag.getFirst(FieldKey.YEAR);
      this.trackOrder = v2tag.getFirst(FieldKey.TRACK);
      this.trackTotal = v2tag.getFirst(FieldKey.TRACK_TOTAL);
      this.discOrder = v2tag.getFirst(FieldKey.DISC_NO);
      this.discTotal = v2tag.getFirst(FieldKey.DISC_TOTAL);
      this.BPM = v2tag.getFirst(FieldKey.BPM);
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
      this.artistSort = v2tag.getFirst(FieldKey.ARTISTS_SORT);
      this.albumSort = v2tag.getFirst(FieldKey.ALBUM_SORT);
      this.albumArtistSort = v2tag.getFirst(FieldKey.ALBUM_ARTIST_SORT);
      this.composer = v2tag.getFirst(FieldKey.COMPOSER_SORT);
      /* File Info. */
      this.size = v2tag.getSize();
      this.identifier = v2tag.getFirst(ID3v24Frames.FRAME_ID_USER_DEFINED_INFO);
      this.bitRate = (int)header.getBitRateAsNumber();
      this.MPEG_Version = header.getMpegVersion();
      this.MPEG_Layer = header.getMpegLayer();
    } catch (Exception e) {
    }
  }

  @Override
  public String getSongName() {
    return this.songName;
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
  public String getAlbumArtist() {
    return this.albumArtist;
  }

  @Override
  public String getComposer() {
    return this.composer;
  }

  @Override
  public String getGenre() {
    return this.genre;
  }

  @Override
  public String getYear() {
    return this.year;
  }

  @Override
  public int getLength() {
    return this.length;
  }

  @Override
  public String getGrouping() {
    return this.grouping;
  }

  @Override
  public String getDiscOrder() {
    return this.discOrder;
  }

  @Override
  public String getDiscTotal() {
    return this.discTotal;
  }

  @Override
  public String getTrackOrder() {
    return this.trackOrder;
  }

  @Override
  public String getTrackTotal() {
    return this.trackTotal;
  }

  @Override
  public String getComments() {
    return this.comments;
  }

  @Override
  public String getBPM() {
    return this.BPM;
  }

  @Override
  public String getArtistSort() {
    return this.artistSort;
  }

  @Override
  public String getAlbumSort() {
    return this.albumSort;
  }

  @Override
  public String getAlbumArtistSort() {
    return this.albumArtistSort;
  }

  @Override
  public String getComposerSort() {
    return this.composerSort;
  }

  @Override
  public String getLyric() {
    return this.lyric;
  }

  @Override
  public String getLyricist() {
    return this.lyricist;
  }

  @Override
  public int getSampleRate() {
    return this.sampleRate;
  }

  @Override
  public int getBitRate() {
    return this.bitRate;
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
  public int getSize() {
    return this.size;
  }

  @Override
  public String getMpegVersion() {
    return this.MPEG_Version;
  }

  @Override
  public String getMpegLayer() {
    return this.MPEG_Layer;
  }

  @Override
  public String getIdentifier() {
    return identifier;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append("\nsong name: " + songName);
    s.append("\n");
    s.append("\nartist: " + artist);
    s.append("\nalbum: " + album);
    s.append("\nalbum artist: " + albumArtist);
    s.append("\ncomposer: " + composer);
    s.append("\ngrouping: " + grouping);
    s.append("\ngenre: " + genre);
    s.append("\nyear: " + year);
    s.append("\ntrack: " + trackOrder + " of " + trackTotal);
    s.append("\ndisc: " + discOrder + " of " + discTotal);
    s.append("\nBPM: " + BPM);
    s.append("\ncomments: " + comments);
    return s.toString();
  }

}
