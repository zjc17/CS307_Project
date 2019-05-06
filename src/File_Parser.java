

public class File_Parser extends Song_Parser{

  Song_Parser song_parser;

  public static void main(String[] args) {
    String path = "C:\\Users\\acezj\\Desktop\\test3.mp3";
    path = "F:\\Music\\王力宏 _ 张靓颖 - 另一个天堂.mp3";
    path = "F:\\Music\\Justin Bieber _ Ludacris - Baby (Album Version).mp3";
    path = "C:\\Users\\acezj\\Desktop\\1.mp3";
//    path = "sss";
    File_Parser file_parser = new File_Parser(path);
    System.out.println(file_parser);
    System.out.println(file_parser.getSongName());

  }

  public File_Parser(String filePath) {
    if (filePath.endsWith(".mp3")) {
      song_parser = new Parser_MP3(filePath);
    } else if (filePath.endsWith(".flac")) {
      throw new IllegalArgumentException("Unsupported file type: .FLAC will be supported in the future version.");
    } else {
      throw new IllegalArgumentException("Unsupported file type");
    }

  }

  @Override
  public String getSongName() {
    return song_parser.getSongName();
  }

  @Override
  public String getArtist() {
    return song_parser.getArtist();
  }

  @Override
  public String getAlbum() {
    return song_parser.getAlbum();
  }

  @Override
  public String getAlbumArtist() {
    return song_parser.getAlbumArtist();
  }

  @Override
  public String getComposer() {
    return song_parser.getComposer();
  }

  @Override
  public String getGenre() {
    return song_parser.getGenre();
  }

  @Override
  public String getYear() {
    return song_parser.getYear();
  }

  @Override
  public int getLength() {
    return song_parser.getLength();
  }

  @Override
  public String getGrouping() {
    return song_parser.getGrouping();
  }

  @Override
  public String getDiscOrder() {
    return song_parser.getDiscOrder();
  }

  @Override
  public String getDiscTotal() {
    return song_parser.getDiscTotal();
  }

  @Override
  public String getTrackOrder() {
    return song_parser.getTrackOrder();
  }

  @Override
  public String getTrackTotal() {
    return song_parser.getTrackTotal();
  }

  @Override
  public String getComments() {
    return song_parser.getComments();
  }

  @Override
  public String getBPM() {
    return song_parser.getBPM();
  }

  @Override
  public String getArtistSort() {
    return song_parser.getArtistSort();
  }

  @Override
  public String getAlbumSort() {
    return song_parser.getAlbumSort();
  }

  @Override
  public String getAlbumArtistSort() {
    return song_parser.getAlbumArtistSort();
  }

  @Override
  public String getComposerSort() {
    return song_parser.getComposerSort();
  }

  @Override
  public String getLyric() {
    return song_parser.getLyric();
  }

  @Override
  public String getLyricist() {
    return song_parser.getLyricist();
  }

  @Override
  public int getSampleRate() {
    return song_parser.getSampleRate();
  }

  @Override
  public String getChannels() {
    return song_parser.getChannels();
  }

  @Override
  public boolean isVariableBitRate() {
    return song_parser.isVariableBitRate();
  }

  @Override
  public int getSize() {
    return song_parser.getSize();
  }

  @Override
  public String getIdentifier() {
    return song_parser.getIdentifier();
  }

  @Override
  public String toString() {
    return song_parser.toString();
  }
}
