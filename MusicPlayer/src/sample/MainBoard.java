package sample;

import java.lang.ModuleLayer.Controller;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class MainBoard extends Application {
    ListView<String> songList = new ListView<>();


    @Override
    public void start(Stage primaryStage) throws Exception{

        // 定义搜索框旁的下拉框
        final ChoiceBox<String> searchType = new ChoiceBox<String>(
            FXCollections.observableArrayList("All", "歌曲", "专辑", "歌手" ));

        searchType.getSelectionModel().selectedIndexProperty()
            .addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue ov, Number value, Number new_value) {
                        label.setText(greetings[new_value.intValue()]);
                }
            });

        searchType.setTooltip(new Tooltip("Select the language"));
        searchType.setValue("English");


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainBoard.fxml"));
        Parent root = fxmlLoader.load();

        // 获取 Controller 的实例对象
        MainBoardCtrl controller = fxmlLoader.getController();

        ListView songList = controller.getSongList();

        primaryStage.setTitle("Music Player");
        primaryStage.setScene(new Scene(root, 1100, 725));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
