import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Main {

    public static void imprimir(String[][] mapa) {
        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[0].length; j++) {
                System.out.print(mapa[i][j] + "     ");
            }
            System.out.println();
        }
    }

    public static String[][] generarMapa(int filas, int columnas, Touple entrada, Touple salida, HashMap<String, Rango> p) throws Exception{
        String[][] mapa = new String[filas][columnas];

        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[0].length; j++) {
                
                if (!(i == entrada.fila && j == entrada.columna) || (i == salida.fila && j == salida.columna)) {
                    int random = 0 + (int)(Math.random() * 10);

                    if (random >= p.get(".").min && random <= p.get(".").max) {mapa[i][j] = ".";}
                    else if (random >=  p.get("#").min && random <= p.get("#").max) {mapa[i][j] = "#";} 
                    //TODO PORTAL else if (random >=  p.get("p").min && random <= p.get("p").max) {mapa[i][j] = "p";}

                }
                mapa[entrada.fila][entrada.columna] = "E";
                mapa[salida.fila][salida.columna] = "S";
            }
        }
        return mapa;
    }

    public static ArrayList<Touple> obtenerMovimientosValidos(String[][] mapa, Touple actual) {
        ArrayList<Touple> movimientosValidos = new ArrayList<>();

        //Fila
        if (actual.fila < mapa.length-1 && 
            !mapa[actual.fila+1][actual.columna].equals("#") && 
            !mapa[actual.fila+1][actual.columna].equals("E") ) {
            movimientosValidos.add(new Touple(actual.fila+1, actual.columna));} //Abajo

        if (actual.fila > 0 && 
            !mapa[actual.fila-1][actual.columna].equals("#") &&
            !mapa[actual.fila-1][actual.columna].equals("E")) {
            movimientosValidos.add(new Touple(actual.fila-1, actual.columna));} //Arriba

        //Columna
         if (actual.columna < mapa[0].length-1 &&
            !mapa[actual.fila][actual.columna+1].equals("#") &&
            !mapa[actual.fila][actual.columna+1].equals("E")) {
                movimientosValidos.add(new Touple(actual.fila, actual.columna+1));} //Derecha
         
         if (actual.columna > 0 &&
            !mapa[actual.fila][actual.columna-1].equals("#") &&
            !mapa[actual.fila][actual.columna-1].equals("E")) {
                movimientosValidos.add(new Touple(actual.fila, actual.columna-1));} //Izquierda

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
        /* Esta función abre un archivo de texto y lo convierte en una matriz de caracteres Costo: O(n*m) */
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
                    //System.out.println(ganador.movimientos+1 + " movimientos");
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
        /* 
        Touple entrada = new Touple(0, 0);
        Touple salida = new Touple(2, 2);
        HashMap<String, Rango> p = new HashMap<>();
        p.put(".", new Rango(0, 6));
        p.put("#", new Rango(7, 9));
        //p.put("p", new Rango(9, 9));
        */

        String[][] mapa = {
            {"E", ".", ".", "#"},
            {".", "#", ".", "."}, 
            {"#", "S", ".", "."}
        }; 
        

        mapa = abrirLaberinto("data/laberinto.txt");
        int movimientos = 0;

        ArrayList<Touple> visitados = new ArrayList<>();
        Ganador g = new Ganador(new ArrayList<Touple>());
        laberintoMagico(mapa, new Touple(0, 0), movimientos, visitados, g);

        if (g.movimientos != Integer.MAX_VALUE) {
            System.out.println(g.movimientos);
        } else {
            System.out.println(-1);
        }

    }

}