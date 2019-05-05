import java.io.File;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;

public abstract class Song_Parser {



  public Song_Parser(String filePath) {
    try {
      File file = new File(filePath);
      MP3File mp3File = (MP3File) AudioFileIO.read(file);
      MP3AudioHeader audioHeader = (MP3AudioHeader) mp3File.getAudioHeader();

      System.out.println("getTrackLength: " + audioHeader.getTrackLength());
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
//      this.song_name = filePath;
    } catch (Exception e) {

    }
  }

  public abstract String getArtist();

  public abstract String getSongName();

  public abstract String getAlbum();

  public abstract String getGenre();

  public abstract int getLength();

  public abstract int getSize();

  public abstract String getBPM();

  public abstract String getTrackOrder();

  public abstract int getSampleRate();

  public abstract String getChannels();

  public abstract boolean isVariableBitRate();
}
