package aed;

class Funciones {

/***  Primera parte: Funciones en java ***/

    int cuadrado(int x) {
        int res= x*x;
        return res;
    }

    double distancia(double x, double y) {
        double res = Math.sqrt(x*x +y*y);
        return res;
    }

    boolean esPar(int n) {
        boolean res = n%2==0;
        return res;
    }

    boolean esBisiesto(int n) {
        boolean res = (n%4==0 && n%100!=0) || n%400==0;
        return res;
    }

    int factorialIterativo(int n) {
        int res=1;
        if (n==0){
            res=1;
        }
    else{
        while (n!=0){
            res= res*n;
            n=n-1;
        }}
        return res;
    }

    int factorialRecursivo(int n) {
        int res=1;
        if (n==0){
            return res;
        }
        else{
            res=res*n;
            res*=factorialRecursivo(n-1);
        }
        return res;
    }

    /**Funciones auxiliares para esPrimo**/
    boolean divideA(int x,int n){
        boolean res= n%x==0;
        return res;
    }

    boolean esPrimo(int n) {
        boolean res=false;
        if (n<=1){
            return false;
        }
        for (int i=2;i<n;i++){
            if (divideA(i,n)){
                return false;
            }   
        }
        return true; 
    }

    int sumatoria(int[] numeros) {
        int res=0;
        for (int i=0;i<numeros.length;i++){
            res+=numeros[i];
        }
        return res;
    }

    int busqueda(int[] numeros, int buscado) {
        int res=0;
        for (int i=0;i<numeros.length;i++){
            if (numeros[i]==buscado){
                res=i;
            }
        }
        return res;
    }

    boolean tienePrimo(int[] numeros) {
        boolean res=false;
        for (int i=0;i<numeros.length;i++){
            if (esPrimo(numeros[i])){
                res=true;
            }
        }
        // COMPLETAR
        return res;
    }

    boolean todosPares(int[] numeros) {
        boolean res=false;
        for (int i=0;i<numeros.length;i++){
            if (esPar(numeros[i])){
                res=true;
            }
            else{
                return false;
            }
        }
        return res;
    }

    boolean esPrefijo(String s1, String s2) {
        boolean res=false;
        for (int i=0;i<s1.length()&&s1.length()<=s2.length();i++){
            if((s1.charAt(i))==(s2.charAt(i))){
                res=true;
            }            
            else{
                return false;
            }
        }
        return res;
    }

    boolean esSufijo(String s1, String s2) {
        boolean res=false;
            for (int j=0;j<s2.length();j++){
                if((s1.charAt(0))==(s2.charAt(j))){
                    res= esPrefijo(s1,s2.substring(j));
                    return res;                    
                }
                else{
                    res=false;
                }
            }
        return res;
    }

/***  Segunda parte: Debugging ***/

    boolean xor(boolean a, boolean b) {
        boolean res= (a || b) && !(a && b);
        return res;
    }

    boolean iguales(int[] xs, int[] ys) {
        boolean res = true;

        if(xs.length!=ys.length){
            res=false;
            return res;
        }
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] != ys[i]) {
                res = false;
            }
        }
        return res;
    }

    boolean ordenado(int[] xs) {
        boolean res = true;
        for (int i = 0; i < (xs.length-1); i++) {
            if (xs[i] > xs [i+1]) {
                res = false;
            }
        }
        return res;
    }

    int maximo(int[] xs) {
        int res = 0;
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] > res) res = xs[i];
            if (xs[i]<0&& res==0) res=xs[i];
        }
        return res;
    }

    boolean todosPositivos(int[] xs) {
        boolean res = true;
        for (int x : xs) {
            if (x >0) {
                res = true;
            } else {
                res = false;
                return res;
            }
        }
        return res;
    }

}
