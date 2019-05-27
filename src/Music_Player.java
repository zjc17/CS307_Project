import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

class Music_Player extends Thread {

  private String p;

  public Music_Player(String path) {
    p = path;
  }

  public void run() {
    try {
      FileInputStream fileInputStream = new FileInputStream(p);
      Player player = new Player(fileInputStream);
      player.play();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (JavaLayerException e) {
      e.printStackTrace();
    }
  }

}