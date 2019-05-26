import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.util.Scanner;

class Music extends Thread {

    private String p;
    public Music(String path){
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

public class test {

    public static void main(String args[]) {
        work("D:/abc.mp3");
    }

    public static void work(String path) {
        Scanner keyboard = new Scanner(System.in);
        Music music = new Music(path);
        music.start();
        System.out.println("Music is playing! Press 0 to stop it.");
        int off = -1;
        while(off == -1){
            off = keyboard.nextInt();
            if (off == 0) {
                music.stop();
                System.out.println("Music is stopped.");
            }
            else {
                System.out.println("Invalid input! Try again.");
                off = -1;
            }
        }
    }
}
