

public class File_Parser {

  public static void main(String[] args) {
    String path = "C:\\Users\\acezj\\Desktop\\test3.mp3";
    path = "F:\\Music\\王力宏 _ 张靓颖 - 另一个天堂.mp3";
    path = "F:\\Music\\Justin Bieber _ Ludacris - Baby (Album Version).mp3";
    path = "C:\\Users\\acezj\\Desktop\\1.mp3";
//    path = "sss";
    File_Parser file_parser = new File_Parser(path);

  }

  public File_Parser(String filePath) {
    Song_Parser song_parser;
    if (filePath.endsWith(".mp3")) {
      song_parser = new Parser_MP3(filePath);
    } else if (filePath.endsWith(".flac")) {
      throw new IllegalArgumentException("Unsupported file type: .FLAC will be supported in the future version.");
    } else {
      throw new IllegalArgumentException("Unsupported file type");
    }

  }

}
