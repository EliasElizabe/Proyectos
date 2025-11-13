package aed;

import java.util.*;

// Todos los tipos de datos "Comparables" tienen el método compareTo()
// elem1.compareTo(elem2) devuelve un entero. Si es mayor a 0, entonces elem1 > elem2
public class ABB<T extends Comparable<T>> {
    // Agregar atributos privados del Conjunto

    private class Nodo {
        T valor;
        Nodo izq;
        Nodo der;
        Nodo padre;
        Nodo (T v){
            valor = v;
            izq=null;
            der=null;
            padre=null;
        }
    }

    private Nodo raiz;
    private int tamaño;
    public ABB() {
        raiz=null;
        tamaño=0;
    }

    public int cardinal() {
        return this.tamaño;
    }

    public T minimo(){
        Nodo actual = this.raiz;
        while (actual!=null && actual.izq!=null){
            actual= actual.izq;
        }
        return actual.valor;
    }

    public T maximo(){
        Nodo actual = this.raiz;
        while (actual!=null && actual.der!=null){
            actual= actual.der;
        }
    return actual.valor;
    }

    //auxiliar para insertar
    Nodo buscar_nodo(T elem , Nodo actual){
    if (actual == null) return null;
    while (true) {
        int cmp = elem.compareTo(actual.valor);
        if (cmp == 0) return actual;
        else if (cmp < 0) {
            if (actual.izq != null) actual = actual.izq;
            else return actual;
            } 
        else {
            if (actual.der != null) actual = actual.der;
            else return actual;
            }
        }
    }

    public void insertar(T elem){
        if (this.raiz == null) {
            this.raiz = new Nodo(elem);
            this.tamaño = 1;
            return;
        }

        Nodo actual = buscar_nodo(elem, this.raiz);
        int cmp = elem.compareTo(actual.valor);

        if (cmp == 0) return; // el elemento ya está

        Nodo nuevoNodo = new Nodo(elem);
        nuevoNodo.padre = actual;
        if (cmp < 0) actual.izq = nuevoNodo;
        else actual.der = nuevoNodo;

        this.tamaño++;
    }

    public boolean pertenece(T elem){
        boolean res=false;
        if (this.raiz!=null){
            Nodo actual=buscar_nodo(elem, this.raiz);
            res=actual.valor.compareTo(elem)==0;
        }
        return res;
    }

    //auxiliares para eliminar
    Nodo buscarMinimo(Nodo nodo){
        Nodo res=nodo;
        while (res.izq!=null){
            res=res.izq;
        }
        return res;
    }
     Nodo buscarMaximo(Nodo nodo){
        Nodo res=nodo;
        while(res.izq!=null){
            res=res.izq;
        }
        return res;
    }

    Nodo sucesor_inmediato(Nodo actual){
        if(actual.der==null) return null;
        else{
            return buscarMinimo(actual.der);
        }
    }
    public void eliminar(T elem) {
    Nodo nodoEliminar = buscar_nodo(elem, raiz);
    if (nodoEliminar == null) return; // Caso 1: no está
    this.tamaño--;
    // Caso 2: sin hijos
    if (nodoEliminar.izq == null && nodoEliminar.der == null) {
        if (nodoEliminar.padre == null) raiz = null;
        else if (nodoEliminar.padre.izq == nodoEliminar)
            nodoEliminar.padre.izq = null;
        else
            nodoEliminar.padre.der = null;
        return;
    }

    // Caso 3a: solo hijo derecho
    if (nodoEliminar.izq == null) {
        if (nodoEliminar.padre == null) {
            raiz = nodoEliminar.der;
            raiz.padre = null;
        } else if (nodoEliminar.padre.izq == nodoEliminar)
            nodoEliminar.padre.izq = nodoEliminar.der;
        else
            nodoEliminar.padre.der = nodoEliminar.der;
        nodoEliminar.der.padre = nodoEliminar.padre;
        return;
    }

    // Caso 3b: solo hijo izquierdo
    if (nodoEliminar.der == null) {
        if (nodoEliminar.padre == null) {
            raiz = nodoEliminar.izq;
            raiz.padre = null;
        } else if (nodoEliminar.padre.izq == nodoEliminar)
            nodoEliminar.padre.izq = nodoEliminar.izq;
        else
            nodoEliminar.padre.der = nodoEliminar.izq;
        nodoEliminar.izq.padre = nodoEliminar.padre;
        return;
    }

    // Caso 4: dos hijos
    Nodo sucesor = sucesor_inmediato(nodoEliminar);
    nodoEliminar.valor = sucesor.valor; // copiamos el valor del sucesor
    // eliminamos el sucesor (que tiene como mucho un hijo derecho)
    if (sucesor.padre.izq == sucesor)
        sucesor.padre.izq = sucesor.der;
    else
        sucesor.padre.der = sucesor.der;
    if (sucesor.der != null)
        sucesor.der.padre = sucesor.padre;
}


    public String toString(){
        String string = "{";

        if (raiz == null) {
            return "{}";
        }

        Nodo actual = raiz;
        while (actual.izq != null) {
            actual = actual.izq;
        }
        string = string + actual.valor;
        if (actual.der != null) {
            actual = actual.der;
            while (actual.izq != null) {
                actual = actual.izq;
            }
        } else {
            actual = actual.padre;
        }
        while (actual != null) {
            string = string + "," + actual.valor;
            if (actual.der != null) {
                actual = actual.der;
                while (actual.izq != null) {
                    actual = actual.izq;
                }
            } else {
                while (actual.padre != null && actual == actual.padre.der) {
                    actual = actual.padre;
                }
                actual = actual.padre;
            }

        }
        string = string + "}";
        return string;

    }

public class ABB_Iterador {
        private Nodo actual;


        public ABB_Iterador() {
            actual = raiz;
            if (actual != null) {
                while (actual.izq != null) {
                    actual = actual.izq;
                }
            }
        }

        public boolean haySiguiente() {
            return actual != null;
        }

        public T siguiente() {
            T valor = actual.valor;

            if (actual.der != null) {
                actual = actual.der;
                while (actual.izq != null) {
                    actual = actual.izq;
                }
            } else {
                while (actual.padre != null && actual == actual.padre.der){
                actual=actual.padre;
            } 
            actual = actual.padre;
            }

            return valor;

            }
    }


    public ABB_Iterador iterador() {
        return new ABB_Iterador();
    }
}