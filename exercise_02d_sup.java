import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;

import java.awt.image.BufferedImage;

import java.util.Arrays;

public class exercise_02d_sup {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("**** Error: Missing or many arguments ****");
            System.exit(1);
        }

        BufferedImage image1 = UtilImageIO.loadImage(args[0]);
        BufferedImage image2 = UtilImageIO.loadImage(args[1]);

        if (image1 == null || image2 == null) { 
            System.out.println("**** Error: Image not found or bad format ****");
            System.exit(1);
        }

        String file_output = args[2];

        GrayU8 img_gray8_1 = ConvertBufferedImage.convertFromSingle(image1, null, GrayU8.class);
        GrayU8 img_gray8_2 = ConvertBufferedImage.convertFromSingle(image2, null, GrayU8.class);

        int width_1  = img_gray8_1.width;
        int height_1 = img_gray8_1.height;
        int width_2  = img_gray8_2.width;
        int height_2 = img_gray8_2.height;

        if (width_1 != width_2 || height_1 != height_2) {
            System.out.println("**** Error: Images must be of the same dimension ****");
            System.exit(1);
        }

        GrayU8 img_gray8_aux = ImageProcessing.supremum(img_gray8_1, img_gray8_2);

        UtilImageIO.saveImage(img_gray8_aux, file_output);
        System.out.println("Output image file: " + file_output);
    }
}