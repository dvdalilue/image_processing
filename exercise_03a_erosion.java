import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import java.awt.image.BufferedImage;

public class exercise_03a_erosion {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("**** Error: Missing or many arguments ****");
            System.exit(1);
        }

        BufferedImage image = UtilImageIO.loadImage(args[1]);

        if (image == null) { 
            System.out.println("**** Error: Image not found or bad format ****");
            System.exit(1);
        }

        int size = Integer.parseInt(args[0]);

        if (size < 0) {
            System.out.println("**** Error: Size must be greater than 0 ****");
            System.exit(1);
        }

        String file_output = args[2];

        GrayU8 img_gray8_ori = ConvertBufferedImage.convertFromSingle(image, null, GrayU8.class);

        GrayU8 img_gray8_aux = ImageProcessing.erosion(img_gray8_ori, size);

        UtilImageIO.saveImage(img_gray8_aux, file_output);
        System.out.println("Output image file: " + file_output);
    }
}