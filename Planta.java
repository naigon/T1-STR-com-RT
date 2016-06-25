import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit; 
import javax.realtime.PriorityScheduler;
import javax.realtime.PriorityParameters;
import javax.realtime.PeriodicParameters;
import javax.realtime.RelativeTime;
import javax.realtime.RealtimeThread;


public class Planta {
	String modo_operacao;  
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {  
    	Planta programa = new Planta();
  		programa.tempoReal();
    }
    
    void tempoReal() throws InterruptedException, FileNotFoundException {
    Caldeira cal = new Caldeira();
    Bomba bom = new Bomba();
    File f = new File("log.txt");//cria arquivo
    PrintWriter pw = new PrintWriter(f);//cria interface para escrita
    Controle cont = new Controle();

        //while(true){
        int priority1 = PriorityScheduler.instance().getMinPriority()+10;
    	PriorityParameters priorityParameters1 = new PriorityParameters(priority1);
    
    	int priority2 = PriorityScheduler.instance().getMinPriority()+10;
    	PriorityParameters priorityParameters2 = new PriorityParameters(priority2);
         
        RelativeTime period1 = new RelativeTime(100 /* ms */, 0 /* ns */);
    	RelativeTime period2 = new RelativeTime(1000/* ms */, 0 /* ns */);
    	
    	PeriodicParameters periodicParameters1 = new PeriodicParameters(null,period1, null,null,null,null);
    	PeriodicParameters periodicParameters2 = new PeriodicParameters(null,period2, null,null,null,null);
    	
    	//Thread RealTime que executa o controle da caldeira num periodo de 200 ms
    	RealtimeThread realtimeThread1 = new RealtimeThread(priorityParameters1,periodicParameters1){ 
        	public void run() {
        		while(true){
        		try{
        	    waitForNextPeriod();
        	    modo_operacao=cont.start(cal,bom);
       	    	}catch(InterruptedException ex){}
       	    	}
       	    }
        };
        //Thread RealTime que executa a planta fisica da caldeira num periodo de 1000 ms     
        RealtimeThread realtimeThread2 = new RealtimeThread(priorityParameters2,periodicParameters2){ 
        public void run() {
        	while(true){
        	waitForNextPeriod();
            switch (modo_operacao) {  
             case "ESVAZIAR":
                try{
                    System.out.println("MENSAGEM: caldera acima do nivel normal");
                    pw.println("MENSAGEM: caldera acima do nivel normal");
                    System.out.println("MENSAGEM: nivel da caldeira: " + cal.getQ() + 'L');
                    pw.println("MENSAGEM: nivel da caldeira: " + cal.getQ() + 'L');
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("MENSAGEM: preparando para esvaziar...\n");
                    pw.println("MENSAGEM: preparando para esvaziar...\n");
                    pw.println("\n");                
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("MODO: ESVAZIAR\n");
                    pw.println("MODO: ESVAZIAR\n");
                    pw.println("\n");
		    //abre a valvula de vazao
                    cal.setLiberar_agua(true);
                    System.out.println("MENSAGEM: nivel da caldeira: " + cal.getQ() + 'L');
                    pw.println("MENSAGEM: nivel da caldeira: " + cal.getQ() + 'L');
                    
                    while(cal.getQ() > (cal.getN1()+cal.getN2())/2){
                        System.out.println("MENSAGEM: esvaziando a caldeira...");
                        pw.println("MENSAGEM: esvaziando a caldeira...");
                        cal.setV((cal.getQ() - cal.getVZ())/100);
                        cal.setQ(cal.getQ() - cal.getVZ());
                        System.out.println("MENSAGEM: nivel da caldeira: " + cal.getQ() + 'L' + "\n");
                        pw.println("MENSAGEM: nivel da caldeira: " + cal.getQ() + 'L' + "\n");
                        pw.println("\n");
                        TimeUnit.SECONDS.sleep(1);
                    }
		    //fecha a valvula de vazao
                    cal.setLiberar_agua(false);  
                    pw.flush();
                     }catch(InterruptedException ex){}
                    break;
                
                case "ENCHER":
                try{
                    System.out.println("MENSAGEM: caldera vazia ou abaixo do nivel normal");
                    pw.println("MENSAGEM: caldera vazia ou abaixo do nivel normal");
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("MENSAGEM: preparando para encher...");
                    pw.println("MENSAGEM: preparando para encher...");
                    pw.println("\n");
                    TimeUnit.SECONDS.sleep(1);
                    
                    while(cal.getQ() < (cal.getN1()+cal.getN2())/2){
                        
                        System.out.println("MODO: ENCHER\n");
                        pw.println("\n");
                        pw.println("MODO: ENCHER\n");
                        pw.println("\n");
                        System.out.println("MENSAGEM: enchendo a Caldeira...");
                        pw.println("MENSAGEM: enchendo a Caldeira...");
                        bom.setEstado(true);
                        System.out.println("MENSAGEM: nivel da caldeira: " + cal.getQ() + 'L' + "\n");
                        pw.println("MENSAGEM: nivel da caldeira: " + cal.getQ() + 'L' + "\n");
                        cal.setV((cal.getQ() + bom.getP())/100);
                        cal.setQ(cal.getQ() + bom.getP());
                        TimeUnit.SECONDS.sleep(1);
                 
                    }
                    pw.println("\n");
                    pw.flush();
                    bom.setEstado(false);
                     }catch(InterruptedException ex){}
                    break;
                
                case "DEGRADADO":
                    try{
                    System.out.println("MENSAGEM: nivel de agua ok, mas foi detectado um problema na bomba ou no sensor de vapor..\n");
                    pw.println("MENSAGEM: nivel de agua ok, mas foi detectado um problema na bomba ou no sensor de vapor..\n");
                    System.out.println("MODO: " + modo_operacao + "\n");
                    pw.println("\n");
                    pw.println("MODO: " + modo_operacao + "\n");
                    pw.println("\n");
                    TimeUnit.SECONDS.sleep(1);
                    //conserta as coisas
                    System.out.println("MENSAGEM: iniciando reparos...");
                    pw.println("MENSAGEM: iniciando reparos...");
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println("MENSAGEM: reparos feitos com sucesso.");
                    pw.println("MENSAGEM: reparos feitos com sucesso.");
                    cal.setFuncionando_sensor_vapor(true);
                    bom.setDefeito(false);
                    System.out.println("MENSAGEM: voltando pro modo normal...\n");
                    pw.println("MENSAGEM: voltando pro modo normal...\n");
                    pw.println("\n");
                    pw.flush();
                     }catch(InterruptedException ex){}
                    break;
                
                case "NORMAL":
		    		try{
                    System.out.println("MODO: " + modo_operacao + "\n");
                    pw.println("MODO: " + modo_operacao + "\n");
                    pw.println("\n");
                    System.out.println("QUALTIDADE DE AGUA: " + cal.getQ() + 'L');
                    pw.println("QUANTIDADE DE AGUA: " + cal.getQ() + 'L');
                    System.out.println("QUANTIDADE DE VAPOR: " + cal.getV() + " L/s");
                    pw.println("QUANTIDADE DE VAPOR: " + cal.getV() + " L/s");
                    System.out.println("STATUS DA BOMBA: OK");
                    pw.println("STATUS DA BOMBA: OK");
                    System.out.println("STATUS DO SENSOR DE NIVEL: OK");
                    pw.println("STATUS DO SENSOR DE NIVEL: OK");
                    System.out.println("STATUS DO SENSOR DE VAPOR: OK\n");
                    pw.println("STATUS DO SENSOR DE VAPOR: OK\n");
                    TimeUnit.SECONDS.sleep(1);
		    cal.setQ(cal.getQ()-cal.getV());	
                    pw.println("\n");
                    pw.flush();
                     }catch(InterruptedException ex){}
                    break;

                case "PARADA DE EMERGENCIA":
                    try{
                    System.out.println("MENSAGEM: detectado problema em um ou mais dispositivos.\n");
                    pw.println("MENSAGEM: detectado problema em um ou mais dispositivos.\n");
                    TimeUnit.SECONDS.sleep(1);
                    pw.println("\n");
                    System.out.println("MODO: " + modo_operacao + "\n");
                    pw.println("MODO: " + modo_operacao);
                    pw.println("\n");
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("MENSAGEM: iniciando reparos...");
                    pw.println("MENSAGEM: iniciando reparos...");
                    //conserta as coisas
                    cal.setFuncionando_sensor_agua(true);
                    TimeUnit.SECONDS.sleep(2);
                    cal.setFuncionando_sensor_vapor(true);
                    TimeUnit.SECONDS.sleep(2);
                    bom.setDefeito(false);
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println("MENSAGEM: programa recuperado, reiniciando atividades...\n");
                    pw.println("MENSAGEM: programa recuperado, reiniciando atividades...\n");
                    TimeUnit.SECONDS.sleep(1);
                    pw.println("\n");
                    pw.flush();
                     }catch(InterruptedException ex){}
                    break;
                
                case "RECUPERACAO":
                    try{
                    System.out.println("MENSAGEM: detectado problema no sensor de nivel de agua...\n");
                    pw.println("MENSAGEM: detectado problema no sensor de nivel de agua...\n");
                    TimeUnit.SECONDS.sleep(1);
                    pw.println("\n");
                    System.out.println("MODO: " + modo_operacao + "\n");
                    pw.println("MODO: " + modo_operacao + "\n");
                    pw.println("\n");
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("MENSAGEM: iniciando reparos...");
                    pw.println("MENSAGEM: iniciando reparos...");
                    TimeUnit.SECONDS.sleep(2);
		    //conserta sensor da agua
                    cal.setFuncionando_sensor_agua(true);
                    System.out.println("MENSAGEM: reparos finalizados.");
                    pw.println("MENSAGEM: reparos finalizados.");
                    TimeUnit.SECONDS.sleep(1);  
                    pw.println("\n");
                    pw.flush();
                    }catch(InterruptedException ex){}
                    break;
                    
                default:
                    /////////////////////////
                }
            }
        	 
       
        }//fim do run
       };//fim da thread
    realtimeThread1.start();
    realtimeThread2.start();
   }
  }
