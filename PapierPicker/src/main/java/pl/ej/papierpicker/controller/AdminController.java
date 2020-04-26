package pl.ej.papierpicker.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.ej.papierpicker.entity.Student;
import pl.ej.papierpicker.entity.Temat;
import pl.ej.papierpicker.entity.Users;
import pl.ej.papierpicker.service.MainService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UserDetailsManager userDetailsManager;
	
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	private MainService mainService;
	
	@GetMapping("/userlist")
	public String showAdmin(Model theModel, Authentication authentication) {
		List<Student> student = mainService.getStudents();
		theModel.addAttribute("student", student);
		
		
		//Dodatkowe informacje na ekran
		Student logged = mainService.getStudentByUsername(authentication.getName());
		theModel.addAttribute("logged", logged);
		
		if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0  ||
			logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0 ) {
			
			Temat temat = mainService.getTematByStudentId(logged.getId());
			theModel.addAttribute("temat", temat.getTemat());
		
		
		
			if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0 ){
				
				Student promotor = mainService.getStudent(temat.getProfesor().getId());
				theModel.addAttribute("promotor", promotor);
			}
		}
		
		return "admin";
	}
	
	@GetMapping("/admin-add-student")
	public String showFormForAdd(Model theModel, Authentication authentication) {
		
		// create model attribute to bind form data
		Student student = new Student();
		
		theModel.addAttribute("student", student);
		theModel.addAttribute("role", "ROLE_STUDENT");
		
		//Dodatkowe informacje na ekran
		Student logged = mainService.getStudentByUsername(authentication.getName());
		theModel.addAttribute("logged", logged);
		
		if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0  ||
			logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0 ) {
			
			Temat temat = mainService.getTematByStudentId(logged.getId());
			theModel.addAttribute("temat", temat.getTemat());
		
		
		
			if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0 ){
				
				Student promotor = mainService.getStudent(temat.getProfesor().getId());
				theModel.addAttribute("promotor", promotor);
			}
		}
		
		return "admin-add-student";
	}
	
	@GetMapping("/admin-add-profesor")
	public String showFormForAddProf(Model theModel, Authentication authentication) {
		
		// create model attribute to bind form data
		Student student = new Student();
		
		theModel.addAttribute("student", student);
		theModel.addAttribute("role", "ROLE_PROFESOR");
		
		//Dodatkowe informacje na ekran
		Student logged = mainService.getStudentByUsername(authentication.getName());
		theModel.addAttribute("logged", logged);
		
		if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0  ||
			logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0 ) {
			
			Temat temat = mainService.getTematByStudentId(logged.getId());
			theModel.addAttribute("temat", temat.getTemat());
		
		
		
			if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0 ){
				
				Student promotor = mainService.getStudent(temat.getProfesor().getId());
				theModel.addAttribute("promotor", promotor);
			}
		}
		
		return "admin-add-student";
	}
	
	
	
	@PostMapping("/process")
	public String processRegistrationForm(
				@Valid @ModelAttribute("student") Student student,
				BindingResult theBindingResult, 
				Model theModel,
				Authentication authentication) {
		
		Users users = student.getUsers();
		
		String userName = users.getUsername();
		
		
		// form validation
					if (theBindingResult.hasErrors() || users.getPassword() == null) {
			
						theModel.addAttribute("users", new Users());
						theModel.addAttribute("student", student);
						theModel.addAttribute("registrationError", "Niepoprawnie wprowadzone dane, pamiętaj że żaden formularz nie może być pusty");
			
						//Dodatkowe informacje na ekran
						Student logged = mainService.getStudentByUsername(authentication.getName());
						theModel.addAttribute("logged", logged);
						
						if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0  ||
							logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0 ) {
							
							Temat temat = mainService.getTematByStudentId(logged.getId());
							theModel.addAttribute("temat", temat.getTemat());
						
						
						
							if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0 ){
								
								Student promotor = mainService.getStudent(temat.getProfesor().getId());
								theModel.addAttribute("promotor", promotor);
							}
						}
						
						return "admin-add-student";	
					}
		
		//If its a new user
		if(!userDetailsManager.userExists(userName)){
	
			// encrypt the password
	        String encodedPassword = passwordEncoder.encode(users.getPassword());
	
	        // prepend the encoding algorithm id
	        encodedPassword = "{bcrypt}" + encodedPassword;
	                 
	        List<GrantedAuthority> authorities;
	        
	        authorities = AuthorityUtils.createAuthorityList("ROLE_STUDENT");

	        // create user object (from Spring Security framework)
	        User tempUser = new User(userName, encodedPassword, authorities);
	
	        // save user in the database
	        userDetailsManager.createUser(tempUser);   
	
			mainService.saveStudent(student);
	        
			List<Student> usersTmp = mainService.getStudents();
			theModel.addAttribute("student", usersTmp);
			
			theModel.addAttribute("infoError", "Użytkownik poprawnie dodany");
			
			//Dodatkowe informacje na ekran
			Student logged = mainService.getStudentByUsername(authentication.getName());
			theModel.addAttribute("logged", logged);
			
			if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0  ||
				logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0 ) {
				
				Temat temat = mainService.getTematByStudentId(logged.getId());
				theModel.addAttribute("temat", temat.getTemat());
			
			
			
				if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0 ){
					
					Student promotor = mainService.getStudent(temat.getProfesor().getId());
					theModel.addAttribute("promotor", promotor);
				}
			}
			
	        return "admin";	
	        
	}
		//User already exist so we might want to change him
		
	    UserDetails oldDetails = userDetailsManager.loadUserByUsername(student.getUsers().getUsername());
	    String newPassword = oldDetails.getPassword();
	    
	    //jeżeli wpisane hasło nie wydaje sie nie być zakodowane
	    if(!users.getPassword().startsWith("{bcrypt}")) {
		    if(!passwordEncoder.matches(users.getPassword(), oldDetails.getPassword())) {
		    	
		    	
		    	String encodedPassword = passwordEncoder.encode(users.getPassword());
		    	newPassword = "{bcrypt}" + encodedPassword;
		    	
		    	System.out.println(newPassword);
		    	System.out.println(oldDetails.getPassword());
		    	
		    	
				UserDetails newDetails = new User(oldDetails.getUsername(), newPassword,  oldDetails.getAuthorities());
			    userDetailsManager.updateUser(newDetails);
		    }
	    }
	    
	    //Unikanie duplikacji
	    Student newStudent = mainService.getStudentByUsername(student.getUsers().getUsername());
	    
	    newStudent.setFirstName(student.getFirstName());
	    newStudent.setLastName(student.getLastName());
		mainService.saveStudent(newStudent);
	    
		
		//Wstaw liste do wyswietlenia
		List<Student> usersTmp = mainService.getStudents();
		theModel.addAttribute("student", usersTmp);
		
		//Dodatkowe informacje na ekran
		Student logged = mainService.getStudentByUsername(authentication.getName());
		theModel.addAttribute("logged", logged);
		
		if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0  ||
			logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0 ) {
			
			Temat temat = mainService.getTematByStudentId(logged.getId());
			theModel.addAttribute("temat", temat.getTemat());
		
		
		
			if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0 ){
				
				Student promotor = mainService.getStudent(temat.getProfesor().getId());
				theModel.addAttribute("promotor", promotor);
			}
		}
		
		theModel.addAttribute("infoError", "Użytkownik poprawnie zmodyfikowany");
		return "admin";
	}
	
	
	
	
	@PostMapping("/process2")
	public String processRegistrationForm2(
				@Valid @ModelAttribute("student") Student student,
				BindingResult theBindingResult, 
				Model theModel,
				Authentication authentication) {
		

		
		Users users = student.getUsers();
		
		String userName = users.getUsername();
		
		
		// form validation
					if (theBindingResult.hasErrors() || users.getPassword() == null) {
			
						theModel.addAttribute("users", new Users());
						theModel.addAttribute("student", student);
						theModel.addAttribute("registrationError", "Niepoprawnie wprowadzone dane, pamiętaj że żaden formularz nie może być pusty");
			
						//Dodatkowe informacje na ekran
						Student logged = mainService.getStudentByUsername(authentication.getName());
						theModel.addAttribute("logged", logged);
						
						if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0  ||
							logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0 ) {
							
							Temat temat = mainService.getTematByStudentId(logged.getId());
							theModel.addAttribute("temat", temat.getTemat());
						
						
						
							if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0 ){
								
								Student promotor = mainService.getStudent(temat.getProfesor().getId());
								theModel.addAttribute("promotor", promotor);
							}
						}
						
						return "admin-add-student";	
					}
		
		
		//If its a new user
		if(!userDetailsManager.userExists(userName)){
		
			// encrypt the password
	        String encodedPassword = passwordEncoder.encode(users.getPassword());
	
	        // prepend the encoding algorithm id
	        encodedPassword = "{bcrypt}" + encodedPassword;
	                 
	        List<GrantedAuthority> authorities;

	        authorities = AuthorityUtils.createAuthorityList("ROLE_PROFESOR");
	        
	        // create user object (from Spring Security framework)
	        User tempUser = new User(userName, encodedPassword, authorities);
	
	        // save user in the database
	        userDetailsManager.createUser(tempUser);   
	
			mainService.saveStudent(student);
	        
			List<Student> usersTmp = mainService.getStudents();
			theModel.addAttribute("student", usersTmp);
			
			theModel.addAttribute("infoError", "Użytkownik poprawnie dodany");
			
			
			//Dodatkowe informacje na ekran
			Student logged = mainService.getStudentByUsername(authentication.getName());
			theModel.addAttribute("logged", logged);
			
			if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0  ||
				logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0 ) {
				
				Temat temat = mainService.getTematByStudentId(logged.getId());
				theModel.addAttribute("temat", temat.getTemat());
			
			
			
				if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0 ){
					
					Student promotor = mainService.getStudent(temat.getProfesor().getId());
					theModel.addAttribute("promotor", promotor);
				}
			}
			
	        return "admin";	
	        
	}
		//User already exist so we might want to change him
		
	    UserDetails oldDetails = userDetailsManager.loadUserByUsername(student.getUsers().getUsername());
	    String newPassword = oldDetails.getPassword();
	    
	    //jeżeli wpisane hasło nie wydaje sie nie być zakodowane
	    if(!users.getPassword().startsWith("{bcrypt}")) {
		    if(!passwordEncoder.matches(users.getPassword(), oldDetails.getPassword())) {
		    	
		    	
		    	String encodedPassword = passwordEncoder.encode(users.getPassword());
		    	newPassword = "{bcrypt}" + encodedPassword;
		    	
		    	System.out.println(newPassword);
		    	System.out.println(oldDetails.getPassword());
		    	
		    	
				UserDetails newDetails = new User(oldDetails.getUsername(), newPassword,  oldDetails.getAuthorities());
			    userDetailsManager.updateUser(newDetails);
		    }
	    }
	    
	    
	    //Unikanie duplikacji 
	    Student newStudent = mainService.getStudentByUsername(student.getUsers().getUsername());
	    
	    newStudent.setFirstName(student.getFirstName());
	    newStudent.setLastName(student.getLastName());
		mainService.saveStudent(newStudent);
	    
		
		//Wstaw liste do wyswietlenia
		List<Student> usersTmp = mainService.getStudents();
		theModel.addAttribute("student", usersTmp);
		
		//Dodatkowe informacje na ekran
		Student logged = mainService.getStudentByUsername(authentication.getName());
		theModel.addAttribute("logged", logged);
		
		if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0  ||
			logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0 ) {
			
			Temat temat = mainService.getTematByStudentId(logged.getId());
			theModel.addAttribute("temat", temat.getTemat());
		
		
		
			if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0 ){
				
				Student promotor = mainService.getStudent(temat.getProfesor().getId());
				theModel.addAttribute("promotor", promotor);
			}
		}
		
		theModel.addAttribute("infoError", "Użytkownik poprawnie zmodyfikowany");
		
		return "admin";
	}

	
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	
	
	//delete
	
	
	@GetMapping("/delete")
	public String deleteStudent(@RequestParam("userId") int theId, Model theModel, Authentication authentication) {
		
		

		
		Student student = mainService.getStudent(theId);
		
		if(student.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0 ){
			
			//wstaw w miejsce studenta w temacie null
			Temat temat = mainService.getTematByStudentId(theId);
			
			//Potrzebne?
			if(temat.getStudent() != null) {
				temat.setStudent(null);
				mainService.saveTemat(temat);
			}
		}else {
			
			//usun wszystkie tematy profesora
			List<Temat> tematy = mainService.getTematsById(theId);
			
			for (Temat temat : tematy) {
				
				//Zmien role z promowanego nas studenta
				if(temat.getStudent() != null) {
					UserDetails oldDetails = userDetailsManager.loadUserByUsername(temat.getStudent().getUsers().getUsername());
			        User tempUser = new User(oldDetails.getUsername(), oldDetails.getPassword(), AuthorityUtils.createAuthorityList("ROLE_STUDENT"));
			        userDetailsManager.updateUser(tempUser);
		        }
			}
			
			
			mainService.deleteTematByProfesor(theId);
		}
		
		
		//usun dane dodatkowe studenta		
		mainService.deleteStudent(theId);
		
		//usun konto studenta
		userDetailsManager.deleteUser(student.getUsers().getUsername());
		
		theModel.addAttribute("infoError", "Użytkownik usunięty");
		
		
		
		//Dodatkowe informacje na ekran
		List<Student> students = mainService.getStudents();
		theModel.addAttribute("student", students);
		
		Student logged = mainService.getStudentByUsername(authentication.getName());
		theModel.addAttribute("logged", logged);
		
		if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0  ||
			logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0 ) {
			
			Temat temat = mainService.getTematByStudentId(logged.getId());
			theModel.addAttribute("temat", temat.getTemat());
		
		
		
			if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0 ){
				
				Student promotor = mainService.getStudent(temat.getProfesor().getId());
				theModel.addAttribute("promotor", promotor);
			}
		}
		
		return "admin";
	}
	
	
	//update
	@GetMapping("/update")
	public String showFormForUpdate(@RequestParam("userId") int theId,
									Model theModel, Authentication authentication) {
		
		// get the customer from our service
		Student student = mainService.getStudent(theId);	
		
		// set customer as a model attribute to pre-populate the form
		theModel.addAttribute("student", student);
		
		if(student.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0 ){
			theModel.addAttribute("role", "ROLE_STUDENT");
		}else {
			theModel.addAttribute("role", "ROLE_PROFESOR");
		}
		// send over to our form
		
		//Dodatkowe informacje na ekran
		Student logged = mainService.getStudentByUsername(authentication.getName());
		theModel.addAttribute("logged", logged);
		
		if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0  ||
			logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0 ) {
			
			Temat temat = mainService.getTematByStudentId(logged.getId());
			theModel.addAttribute("temat", temat.getTemat());
		
		
		
			if(logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0 ){
				
				Student promotor = mainService.getStudent(temat.getProfesor().getId());
				theModel.addAttribute("promotor", promotor);
			}
		}
		
		return "admin-add-student";
	}
}
