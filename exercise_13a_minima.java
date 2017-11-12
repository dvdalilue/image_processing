import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileReader;

public class exercise_13a_minima {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("**** Error: Missing or many arguments ****");
            System.exit(1);
        }

        BufferedImage image = UtilImageIO.loadImage(args[0]);

        if (image == null) { 
            System.out.println("**** Error: Image not found or bad format ****");
            System.exit(1);
        }

        GrayU8 img_gray8_ori = ConvertBufferedImage.convertFromSingle(image, null, GrayU8.class);

        int conectivity = 8;

        GrayU8 img_gray8_aux = img_gray8_ori.clone();

        if (conectivity == 8) {
            img_gray8_aux = ImageProcessing.flatzoneRegion(img_gray8_ori, new EightConnectivity(), Region.Minima);
        } else if (conectivity == 4) {      
            img_gray8_aux = ImageProcessing.flatzoneRegion(img_gray8_ori, new FourConnectivity(), Region.Minima);
        }

        String file_output = args[1];

        UtilImageIO.saveImage(img_gray8_aux, file_output);
        System.out.println("Output image file: " + file_output);
    }
}