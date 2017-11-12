import boofcv.struct.image.GrayU8;
import java.util.*;

interface Connectivity {
    ArrayList<Tuple<Integer,Integer>> allNeighborPoints(GrayU8 image, int i, int j, int size);
}