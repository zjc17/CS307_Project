
import java.time.LocalDateTime;


public class Song {

  int id;
  String name;
  String artist;
  String album;
  String length;
  String genre;
  long lengthInSeconds;
  int trackNumber;
  int discNumber;
  int playCount;
  LocalDateTime playDate;
  String location;
  boolean playing;
  boolean selected;

  public int getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getArtist() {
    return this.artist;
  }

  public Song(int id, String Name, String Artist) {
    this.id = id;
    this.name = Name;
    this.artist = Artist;
  }

  public void printList(int offset) {

  }

}