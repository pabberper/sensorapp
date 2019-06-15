package sensor;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

public class DataBase {
	
	public void insertarDatos(Notification notificacion){
		
		Dato nuevo=null;
		Entity entity=null;
		
	    //Comenzamos una transacción con la base de datos
		SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		/*Necesitamos un bucle para que guarde todos las entidades de la notificacion inicial*/
		for(Iterator<Entity> i=notificacion.getData().iterator();i.hasNext();) {
			
			entity=i.next();
			nuevo = new Dato((String) entity.getId(),
					((Number) entity.getLatitud().getValue()).doubleValue(),
					((Number) entity.getLongitud().getValue()).doubleValue(),
					((Number) entity.getAltitud().getValue()).doubleValue(),
					((Number) entity.getVelocidad().getValue()).doubleValue(),
					((Number) entity.getDireccion().getValue()).doubleValue(),
					((Number) entity.getSubida().getValue()).doubleValue(),
					((Number) entity.getRx().getValue()).doubleValue(),
					((Number) entity.getRy().getValue()).doubleValue(),
					(String) entity.getFecha().getValue(),
					(String) entity.getHora().getValue()
					);
			
			//Nos aseguramos de que la Matricula se encuentra en la BD (es clave externa)
			Query q =session.createQuery("FROM Vehiculo V WHERE V.matricula= :mat_id");
			q.setParameter("mat_id", entity.getId());
			List resultados = q.list();
			Iterator iterator = resultados.iterator();
			//Si no se encuentra la matrícula en la BD...
			if (!iterator.hasNext()) {	
				//...la introducimos en la BD
				Vehiculo nueva = new Vehiculo(entity.getId(), entity.getType());
				session.save(nueva);
			}
			
			//Introducimos la nueva información en la BD
			session.save(nuevo);
		}
		
		session.getTransaction().commit(); //Terminamos transacción
		session.close(); //Cerramos sesión
		sessionFactory.close(); //Cerramos conexiones con BD
	}
	
	public List<Tipo> consultarTipos(String tipo, Integer vmax){
		
        //Comenzamos una transacción con la base de datos
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		//Configuramos los parámetros necesarios para las consultas dinámicas
		Criteria cr = session.createCriteria(Tipo.class);
		
		//Añadimos las condiciones dinámicas según los parámetros introducidos
		if(tipo!=null) {
			cr.add(Restrictions.eq("tipo", tipo));
		}
		if(vmax!=null) {
			cr.add(Restrictions.eq("vmax", vmax));
		}
		List<Tipo> datos = cr.list();
		
		session.getTransaction().commit(); //Terminamos transacción
		session.close(); //Cerramos sesión
		sessionFactory.close(); //Cerramos conexiones con BD
		
		return datos;
	}
	
	public List<Vehiculo> consultarVehiculos(String mat, String tipo){
		
        //Comenzamos una transacción con la base de datos
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		//Configuramos los parámetros necesarios para las consultas dinámicas
		Criteria cr = session.createCriteria(Vehiculo.class);
		
		//Añadimos las condiciones dinámicas según los parámetros introducidos
		if(mat!=null) {
			cr.add(Restrictions.eq("matricula", mat));
		}
		if(tipo!=null) {
			cr.add(Restrictions.eq("tipo", tipo));
		}
		List<Vehiculo> datos = cr.list();
		
		session.getTransaction().commit(); //Terminamos transacción
		session.close(); //Cerramos sesión
		sessionFactory.close(); //Cerramos conexiones con BD
		
		return datos;
	}
	
	public List<Dato> consultarDatos(Long id, String mat, Double lat, Double lon, Double alt, 
			Double vel, Double dir, Double sub, Double rx, Double ry, String fecha, String hora){
		
        //Comenzamos una transacción con la base de datos
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		//Configuramos los parámetros necesarios para las consultas dinámicas
		Criteria cr = session.createCriteria(Dato.class);
		
		//Añadimos las condiciones dinámicas según los parámetros introducidos
		if(id!=null) {
			cr.add(Restrictions.eq("id", id));
		}
		if(mat!=null) {
			cr.add(Restrictions.eq("matricula", mat));
		}
		if(lat!=null) {
			cr.add(Restrictions.eq("latitud", lat));
		}
		if(lon!=null) {
			cr.add(Restrictions.eq("longitud", lon));
		}
		if(alt!=null) {
			cr.add(Restrictions.eq("altitud", alt));
		}
		if(vel!=null) {
			cr.add(Restrictions.eq("velocidad", vel));
		}
		if(dir!=null) {
			cr.add(Restrictions.eq("direccion", dir));
		}
		if(sub!=null) {
			cr.add(Restrictions.eq("subida", sub));
		}
		if(rx!=null) {
			cr.add(Restrictions.eq("rx", rx));
		}
		if(ry!=null) {
			cr.add(Restrictions.eq("ry", ry));
		}
		if(fecha!=null) {
			cr.add(Restrictions.eq("fecha", fecha));
		}
		if(hora!=null) {
			cr.add(Restrictions.eq("hora", hora));
		}
		List<Dato> datos = cr.list();
		
		session.getTransaction().commit(); //Terminamos transacción
		session.close(); //Cerramos sesión
		sessionFactory.close(); //Cerramos conexiones con BD
		
		return datos;
	}
	
