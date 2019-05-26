package sample;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainBoardCtrl {
  @FXML ListView songList;
  @FXML TextField searchField;



  public void initialize() {

  }




  public void setSongList(ListView songList) {
    this.songList = songList;
  }

  public ListView getSongList() {
    return songList;
  }
}
