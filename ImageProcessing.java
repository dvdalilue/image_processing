import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.ImageAccessException;
import java.util.*;

public class ImageProcessing {
    public static GrayU8 thershold(GrayU8 image, int value) {
        int width = image.width;
        int height = image.height;

        GrayU8 aux = new GrayU8(width, height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (image.get(i,j) < value) {
                    aux.set(i,j,0);
                } else {
                    aux.set(i,j,255);
                }
            }
        }
        return aux;
    }

    public static boolean compare(GrayU8 img1, GrayU8 img2) {
        int width_1 = img1.width;
        int height_1 = img1.height;
        int width_2 = img2.width;
        int height_2 = img2.height;

        if (width_1 != width_2 || height_1 != height_2) {
            return false;
        }

        for (int i = 0; i < width_1; i++) {
            for (int j = 0; j < height_1; j++) {
                if (img1.get(i,j) != img2.get(i,j)) { return false; }
            }
        }

        return true;
    }

    public static GrayU8 supremum(GrayU8 img1, GrayU8 img2) {
        // Assuming equal width and height
        int width  = img1.width;
        int height = img1.height;
        
        GrayU8 aux = new GrayU8(width, height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                aux.set(i,j,Math.max(img1.get(i,j),img2.get(i,j)));
            }
        }
        return aux;
    }

    public static GrayU8 infimum(GrayU8 img1, GrayU8 img2) {
        // Assuming equal width and height
        int width  = img1.width;
        int height = img1.height;
        
        GrayU8 aux = new GrayU8(width, height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                aux.set(i,j,Math.min(img1.get(i,j),img2.get(i,j)));
            }
        }
        return aux;
    }

    public static int erosionPoint(GrayU8 image, int i, int j, int size) {
        int min = image.get(i,j);

        for (int neighbor = 1; neighbor <= size; neighbor++) {
            for (int n = i - neighbor; n <= i + neighbor; n++) {
                if (image.isInBounds(n,j + neighbor))
                    { min = Math.min(min,image.get(n,j + neighbor)); }
            }

            for (int n = i - neighbor; n <= i + neighbor; n++) {
                if (image.isInBounds(n,j - neighbor))
                    { min = Math.min(min,image.get(n,j - neighbor)); }
            }

            for (int n = j - neighbor + 1; n < j + neighbor; n++) {
                if (image.isInBounds(i - neighbor,n))
                    { min = Math.min(min,image.get(i - neighbor,n)); }
            }

            for (int n = j - neighbor + 1; n < j + neighbor; n++) {
                if (image.isInBounds(i + neighbor,n))
                    { min = Math.min(min,image.get(i + neighbor,n)); }
            }
        }
        return min;
    }

    public static GrayU8 erosion(GrayU8 original, int size) {
        int width  = original.width;
        int height = original.height;

        GrayU8 aux = new GrayU8(width, height);

        for (int times = 0; times < size; times++) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    aux.set(i,j,erosionPoint(original,i,j,1));
                }
            }
            original = aux.clone();            
        }
        return aux;
    }

    public static int dilationPoint(GrayU8 image, int i, int j, int size) {
        int max = image.get(i,j);

        for (int neighbor = 1; neighbor <= size; neighbor++) {
            for (int n = i - neighbor; n <= i + neighbor; n++) {
                if (image.isInBounds(n,j + neighbor))
                    { max = Math.max(max,image.get(n,j + neighbor)); }
            }

            for (int n = i - neighbor; n <= i + neighbor; n++) {
                if (image.isInBounds(n,j - neighbor))
                    { max = Math.max(max,image.get(n,j - neighbor)); }
            }

            for (int n = j - neighbor + 1; n < j + neighbor; n++) {
                if (image.isInBounds(i - neighbor,n))
                    { max = Math.max(max,image.get(i - neighbor,n)); }
            }

            for (int n = j - neighbor + 1; n < j + neighbor; n++) {
                if (image.isInBounds(i + neighbor,n))
                    { max = Math.max(max,image.get(i + neighbor,n)); }
            }
        }
        return max;
    }

    public static GrayU8 dilation(GrayU8 original, int size) {
        int width  = original.width;
        int height = original.height;

        GrayU8 aux = new GrayU8(width, height);

        for (int times = 0; times < size; times++) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    aux.set(i,j,dilationPoint(original,i,j,1));
                }
            }
            original = aux.clone();            
        }
        return aux;
    }

    public static GrayU8 opening(GrayU8 original, int size) {
        int width = original.width;
        int height = original.height;

        GrayU8 aux = dilation(erosion(original, size), size);

        return aux;
    }

    public static GrayU8 closing(GrayU8 original, int size) {
        int width = original.width;
        int height = original.height;

        GrayU8 aux = erosion(dilation(original, size), size);

        return aux;
    }

    public static GrayU8 flatzone(GrayU8 original, int x, int y, int label, Connectivity connected) {
        int width = original.width;
        int height = original.height;

        GrayU8 aux = new GrayU8(width, height);

        PriorityQueue<Tuple<Integer,Integer>> queue = new PriorityQueue<Tuple<Integer,Integer>>();
        PriorityQueue<Tuple<Integer,Integer>> visited = new PriorityQueue<Tuple<Integer,Integer>>();

        Tuple<Integer,Integer> current = new Tuple<Integer,Integer>(x,y);

        queue.add(current);

        while (!queue.isEmpty()) {
            current = (Tuple<Integer,Integer>) queue.poll();
            
            visited.add(new Tuple<Integer,Integer>(current.left,current.right));
            aux.set(current.left, current.right, label);

            for (Tuple<Integer,Integer> point : connected.allNeighborPoints(original, current.left, current.right, 1)) {
                if (original.get(point.left,point.right) == original.get(current.left,current.right)
                    && !visited.contains(point))
                    { queue.add(point); }
            }
        }

        return aux;
    }

    public static int flatzoneNumber(GrayU8 original, Connectivity connected) {
        int width = original.width;
        int height = original.height;

        int visited[][] = new int[width][height];

        int fz = 0;

        PriorityQueue<Tuple<Integer,Integer>> queue = new PriorityQueue<Tuple<Integer,Integer>>();
        Tuple<Integer,Integer> current;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (visited[x][y] == 1) { continue; }

                fz++;

                current = new Tuple<Integer,Integer>(x,y);                
                queue.add(current);

                while (!queue.isEmpty()) {
                    current = (Tuple<Integer,Integer>) queue.poll();

                    visited[current.left][current.right] = 1;

                    for (Tuple<Integer,Integer> point : connected.allNeighborPoints(original, current.left, current.right, 1)) {
                        if (original.get(point.left,point.right) == original.get(current.left,current.right)
                            && visited[point.left][point.right] == 0)
                            { queue.add(point); }
                    }
                }
            }
        }

        return fz;
    }

    public static GrayU8 flatzoneRegion(GrayU8 original, Connectivity connected, Region region) {
        int width = original.width;
        int height = original.height;

        GrayU8 aux = new GrayU8(width, height);

        PriorityQueue<Tuple<Integer,Integer>> queue = new PriorityQueue<Tuple<Integer,Integer>>();
        PriorityQueue<Tuple<Integer,Integer>> in_flatzone = new PriorityQueue<Tuple<Integer,Integer>>();
        PriorityQueue<Tuple<Integer,Integer>> out_flatzone = new PriorityQueue<Tuple<Integer,Integer>>();
        
        int visited[][] = new int[width][height];

        Tuple<Integer,Integer> current;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (visited[x][y] == 1) { continue; }

                queue.clear();
                in_flatzone.clear();
                out_flatzone.clear();

                current = new Tuple<Integer,Integer>(x,y);
                queue.add(current);

                while (!queue.isEmpty()) {
                    current = (Tuple<Integer,Integer>) queue.poll();
                    in_flatzone.add(current);

                    visited[current.left][current.right] = 1;

                    for (Tuple<Integer,Integer> point : connected.allNeighborPoints(original, current.left, current.right, 1)) {
                        if (original.get(point.left,point.right) == original.get(current.left,current.right)
                            && visited[point.left][point.right] == 0)
                            { queue.add(point); }
                        else if (region.compare(original.get(point.left,point.right), original.get(current.left,current.right)))
                            { out_flatzone.add(point); }
                    }

                    if (!out_flatzone.isEmpty()) { break; }
                }

                if (out_flatzone.isEmpty()) {
                    for (Tuple<Integer,Integer> point : in_flatzone) {
                        aux.set(point.left, point.right, 255);
                    }
                } else {
                    for (Tuple<Integer,Integer> point : in_flatzone) {
                        visited[point.left][point.right] = 0;
                    }
                    // This line gives a wronge answer 'cause it's making
                    // an assumption that this point is not on any flatzone.
                    // Even when can be equal to a neighbor pixel.
                    // visited[x][y] = 1;
                }
            }
        }

        return aux;
    }
}