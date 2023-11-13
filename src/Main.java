import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static ArrayList<Touple> posPortal(String[][] mapa, String portal, Touple inicial) {
        ArrayList<Touple> posPortal = new ArrayList<>();

        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[0].length; j++) {
                Touple pos = new Touple(i, j);
                if (!(inicial.fila == i && inicial.columna == j)) {
                    if (mapa[i][j].equals(portal)) {
                        posPortal.add(pos);
                    }
                }
            }
        }      

        return posPortal;
    }

    public static ArrayList<Touple> obtenerMovimientosValidos(String[][] mapa, Touple actual) {
        ArrayList<Touple> movimientosValidos = new ArrayList<>();

        //Portal
        String letra = mapa[actual.fila][actual.columna];
        if (!letra.equals("E") && letra.chars().allMatch(Character::isLetter)) {
            ArrayList<Touple> posPortal = posPortal(mapa, letra, actual);
            for (Touple t : posPortal) {
                movimientosValidos.add(t);
            }
        } 

        //Fila
        if (actual.fila < mapa.length-1 && 
            !mapa[actual.fila+1][actual.columna].equals("#") && 
            !mapa[actual.fila+1][actual.columna].equals("E") ) {
                movimientosValidos.add(new Touple(actual.fila+1, actual.columna));
        } //Abajo

        if (actual.fila > 0 && 
            !mapa[actual.fila-1][actual.columna].equals("#") &&
            !mapa[actual.fila-1][actual.columna].equals("E")) {
                movimientosValidos.add(new Touple(actual.fila-1, actual.columna));
        } //Arriba

        //Columna
        if (actual.columna < mapa[0].length-1 &&
            !mapa[actual.fila][actual.columna+1].equals("#") &&
            !mapa[actual.fila][actual.columna+1].equals("E")) {
                movimientosValidos.add(new Touple(actual.fila, actual.columna+1));
        } //Derecha
            
        if (actual.columna > 0 &&
            !mapa[actual.fila][actual.columna-1].equals("#") &&
            !mapa[actual.fila][actual.columna-1].equals("E")) {
                movimientosValidos.add(new Touple(actual.fila, actual.columna-1));
        } //Izquierda


        return movimientosValidos;

    }
    
    public static boolean pertenece(Touple tupla, ArrayList<Touple> visitados) {
        for (Touple t : visitados) {
            if (tupla.fila == t.fila && tupla.columna == t.columna) {
                return true;
            }
        }
    return false;
    }

    public static String[][] abrirLaberinto(String fuente) {
        List<List<String>> laberinto = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fuente))) {
            String linea = br.readLine();
            while (linea != null) {
                List<String> fila = new ArrayList<>();
                for (String c : linea.split("")) {
                    fila.add(c);
                }
                laberinto.add(fila);
                linea = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[][] laberintoArray = new String[laberinto.size()][];
        for (int i = 0; i < laberinto.size(); i++) {
            List<String> fila = laberinto.get(i);
            laberintoArray[i] = fila.toArray(new String[fila.size()]);
        }
        return laberintoArray;
    }


    public static void laberintoMagico(String[][] mapa, Touple actual, int movimientos, ArrayList<Touple> visitados, Ganador ganador) 
    {

        if (mapa.length != 0) 
        {
            if (mapa[actual.fila][actual.columna].equals("S")) 
            {
                if (movimientos < ganador.movimientos) 
                {
                    ganador.movimientos = movimientos;
                    ganador.visitados = visitados;
                    //System.out.println(ganador.movimientos+1 + " movimientos")
                }
                return;
            }

            ArrayList<Touple> movimientosValidos = obtenerMovimientosValidos(mapa, actual);
            while (movimientosValidos.size() != 0) {
                Touple sig = movimientosValidos.get(0);

                if (!pertenece(sig, visitados)) {
                    visitados.add(sig);
                    laberintoMagico(mapa, sig, movimientos+1, visitados, ganador);
                    visitados.remove(sig);
                } 
                movimientosValidos.remove(0);
                
            }
        } 
        return;
    }

    public static void main(String[] args) {
        int movimientos = 0;
        ArrayList<Touple> visitados = new ArrayList<>();
        Ganador g = new Ganador(new ArrayList<Touple>());
        String[][] mapa = abrirLaberinto("laberintos/portales.txt");
        laberintoMagico(mapa, new Touple(0, 0), movimientos, visitados, g);

        if (g.movimientos != Integer.MAX_VALUE) {
            System.out.println(g.movimientos);
        } else {
            System.out.println(-1);
        }

    }

}