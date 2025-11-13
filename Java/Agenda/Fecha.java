package aed;

public class Fecha {
    private int dia;
    private int mes;
    public Fecha(int dia, int mes) {
        this.dia=dia;
        this.mes=mes;
    }

    public Fecha(Fecha fecha) {
        this.dia = fecha.dia;
        this.mes = fecha.mes;
    }

    public Integer dia() {
        return this.dia;
    }

    public Integer mes() {
        return this.mes;
    }

    public String toString() {
        String res=dia()+ "/" + mes();
        return res;
    }

    @Override
    public boolean equals(Object otra) {
        boolean res=false;
        boolean esMismaClase = this.getClass() == otra.getClass();
       
        if (esMismaClase) {
        Fecha f1 = (Fecha) otra;
        res = (this.dia==f1.dia) && (this.mes==f1.mes);
        }
        return res;
    }

    public void incrementarDia() {
        int resdia=this.dia;
        int resmes=this.mes;
        if (diasEnMes(resmes)==resdia && resmes!=12){
            resdia=1;
            resmes+=1;
        }
        else if (this.mes==12 &&this.dia==31){
            
            resmes=1;
            resdia=1;
        }
        else{
            resdia+=1;
        }
        this.dia=resdia;
        this.mes=resmes;

    }

    private int diasEnMes(int mes) {
        int dias[] = {
                // ene, feb, mar, abr, may, jun
                31, 28, 31, 30, 31, 30,
                // jul, ago, sep, oct, nov, dic
                31, 31, 30, 31, 30, 31
        };
        return dias[mes - 1];
    }

}
