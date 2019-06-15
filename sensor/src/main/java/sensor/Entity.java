package sensor;

import com.fasterxml.jackson.annotation.JsonProperty;

//POJO class para las entidades del context broker
public class Entity{

	@JsonProperty("id")
	private String id;
	@JsonProperty("type")
    private String type;
	@JsonProperty("latitud")
    private Attribute latitud;
	@JsonProperty("longitud")
	private Attribute longitud;
	@JsonProperty("altitud")
    private Attribute altitud;
	@JsonProperty("velocidad")
    private Attribute velocidad;
	@JsonProperty("direccion")
    private Attribute direccion;
	@JsonProperty("subida")
    private Attribute subida;
	@JsonProperty("rx")
    private Attribute rx;
	@JsonProperty("ry")
	private Attribute ry;
	@JsonProperty("fecha")
    private Attribute fecha;
	@JsonProperty("hora")
    private Attribute hora;
    
    public Entity(){}
    
    public Entity(Attribute fecha, Attribute hora, String id, Attribute latitud, Attribute longitud, Attribute altitud, 
    		Attribute velocidad, Attribute direccion, Attribute subida, Attribute rx, Attribute ry, String type){
    this.id=id;
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
	this.type=type;
    }

    public String getId(){
	return id;
    }
    public void setId(String id){
    	this.id=id;
    }
    
    public Attribute getLatitud(){
	return latitud;
    }
    public void setLatitud(Attribute latitud){
    	this.latitud=latitud;
    }

    public Attribute getLongitud(){
	return longitud;
    }
    public void setLongitud(Attribute longitud){
    	this.longitud=longitud;
    }
    
    public Attribute getAltitud(){
	return altitud;
    }
    public void setAltitud(Attribute altitud){
    	this.altitud=altitud;
    }
    
    public Attribute getVelocidad(){
	return velocidad;
    }
    public void setVelocidad(Attribute velocidad){
    	this.velocidad=velocidad;
    }
    
    public Attribute getDireccion(){
	return direccion;
    }
    public void setDireccion(Attribute direccion){
    	this.direccion=direccion;
    }
    
    public Attribute getSubida(){
	return subida;
    }
    public void setSubida(Attribute subida){
    	this.subida=subida;
    }
    
    public Attribute getRx(){
	return rx;
    }
    public void setRx(Attribute rx){
    	this.rx=rx;
    }
    
    public Attribute getRy(){
	return ry;
    }
    public void setRy(Attribute ry){
    	this.ry=ry;
    }
    
    public Attribute getFecha(){
	return fecha;
    }
    public void setFecha(Attribute fecha){
    	this.fecha=fecha;
    }

    public Attribute getHora(){
	return hora;
    }
    public void setHora(Attribute hora){
    	this.hora=hora;
    }
    
    public String getType(){
	return type;
    }
    public void setType(String type){
    	this.type=type;
    }
    
}
