package sensor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.json.JSONObject;

@RestController
public class SensorController {
	
	//Se obtienen las direcciones y puertos del contex broker y la propia aplicacion web
	@Value("${ip_port_sensorapp}")
	private String ip_port_sensorapp;
	@Value("${ip_port_contextbroker}")
	private String ip_port_contextbroker;


	//Consulta la base de datos de los sensores
    @RequestMapping("/sensor/datos")
    public List<Dato> datos(
	  @RequestParam(value="id", required=false) Long id,
      @RequestParam(value="matricula", required=false) String mat,
      @RequestParam(value="latitud", required=false) Double lat,
      @RequestParam(value="longitud", required=false) Double lon,
      @RequestParam(value="altitud", required=false) Double alt,
      @RequestParam(value="velocidad", required=false) Double vel,
      @RequestParam(value="direccion", required=false) Double dir,
      @RequestParam(value="subida", required=false) Double sub,
      @RequestParam(value="rx", required=false) Double rx,
      @RequestParam(value="ry", required=false) Double ry,
      @RequestParam(value="fecha", required=false) String fecha,
      @RequestParam(value="hora", required=false) String hora) {
    	
    	DataBase bd = new DataBase();
    	List<Dato> datos = bd.consultarDatos(id, mat, lat, lon, alt, vel, dir, sub, rx, ry, fecha, hora);
	    
	return datos;
    }
    
	//Consulta la base de datos de los vehiculos
    @RequestMapping("/sensor/vehiculos")
    public List<Vehiculo> vehiculos(
      @RequestParam(value="matricula", required=false) String mat,
      @RequestParam(value="tipo", required=false) String tipo) {
    	
    	DataBase bd = new DataBase();
    	List<Vehiculo> datos = bd.consultarVehiculos(mat, tipo);
	    
	return datos;
    }
    
	//Consulta la base de datos de los tipos de vehiculos
    @RequestMapping("/sensor/tipos")
    public List<Tipo> tipos(
      @RequestParam(value="tipo", required=false) String tipo,
      @RequestParam(value="velocidadMaxima", required=false) Integer vmax) {
    	
    	DataBase bd = new DataBase();
    	List<Tipo> datos = bd.consultarTipos(tipo, vmax);
	    
	return datos;
    }
    
	//Informa sobre un viaje (datos comprendidos entre dos ids de la tabla datos, para una matricula dada)
    @RequestMapping("/sensor/datos_viaje")
    public String datos_viaje(
	  @RequestParam(value="idInicio", required=false) Long idInicio,
	  @RequestParam(value="idFin", required=false) Long idFin,
      @RequestParam(value="matricula", required=true) String mat,
      @RequestParam(value="vmp", required=false) Double vmp) {
    	
    	Dato dato;
    	Double vInst=0.0, vAvg=0.0, vMax=0.0, vSum=0.0;
    	Double hInst=0.0, hAvg=0.0, hMax=0.0, hSum=0.0;
    	Double sInst=0.0, sAvg=0.0, sMax=0.0, sSum=0.0;
    	int count=1;
    	String mensaje="No hay datos de viaje para los valores introducidos";
    	String aviso="";
    	DataBase bd = new DataBase();
    	
    	//Consultamos la base de datos (si no se especifican idInicio e idFin se consultan todos los datos)
    	List<Dato> viaje = bd.consultarViaje(idInicio, idFin, mat);
    	
    	//Si no se ha especificado una velocidad maxima para el viaje...
    	if (vmp == null) {
    		//.. esta sera la maxima permitida para el tipo de vehiculo analizado
    		vmp=bd.consultarVmax(mat);
    	}
    	
    	//Recorremos los datos del viaje
    	for(Iterator<Dato> i=viaje.iterator();i.hasNext();count++) {
    		dato=i.next();
    		//Velocidad media
    		vInst=dato.getVelocidad();
    		vSum += vInst;
    		vAvg=vSum/count;
    		//Velocidad maxima alcanzada
    		if(vInst>vMax) {
    			vMax=vInst;
    		}
    		//Comprobamos si se ha superado la velocidad maxima permitida
    		if(vInst>vmp) {
    			aviso+="<br>Velocidad máxima permitida superada el "+dato.getFecha()+" a las "+dato.getHora()
    					+", en "+dato.getLatitud()+" Norte, "+dato.getLongitud()+" Este"+", con "+vInst+" km/h";
    		}
    		//Altitud media
    		hInst=dato.getAltitud();
    		hSum += hInst;
    		hAvg=hSum/count;
    		//Altitud maxima
    		if(hInst>hMax) {
    			hMax=hInst;
    		}
    		//Velocidad media de subida
    		sInst=dato.getSubida();
    		sSum += sInst;
    		sAvg=sSum/count;
    		//Velocidad maxima de subida, calculamos a partir del valor absoluto
    		if(Math.abs(sInst)>Math.abs(sMax)) {
    			sMax=sInst;
    		}
    	}
    	
    	//Si se han encontrado datos de viaje para los valores introducidos...
    	if(viaje.iterator().hasNext()) {
    		mensaje="La velocidad media del viaje es: "+vAvg+" km/h"
    				+"<br>La velocidad máxima alcanzada es: "+vMax+" km/h"
    				+"<br>La velocidad máxima permitida es: "+vmp+" km/h"
    				+"<br><br>La altitud media del viaje es: "+hAvg+" m (WGS84)"
    				+"<br>La altitud máxima alcanzada es: "+hMax+" m (WGS84)"
    				+"<br><br>La velocidad media de subida del viaje es: "+sAvg+" m/min"
    				+"<br>La velocidad máxima de subida alcanzada es: "+sMax+" m/min"
    				+"<br>"+aviso;
    	}
	    
	return mensaje;
    }
    
