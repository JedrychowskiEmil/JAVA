package pl.ej.papierpicker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.ej.papierpicker.dao.MainDAO;
import pl.ej.papierpicker.entity.Propozycja;
import pl.ej.papierpicker.entity.Student;
import pl.ej.papierpicker.entity.Temat;
import pl.ej.papierpicker.entity.Users;
import pl.ej.papierpicker.user.ListaProfesorow;
import pl.ej.papierpicker.user.ProponowanyTemat;

@Service
public class MainServiceImpl implements MainService {

	// need to inject customer dao
	@Autowired
	private MainDAO mainDAO;

	@Override
	@Transactional
	public List<Student> getStudents() {
		return mainDAO.getStudents();
	}

	@Override
	@Transactional
	public List<Users> getUsers() {
		return mainDAO.getUsers();
	}
	
	@Override
	@Transactional
	public void saveStudent(Student student) {
		mainDAO.saveStudent(student);
	}
	
	@Override
	@Transactional
	public void deleteStudent(int theId) {
		
		mainDAO.deleteStudent(theId);
	}
	
	@Override
	@Transactional
	public Student getStudent(int theId) {
		
		return mainDAO.getStudent(theId);
	}
	
	@Override
	@Transactional
	public Student getStudentByUsername(String name) {
		
		return mainDAO.getStudentByUsername(name);
	}
	
	@Override
	@Transactional
	public void saveTemat(Temat temat) {
		mainDAO.saveTemat(temat);
		
	}
	
	@Override
	@Transactional
	public List<Temat> getTemats() {
		
		return mainDAO.getTemats();
	}
	
	@Override
	@Transactional
	public void deleteTemat(int theId) {
		mainDAO.deleteTemat(theId);
		
	}
	
	
	@Override
	@Transactional
	public List<Temat> getTematsById(int theId) {
		return mainDAO.getTematsById(theId);
	}
	

	
	@Override
	@Transactional
	public Temat getTematbyId(int theId) {
		return mainDAO.getTematById(theId);
	}
	
	@Override
	@Transactional
	public ListaProfesorow getSortedListOfProfesors() {
		return mainDAO.getSortedListOfProfesors();
	}
	
	@Override
	@Transactional
	public Temat getTematByStudentId(int theId) {
		
		return mainDAO.getTematByStudentId(theId);
	}
	
	@Override
	@Transactional
	public void deleteTematByProfesor(int theId) {
		mainDAO.deleteTematByProfesor(theId);
		
	}
	
	@Override
	@Transactional
	public void savePropozycja(Propozycja propozycja) {
		mainDAO.savePropozycja(propozycja);
		
	}
	
	@Override
	@Transactional
	public List<ProponowanyTemat> getProponowaneByStudent(int theId){
		return mainDAO.getProponowaneByStudent(theId);
	}
	
	@Override
	@Transactional
	public void deletePropozycja(int theId) {
		mainDAO.deletePropozycja(theId);
	}
	
	@Override
	@Transactional
	public List<ProponowanyTemat> getProponowaneByProfesor(int theId) {
		return mainDAO.getProponowaneByProfesor(theId);
	}
	
	@Override
	@Transactional
	public Propozycja getPropozycjaById(int theId) {
		return mainDAO.getPropozycjaById(theId);
	}
}





