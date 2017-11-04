import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;

public class exercise_11a_flatzone {
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

        int[] inputs = {0,0,0,0};

        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            int i = 0;
            for(String line; (line = br.readLine()) != null; i++) {
                inputs[i] = Integer.parseInt(line);
            }
        } catch (Exception e) {
            System.out.println("**** Error: Something went wrong reading the file ****");
            System.exit(1);
        }

        int x = inputs[0];
        int y = inputs[1];
        int conectivity = inputs[2];
        int label = inputs[3];

        String file_output = args[2];

        GrayU8 img_gray8_ori = ConvertBufferedImage.convertFromSingle(image, null, GrayU8.class);

        GrayU8 img_gray8_aux = img_gray8_ori.clone();

        if (conectivity == 8) {
            img_gray8_aux = ImageProcessing.flatzone_8_connectivity(img_gray8_ori, x, y, label);
        } else if (conectivity == 4) {      
            img_gray8_aux = ImageProcessing.flatzone_4_connectivity(img_gray8_ori, x, y, label);
        }

        UtilImageIO.saveImage(img_gray8_aux, file_output);
        System.out.println("Output image file: " + file_output);
    }
}