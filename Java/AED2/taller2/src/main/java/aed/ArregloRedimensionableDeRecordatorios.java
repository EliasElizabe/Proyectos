package aed;

class ArregloRedimensionableDeRecordatorios {
    private Recordatorio[] recordatorios;
    private int cantidad;
    public ArregloRedimensionableDeRecordatorios() {
        this.recordatorios= new Recordatorio[0];
        this.cantidad = 0;
    }

    public int longitud() {
        this.cantidad = this.recordatorios.length;
        return this.cantidad;
    }

    public void agregarAtras(Recordatorio i) {
        Recordatorio[] nuevo = new Recordatorio[this.recordatorios.length+1];
        if (this.cantidad!=0) {
            for (int j =0; j < this.cantidad; j++){
                nuevo[j]= this.recordatorios[j];
            }
        }
    nuevo[this.cantidad]= i;
    this.recordatorios=nuevo;
    this.cantidad++;
    }

    public Recordatorio obtener(int i) {
        Recordatorio res=this.recordatorios[i];
        return res;
    }

    public void quitarAtras() {
        Recordatorio[] nuevo = new Recordatorio[this.recordatorios.length-1];
        for (int j =0; j < this.cantidad-1; j++){
            nuevo[j]= this.recordatorios[j];
        }
    this.recordatorios=nuevo;
    this.cantidad--;
    }

    public void modificarPosicion(int indice, Recordatorio valor) {
        this.recordatorios[indice]=valor;
    }

    public ArregloRedimensionableDeRecordatorios(ArregloRedimensionableDeRecordatorios vector) {
        int longitud=vector.longitud();
        this.recordatorios = new Recordatorio[longitud];
        this.cantidad= longitud;
        for (int i=0;i<longitud;i++){
            this.recordatorios[i]= (vector.copiar()).recordatorios[i];
        }
    }

    public ArregloRedimensionableDeRecordatorios copiar() {
    ArregloRedimensionableDeRecordatorios copia = new ArregloRedimensionableDeRecordatorios();
    
    for (int i = 0; i < this.cantidad; i++) {
        Recordatorio rOriginal = this.recordatorios[i];
        Recordatorio rCopia = new Recordatorio(rOriginal.mensaje(),rOriginal.fecha(),rOriginal.horario());
        copia.agregarAtras(rCopia);
    }
    
    return copia;
}
}
