import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import boofcv.gui.binary.VisualizeBinaryData;
import java.awt.image.BufferedImage;

public class coffeeGrains {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("**** Error: Missing or many arguments ****");
            System.exit(1);
        }

        BufferedImage image = UtilImageIO.loadImage(args[0]);

        if (image == null) { 
            System.out.println("**** Error: Image not found or bad format ****");
            System.exit(1);
        }

        GrayU8 original = ConvertBufferedImage.convertFromSingle(image, null, GrayU8.class);

        int size = Segmentation.countGrains(original);

        System.out.println("Coffee grains: " + size);
    }
}