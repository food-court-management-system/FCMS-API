package xiaolin.uploader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface Uploader {
    String upload(File file);

    String upload(BufferedImage bufferedImage, String path) throws IOException;
}
