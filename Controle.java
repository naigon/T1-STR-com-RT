
public class Controle{

        public int verifica_nivel_vapor(Caldeira c){
        return c.getV();
        }

    public int verifica_nivel_agua(Caldeira c){
        return c.getQ();
        }

    public int verifica_sensores(Caldeira c, Bomba b){//testa os dois sensores e se há defeiro na bomba
        if(c.getFuncionando_sensor_agua()==true && c.getFuncionando_sensor_vapor()==true && b.getDefeito()==false)
            return 1;//tudo funcionando
        else return -1;//defeito encontrado em um ou mais deles
        }
    
	/*metodo que recebe os objetos bomba e caldeira e faz as medições dos sensores e o status da bomba, com isso devolte para a planta
	o modo de operação correspondente de acordo com a situação dos dispositivos e das medições*/
    public String start(Caldeira c,Bomba b) throws InterruptedException{ 
    	
	String modo=" ";
       
        //inicio: se caldeira nao tem agua nem vapor e os dispositivos estiverem funcionando ela enche e começa a funcionar
       	if(verifica_nivel_agua(c)<=c.getN2() && verifica_nivel_agua(c)>=c.getN1() && verifica_sensores(c,b)==1){
           		modo="NORMAL";
           	}
        //caldeira vazia ou nivel abaixo do normal
        if(verifica_nivel_agua(c)==0 || verifica_nivel_agua(c)<=c.getN1()) {
               
                 if(verifica_sensores(c,b)==1)
                 	modo="ENCHER";
            	 else
            	 	modo="PARADA DE EMERGENCIA";
            }

	
	    if(verifica_nivel_agua(c)>=c.getN2() && verifica_sensores(c,b)==1){

            if(verifica_sensores(c,b)==1)
            	modo="ESVAZIAR";
            else
            	modo="PARADA DE EMERGENCIA";
            }
          //nivel de agua ideal mas com defeito no medidor de agua
        if(c.getFuncionando_sensor_agua()==false){
       
                modo="RECUPERACAO";
                if(c.getQ()>c.getM2() || c.getQ()<c.getM1())
                	modo="PARADA DE EMERGENCIA";                
        }
        //se o nivel de agua está ok, mas alguma das unidades fisicas apresentar defeito, vai pro modo degradado
        if (verifica_nivel_agua(c)<c.getN2() && verifica_nivel_agua(c)>c.getN1() && c.getFuncionando_sensor_agua()==true ){
            modo="NORMAL";
          	if(c.getFuncionando_sensor_vapor()==false || b.getDefeito()==true)
          		modo="DEGRADADO";	
          }
    return modo;
	}// fim do metodo start
	

}

        
    

