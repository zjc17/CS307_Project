public class Main {
  private static long startTime, endTime;

  public static void main(String[] args) {

//    String filePath = "C:\\Users\\acezj\\Desktop\\1.mp3";
//    File_Parser parser = new File_Parser(filePath);
//    System.out.println(parser);
//
//    String songName = parser.getSongName();
//    String artist = parser.getArtist();
//    String album = parser.getAlbum();
//    String year = parser.getYear();
//    System.out.println(year==null);
//    String
    Database_Connector connector = new Database_Connector();
    startTime = System.currentTimeMillis();
    connector.getConnection();
    endTime = System.currentTimeMillis();
    System.out.println("Time for connection: " + ( endTime - startTime ) + " ms");
    System.out.println("connect");


    startTime = System.currentTimeMillis();
    connector.test();
    endTime = System.currentTimeMillis();
    System.out.println("Time for test: " + ( endTime - startTime ) + " ms");


    startTime = System.currentTimeMillis();
    connector.close();
    endTime = System.currentTimeMillis();
    System.out.println("Time for close: " + ( endTime - startTime ) + " ms");


    System.out.println("close");
  }

}
