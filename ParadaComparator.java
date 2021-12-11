import java.util.Comparator;

public class ParadaComparator implements Comparator<Parada> {

    @Override
    public int compare(Parada p1, Parada p2) {
        return (int)(p2.f()-p1.f());
    }
    
}
