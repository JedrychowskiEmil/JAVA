package pl.ej.papierpicker.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="propozycja")
public class Propozycja {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="temat")
	private String temat;
	
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "profesor_id", referencedColumnName="id")
	private Student profesor;
	
	//@JoinColumn(name = "COMPANY_ID", referencedColumnName = "COMPANY_ID", insertable = false, updatable = false
	
	@OneToOne
	@JoinColumn(name = "student_id", referencedColumnName="id")
	private Student student;

	
	
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTemat() {
		return temat;
	}

	public void setTemat(String temat) {
		this.temat = temat;
	}





	public Student getProfesor() {
		return profesor;
	}

	public void setProfesor(Student profesor) {
		this.profesor = profesor;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Propozycja() {
	}
	
}
