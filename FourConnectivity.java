import boofcv.struct.image.GrayU8;
import boofcv.struct.image.ImageAccessException;
import java.util.*;

public class FourConnectivity implements Connectivity {
    public ArrayList<Tuple<Integer,Integer>> allNeighborPoints(GrayU8 image, int i, int j, int size) {
        ArrayList<Tuple<Integer,Integer>> list = new ArrayList<Tuple<Integer,Integer>>(4);

        for (int n = i - size; n <= i + size; n++) {
            if (n == i) { continue; }

            if (image.isInBounds(n,j))
                { list.add(new Tuple<Integer,Integer>(n,j)); }
        }

        for (int n = j - size; n <= j + size; n++) {
            if (n == j) { continue; }

            if (image.isInBounds(i,n))
                { list.add(new Tuple<Integer,Integer>(i,n)); }
        }

        return list;
    }
}