import org.junit.Test;

public class WriteXLSTest {

    final Object[][] bookData = {
            {"00:00", 205, 300, 79, 409, 0, 1, 5, 6},
            {"00:00", 205, 300, 79, 409, 0, 1, 5, 6},
            {"00:00", 205, 300, 79, 409, 0, 1, 5, 6},
            {"00:00", 205, 300, 79, 409, 0, 1, 5, 6},
            {"00:00", 205, 300, 79, 409, 0, 1, 5, 6},
            {"00:00", 205, 300, 79, 409, 0, 1, 5, 6},

            {"00:00", 205, 300, 79, 409, 0, 1, 5, 6},
            {"00:00", 205, 300, 79, 409, 0, 1, 5, 6},
            {"00:00", 205, 300, 79, 409, 0, 1, 5, 6},
            {"00:00", 205, 300, 79, 409, 0, 1, 5, 6},
            {"00:00", 205, 300, 79, 409, 0, 1, 5, 6},

            {"00:00", 205, 300, 79, 409, 0, 1, 5, 6},
            {"00:00", 205, 300, 79, 409, 0, 1, 5, 6},
            {"00:00", 205, 300, 79, 409, 0, 1, 5, 6},
            {"00:00", 205, 300, 79, 409, 0, 1, 5, 6},
            {"00:00", 205, 300, 79, 409, 0, 1, 5, 6},
    };

    @Test
    public void testWriteXLSEmotiv () {
        WriteXLS_Emotiv wEmotiv = new WriteXLS_Emotiv();
        wEmotiv.writeXLS("leituraW", bookData);
    }

    @Test
    public void testWriteXLSNeurosky () {
        WriteXLS_NeuroSky wNeuroSky = new WriteXLS_NeuroSky();
        wNeuroSky.writeXLS("leituraWW", bookData);
    }
}
