import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.ImageIcon;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;

public class func {

    public static void main(String args[]) {
        copyFile("D:/abc.mp3", "E:/123.mp3");
        getPic("D:/abc.mp3");
    }

    public static void copyFile(String oldPath, String newPath) {
        try {
            int byteRead;
            File oldFile = new File(oldPath);
            if (oldFile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int byteSum = 0;
                while ((byteRead = inStream.read(buffer)) != -1) {
                    byteSum = byteSum + byteRead;
                    fs.write(buffer, 0, byteRead);
                }
                inStream.close();
                System.out.println("复制成功！");
            }
            else
                System.out.println("文件不存在！");
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错！");
            e.printStackTrace();
        }
    }

    public static Image getPic(String path){
        Image img;
        try {
            String url = path;
            File sourceFile = new File(url);
            MP3File mp3file = new MP3File(sourceFile);

            AbstractID3v2Tag tag = mp3file.getID3v2Tag();
            AbstractID3v2Frame frame = (AbstractID3v2Frame) tag.getFrame("APIC");
            FrameBodyAPIC body = (FrameBodyAPIC) frame.getBody();
            byte[] imageData = body.getImageData();
            img = Toolkit.getDefaultToolkit().createImage(imageData, 0,imageData.length);
            ImageIcon icon = new ImageIcon(img);
            String storePath=path;
            storePath = storePath.substring(0, storePath.length()-3);
            storePath+="jpg";
            FileOutputStream fos = new FileOutputStream(storePath);
            fos.write(imageData);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读取Mp3图片出错");
        }
        return img;
    }

}
