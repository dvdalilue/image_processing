import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import java.awt.image.BufferedImage;

public class exercise_02a_thresh {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("**** Error: Missing arguments ****");
            System.exit(1);
        }

        BufferedImage image = UtilImageIO.loadImage(args[0]);

        if (image == null) { 
            System.out.println("**** Error: Image not found or bad format ****");
            System.exit(1);
        }

        int value = Integer.parseInt(args[1]);

        if (value > 255 || value < 0) {
            System.out.println("**** Error: Thershold out of range (0-255) ****");
            System.exit(1);
        }

        String file_output = args[2];

        GrayU8 img_gray8_ori = ConvertBufferedImage.convertFromSingle(image, null, GrayU8.class);

        GrayU8 img_gray8_aux = ImageProcessing.thershold(img_gray8_ori, value);

        UtilImageIO.saveImage(img_gray8_aux, file_output);
        System.out.println("Output image file: " + file_output);
    }
}