import boofcv.struct.image.GrayU8;
import boofcv.struct.image.GrayU16;
import boofcv.struct.ConnectRule;
import boofcv.core.image.ConvertImage;
import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.misc.PixelMath;
import java.awt.image.BufferedImage;
import java.util.*;

public class Segmentation {
    public static GrayU8 closing(GrayU8 image, int size) {
        return BinaryImageOps.erode8(BinaryImageOps.dilate8(image,size,null),size,null);
    }

    public static GrayU8 opening(GrayU8 image, int size) {
        return BinaryImageOps.dilate8(BinaryImageOps.erode8(image,size,null),size,null);
    }

    public static GrayU8 gradient(GrayU8 original) {
        int width = original.width;
        int height = original.height;

        GrayU8 thresholded = new GrayU8(width, height);
        GrayU8 eroded = new GrayU8(width, height);
        GrayU16 subtracted = new GrayU16(width, height);
        GrayU8 output = new GrayU8(width, height);
        
        ThresholdImageOps.threshold(original,thresholded,60,false);

        BinaryImageOps.erode8(thresholded,1,eroded);

        PixelMath.subtract(thresholded, eroded, subtracted);

        ConvertImage.convert(subtracted,output);

        return output;
    }

    public static boolean isInside(int lhs, int rhs) {
        return (Math.abs(lhs - rhs) < 20);
    }

    public static GrayU8 grassfire(GrayU8 original, Connectivity connected, int size) {
        int width = original.width;
        int height = original.height;

        GrayU8 aux = new GrayU8(width, height);

        // original = ThresholdImageOps.threshold(original,null,127,false);

        PriorityQueue<Tuple<Integer,Integer>> queue = new PriorityQueue<Tuple<Integer,Integer>>();
        PriorityQueue<Tuple<Integer,Integer>> in_flatzone = new PriorityQueue<Tuple<Integer,Integer>>();
        
        int visited[][] = new int[width][height];

        Tuple<Integer,Integer> current;

        int region = 0;
        int current_color = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                region += 10;

                if (visited[x][y] == 1) { continue; }

                queue.clear();
                in_flatzone.clear();

                queue.add(new Tuple<Integer,Integer>(x,y));
                current_color = original.get(x,y);

                while (!queue.isEmpty()) {
                    current = (Tuple<Integer,Integer>) queue.poll();
                    in_flatzone.add(current);

                    visited[current.left][current.right] = 1;

                    for (Tuple<Integer,Integer> point : connected.allNeighborPoints(original, current.left, current.right, size)) {
                        if (isInside(original.get(point.left,point.right), current_color)
                            && visited[point.left][point.right] == 0)
                            { queue.add(point); }
                    }
                }

                for (Tuple<Integer,Integer> point : in_flatzone) {
                    aux.set(point.left, point.right, region % 256);
                }
            }
        }
        return aux;
    }

    public static int countGrains(GrayU8 original) {
        int width = original.width;
        int height = original.height;

        GrayU8 thresholded = new GrayU8(width, height);
        GrayU8 aux = new GrayU8(width, height);
        
        ThresholdImageOps.threshold(original,thresholded,145,false);

        aux = closing(opening(thresholded,1),1);
        aux = BinaryImageOps.dilate4(aux,5,null);
        aux = opening(aux,1);
        aux = BinaryImageOps.dilate4(aux,6,null);
        aux = BinaryImageOps.invert(aux,null);

        List<Contour> contours = BinaryImageOps.contour(aux, ConnectRule.EIGHT, null); 

        return contours.size();
    }

    public static int countTeeths(GrayU8 original) {
        int width = original.width;
        int height = original.height;

        GrayU8 thresholded = new GrayU8(width, height);
        GrayU8 aux = new GrayU8(width, height);
        GrayU16 subtracted = new GrayU16(width, height);
        
        ThresholdImageOps.threshold(original,thresholded,100,false);

        aux = thresholded;

        aux = closing(aux,5);
        aux = BinaryImageOps.erode4(aux,1,null);
        aux = BinaryImageOps.dilate4(aux,2,null);

        PixelMath.subtract(aux, thresholded, subtracted);

        ConvertImage.convert(subtracted,aux);

        aux = BinaryImageOps.erode4(aux,1,null);

        aux = opening(aux,1);

        List<Contour> contours = BinaryImageOps.contour(aux, ConnectRule.EIGHT, null); 

        return contours.size();
    }
}