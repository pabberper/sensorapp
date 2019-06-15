package sensor;

//POJO class para la tabla datos
public class Dato{

	private long id;
	private String matricula;
    private double latitud;
    private double longitud;
    private double altitud;
    private double velocidad;
    private double direccion;
    private double subida;
    private double rx;
    private double ry;
    private String fecha;
    private String hora;
    
    public Dato(){}

    public Dato(String matricula, double latitud, double longitud, double altitud, 
    		double velocidad, double direccion, double subida, double rx, double ry, String fecha, String hora){
	this.matricula=matricula;
	this.latitud=latitud;
	this.longitud=longitud;
	this.altitud=altitud;
	this.velocidad=velocidad;
	this.direccion=direccion;
	this.subida=subida;
	this.rx=rx;
	this.ry=ry;
	this.fecha=fecha;
	this.hora=hora;
    }
    
    public Dato(long id, String matricula, double latitud, double longitud, double altitud, 
    		double velocidad, double direccion, double subida, double rx, double ry, String fecha, String hora){
    this.id=id;
	this.matricula=matricula;
	this.latitud=latitud;
	this.longitud=longitud;
	this.altitud=altitud;
	this.velocidad=velocidad;
	this.direccion=direccion;
	this.subida=subida;
	this.rx=rx;
	this.ry=ry;
	this.fecha=fecha;
	this.hora=hora;
    }

    public long getId(){
	return id;
    }
    public void setId(long id){
    	this.id=id;
    }
    
    public String getMatricula(){
	return matricula;
    }
    public void setMatricula(String matricula){
    	this.matricula=matricula;
    }
    
    public double getLatitud(){
	return latitud;
    }
    public void setLatitud(double latitud){
    	this.latitud=latitud;
    }
    
    public double getLongitud(){
	return longitud;
    }
    public void setLongitud(double longitud){
    	this.longitud=longitud;
    }
    
    public double getAltitud(){
	return altitud;
    }
    public void setAltitud(double altitud){
    	this.altitud=altitud;
    }
    
    public double getVelocidad() {
		return velocidad;
	}
	public void setVelocidad(double velocidad) {
		this.velocidad=velocidad;
	}

	public double getDireccion() {
		return direccion;
	}
	public void setDireccion(double direccion) {
		this.direccion=direccion;
	}

	public double getSubida() {
		return subida;
	}
	public void setSubida(double subida) {
		this.subida=subida;
	}

	public double getRx(){
	return rx;
    }
    public void setRx(double rx){
    	this.rx=rx;
    }
    
    public double getRy(){
	return ry;
    }
    public void setRy(double ry){
    	this.ry=ry;
    }

    public String getFecha(){
	return fecha;
    }
    public void setFecha(String fecha){
    	this.fecha=fecha;
    }

    public String getHora(){
	return hora;
    }
    public void setHora(String hora){
    	this.hora=hora;
    }

}
