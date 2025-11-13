package aed;

public class Horario {
    private int hora;
    private int minutos;
    public Horario(int hora, int minutos) {
        this.hora=hora;
        this.minutos=minutos;
    }

    public int hora() {
        return this.hora;
    }

    public int minutos() {
        return this.minutos;
    }

    @Override
    public String toString() {
        String res=hora()+":"+minutos();
        return res;
    }

    @Override
    public boolean equals(Object otro) {
        boolean res=false;
        boolean esMismaClase = this.getClass() == otro.getClass();
       
        if (esMismaClase) {
        Horario h1 = (Horario) otro;
        res = (this.hora==h1.hora) && (this.minutos==h1.minutos);
        }
        return res;
    }

}
