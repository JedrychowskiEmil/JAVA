package pl.ej.papierpicker.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.ej.papierpicker.entity.Authorities;
import pl.ej.papierpicker.entity.Propozycja;
import pl.ej.papierpicker.entity.Student;
import pl.ej.papierpicker.entity.Temat;
import pl.ej.papierpicker.entity.Users;
import pl.ej.papierpicker.user.ListaProfesorow;
import pl.ej.papierpicker.user.ProfesorTematy;
import pl.ej.papierpicker.user.ProponowanyTemat;

@Repository
public class MainDAOImpl implements MainDAO {

	// need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;
	
	
	public List<Student> getStudents() {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
				
		// create a query  ... sort by last name
		Query<Student> theQuery = 
				currentSession.createQuery("from Student",
											Student.class);
		
		// execute query and get result list
		List<Student> students = theQuery.getResultList();
				
		// return the results		
		return students;
	}

	@Override
	public List<Users> getUsers() {
		// get the current hibernate session
				Session currentSession = sessionFactory.getCurrentSession();
						
				// create a query  ... sort by last name
				Query<Users> theQuery = 
						currentSession.createQuery("from Users",
													Users.class);
				
				// execute query and get result list
				List<Users> users = theQuery.getResultList();
						
				// return the results		
				return users;
	}
	
	
	@Override
	public void saveStudent(Student student) {

		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// save/upate the customer ... finally LOL
		currentSession.saveOrUpdate(student);
		
	}

	@Override
	public void deleteStudent(int theId) {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// delete object with primary key
		Query theQuery = currentSession.createQuery("delete from Student where id=:studentId");
		
		theQuery.setParameter("studentId", theId);
		
		theQuery.executeUpdate();	
	}
	
	@Override
	public Student getStudent(int theId) {

		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// now retrieve/read from database using the primary key
		Student student = currentSession.get(Student.class, theId);
		
		return student;
	}

	@Override
	public Student getStudentByUsername(String name) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query<Student> theQuery = 
				currentSession.createQuery("from Student where users.username=:name",
											Student.class);
		theQuery.setParameter("name", name);
		
