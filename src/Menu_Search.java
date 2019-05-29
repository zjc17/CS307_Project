import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu_Search {
    private DB_Writer writter;
    private DB_Reader reader;
    private int pageNum;
    private Scanner input;

    public Menu_Search(DB_Writer writter, DB_Reader reader) {
        this.writter = writter;
        this.reader = reader;
        this.input = new Scanner(System.in);
        System.out.println("Input 1 if you want to search in Chinese\nInput 2 if you want to search in English\nPlease choose: ");
        int language = input.nextInt();
        if(language==2) {
            System.out.println("Input 1 if you want to search a song\nInput 2 if you want to search an artist\nInput 3 if you want to search an album\nInput 0 if you want to exit");
            System.out.println("Please choose input number: ");
            int next = input.nextInt();
            switch (next) {
                case 1:
                    searchSong();
                    break;
                case 2:
                    searchArtist();
                    break;
                case 3:
                    searchAlbum();
                    break;
                case 0:
                    Menu_Main menuMain = new Menu_Main();
                    menuMain.Menu();
                    break;

            }
        }else if(language==1){
            System.out.println("请输入1查询歌曲\n请输入2查询歌手\n请输入3查找专辑\n请输入0退出");
            System.out.println("请选择: ");
            int next = input.nextInt();
            switch (next) {
                case 1:
                    searchSongChinese();
                    break;
                case 2:
                    searchArtistChinese();
                    break;
                case 3:
                    searchAlbumChinese();
                    break;
                case 0:
                    Menu_Main menuMain = new Menu_Main();
                    menuMain.Menu();
                    break;

            }

        }


    }
    //TO DO:
    private void searchSong() {
        System.out.println("Please input the name of song you want to search: ");
        String songName = input.next();

    }

    private void searchArtist() {
        System.out.println("Please input the name of artist you want to search: ");
        String songName = input.next();

    }

    private void searchAlbum() {
        System.out.println("Please input the name of album you want to search: ");
        String songName = input.next();

    }
    private void searchSongChinese() {
        System.out.println("请输入你要搜索的歌曲名: ");
        String songName = input.next();

    }

    private void searchArtistChinese() {
        System.out.println("请输入你要搜索的歌手名: ");
        String songName = input.next();
        System.out.println("Artist ID    Artist Name\n1            张学友\n2            张杰");
        System.out.println("请输入你想要查看的歌手：");
        int singer = input.nextInt();
        ResultSet rs = reader.getSongFromArtistId(9, 0);
        int temp = 0;
        try {
            rs.next();
            if (rs.isClosed()) {
                return;
            } else {

                do {
                    temp++;
                    int id = rs.getInt("song_id");
                    String name = rs.getString("song_name");
                    System.out.printf("%2d\t\t%20s\n", temp, name);
                    rs.next();
                } while (!rs.isClosed());
            }
        } catch (SQLException e) {
            System.err.println("Error in printAlbumInfo");
        }

        System.out.println("输入1返回主菜单\n输入0退出");
        int next = input.nextInt();
        if(next==0){
            System.exit(0);
        }
    }

    private void searchAlbumChinese() {
        System.out.println("请输入你要搜索的专辑名: ");
        String songName = input.next();

    }
}
