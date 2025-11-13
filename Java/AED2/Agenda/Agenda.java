package aed;

public class Agenda {
    private Fecha fechaActual;
    private ArregloRedimensionableDeRecordatorios recordatorio;

    public Agenda(Fecha fechaActual) {
       this.fechaActual = new Fecha (fechaActual);
       this.recordatorio = new ArregloRedimensionableDeRecordatorios();
    }

    public void agregarRecordatorio(Recordatorio recordatorio) {;
        ArregloRedimensionableDeRecordatorios nuevoRecordatorio= new ArregloRedimensionableDeRecordatorios();
        boolean existeRecordatorio = this.recordatorio!=null;
        if (existeRecordatorio ){
            nuevoRecordatorio= this.recordatorio;
            nuevoRecordatorio.agregarAtras(recordatorio);
        }
        else{
            nuevoRecordatorio.agregarAtras(recordatorio);
        }
        this.recordatorio=nuevoRecordatorio;
        
    }

    @Override
    public String toString() {
        ArregloRedimensionableDeRecordatorios recordatorios = this.recordatorio;
        Fecha fechaActual=this.fechaActual;
        String texto;
        texto=fechaActual+ "\n" + "=====\n";
        for (int i =0 ;i < recordatorios.longitud();i++){
            String fechaRecordatorio= recordatorios.obtener(i).fecha().toString();
            String textoFechaActual= fechaActual.toString();
            if (textoFechaActual.equals(fechaRecordatorio)) {
                texto+= recordatorios.obtener(i).toString() +"\n";
            }
        }
        return texto;
    }

    public void incrementarDia() {
        Fecha proximaFecha = new Fecha(this.fechaActual);
        proximaFecha.incrementarDia();
        this.fechaActual = new Fecha(proximaFecha);
    }

    public Fecha fechaActual() {
        return this.fechaActual;
    }

}
