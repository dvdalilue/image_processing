import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileReader;

public class exercise_12a_fznumber {
    public static void writeOutMessage(String filename, int fz) {
        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            writer.println(fz);
            writer.close();
        } catch (Exception e) {
            System.out.println("Something bad happen with the file writer");
            System.exit(1);
        }
    }

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

        int conectivity = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            int i = 0;
            String line;

            if ((line = br.readLine()) != null) {
                conectivity = Integer.parseInt(line);
            }
        } catch (Exception e) {
            System.out.println("**** Error: Something went wrong reading the file ****");
            System.exit(1);
        }

        GrayU8 img_gray8_ori = ConvertBufferedImage.convertFromSingle(image, null, GrayU8.class);

        int flatzones = 0;

        if (conectivity == 8) {
            flatzones = ImageProcessing.flatzoneNumber(img_gray8_ori, new EightConnectivity());
        } else if (conectivity == 4) {      
            flatzones = ImageProcessing.flatzoneNumber(img_gray8_ori, new FourConnectivity());
        }

        String message_output_file = args[2];
        writeOutMessage(message_output_file, flatzones);
    }
}