		// execute query
		//Student student = currentSession.get(Student.class, theQuery.getFirstResult());
		Student student;
		student = theQuery.getSingleResult();
		return student;
	}

	@Override
	public void saveTemat(Temat temat) {
		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// save/upate the customer ... finally LOL
		currentSession.saveOrUpdate(temat);
		
	}
	
	@Override
	public List<Temat> getTemats() {
		// get the current hibernate session
				Session currentSession = sessionFactory.getCurrentSession();
						
				// create a query  ... sort by last name
				Query<Temat> theQuery = 
						currentSession.createQuery("from Temat",
													Temat.class);
				
				// execute query and get result list
				List<Temat> temats = theQuery.getResultList();
						
				// return the results		
				return temats;
	}
	
	@Override
	public void deleteTemat(int theId) {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// delete object with primary key
		Query theQuery = currentSession.createQuery("delete from Temat where id=:tematId");
		
		theQuery.setParameter("tematId", theId);
		
		theQuery.executeUpdate();	
		
	}
	
	@Override
	public void deleteTematByProfesor(int theId) {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// delete object with primary key
		Query theQuery = currentSession.createQuery("delete from Temat where profesor=:theId");
		
		theQuery.setInteger("theId", theId);
		
		theQuery.executeUpdate();	
		
	}
	
	

	@Override
	public List<Temat> getTematsById(int theId) {
		
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query<Temat> theQuery =
				currentSession.createQuery("from Temat where profesor=:theId", Temat.class);

		theQuery.setInteger("theId", theId);
			
		// execute query and get result list
		List<Temat> temats = theQuery.getResultList();
				
		// return the results		
		return temats;
	}
	
	@Override
	public Temat getTematById(int theId) {
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// now retrieve/read from database using the primary key
		Temat temat = currentSession.get(Temat.class, theId);
		
		return temat;
	}
	
	
	
	@Override
	public ListaProfesorow getSortedListOfProfesors() {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query<Student> theQuery =
				currentSession.createQuery("select distinct profesor from Temat", Student.class);
		List<Student> listaIdOfProfessors = theQuery.getResultList();
		
		List<ProfesorTematy> profesorTematies = new ArrayList<ProfesorTematy>();
		

		
		for (Student i : listaIdOfProfessors) {
			
			List<Temat> tematy = getTematsById(i.getId());
			
			ProfesorTematy profesorTematy = new ProfesorTematy();
			profesorTematy.setProfesor(i);
			
			profesorTematy.setTematy(tematy);
			profesorTematies.add(profesorTematy);
		}
		
		//Dodaj tych profesorow ktorze nie dodali jeszcze tematu
			//usun z listy tych ktorzy dodali
		List<Student> allUsers = getStudents();
		allUsers.removeAll(listaIdOfProfessors);
		
			//dodaj reszte
		for(Student i : allUsers) {
			List<Authorities> roles = i.getUsers().getAuthorities();
			for(Authorities j: roles) {
				if(j.getAuthority().compareTo("ROLE_PROFESOR") == 0) {
						ProfesorTematy profesorTematy = new ProfesorTematy();
						profesorTematy.setProfesor(i);
						profesorTematies.add(profesorTematy);
				}
			}
		}
		
		ListaProfesorow listaProfesorow = new ListaProfesorow();
		listaProfesorow.setProfesorowie(profesorTematies);
		
		return listaProfesorow;
	}
	
	
	@Override
	public Temat getTematByStudentId(int theId) {
			
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query<Temat> theQuery = currentSession.createQuery("from Temat where student=:theId", Temat.class);
		theQuery.setInteger("theId", theId);
		
		Temat temat = null;
		try{
			temat = theQuery.getSingleResult();
		}catch(NoResultException e) {}
		
		
		if(temat == null) {
			temat = new Temat();
		}
		
		
		return temat;
	}
	
	@Override
	public void savePropozycja(Propozycja propozycja) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(propozycja);	
	}
	
	@Override
	public List<ProponowanyTemat> getProponowaneByStudent(int theId) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query<Propozycja> theQuery = currentSession.createQuery("from Propozycja where student=:theId", Propozycja.class);
		theQuery.setInteger("theId", theId);
		
		List<Propozycja> listaWszystkich = theQuery.getResultList();
		
		List<ProponowanyTemat> listaWszystkichProponowanychStudenta = new ArrayList<ProponowanyTemat>();
		

		
		for (Propozycja i : listaWszystkich) {
			ProponowanyTemat proponowanyTemat = new ProponowanyTemat();
			proponowanyTemat.setTemat(i);
			proponowanyTemat.setProfesor(getStudent(i.getProfesor().getId()));
			
			listaWszystkichProponowanychStudenta.add(proponowanyTemat);
		}
		
		
		return listaWszystkichProponowanychStudenta;
	}
	
	@Override
	public List<ProponowanyTemat> getProponowaneByProfesor(int theId) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query<Propozycja> theQuery = currentSession.createQuery("from Propozycja where profesor=:theId", Propozycja.class);
		theQuery.setInteger("theId", theId);
		
		List<Propozycja> listaWszystkich = theQuery.getResultList();
		
		List<ProponowanyTemat> listaWszystkichProponowanychStudenta = new ArrayList<ProponowanyTemat>();
		

		
		for (Propozycja i : listaWszystkich) {
			ProponowanyTemat proponowanyTemat = new ProponowanyTemat();
			proponowanyTemat.setTemat(i);
			proponowanyTemat.setProfesor(getStudent(i.getStudent().getId()));
			
			listaWszystkichProponowanychStudenta.add(proponowanyTemat);
		}
		
		
		return listaWszystkichProponowanychStudenta;
	}
	
	
	
	@Override
	public void deletePropozycja(int theId) {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// delete object with primary key
		Query theQuery = currentSession.createQuery("delete from Propozycja where id=:tematId");
		
		theQuery.setParameter("tematId", theId);
		
		theQuery.executeUpdate();	
		
	}
	
	@Override
	public Propozycja getPropozycjaById(int theId) {
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// now retrieve/read from database using the primary key
		Propozycja propozycja = currentSession.get(Propozycja.class, theId);
		
		return propozycja;
	}

}











