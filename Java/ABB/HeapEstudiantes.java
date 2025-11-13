public class HeapEstudiantes{
    private Estudiante[] _estudiantes;
    private HandleHeap[]_handles;
    private int _longitud;

    public class HandleHeap{
        private HeapEstudiantes _heap;
        private int _indiceHeap;
        private HandleHeap(int indice,HeapEstudiantes heap){
            _indiceHeap = indice;
            _heap = heap;
        }
        public Estudiante obtenerEstudiante(){
            return _heap._estudiantes[_indiceHeap];
        }
        
        public void eliminar(){
            _heap.eliminar(_indiceHeap);
            _indiceHeap= -1;
        }

        public void resolverUnEjercicio_Handle(int ejercicio, int respuesta){
            this._heap._estudiantes[_indiceHeap].resolverUnEjercicio(ejercicio,respuesta);
            reordenar(_indiceHeap);
        }

        public void cambiarExamen_Handle(int[] examen){
            this._heap._estudiantes[_indiceHeap].cambiarExamen(examen);
            reordenar(_indiceHeap);
        }
    }

    public HeapEstudiantes(int cantidadDeEstudiantes,int[] examenSolucion){
        _estudiantes= new Estudiante[cantidadDeEstudiantes]; //lista vacia de _estudiantes tamaño cantidadEstudiantes
        _handles = new HandleHeap[cantidadDeEstudiantes]; //lista vacia de_handles tamaño cantidadEstudiantes
        _longitud = cantidadDeEstudiantes;
        for (int i=0;i<cantidadDeEstudiantes;i++){
            _estudiantes[i]= new Estudiante(i,examenSolucion); //relleno la lista de manera ordenada, ya que la nota es 0 al iniciar
        _handles[i] = new HandleHeap(i,this); //creo y asigno_handles
        }
    }

    public HandleHeap[] getHandleHeaps(){
        return _handles;
    }
  
    
    private void reordenar(int pos) {
    // Primero intentamos subir si corresponde
        int padre = (pos - 1) / 2;
        if (pos > 0 && _estudiantes[pos].nota() < _estudiantes[padre].nota()) { //si pos es 0 es decir la raiz da false, caso contrario se compara con el padre
            swap(pos, padre); //como pos es mas pequeño que el padre entonces se swapea hacia arriba
            reordenar(padre);  // hacemos recursividad para ver nuevamente si pos debe seguir subiendo
        } else {
        // Si no sube, verificamos si necesita bajar
            heapifyDown(pos);
        }
    }
    
    private void heapifyDown(int pos) {
        int min = pos; //renombro variable
        int izq = 2 * pos + 1; //ubico hijo izq
        int der = 2 * pos + 2; //ubico hijo der

        if (izq < _longitud && _estudiantes[izq].nota() < _estudiantes[min].nota()) { //si hijo izq existe y izq es menor
            min = izq; //entonces izq es el minimo y pos no lo es
        }
        if (der < _longitud && _estudiantes[der].nota() < _estudiantes[min].nota()) { //si hijo der existe y der es menor
            min = der; //entonces der es el minimo y pos no lo es
        }

        if (min != pos) { //si el min no es pos
            swap(pos, min); //entonces swapeo pos con el min (es decir bajo el seleccionado por el que es mas pequeño)
            heapifyDown(min); //verifico si tiene que seguir bajando
        //si ninguno se cumple el seleccionado queda donde esta
        }
    }
    
    private void swap(int i, int j) {
        Estudiante tmpE = _estudiantes[i]; //variable temporal para el swap en heap
        _estudiantes[i] = _estudiantes[j]; //swap de heap
        _estudiantes[j] = tmpE; //swap de heap

        //una vez swapeado en el heap, selecciono los_handles con el ID de los modificados y le asigno el nuevo indice del heap (ya que el ID me indica la ubicacion del handle correcto)
        _handles[_estudiantes[j].ID()]._indiceHeap = j; //selecciono el handle desde el ID estudiante y actualizo el puntero (antes era i ahora j)
        _handles[_estudiantes[i].ID()]._indiceHeap = i ; //selecciono el otro handle desde el ID estudiante y acutalizo el puntero (antes era j ahora i)
    }

    public void eliminar(int indiceHeap) {
        if (indiceHeap < 0 || indiceHeap >= _longitud) return; //el indice esta fuera de rango
        
        // O mover el último elemento si quieres mantener compacto
        swap(indiceHeap, _longitud - 1); //pongo al final el que voy a eliminar y swapeo al ultimo
        _longitud--; // achico la longitud para que mis metodos no tomen en cuenta el ultimo (el anteultimo pasa a ser el ultimo en la logica)
        
        if (indiceHeap < _longitud) { //esto cubre el caso de que el que quiera eliminar sea distinto al ultimo (si es el ultimo entonces no hay que reordenar)
            reordenar(indiceHeap); //como swapie el ultimo con el que queria eliminar, entonces tengo reordenar
        }
    }
}