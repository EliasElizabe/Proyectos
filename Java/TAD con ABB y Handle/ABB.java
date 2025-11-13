public class ABB<T extends Comparable<T>> {
    private Nodo raiz;
    private int tamaño;

    private class Nodo {
        T valor;
        Nodo izq;
        Nodo der;
        Nodo padre;

        public Nodo(T valor) {
            this.valor = valor;
            this.izq = null;
            this.der = null;
            this.padre = null;
        }
    }

    public class HandleABB {
        private Nodo nodo;

        private HandleABB(Nodo nodo) {
            this.nodo = nodo;
        }

        public T valor() {
            if (nodo == null) return null;
            return nodo.valor;
        }

        public boolean esVacio() {
            return nodo == null;
        }

        public HandleABB hijoIzquierdo() {
            if (nodo == null || nodo.izq == null) {
                return new HandleABB(null);
            }
            return new HandleABB(nodo.izq);
        }

        public HandleABB hijoDerecho() {
            if (nodo == null || nodo.der == null) {
                return new HandleABB(null);
            }
            return new HandleABB(nodo.der);
        }

        public HandleABB padre() {
            if (nodo == null || nodo.padre == null) {
                return new HandleABB(null);
            }
            return new HandleABB(nodo.padre);
        }

        public void eliminar() {
            if (nodo != null) {
                ABB.this.eliminar(nodo.valor);
                nodo = null; // El handle queda inválido después de eliminar
            }
        }
    }

    public ABB() {
        raiz = null;
        tamaño = 0;
    }

    public int cardinal() {
        return this.tamaño;
    }

    public T minimo() {
        Nodo actual = this.raiz;
        while (actual != null && actual.izq != null) {
            actual = actual.izq;
        }
        return actual != null ? actual.valor : null;
    }

    public T maximo() {
        Nodo actual = this.raiz;
        while (actual != null && actual.der != null) {
            actual = actual.der;
        }
        return actual != null ? actual.valor : null;
    }

    // Método para obtener un HandleABB a la raíz
    public HandleABB raiz() {
        return new HandleABB(raiz);
    }

    // Método para buscar un elemento y devolver un HandleABB
    public HandleABB buscar(T elem) {
        Nodo nodo = buscar_nodo(elem, raiz);
        if (nodo != null && nodo.valor.compareTo(elem) == 0) {
            return new HandleABB(nodo);
        }
        return new HandleABB(null);
    }

    // Nuevo método insertar que devuelve HandleABB
    public HandleABB insertar(T elem) {
        if (this.raiz == null) {
            this.raiz = new Nodo(elem);
            this.tamaño = 1;
            return new HandleABB(raiz);
        }

        Nodo actual = buscar_nodo(elem, this.raiz);
        int cmp = elem.compareTo(actual.valor);

        if (cmp == 0) return new HandleABB(actual); // el elemento ya está

        Nodo nuevoNodo = new Nodo(elem);
        nuevoNodo.padre = actual;
        if (cmp < 0) actual.izq = nuevoNodo;
        else actual.der = nuevoNodo;

        this.tamaño++;
        return new HandleABB(nuevoNodo);
    }

    //auxiliar para insertar
    Nodo buscar_nodo(T elem, Nodo actual) {
        if (actual == null) return null;
        while (true) {
            int cmp = elem.compareTo(actual.valor);
            if (cmp == 0) return actual;
            else if (cmp < 0) {
                if (actual.izq != null) actual = actual.izq;
                else return actual;
            } else {
                if (actual.der != null) actual = actual.der;
                else return actual;
            }
        }
    }

    public boolean pertenece(T elem) {
        boolean res = false;
        if (this.raiz != null) {
            Nodo actual = buscar_nodo(elem, this.raiz);
            res = actual != null && actual.valor.compareTo(elem) == 0;
        }
        return res;
    }

    //auxiliares para eliminar
    Nodo buscarMinimo(Nodo nodo) {
        if (nodo == null) return null;
        Nodo res = nodo;
        while (res.izq != null) {
            res = res.izq;
        }
        return res;
    }

    Nodo sucesor_inmediato(Nodo actual) {
        if (actual.der == null) return null;
        else {
            return buscarMinimo(actual.der);
        }
    }

    public void eliminar(T elem) {
        Nodo nodoEliminar = buscar_nodo(elem, raiz);
        if (nodoEliminar == null || nodoEliminar.valor.compareTo(elem) != 0) return;
        
        this.tamaño--;
        
        // Caso 1: sin hijos
        if (nodoEliminar.izq == null && nodoEliminar.der == null) {
            if (nodoEliminar.padre == null) {
                raiz = null;
            } else if (nodoEliminar.padre.izq == nodoEliminar) {
                nodoEliminar.padre.izq = null;
            } else {
                nodoEliminar.padre.der = null;
            }
            return;
        }

        // Caso 2: solo hijo derecho
        if (nodoEliminar.izq == null) {
            if (nodoEliminar.padre == null) {
                raiz = nodoEliminar.der;
                raiz.padre = null;
            } else if (nodoEliminar.padre.izq == nodoEliminar) {
                nodoEliminar.padre.izq = nodoEliminar.der;
            } else {
                nodoEliminar.padre.der = nodoEliminar.der;
            }
            if (nodoEliminar.der != null) {
                nodoEliminar.der.padre = nodoEliminar.padre;
            }
            return;
        }

        // Caso 3: solo hijo izquierdo
        if (nodoEliminar.der == null) {
            if (nodoEliminar.padre == null) {
                raiz = nodoEliminar.izq;
                raiz.padre = null;
            } else if (nodoEliminar.padre.izq == nodoEliminar) {
                nodoEliminar.padre.izq = nodoEliminar.izq;
            } else {
                nodoEliminar.padre.der = nodoEliminar.izq;
            }
            if (nodoEliminar.izq != null) {
                nodoEliminar.izq.padre = nodoEliminar.padre;
            }
            return;
        }

        // Caso 4: dos hijos
        Nodo sucesor = sucesor_inmediato(nodoEliminar);
        nodoEliminar.valor = sucesor.valor;
        
        // Eliminar el sucesor
        if (sucesor.padre.izq == sucesor) {
            sucesor.padre.izq = sucesor.der;
        } else {
            sucesor.padre.der = sucesor.der;
        }
        if (sucesor.der != null) {
            sucesor.der.padre = sucesor.padre;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        
        ABB_Iterador iter = iterador();
        boolean primero = true;
        while (iter.haySiguiente()) {
            if (!primero) {
                sb.append(", ");
            }
            sb.append(iter.siguiente().toString());
            primero = false;
        }
        
        sb.append("}");
        return sb.toString();
    }

    public class ABB_Iterador {
        private Nodo actual;
        private Nodo siguiente;

        public ABB_Iterador() {
            actual = null;
            siguiente = raiz;
            if (siguiente != null) {
                while (siguiente.izq != null) {
                    siguiente = siguiente.izq;
                }
            }
        }

        public boolean haySiguiente() {
            return siguiente != null;
        }

        public T siguiente() {
            if (!haySiguiente()) return null;
            
            actual = siguiente;
            T valor = actual.valor;
            
            // Encontrar el siguiente nodo en orden
            if (siguiente.der != null) {
                siguiente = siguiente.der;
                while (siguiente.izq != null) {
                    siguiente = siguiente.izq;
                }
            } else {
                while (siguiente.padre != null && siguiente == siguiente.padre.der) {
                    siguiente = siguiente.padre;
                }
                siguiente = siguiente.padre;
            }
            
            return valor;
        }
    }

    public ABB_Iterador iterador() {
        return new ABB_Iterador();
    }
}