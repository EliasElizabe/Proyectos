package aed;
import java.util.ArrayList;

public class Edr {
    Heap<Estudiante> estudiantesEntregaron; //max heap
    Heap<Estudiante> estudiantesNoEntregaron; //min heap
    Heap<Estudiante>.HandleHeap[] hEntregaron; //lista de handles max heap 
    Heap<Estudiante>.HandleHeap[] hNoEntregaron; //lista handles min heap
    int[] solucionExamen;
    int dimensionAula; 
    int cantidadEstudiantes; 
    boolean[] listaEsSospechoso;
    int cantidadDeSospechosos;


    public Edr (int ladoAula, int cantEstudiantes, int[] examenCanonico) { //O(E*R)
        cantidadEstudiantes = cantEstudiantes;
        dimensionAula = ladoAula; 
        solucionExamen = examenCanonico;
        
        Estudiante[] estudiantesListaVacia = new Estudiante[cantEstudiantes];
        Estudiante[] estudiantesParaHeap = new Estudiante[cantEstudiantes];
        for (int i = 0; i < cantEstudiantes; i ++) { //O(E)
            estudiantesParaHeap[i] = new Estudiante (i, examenCanonico); //?
        } //O(E*R)

        estudiantesEntregaron = new Heap<Estudiante>(estudiantesListaVacia, true,0); //true: max heap
        estudiantesNoEntregaron = new Heap<Estudiante>(estudiantesParaHeap, false,cantEstudiantes); //false: min heap 
        //O(E) construccion de heap

        hEntregaron = estudiantesEntregaron.getHandleHeap();
        hNoEntregaron = estudiantesNoEntregaron.getHandleHeap(); 
    } //O(E*R) + O(E) = O(E*R) 

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){ // O(E)
        //devuelve notas de los estudiantes ordenada por ID
        double[] res = new double[cantidadEstudiantes];
        for (int i = 0; i < cantidadEstudiantes; i++){ //repetir una operacion E veces O(E)
            if(!entrego(i)) { 
                Estudiante estudiante = hNoEntregaron[i].valor();
                res[i] = estudiante.nota(); 
            } else { 
                Estudiante estudiante = hEntregaron[i].valor();
                res[i]= estudiante.nota();
            }
        }
        return res;
    } //O(E)

//------------------------------------------------COPIARSE------------------------------------------------------------------------

public void copiarse(int iDestudiante){ //O(R+log(E))
        Estudiante estudiante = hNoEntregaron[iDestudiante].valor();
        int compañeroDer = -1; 
        int compañeroIzq = -1; 
        int compañeroAdel = -1; 
        int cantRespuestasCopiablesDer = 0;
        int cantRespuestasCopiablesIzq = 0;
        int cantRespuestasCopiablesAdel = 0;
        int ejercicioACopiar;
        int respuestaACopiar; 
    
        if (estudiante.hayCompañeroAdel(dimensionAula)) {
            compañeroAdel = estudiante.compañeroAdel(dimensionAula);
        }
        cantRespuestasCopiablesAdel = cantidadDeRespuestasCopiables(iDestudiante, compañeroAdel); //O(R)
        
        if (estudiante.hayCompañeroIzq(dimensionAula)) {
            compañeroIzq = estudiante.compañeroIzq();
        }
        cantRespuestasCopiablesIzq = cantRespuestasCopiablesAdel = cantidadDeRespuestasCopiables(iDestudiante, compañeroIzq); //O(R)
        
        if (estudiante.hayCompañeroDer(cantidadEstudiantes, dimensionAula)) {
            compañeroDer = estudiante.compañeroDer();
        }
        cantRespuestasCopiablesDer = cantidadDeRespuestasCopiables(iDestudiante, compañeroDer); //O(R)
        
        int idDelQueMeCopio = deQuienMeCopio(compañeroAdel, cantRespuestasCopiablesAdel, compañeroIzq, cantRespuestasCopiablesIzq, compañeroDer, cantRespuestasCopiablesDer);

        int i = 0;
        while(i < solucionExamen.length && !(!resolvioElEjercicio(iDestudiante, i) && resolvioElEjercicio(idDelQueMeCopio, i))) { //O(R)
                i ++;
            }
        //el while avanza hasta que llega a la primer respuesta que se puede copiar
        ejercicioACopiar = i;
        respuestaACopiar = hNoEntregaron[idDelQueMeCopio].valor().examen()[i];

        estudiante.resolverUnEjercicio(ejercicioACopiar, respuestaACopiar); 
        estudiantesNoEntregaron.reordenar(hNoEntregaron[iDestudiante]); //O(log(E))
    } // O(log(E)) + O(R) = O(R+log(E))

//-----------------------------------------------RESOLVER----------------------------------------------------------------

    public void resolver (int estudiante, int nroEjercicio, int respuestaEjercicio) { //O(log(E))
        Estudiante _estudiante = hNoEntregaron[estudiante].valor();
        _estudiante.resolverUnEjercicio(nroEjercicio, respuestaEjercicio); 
        estudiantesNoEntregaron.reordenar(hNoEntregaron[estudiante]); 
    } //O(log(E))

