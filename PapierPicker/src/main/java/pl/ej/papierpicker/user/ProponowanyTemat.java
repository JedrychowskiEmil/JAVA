package pl.ej.papierpicker.user;

import pl.ej.papierpicker.entity.Propozycja;
import pl.ej.papierpicker.entity.Student;
import pl.ej.papierpicker.entity.Temat;

public class ProponowanyTemat {

	private Student profesor;
	private Propozycja temat;


	public Student getProfesor() {
		return profesor;
	}


	public void setProfesor(Student profesor) {
		this.profesor = profesor;
	}


	public Propozycja getTemat() {
		return temat;
	}


	public void setTemat(Propozycja temat) {
		this.temat = temat;
	}


	public ProponowanyTemat() {
	}

}
