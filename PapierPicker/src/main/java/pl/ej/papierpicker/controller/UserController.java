package pl.ej.papierpicker.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.ej.papierpicker.entity.Propozycja;
import pl.ej.papierpicker.entity.Student;
import pl.ej.papierpicker.entity.Temat;
import pl.ej.papierpicker.service.MainService;
import pl.ej.papierpicker.user.ListaProfesorow;
import pl.ej.papierpicker.user.ProponowanyTemat;

@Controller
@RequestMapping("/user")
public class UserController {

	// need to inject our customer service
	@Autowired
	private MainService mainService;

	@Autowired
	private UserDetailsManager userDetailsManager;

	@GetMapping("/home")
	public String showHome(Authentication authentication, Model theModel) {

		// Dodatkowe informacje na ekran
		Student logged = mainService.getStudentByUsername(authentication.getName());
		theModel.addAttribute("logged", logged);
		

		
		
		if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0
				|| logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0) {

			Temat temat = mainService.getTematByStudentId(logged.getId());
			theModel.addAttribute("temat", temat.getTemat());

			if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0) {

				Student promotor = mainService.getStudent(temat.getProfesor().getId());
				theModel.addAttribute("promotor", promotor);
			}
			
			if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0) {
				List<ProponowanyTemat> proponowanyTemats = mainService.getProponowaneByStudent(logged.getId());
				theModel.addAttribute("proponowane", proponowanyTemats);
			}
			
		}

		return "user";
	}

	@GetMapping("/temat")
	public String showTemat(Authentication authentication, Model theModel) {

		Temat tematTmp = new Temat();
		theModel.addAttribute("tematTmp", tematTmp);

		Student student = mainService.getStudentByUsername(authentication.getName());
		List<Temat> temats = mainService.getTematsById(student.getId());
		theModel.addAttribute("temats", temats);

		// Dodatkowe informacje na ekran
		Student logged = mainService.getStudentByUsername(authentication.getName());
		theModel.addAttribute("logged", logged);
		
		List<ProponowanyTemat> proponowanyTemats = mainService.getProponowaneByProfesor(logged.getId());
		theModel.addAttribute("proponowane", proponowanyTemats);

		if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0
				|| logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0) {

			Temat temat = mainService.getTematByStudentId(logged.getId());
			theModel.addAttribute("temat", temat.getTemat());

			if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0) {

				Student promotor = mainService.getStudent(temat.getProfesor().getId());
				theModel.addAttribute("promotor", promotor);
			}
		}

		return "temat";
	}

	@PostMapping("/addTemat")
	public String addTemat(Authentication authentication, @Valid @ModelAttribute("tematTmp") Temat tematTmp,
			BindingResult theBindingResult, Model theModel) {

		// Jeżeli temat o takim ID jest juz w bazie to wez z niego dane i dodaj do tego
		// modelu
		Temat oldTemat = mainService.getTematbyId(tematTmp.getId());
		if (oldTemat != null) {
			tematTmp.setProfesor(oldTemat.getProfesor());
			tematTmp.setStudent(oldTemat.getStudent());
		} else {

			// Tematu jeszcze nie ma wiec nadaj mi za profesora osobe ktora temat dodaje
			Student profesor = mainService.getStudentByUsername(authentication.getName());
			tematTmp.setProfesor(profesor);
		}

		// validacja
		if (theBindingResult.hasErrors()) {
			tematTmp = new Temat();
			theModel.addAttribute("tematTmp", tematTmp);

			Student student = mainService.getStudentByUsername(authentication.getName());
			List<Temat> temats = mainService.getTematsById(student.getId());
			theModel.addAttribute("temats", temats);

			// Dodatkowe informacje na ekran
			Student logged = mainService.getStudentByUsername(authentication.getName());
			theModel.addAttribute("logged", logged);
			
			List<ProponowanyTemat> proponowanyTemats = mainService.getProponowaneByProfesor(logged.getId());
			theModel.addAttribute("proponowane", proponowanyTemats);

			if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0
					|| logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0) {

				Temat temat = mainService.getTematByStudentId(logged.getId());
				theModel.addAttribute("temat", temat.getTemat());

				if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0) {

					Student promotor = mainService.getStudent(temat.getProfesor().getId());
					theModel.addAttribute("promotor", promotor);
				}
			}
			theModel.addAttribute("registrationError", "Wprowadzony temat nie może być pusty");

			return "temat";
		}

		mainService.saveTemat(tematTmp);

		tematTmp = new Temat();
		theModel.addAttribute("tematTmp", tematTmp);

		Student student = mainService.getStudentByUsername(authentication.getName());
		List<Temat> temats = mainService.getTematsById(student.getId());
		theModel.addAttribute("temats", temats);

		// Dodatkowe informacje na ekran
		Student logged = mainService.getStudentByUsername(authentication.getName());
		theModel.addAttribute("logged", logged);
		
		List<ProponowanyTemat> proponowanyTemats = mainService.getProponowaneByProfesor(logged.getId());
		theModel.addAttribute("proponowane", proponowanyTemats);

		if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0
				|| logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0) {

			Temat temat = mainService.getTematByStudentId(logged.getId());
			theModel.addAttribute("temat", temat.getTemat());

			if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0) {

				Student promotor = mainService.getStudent(temat.getProfesor().getId());
				theModel.addAttribute("promotor", promotor);
			}
		}

		return "temat";
	}

	@GetMapping("/lista")
	public String showList(Model theModel, Authentication authentication) {

		ListaProfesorow listaProfesorow = mainService.getSortedListOfProfesors();

		theModel.addAttribute("list", listaProfesorow);

		theModel.addAttribute("propozycja", new Propozycja());

		// Dodatkowe informacje na ekran
		Student logged = mainService.getStudentByUsername(authentication.getName());
		theModel.addAttribute("logged", logged);

		if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0
				|| logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0) {

			Temat temat = mainService.getTematByStudentId(logged.getId());
			theModel.addAttribute("temat", temat.getTemat());

			if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0) {

				Student promotor = mainService.getStudent(temat.getProfesor().getId());
				theModel.addAttribute("promotor", promotor);
			}
		}

		return "temat-lista";
	}

	// delete

	@GetMapping("/delete")
	public String deleteTemat(@RequestParam("tematId") int theId, Model theModel, Authentication authentication) {

		Temat temat = mainService.getTematbyId(theId);

		if (temat.getStudent() != null) {
			UserDetails oldDetails = userDetailsManager.loadUserByUsername(temat.getStudent().getUsers().getUsername());
			User tempUser = new User(oldDetails.getUsername(), oldDetails.getPassword(),
					AuthorityUtils.createAuthorityList("ROLE_STUDENT"));
			userDetailsManager.updateUser(tempUser);
		}

		mainService.deleteTemat(theId);

		Temat tematTmp = new Temat();
		theModel.addAttribute("tematTmp", tematTmp);

		Student student = mainService.getStudentByUsername(authentication.getName());
		List<Temat> temats = mainService.getTematsById(student.getId());
		theModel.addAttribute("temats", temats);

		// Dodatkowe informacje na ekran
		Student logged = mainService.getStudentByUsername(authentication.getName());
		theModel.addAttribute("logged", logged);
		
		List<ProponowanyTemat> proponowanyTemats = mainService.getProponowaneByProfesor(logged.getId());
		theModel.addAttribute("proponowane", proponowanyTemats);

		if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0
				|| logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0) {

			temat = mainService.getTematByStudentId(logged.getId());
			theModel.addAttribute("temat", temat.getTemat());

			if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0) {

				Student promotor = mainService.getStudent(temat.getProfesor().getId());
				theModel.addAttribute("promotor", promotor);
			}
		}

		theModel.addAttribute("infoError", "Poprawnie usunięto temat");
		return "temat";
	}
	
	@GetMapping("/deletePropozycja")
	public String delateProp(@RequestParam("tematId") int theId, Model theModel, Authentication authentication) {
		
		mainService.deletePropozycja(theId);
		
		return "redirect:/user/home";
	}
	
	
	@GetMapping("/pickPropozycja")
	public String pickProp(@RequestParam("tematId") int theId, Model theModel, Authentication authentication) {
		
		//wstaw propozycje jako temat
		Propozycja propozycja = mainService.getPropozycjaById(theId);
		Temat temat = new Temat();
		temat.setProfesor(propozycja.getProfesor());
		temat.setStudent(propozycja.getStudent());
		temat.setTemat(propozycja.getTemat());
		mainService.saveTemat(temat);
		
		
		//zmien role studentowi ktory zaproponowal
		Student student = propozycja.getStudent();
		UserDetails oldDetails = userDetailsManager.loadUserByUsername(student.getUsers().getUsername());
		User tempUser = new User(oldDetails.getUsername(), oldDetails.getPassword(),
				AuthorityUtils.createAuthorityList("ROLE_PROMOWANY"));
		userDetailsManager.updateUser(tempUser);
		
		
		//usun wszystkie pozostale propozycje studenta
		List<ProponowanyTemat> proponowanyTemats = mainService.getProponowaneByStudent(propozycja.getStudent().getId());
		for (ProponowanyTemat i : proponowanyTemats) {
			mainService.deletePropozycja(i.getTemat().getId());
		}
		
		
		return "redirect:/user/temat";
	}
	
	@GetMapping("/deletePropozycja2")
	public String delateProp2(@RequestParam("tematId") int theId, Model theModel, Authentication authentication) {
		
		mainService.deletePropozycja(theId);
		
		return "redirect:/user/temat";
	}
	
	
	
	// update
	@GetMapping("/update")
	public String showFormForUpdate(@RequestParam("tematId") int theId, Model theModel, Authentication authentication) {

		Temat tematTmp = mainService.getTematbyId(theId);
		theModel.addAttribute("tematTmp", tematTmp);

		Student student = mainService.getStudentByUsername(authentication.getName());
		List<Temat> temats = mainService.getTematsById(student.getId());
		theModel.addAttribute("temats", temats);

		// Dodatkowe informacje na ekran
		Student logged = mainService.getStudentByUsername(authentication.getName());
		theModel.addAttribute("logged", logged);
		
		List<ProponowanyTemat> proponowanyTemats = mainService.getProponowaneByProfesor(logged.getId());
		theModel.addAttribute("proponowane", proponowanyTemats);

		if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0
				|| logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0) {

			Temat temat = mainService.getTematByStudentId(logged.getId());
			theModel.addAttribute("temat", temat.getTemat());

			if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0) {

				Student promotor = mainService.getStudent(temat.getProfesor().getId());
				theModel.addAttribute("promotor", promotor);
			}
		}
		theModel.addAttribute("infoError", "Następuje modyfikacja");

		return "temat";
	}

	@GetMapping("/pick")
	public String pickTemat(@RequestParam("tematId") int theId, Model theModel, Authentication authentication,
			HttpSession session, HttpServletRequest request) {

		Student student = mainService.getStudentByUsername(authentication.getName());

		// Znajdz, zmien i zapisz w temacie kto go wybral
		Temat temat = mainService.getTematbyId(theId);
		temat.setStudent(student);
		mainService.saveTemat(temat);

		theModel.addAttribute("infoError", "Wybrałeś temat " + temat.getTemat() + "!");

		// Zmien role ze studenta na promowanego
		UserDetails oldDetails = userDetailsManager.loadUserByUsername(student.getUsers().getUsername());
		User tempUser = new User(oldDetails.getUsername(), oldDetails.getPassword(),
				AuthorityUtils.createAuthorityList("ROLE_PROMOWANY"));
		userDetailsManager.updateUser(tempUser);

		// Reset zapisanej roli w pamięci
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(),
				AuthorityUtils.createAuthorityList("ROLE_PROMOWANY"));

		SecurityContextHolder.getContext().setAuthentication(newAuth);

		// Dodatkowe informacje na ekran
		Student logged = mainService.getStudentByUsername(authentication.getName());
		theModel.addAttribute("logged", logged);

		if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0
				|| logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0) {

			temat = mainService.getTematByStudentId(logged.getId());
			theModel.addAttribute("temat", temat.getTemat());

			if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0) {

				Student promotor = mainService.getStudent(temat.getProfesor().getId());
				theModel.addAttribute("promotor", promotor);
			}
			
			if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0) {
				List<ProponowanyTemat> proponowanyTemats = mainService.getProponowaneByStudent(logged.getId());
				theModel.addAttribute("proponowane", proponowanyTemats);
			}
		}

		return "user";
	}

	@PostMapping("/propose")
	public String processPropose(Authentication authentication, @ModelAttribute("propozycja") Propozycja propozycja,
			Model theModel) {

		//w id zapisany jest id profesora ktorego to dotyczy
		propozycja.setProfesor(mainService.getStudent(propozycja.getId()));
		propozycja.setId(0);
		propozycja.setStudent(mainService.getStudentByUsername(authentication.getName()));
		
		mainService.savePropozycja(propozycja);
		
		
		ListaProfesorow listaProfesorow = mainService.getSortedListOfProfesors();

		theModel.addAttribute("list", listaProfesorow);

		theModel.addAttribute("propozycja", new Propozycja());
		
		theModel.addAttribute("infoError", "Propozycja tematu wysłana, poczekaj na akceptacje tematu ze strony promotora");
		
		
		// Dodatkowe informacje na ekran

		Student logged = mainService.getStudentByUsername(authentication.getName());
		theModel.addAttribute("logged", logged);

		if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0
				|| logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0) {

			Temat temat = mainService.getTematByStudentId(logged.getId());
			theModel.addAttribute("temat", temat.getTemat());

			if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0) {

				Student promotor = mainService.getStudent(temat.getProfesor().getId());
				theModel.addAttribute("promotor", promotor);
			}
		}

		return "temat-lista";
	}

	
	
	
	
	
	
	@PostMapping("/changeEmail")
	public String changeEmail(Authentication authentication, @ModelAttribute("logged") Student student,
			Model theModel) {

		String email = student.getEmail();
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

		// validacja
		if (!student.getEmail().matches(regex)) {
			theModel.addAttribute("registrationError",
					"Wprowadzony email nie jest prawidłowy i nie zostanie wprowadzony");

			// Dodatkowe informacje na ekran
			Student logged = mainService.getStudentByUsername(authentication.getName());
			theModel.addAttribute("logged", logged);

			if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0
					|| logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0) {

				Temat temat = mainService.getTematByStudentId(logged.getId());
				theModel.addAttribute("temat", temat.getTemat());

				if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0) {

					Student promotor = mainService.getStudent(temat.getProfesor().getId());
					theModel.addAttribute("promotor", promotor);
				}
				
				if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0) {
					List<ProponowanyTemat> proponowanyTemats = mainService.getProponowaneByStudent(logged.getId());
					theModel.addAttribute("proponowane", proponowanyTemats);
				}
			}

			return "user";
		}

		student = mainService.getStudentByUsername(authentication.getName());
		student.setEmail(email);

		mainService.saveStudent(student);

		// Dodatkowe informacje na ekran
		Student logged = mainService.getStudentByUsername(authentication.getName());
		theModel.addAttribute("logged", logged);

		if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0
				|| logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0) {

			Temat temat = mainService.getTematByStudentId(logged.getId());
			theModel.addAttribute("temat", temat.getTemat());

			if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_PROMOWANY") == 0) {

				Student promotor = mainService.getStudent(temat.getProfesor().getId());
				theModel.addAttribute("promotor", promotor);
			}
			
			if (logged.getUsers().getAuthorities().get(0).getAuthority().compareTo("ROLE_STUDENT") == 0) {
				List<ProponowanyTemat> proponowanyTemats = mainService.getProponowaneByStudent(logged.getId());
				theModel.addAttribute("proponowane", proponowanyTemats);
			}
		}

		theModel.addAttribute("infoError", "Email zmieniono!");

		return "user";
	}

}
