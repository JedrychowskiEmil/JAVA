package pl.ej.papierpicker.service;

import java.util.List;

import pl.ej.papierpicker.entity.Propozycja;
import pl.ej.papierpicker.entity.Student;
import pl.ej.papierpicker.entity.Temat;
import pl.ej.papierpicker.entity.Users;
import pl.ej.papierpicker.user.ListaProfesorow;
import pl.ej.papierpicker.user.ProponowanyTemat;

public interface MainService {

	public List<Student> getStudents();
	
	public List<Users> getUsers();
	
	public void saveStudent(Student student);
	
	public void deleteStudent(int theId);
	
	public Student getStudent(int theId);
	
	public Student getStudentByUsername(String name); 
	
	public void saveTemat(Temat temat);
	
	public List<Temat> getTemats();
	
	public List<Temat> getTematsById(int theId);
	
	public void deleteTemat(int theId);

	public Temat getTematbyId(int theId);
	
	public ListaProfesorow getSortedListOfProfesors();
	
	public Temat getTematByStudentId(int theId);
	
	void deleteTematByProfesor(int theId);
	
	public void savePropozycja(Propozycja propozycja);
	
	public List<ProponowanyTemat> getProponowaneByStudent(int theId);

	public void deletePropozycja(int theId);

	List<ProponowanyTemat> getProponowaneByProfesor(int theId);

	Propozycja getPropozycjaById(int theId);
}

