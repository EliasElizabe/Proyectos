package aed;

public class Heap<T extends Comparable<T>>{
    private T[] _listaHeap; //array donde estan los elementos T del heap
    private HandleHeap[] _handles; //array con los handles ordenados por orden de llegada, el handle indica el índice del elemento T correspondiente dentro del heap
    private int[] _posHandlesPorHeap; //array que me indica para cada elemento T qué handle le corresponde dentro de la lista handles
    private int _cantidadDeElementos; //me indica la longitud del heap (cambia a medida que inserto o desencolo)
    private boolean _esMaxHeap; //boolean que me dice si es MaxHeap o MinHeap

    public class HandleHeap {
        private int _indiceHeap;

        private HandleHeap(int indice) {
            this._indiceHeap = indice;
        }
        
        public T valor() {
            return _listaHeap[_indiceHeap];
        }
    }

    public Heap(T[] listaHeap, boolean esMaxHeap, int cantElementos) {
        this._posHandlesPorHeap = new int[listaHeap.length]; 
        this._handles = new Heap.HandleHeap[listaHeap.length];
        this._listaHeap = listaHeap; 
        this._cantidadDeElementos = cantElementos; //la cantidad de elementos es variable
        this._esMaxHeap = esMaxHeap; 

        for (int i = 0; i < listaHeap.length; i++) {
            if (i < cantElementos) { 
                _handles[i] = new HandleHeap(i); //genero los handles
                _posHandlesPorHeap[i] = i;      //mismo que arriba
            } else {                            //si esta fuera del rango de cantidad de elementos rellena con nulls
                _handles[i] = null;
                _posHandlesPorHeap[i] = -1;
            }
        }
        
        if (_cantidadDeElementos > 0) {
            this.heapify(); 
        }
    }

    public HandleHeap[] getHandleHeap() {
        return _handles; 
    }

    public void reordenar(HandleHeap handlePos) { 
        //cuando realizo una modificacion se debe reordenar el heap, también actualiza los handles
        int pos = handlePos._indiceHeap;
                
        //verifico si debe subir y si es asi sube
        while (debeSwapear((pos - 1) / 2, pos)) { //verifico que pos deba swapear con el padre y su existencia
            int padre = (pos - 1) / 2; //ubico padre       
            swap(pos, padre);
            pos = padre; //como swapearon ahora el padre es pos, y entra en el ciclo nuevamente
        }
        
        //despues de subir o no, verifico si tiene que bajar
        heapifyDown(pos);
    }

    public void eliminar(HandleHeap handle) {
        int elementoAEliminar = handle._indiceHeap; //ubico mi elemento en el heap
        if (elementoAEliminar < 0 || elementoAEliminar >= _cantidadDeElementos) { //si esta fuera de rango no hacer nada
            return;
        }
        swap(elementoAEliminar, _cantidadDeElementos - 1); //envio al que quiero eliminar al final del heap 
        int elQueQuieroEliminar = _cantidadDeElementos - 1; //como realice el swap se cambia el indice
        _listaHeap[elQueQuieroEliminar] = null;
        _cantidadDeElementos--; //achico la longitud, por lo que toda la logica de reordenar no tomara en cuenta el ultimo elemento (el eliminado)
        
        int conElQueSwapie = elementoAEliminar;
        int idEliminado = _posHandlesPorHeap[_cantidadDeElementos]; //busco el indice handle del eliminado
        _handles[idEliminado] = null; //el handle apunta fuera de rango => no existe
        _posHandlesPorHeap[elQueQuieroEliminar] = -1; //se elimina referencia indiceHeaps -> indiceHandles

        if (conElQueSwapie < _cantidadDeElementos) {
            reordenar(_handles[_posHandlesPorHeap[conElQueSwapie]]); //si elimino un elemento distinto al ultimo, entonces indiceHeap es con el quien swapie, es decir es el que antes era el ultimo, necesito reordenar para asegurar que este en el lugar correcto
        }       
    }

