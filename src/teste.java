import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by cenas on 14/04/2016.
 */

public class teste {
    public static void main(String[] args) {

        EmotivDevice emo = new EmotivDevice();
        emo.connectEmotiv();
        emo.run();
        emo.emotivDeviceDisconnect();

    }
}
