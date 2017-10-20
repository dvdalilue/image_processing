import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.ImageAccessException;

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

    public static int erosion_point(GrayU8 image, int i, int j, int size) {
        int min = image.get(i,j);

        for (int neighbor = 1; neighbor <= size; neighbor++) {
            for (int n = i - neighbor; n <= i + neighbor; n++) {
                try {
                    min = Math.min(min,image.get(n,j + neighbor));
                } catch (ImageAccessException e) { continue; }
            }

            for (int n = i - neighbor; n <= i + neighbor; n++) {
                try {
                    min = Math.min(min,image.get(n,j - neighbor));
                } catch (ImageAccessException e) { continue; }
            }

            for (int n = j - neighbor + 1; n < j + neighbor; n++) {
                try {
                    min = Math.min(min,image.get(i - neighbor,n));
                } catch (ImageAccessException e) { continue; }
            }

            for (int n = j - neighbor + 1; n < j + neighbor; n++) {
                try {
                    min = Math.min(min,image.get(i + neighbor,n));
                } catch (ImageAccessException e) { continue; }
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
                    aux.set(i,j,erosion_point(original,i,j,1));
                }
            }
            original = aux.clone();            
        }
        return aux;
    }

    public static int dilation_point(GrayU8 image, int i, int j, int size) {
        int max = image.get(i,j);

        for (int neighbor = 1; neighbor <= size; neighbor++) {
            for (int n = i - neighbor; n <= i + neighbor; n++) {
                try {
                    max = Math.max(max,image.get(n,j + neighbor));
                } catch (ImageAccessException e) { continue; }
            }

            for (int n = i - neighbor; n <= i + neighbor; n++) {
                try {
                    max = Math.max(max,image.get(n,j - neighbor));
                } catch (ImageAccessException e) { continue; }
            }

            for (int n = j - neighbor + 1; n < j + neighbor; n++) {
                try {
                    max = Math.max(max,image.get(i - neighbor,n));
                } catch (ImageAccessException e) { continue; }
            }

            for (int n = j - neighbor + 1; n < j + neighbor; n++) {
                try {
                    max = Math.max(max,image.get(i + neighbor,n));
                } catch (ImageAccessException e) { continue; }
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
                    aux.set(i,j,dilation_point(original,i,j,1));
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
}