import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import boofcv.gui.binary.VisualizeBinaryData;
import java.awt.image.BufferedImage;

public class grassfire {
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

        String file_output = args[0].substring(0, args[0].lastIndexOf('.')) + "_grassfire" + args[0].substring(args[0].lastIndexOf('.'), args[0].length());

        BufferedImage visualBinary = VisualizeBinaryData.renderBinary(Segmentation.grassfire(original, new EightConnectivity(), 1), false, null);

        UtilImageIO.saveImage(visualBinary, file_output);
        System.out.println("Output image file: " + file_output);
    }
}