import java.util.ArrayList;

public class Ganador {
    int movimientos;
    ArrayList<Touple> visitados;
    
    public Ganador(ArrayList<Touple> visitados) {
        this.movimientos = Integer.MAX_VALUE;
        this.visitados = visitados;

    }
}