    //Recibe notificaciones del context broker e inserta datos en BD GPS
    @PostMapping("/sensor/notificaciones")
    public void notificaciones(
      @RequestBody Notification notificacion){
    	
    	//Introducimos los datos de la notificación en la base de datos
		if (notificacion.getData().iterator().hasNext()) {
			
			DataBase bd= new DataBase();
			bd.insertarDatos(notificacion);
		}
    }
    
    //Cancela una suscripción dada por su id
    @RequestMapping("/sensor/cancelar_suscripcion")
    public String cancelar_suscripcion(
    	@RequestParam(value="subscriptionId") String id){
    	
    	String mensaje = "No se ha podido cancelar la suscripcion: "+id;
    	
    	//Enviamos un mensaje DELETE para cancelar la suscripcion
    	ResteasyClient client = new ResteasyClientBuilder().build();
    	ResteasyWebTarget target = client.target("http://"+ip_port_contextbroker+"/v2/subscriptions/"+id);
    	Response response = target.request().delete();
    	response.close();
    	
    	if(response.getStatus()==204) {
    		mensaje="Se ha cancelado la suscripcion: "+id;
    	}
    	
    	return mensaje;
    }
    
    //Suscribe una matrícula y la inserta en BD
    @RequestMapping("/sensor/suscribir")
    public String suscribir(
    	@RequestParam(value="matricula") String mat) {
        
    	String mensaje = "No se ha podido suscribir a la matrícula: "+mat;
        
    	try {
    	 // Construimos el mensaje de suscripcion a la matricula dada
        JSONObject sub_gps = new JSONObject("{\"description\": \"A subscription to get info about GPS\", "
        		+ "\"subject\": {\"entities\": [{\"id\": \""+mat+"\", \"typePattern\": \".*\"}], "
        		+ "\"condition\": {\"attrs\": [\"latitud\", \"longitud\", \"altitud\"]}}, " //Si cambia alguno se envia notificacion
        		+ "\"notification\": {\"http\": {\"url\": \"http://"+ip_port_sensorapp+"/sensor/notificaciones\"}, "
        		+ "\"attrs\": [], " //Si no se especifican atributos se notifican todos
        		+ "\"attrsFormat\": \"normalized\"}, "
        		+ "\"throttling\": 5}"); //Tiempo de espera tras enviar una notificacion (segundos)

        // URL and parameters for the connection, This particulary returns the information passed
        URL url = new URL("http://"+ip_port_contextbroker+"/v2/subscriptions");
        HttpURLConnection httpConnection  = (HttpURLConnection) url.openConnection();
        httpConnection.setDoOutput(true);
        httpConnection.setRequestMethod("POST");
        httpConnection.setRequestProperty("Content-Type", "application/json");
        httpConnection.setRequestProperty("Accept", "application/json");
        // Not required
        // urlConnection.setRequestProperty("Content-Length", String.valueOf(input.getBytes().length));

        // Writes the JSON parsed as string to the connection
        DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
        wr.write(sub_gps.toString().getBytes());
        Integer responseCode = httpConnection.getResponseCode();
        
        //Si se ha podido crear la suscripcion se devuelve un mensaje de exito
        if(responseCode==201)
        	mensaje = "Se ha suscrito con éxito a la matrícula: "+mat;
    	}
    	catch(Exception e) {
    		
    	}
	    
	return mensaje;
    }
    
