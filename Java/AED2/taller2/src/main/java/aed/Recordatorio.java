package aed;

public class Recordatorio {
    private String mensaje;
    private Fecha fecha;
    private Horario horario;
    public Recordatorio(String mensaje, Fecha fecha, Horario horario) {
        this.mensaje = mensaje;
        this.fecha = new Fecha (fecha);
        this.horario = new Horario(horario.hora(),horario.minutos());
    }

      public Horario horario() {
        Horario res=new Horario(this.horario.hora(),this.horario.minutos());
        return res;
    }

    public Fecha fecha() {
        Fecha res= new Fecha (this.fecha);
        return res;
    }

    public String mensaje() {
        // Implementar
        return this.mensaje;
    }

    @Override
    public String toString() {
        // Implementar
        return this.mensaje + " @ " + this.fecha + " " + this.horario;
    }

    @Override
    public boolean equals(Object otro) {
        // Implementar
        return true;
    }

}
