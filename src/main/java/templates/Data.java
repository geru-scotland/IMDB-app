package templates;

import java.util.ArrayList;

public class Data<T extends Comparable<T>> {
    private ArrayList<T> listaGenerica;

    public Data(){
        listaGenerica = new ArrayList<>();
    }

    public void add(T elemento){
        listaGenerica.add(elemento);
    }

    public int getSize(){ return listaGenerica.size(); }

    public T get(int pos) { return listaGenerica.get(pos); }

    /**
     * Búsqueda dicotómica
     * @param elemento
     * @return
     */
    public T buscar(String elemento){ return null; }
}