    //Se suscribe a todas las matrículas
    @RequestMapping("/sensor/suscribir_todo")
    public String suscribir_todo() {
        
    	String mensaje = "No se ha podido suscribir a todas las matriculas";
        
    	try {
    	 // Construimos el mensaje de suscripcion a todas las matriculas y todos los tipos
        JSONObject sub_gps = new JSONObject("{\"description\": \"A subscription to get info about GPS\", "
        		+ "\"subject\": {\"entities\": [{\"idPattern\": \".*\", \"typePattern\": \".*\"}], "
        		+ "\"condition\": {\"attrs\": [\"latitud\", \"longitud\", \"altitud\"]}}, " //Si cambia alguno se envia notificacion
        		+ "\"notification\": {\"http\": {\"url\": \"http://"+ip_port_sensorapp+"/sensor/notificaciones\"}, "
        		+ "\"attrs\": [], " //Si no se especifican atributos se notifican todos
        		+ "\"attrsFormat\": \"normalized\"}}"); //En este caso no es posible el throttling

        // URL and parameters for the connection, This particulary returns the information passed
        URL url = new URL("http://"+ip_port_contextbroker+"/v2/subscriptions");
        HttpURLConnection httpConnection  = (HttpURLConnection) url.openConnection();
        httpConnection.setDoOutput(true);
        httpConnection.setRequestMethod("POST");
        httpConnection.setRequestProperty("Content-Type", "application/json");
        httpConnection.setRequestProperty("Accept", "application/json");
        // Not required
        // urlConnection.setRequestProperty("Content-Length", String.valueOf(input.getBytes().length));

        // Writes the JSON parsed as string to the connection
        DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
        wr.write(sub_gps.toString().getBytes());
        Integer responseCode = httpConnection.getResponseCode();
        
        //Si se ha podido crear la suscripcion se devuelve un mensaje de exito
        if(responseCode==201)
        	mensaje = "Se ha suscrito con éxito a todas las matrículas";
    	}
    	catch(Exception e) {
    		
    	}
	    
	return mensaje;
    }
    
    //Consulta las suscripciones existentes
    @RequestMapping("/sensor/suscripciones")
    public String suscripciones(){
    	
    	//Consultamos las suscripciones existentes en el context broker mediante peticion GET
    	ResteasyClient client = new ResteasyClientBuilder().build();
    	ResteasyWebTarget target = client.target("http://"+ip_port_contextbroker+"/v2/subscriptions");
    	Response response = target.request().get();
    	String suscripciones = response.readEntity(String.class);
    	response.close();
    	
    return suscripciones;
    }
    
    /*//Elimina de la base de datos la información referente a una matricula dada
    @RequestMapping("/sensor/eliminar_datos")
    public String eliminar_datos(
    	@RequestParam(value="matricula", required=true) String mat){
    	
    	String mensaje = "No se han podido eliminar los datos del vehículo "+mat;
    	
    	DataBase bd = new DataBase();
    	boolean elim=bd.eliminarDatos(mat);
    	
    	if(elim) {
    		mensaje="Los datos del vehículo "+mat+" se han eliminado correctamente";
    	}
    	
    	return mensaje;
    }*/
    
    /*------------------------------------------------------------------------------------------------------- */
    
	//Proporciona vmax a los vehículos que la soliciten (sustituir por servidor de vmax por posicionamiento)
    @RequestMapping("/sensor/vmax")
    public int vmax(
      @RequestParam(value="latitud", required=true) Double lat,
      @RequestParam(value="longitud", required=true) Double lon,
      @RequestParam(value="tipo", required=true) String tipo) {
    	
    	int vmax=0;
    	DataBase bd = new DataBase();
    	vmax=bd.consultarVmaxTipo(tipo);
	    
	return vmax;
    }
    
    /*------------------------------------------------------------------------------------------------------- */
    
	//Pagina principal de la aplicacion
    @RequestMapping("/")
    public ModelAndView home (){
    	
    	ModelAndView mav = new ModelAndView("index.html");
	    
	return mav;
    }
    
	//Interfaz de busqueda de datos
    @RequestMapping("/datos")
    public ModelAndView datos_view(){
    	
    	ModelAndView mav = new ModelAndView("datos.html");
	    
	return mav;
    }
	//Interfaz de busqueda de vehiculos
    @RequestMapping("/vehiculos")
    public ModelAndView vehiculos_view(){
    	
    	ModelAndView mav = new ModelAndView("vehiculos.html");
	    
	return mav;
    }
	//Interfaz de busqueda de tipos de vehiculos
    @RequestMapping("/tipos")
    public ModelAndView tipos_view(){
    	
    	ModelAndView mav = new ModelAndView("tipos.html");
	    
	return mav;
    }
	//Interfaz de consulta de viajes
    @RequestMapping("/viajes")
    public ModelAndView viajes_view(){
    	
    	ModelAndView mav = new ModelAndView("viajes.html");
	    
	return mav;
    }
	//Interfaz de suscripcion de un vehiculo
    @RequestMapping("/suscribir")
    public ModelAndView suscribir_view(){
    	
    	ModelAndView mav = new ModelAndView("suscribir.html");
	    
	return mav;
    }
	//Interfaz de cancelacion de suscripcion
    @RequestMapping("/cancelar_suscripcion")
    public ModelAndView cancelar_suscripcion_view(){
    	
    	ModelAndView mav = new ModelAndView("cancelar_suscripcion.html");
	    
	return mav;
    }
    
}
