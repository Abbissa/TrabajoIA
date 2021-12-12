import java.util.Comparator;

public class ParadaComparator implements Comparator<Parada> {

    @Override
    public int compare(Parada p1, Parada p2) {
        return (p2.f()-p1.f())>0 ? 1 : -1;
    }
    
}