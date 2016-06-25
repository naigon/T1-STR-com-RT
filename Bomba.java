import java.util.concurrent.TimeUnit;
import java.util.Random;

public class Bomba {
    
    //capacidade da bomba (litros p/ segundo)
    private final int P = 250;
    //estado bomba(ligada/desligada)
    private boolean estado  = false;
    //variavel p/ defeito na bomba
    private boolean defeito = false;

    public boolean getEstado() {
        return estado;
    }

    public int getP() {
        return P;
    }

    public void setEstado(boolean estado) throws InterruptedException {
        this.estado = estado;
        TimeUnit.SECONDS.sleep(1);
    }

    public boolean getDefeito() {
          Random gerador = new Random();
 		int numero = gerador.nextInt(100);
        if(numero<85)
        	return false;
        else
        	return true;
    }

    public void setDefeito(boolean defeito) {
        this.defeito = defeito;
    }
    
}
