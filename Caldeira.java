import java.util.Random;

public class Caldeira {
    
    //CONSTANTES
    //capacidade maxima(litros)
    private final int C = 10000;
    //limite minimo(litros)
    private final int M1 = 2000;
    //limite maximo(litros)
    private final int M2 = 8000;
    //minimo normal(litros)
    private final int N1 = 4000;
    //maximo normal(litros)
    private final int N2 = 6000;
    //capacidade da valvula de vazao(litros p/ segundo)
    private final int VZ = 500;
    //quantidade de vapor existente na caldeira
    private int v=0;
    //status do sensor que mede o vapor
    private boolean funcionando_sensor_vapor = true;
    //quantidade de agua na caldeira
    private int q=0;
    //estado da valvula de vazao
    private boolean liberar_agua = false;
    //status do sensor de agua
    private boolean funcionando_sensor_agua = true;

    public int getC() {
        return C;
    }
    
    public int getQ() {
        return q;
    }

    public int getM1() {
        return M1;
    }

    public int getM2() {
        return M2;
    }

    public int getN1() {
        return N1;
    }

    public int getN2() {
        return N2;
    }

    public int getVZ() {
        return VZ;
    }
    
    public void setQ(int q) {
        this.q = q;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public boolean getFuncionando_sensor_vapor() {
        Random gerador = new Random();
 		int numero = gerador.nextInt(100);
        if(numero<85)
        	return true;
        else
        	return false;
    }

    public void setFuncionando_sensor_vapor(boolean funcionando_sensor_vapor) {
        this.funcionando_sensor_vapor = funcionando_sensor_vapor;
    }

    public boolean getFuncionando_sensor_agua() {
         Random gerador = new Random();
 		int numero = gerador.nextInt(100);
        if(numero<85)
        	return true;
        else
        	return false;
    }

    public void setFuncionando_sensor_agua(boolean funcionando_sensor_agua) {
        this.funcionando_sensor_agua = funcionando_sensor_agua;
    }

    public boolean getLiberar_agua() {
        return liberar_agua;
    }

    public void setLiberar_agua(boolean liberar_agua) {
        this.liberar_agua = liberar_agua;
    }

        
}
    

 


