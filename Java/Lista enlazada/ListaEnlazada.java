package aed;

import java.util.Arrays;
import java.util.ListIterator;

public class ListaEnlazada<T> {
    private Nodo primero;
    private Nodo ultimo;
    private int longitud;

    private class Nodo {
        T valor;
        Nodo ant;
        Nodo sig;
        Nodo (T v){valor=v;}
    }

    public ListaEnlazada() {
        this.primero=null;
        this.ultimo=null;
        this.longitud=0;
    }

    public int longitud() {
        return this.longitud;
    }

    public void agregarAdelante(T elem) {
        Nodo nuevo =new Nodo(elem);
        if (primero ==null){
            primero=nuevo;
            ultimo=nuevo;
        }
        else{
            nuevo.sig=primero;
            primero.ant=nuevo;
            primero=nuevo;
        }
        this.longitud++;
    }

    public void agregarAtras(T elem) {
        Nodo nuevo =new Nodo(elem);
        if(ultimo==null){
            this.primero=nuevo;
            this.ultimo=nuevo;
        }
        else{
            nuevo.ant=this.ultimo;
            this.ultimo.sig=nuevo;
            this.ultimo=nuevo;
        }
        this.longitud++;
    }

    public T obtener(int i) {
        Nodo actual=primero;
        T res;
            while(i>0){
            actual=actual.sig;
            i--;
        }
    res=actual.valor;
    return res;
    }

    public void eliminar(int i) {
        Nodo actual=primero;
        while(i>0){
            actual=actual.sig;
            i--;
        }
        if(actual.ant!=null){
            actual.ant.sig=actual.sig;
        }
        else{
            primero=actual.sig;
        }
        if(actual.sig!=null){
            actual.sig.ant=actual.ant;
        }
        else{
            ultimo=actual.ant;
        }
        
        this.longitud--;

    }

    public void modificarPosicion(int indice, T elem) {
         Nodo actual=primero;
        while(indice>0){
            actual=actual.sig;
            indice--;
        }
        actual.valor=elem;
    }

    public ListaEnlazada(ListaEnlazada<T> lista) {
        this.primero=null;
        this.ultimo=null;
        this.longitud=0;
        ListaIterador it = lista.iterador();
        while (it.haySiguiente()){
            this.agregarAtras(it.siguiente());
        }
        }
    
    @Override
    public String toString() {
        Object[] elementos = new Object[this.longitud];
        ListaIterador it = this.iterador();
    
        for (int i = 0; i < this.longitud && it.haySiguiente(); i++) {
            elementos[i] = it.siguiente();
    }
    
        return Arrays.toString(elementos);
    }

    public class ListaIterador{
    	private Nodo siguiente;
        private Nodo anterior;

        public boolean haySiguiente() {
	        return siguiente!=null;
        }
        
        public boolean hayAnterior() {
	        return anterior!=null;
        }

        public T siguiente() {
            if(!haySiguiente()){
                return null;
            }
            T res = siguiente.valor;
            anterior = siguiente;
            siguiente = siguiente.sig;
            return res;
        }
        

        public T anterior() {
            if(!hayAnterior()){
            return null;
            }
            T res = anterior.valor;
            siguiente = anterior;
            anterior = anterior.ant;
            return res;
        }
    }

    public ListaIterador iterador() {
	    ListaIterador it= new ListaIterador();
        it.siguiente=this.primero;
        it.anterior= null;
        return it;
    }

}
