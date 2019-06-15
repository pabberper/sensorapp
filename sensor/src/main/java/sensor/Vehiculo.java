package sensor;

//POJO class para la tabla vehiculos
public class Vehiculo {
	private String matricula;
	private String tipo;
    
    public Vehiculo(){}

    public Vehiculo(String matricula, String tipo){
	this.matricula=matricula;
	this.tipo=tipo;
    }
    
    public String getMatricula(){
	return matricula;
    }
    public void setMatricula(String matricula){
    	this.matricula=matricula;
    }
    
    public String getTipo(){
	return tipo;
    }
    public void setTipo(String tipo){
    	this.tipo=tipo;
    }
    
}