	public List<Dato> consultarViaje(Long idInicio, Long idFin, String mat){
		
	    //Comenzamos una transacción con la base de datos
		SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		//Configuramos los parámetros necesarios para las consultas dinámicas
		Criteria cr = session.createCriteria(Dato.class);
		cr.add(Restrictions.eq("matricula", mat));
		
		//Añadimos las condiciones dinámicas según los parámetros introducidos
		if(idInicio!=null) {
			cr.add(Restrictions.ge("id", idInicio));
		}
		if(idFin!=null) {
			cr.add(Restrictions.le("id", idFin));
		}

		List<Dato> resultados = cr.list();
			
		session.getTransaction().commit(); //Terminamos transacción
		session.close(); //Cerramos sesión
		sessionFactory.close(); //Cerramos conexiones con BD
		
		return resultados;
	}
	
	public Double consultarVmax(String mat){
		
		Double vmax=0.0;
		
	    //Comenzamos una transacción con la base de datos
		SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
			
		//Realizamos la consulta
		Query q =session.createQuery("FROM Tipo T WHERE T.tipo IN (SELECT V.tipo FROM Vehiculo V WHERE V.matricula= :mat_id)");
		q.setParameter("mat_id", mat);
		List<Tipo> resultados = q.list();
		Iterator iterator = resultados.iterator();
		
		if (iterator.hasNext()) {
			vmax=((Number) resultados.iterator().next().getVmax()).doubleValue();
		}
			
		session.getTransaction().commit(); //Terminamos transacción
		session.close(); //Cerramos sesión
		sessionFactory.close(); //Cerramos conexiones con BD
		
		return vmax;
	}
	
	//Para servidor de velocidad maxima
	public int consultarVmaxTipo(String tipo){
		
		int vmax=0;
		
	    //Comenzamos una transacción con la base de datos
		SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
			
		//Realizamos la consulta
		Query q =session.createQuery("FROM Tipo T WHERE T.tipo= :tipo_v");
		q.setParameter("tipo_v", tipo);
		List<Tipo> resultados = q.list();
		Iterator iterator = resultados.iterator();
		
		if (iterator.hasNext()) {
			vmax=resultados.iterator().next().getVmax();
		}
			
		session.getTransaction().commit(); //Terminamos transacción
		session.close(); //Cerramos sesión
		sessionFactory.close(); //Cerramos conexiones con BD
		
		return vmax;
	}
	
	public boolean eliminarDatos(String mat) {
		
		boolean elim = false;
				
	    //Comenzamos una transacción con la base de datos
		SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();		
		
		//Buscamos las tuplas que queremos borrar
		//Configuramos los parámetros necesarios para las consultas dinámicas
		Criteria cd = session.createCriteria(Dato.class);
		cd.add(Restrictions.eq("matricula", mat));
		Criteria cv = session.createCriteria(Vehiculo.class);
		cv.add(Restrictions.eq("matricula", mat));
		
		List<Dato> datos=cd.list();
		List<Vehiculo> vehiculo=cv.list();
		Dato dato;
		
		//Se borran todos los datos para la matricula dada
		for(Iterator<Dato> i=datos.iterator();i.hasNext();) {
			dato=i.next();
			session.delete(dato);
		}
		
		//Despues de haber borrado los datos (la matricula es clave externa)...
		if (vehiculo.iterator().hasNext()) {
			//... se borra el vehiculo
			session.delete(vehiculo.iterator().next()); 
			elim=true;
		}
		
		session.getTransaction().commit(); //Terminamos transacción
		session.close(); //Cerramos sesión
		sessionFactory.close(); //Cerramos conexiones con BD
		
		return elim;
	}
	
}
