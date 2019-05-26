import java.util.ArrayList;

public class Album {
	  int id;
    int trackNum;
    String name;
    String artist;
    ArrayList<Song> track = new ArrayList<Song>();
    public Album(int id,String Name, String Artist, int num) {
    	  this.id = id;
        this.name = Name;
        this.artist = Artist;
        this.trackNum = num;
    }

}
