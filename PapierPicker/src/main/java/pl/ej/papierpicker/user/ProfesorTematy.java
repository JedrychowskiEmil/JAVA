package pl.ej.papierpicker.user;

import java.util.List;

import pl.ej.papierpicker.entity.Student;
import pl.ej.papierpicker.entity.Temat;

public class ProfesorTematy {

	private Student profesor;
	private List<Temat> tematy;


	public Student getProfesor() {
		return profesor;
	}


	public void setProfesor(Student profesor) {
		this.profesor = profesor;
	}


	public List<Temat> getTematy() {
		return tematy;
	}


	public void setTematy(List<Temat> tematy) {
		this.tematy = tematy;
	}


	public ProfesorTematy() {
	}

}