    public T desencolar() { 
        if (_cantidadDeElementos == 0) { //si esta vacio no desencola nada
            return null;
        }
        T raiz = _listaHeap[0];  //localizo la raiz
        eliminar(_handles[_posHandlesPorHeap[0]]); //elimino la raiz del heap
        return raiz; //devuelvo la raiz
    }

    public void insertar(T nuevoElemT, int IDHandle) {
        if (_cantidadDeElementos == _listaHeap.length) {
            return; //array lleno
        }
    
        _listaHeap[_cantidadDeElementos] = nuevoElemT; //coloco el nuevo elemento al final
        _handles[IDHandle] = new HandleHeap(_cantidadDeElementos); //crea nuevo handle que apunta al nuevoElemT
        _posHandlesPorHeap[_cantidadDeElementos] = IDHandle; //actualizo la lista de indicesHeaps -> indicesHandles
        
        _cantidadDeElementos++; //agrando la longitud 
        reordenar(_handles[IDHandle]); //ordeno el nuevoElemeT 
    }

    private void heapify() {
        for (int i = (_cantidadDeElementos / 2) - 1; i >= 0; i--) { //heapify agarra todos los hijos y les ejecuta heapifyDown siguiendo el algoritmo de Floyd
            heapifyDown(i);
        }
    }
    
    private void heapifyDown(int pos) {
        int mejorHijo = pos; //supongo que el mejorCandidato para swapear es el mismo, por una cuestion algoritmica
        int izq = 2 * pos + 1; 
        int der = 2 * pos + 2;

        if (izq < _cantidadDeElementos && debeSwapear(mejorHijo, izq)) { //si izq esta dentro de rango y debeSwapear entonces es el mejorHijo
            mejorHijo = izq;
        }
        
        if (der < _cantidadDeElementos && debeSwapear(mejorHijo, der)) { //idem arriba, pero mejorHijo pudo haberse actualizado por lo que se compara nuevamente para saber donde corresponde
            mejorHijo = der;
        }

        if (mejorHijo != pos) { //si el mejorCandidato no es pos, entonces debe swapear
            swap(pos, mejorHijo);
            int nuevoIndicePos = mejorHijo; //el indice de pos ahora es mejorHijo por el swap
            heapifyDown(nuevoIndicePos);  
        }
    }

    private boolean debeSwapear(int indice1, int indice2) {
        if (indice1 < 0 || indice1 >= _cantidadDeElementos || //chequeo que ambos índices estén dentro de rango
            indice2 < 0 || indice2 >= _cantidadDeElementos) {
            return false;
        }
        
        if (_listaHeap[indice1] == null || _listaHeap[indice2] == null) { //chequeo que ninguno sea null
            return false; 
        }
        
        int comparacion = _listaHeap[indice1].compareTo(_listaHeap[indice2]);
        
        // para min-heap: queremos que el elemento con menor valor esté arriba
        // para max-heap: queremos que el elemento con mayor valor esté arriba
        if (_esMaxHeap) {
            return comparacion < 0; // en max-heap, swap si indice1 < indice2
        } else {
            return comparacion > 0; // en min-heap, swap si indice1 > indice2
        }
    }

    private void swap(int i, int j) {
        int indiceI = _posHandlesPorHeap[i]; //ubico el indice del handle correspondiente al elemento en el heap
        int indiceJ = _posHandlesPorHeap[j]; //idem 
        
        T tmpT = _listaHeap[i];
        _listaHeap[i] = _listaHeap[j]; //swap posiciones del heap
        _listaHeap[j] = tmpT;          //idem

        _handles[indiceI]._indiceHeap = j; //swap indices del handle. handle -> heap
        _handles[indiceJ]._indiceHeap = i; //idem

        _posHandlesPorHeap[i] = indiceJ; //swap indicesHeaps -> indicesHandles
        _posHandlesPorHeap[j] = indiceI; //idem
    }
}