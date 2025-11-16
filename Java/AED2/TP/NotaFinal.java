package aed;

public class NotaFinal {
    public double _nota;
    public int _id;

    public NotaFinal(double nota, int id) {
        _nota = nota;
        _id = id;
    }

    public int compareTo(NotaFinal otra) {
        if (Double.compare(this._nota, otra._nota)!=0) {
            return Double.compare(this._nota, otra._nota);
        }
        return this._id - otra._id;
    }

    @Override
    public boolean equals(Object otro) {
        if (otro.getClass() != getClass()) {
            return false;
        }
        NotaFinal otra = (NotaFinal) otro;
        return _id == otra._id && _nota == otra._nota; // comparamos el id Y la nota
    }

}
