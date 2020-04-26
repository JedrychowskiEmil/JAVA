package pl.ej.papierpicker.dao;

import java.util.List;

import pl.ej.papierpicker.entity.Propozycja;
import pl.ej.papierpicker.entity.Student;
import pl.ej.papierpicker.entity.Temat;
import pl.ej.papierpicker.entity.Users;
import pl.ej.papierpicker.user.ListaProfesorow;
import pl.ej.papierpicker.user.ProponowanyTemat;

public interface MainDAO {
	
	public List<Student> getStudents();
	
	public List<Users> getUsers();
	
	public void saveStudent(Student student);
	
	public void deleteStudent(int theId);
	
	public Student getStudent(int theId);
	
	public Student getStudentByUsername(String username);
	
	public void saveTemat (Temat temat);
	
	public List<Temat> getTemats();

	public void deleteTemat(int theId);

	public List<Temat> getTematsById(int theId);

	public Temat getTematById(int theId);
	
	public ListaProfesorow getSortedListOfProfesors();
	
	public Temat getTematByStudentId(int theId);

	void deleteTematByProfesor(int theId);
	
	public void savePropozycja(Propozycja propozycja);

	public List<ProponowanyTemat> getProponowaneByStudent(int theId);

	void deletePropozycja(int theId);

	List<ProponowanyTemat> getProponowaneByProfesor(int theId);

	Propozycja getPropozycjaById(int theId);
	
}