//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

    public void consultarDarkWeb(int k, int[] examenDW) { //O(k(R+log(E)))
        //los k estudiantes con peor nota cambian su examen a examenDW, se desempata por menor ID
        Estudiante[] estudiantesQueSeCopianDW = new Estudiante[k];
        for (int i = 0; i < k; i++){ //O(k) 
            estudiantesQueSeCopianDW[i] = estudiantesNoEntregaron.desencolar(); //saca el peor nota y menor ID y reordena O(log(E))
            estudiantesQueSeCopianDW[i].cambiarExamen(examenDW); //reemplaza su examen O(R)
        }  //O(k *( R + log(E))
        for (int i = 0; i < k; i++){ //otro for para meterlos al heap 
            estudiantesNoEntregaron.insertar(estudiantesQueSeCopianDW[i],estudiantesQueSeCopianDW[i].ID()); //insertar O(log(E))
        } //O(k * log(E))
    } //O(k*(R + log(E)) + O(k * log(E)) = O(k*(R + log(E))

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante){ //O(log(E))
        //insertar el estudiante al heapEntregaron y asegurarnos que tenga su handle en hEntregaron
        Estudiante _estudiante = hNoEntregaron[estudiante].valor(); 
        estudiantesNoEntregaron.eliminar(hNoEntregaron[estudiante]); //elimina el estudiante del heapEstudiantesNoEntregaron y tambien es eliminado del handle O(log(E))
        estudiantesEntregaron.insertar(_estudiante, estudiante);
    } //O(log(E))

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir(){ //O(E*log(E))
        //tiene que devolver las notas de mayor a menor y se desempata por ID mayor
        NotaFinal[] res = new NotaFinal[cantidadEstudiantes-cantidadDeSospechosos];
        int posEnRes = 0;
        int posEnHeap = 0;
        while (posEnHeap < cantidadEstudiantes){ //O(E)
            Estudiante estudiante = estudiantesEntregaron.desencolar(); //O(log(E))
            int indice = estudiante.ID();

            if (listaEsSospechoso[indice] == false) {
                res[posEnRes] = new NotaFinal(estudiante.nota(), estudiante.ID());
                posEnRes ++; 
            }
            posEnHeap ++;
        } //O(E*log(E))
        return res;
    } // O(E*log(E)) 

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() { //O(E*R)
        //tiene que devolver la lista de sospechosos ordenada por ID
        int[][] matrizRespuestas = new int[solucionExamen.length][10]; //matriz con la cantidad de respuestas de cada pregunta
        cantidadDeSospechosos = 0;
        listaEsSospechoso = new boolean[cantidadEstudiantes];
        
        matrizRespuestas = llenarMatriz(matrizRespuestas); //O(E*R)
        
        double minimoQuePuedenTenerElMismoExamen = 0.25 * (cantidadEstudiantes - 1); 
        
        for (int i = 0; i < cantidadEstudiantes; i++){ // O(E)
            boolean esSospechoso = true;
            boolean todoEnBlanco = true;
            for (int j = 0; j < solucionExamen.length; j++){ // O(R)
                int respuesta = hEntregaron[i].valor().examen()[j]; //buscar por handle la respuesta j del estudiante i
                if (resolvioElEjercicio(i, j)){
                    int totalRespuestasIgualesAMi = matrizRespuestas[j][respuesta] - 1;
                    todoEnBlanco = false;
                    if (totalRespuestasIgualesAMi < minimoQuePuedenTenerElMismoExamen){ //si no tiene la respuesta como el 25% del resto -> no es sospechoso
                        esSospechoso = false;
                        break;
                    } 
                } 
            } 
            if (esSospechoso && !todoEnBlanco) {
                cantidadDeSospechosos ++;
                listaEsSospechoso[i] = true;
            } else {
                listaEsSospechoso[i] = false;
            }
        } // O(E*R)

            return sospechosos(); // O(E)
    } // O(E*R) + O(E*R) + O(E) = O(E*R)

//-------------------------------------------AUXILIARES----------------------------------------------------------------

    private boolean entrego(int id) { //O(1)
        return ((hNoEntregaron[id] == null) && (hEntregaron[id] != null));
    }

    private int cantidadDeRespuestasCopiables(int estudiante, int compañero) { //O(R)
        int res = 0;
        if ((compañero != -1) && (!entrego(compañero))) { //compañero = -1 si existe
            for (int i = 0; i < solucionExamen.length; i ++) {
                if ((!resolvioElEjercicio(estudiante, i)) && (resolvioElEjercicio(compañero, i))) {
                    res ++;
                }
            }
        }
        return res;
    }

    private boolean resolvioElEjercicio(int id, int ejercicio) { //O(1)
        if (!entrego(id)) {
            return hNoEntregaron[id].valor().examen()[ejercicio] != -1;
        } else {
            return hEntregaron[id].valor().examen()[ejercicio] != -1;
        }
    }

    private int deQuienMeCopio(int idA, int cantidadRA, int idI, int cantidadRI, int idD, int cantidadRD) { //O(1)
        boolean meCopioAdel = (cantidadRA > cantidadRI) && (cantidadRA > cantidadRD);
        boolean meCopioIzq =  (cantidadRI >= cantidadRA) && (cantidadRI > cantidadRD); 
        boolean meCopioDer = (cantidadRD >= cantidadRA) && (cantidadRD >= cantidadRI);

        if (meCopioAdel) {
            return idA;
        } else if (meCopioIzq) {
            return idI;
        } else if (meCopioDer) {
            return idD;
        }
        return idD;
    }

    private int[][] llenarMatriz(int[][] matriz) { // O(E * R)

        for (int i = 0; i < cantidadEstudiantes; i++){ //itera estudiantes
            for (int j = 0; j < solucionExamen.length; j++){ //itera respuestas
                int respuesta = hEntregaron[i].valor().examen()[j]; // tengo que buscar al estudiante i en j pregunta
                if (resolvioElEjercicio(i, j)){
                    matriz[j][respuesta] += 1;
                }
            }
        }
        return matriz;
    }
 
    private int[] sospechosos() { // O(E)
        int[] res = new int[cantidadDeSospechosos];
        int i = 0;
            for (int id = 0; id < listaEsSospechoso.length; id++){ 
                if (listaEsSospechoso[id]) {
                    res[i] = id;
                    i ++;
                }
            }
            return res; 
    }
}


