import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;

public class exercise_02b_compare {
    public static void writeOutMessage(String filename, boolean eq) {
        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            if (eq) { writer.println("="); } else { writer.println("!="); }
            writer.close();
        } catch (Exception e) {
            System.out.println("Something bad happen with the file writer");
            System.exit(1);
        }
    }

    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("**** Error: Missing arguments ****");
            System.exit(1);
        }

        BufferedImage image1 = UtilImageIO.loadImage(args[0]);
        BufferedImage image2 = UtilImageIO.loadImage(args[1]);

        if (image1 == null || image2 == null) { 
            System.out.println("**** Error: Image not found or bad format ****");
            System.exit(1);
        }

        String message_output_file = args[2];

        GrayU8 img_gray8_1 = ConvertBufferedImage.convertFromSingle(image1, null, GrayU8.class);
        GrayU8 img_gray8_2 = ConvertBufferedImage.convertFromSingle(image2, null, GrayU8.class);

        writeOutMessage(message_output_file, ImageProcessing.compare(img_gray8_1,img_gray8_2));
    }
}