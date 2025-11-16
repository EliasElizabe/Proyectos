package aed;

public class Estudiante implements Comparable<Estudiante> {
    private int _id; 
    private int[] _examen;
    private int[] solucionExamen;
    private int _respuestasCorrectas; 

    public Estudiante(int id, int[] examenSolucion) {
        _id = id;
        solucionExamen = examenSolucion;
        int longitudExamen = examenSolucion.length;
        _examen = new int[longitudExamen];

        for (int i = 0; i < longitudExamen; i++) { //respuesta = -1 si está en blanco
            _examen[i] = -1; 
        }
        _respuestasCorrectas = 0;
    }

    public int[] examen() {
        return _examen;
    }

    public int ID() {
        return _id;
    }
    
    public double nota() {
        int longitudExamen = solucionExamen.length;
        return ((double) _respuestasCorrectas / longitudExamen) * 100;
    }
    
    public void resolverUnEjercicio(int ejercicio, int respuesta) { //no se puede resolver el mismo ejercicio 2 veces
        _examen[ejercicio] = respuesta;
        if (solucionExamen[ejercicio] == _examen[ejercicio]) {
            _respuestasCorrectas++;    
        }
    }

    public void cambiarExamen(int[] examen) {
        _examen = new int[examen.length];
        _respuestasCorrectas = 0;

        int longitudExamen = solucionExamen.length;
        for (int i = 0; i < longitudExamen; i ++) {
            _examen[i] = examen[i];
            if (_examen[i] == solucionExamen[i]) {
                _respuestasCorrectas ++;
            }
        }
    }

    public int compañeroDer() {
        return _id +1;
    }

    public int compañeroIzq() {
        return _id -1;
    }

    public int compañeroAdel(int dimensionAula) {
        return _id - (dimensionAula + 1) / 2;
    }

    public boolean hayCompañeroDer(int cantidadEstudiantes, int dimensionAula) {
        boolean estaEnRango = (this.compañeroDer() < cantidadEstudiantes); 
        boolean estoyEnLaParedDerecha = ((this._id % ((dimensionAula + 1) / 2)) == (((dimensionAula + 1) / 2) - 1));
        return estaEnRango && !estoyEnLaParedDerecha;
    }

    public boolean hayCompañeroIzq (int dimensionAula) {
        boolean estaEnRango = (this.compañeroIzq() >= 0);
        boolean estoyEnLaParedIzquierda = this._id % ((dimensionAula + 1) / 2) == 0;
        return estaEnRango && !estoyEnLaParedIzquierda;
    }

    public boolean hayCompañeroAdel (int dimensionAula) {
        return this.compañeroAdel(dimensionAula) >= 0;
    }
    
    @Override
    public int compareTo(Estudiante otroEstudiante) {
        if(otroEstudiante == null) {
            return 1;
        }
        if (Double.compare(this.nota(), otroEstudiante.nota())!= 0) {
            return Double.compare(this.nota(), otroEstudiante.nota());
        }
        return Integer.compare(this._id, otroEstudiante._id);
    }
}
