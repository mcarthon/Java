package BookClub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import BookClub.models.LoginUser;
import BookClub.models.User;
import BookClub.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {

	private final UserService userService;
	
	public UserController ( UserService userService ) {
		
		this.userService = userService;
		
	}
	
	@GetMapping("/")
	public String loginRegisterPage ( Model model ) {
		
		model.addAttribute("userRegister", new User());
		
		model.addAttribute("userLogin", new LoginUser());
		
		return "loginRegister.jsp";
		
	}
	
	@PostMapping("/register")
	public String register ( @Valid @ModelAttribute ( "userRegister" ) User userRegister, BindingResult bindingResult, Model model, HttpSession session ) {
		
		User registeredUser =  this.userService.register ( userRegister, bindingResult );
		
		if ( bindingResult.hasErrors() ) {
			
			model.addAttribute ( "userLogin", new LoginUser () );
			
			return "loginRegister.jsp"; 
			
		}
		
		session.setAttribute ( "user_id", registeredUser.getId () );
		
		session.setAttribute ( "name", registeredUser.getName() );
		
	    return "redirect:/books";
		
	}
	
	@PostMapping ( "/login" )
	public String login( @Valid @ModelAttribute( "userLogin" ) LoginUser userLogin, BindingResult bindingResult, Model model, HttpSession session ) {
 
		User loggedInUser = this.userService.login ( userLogin, bindingResult );
		
		if( bindingResult.hasErrors() ) {
			
			model.addAttribute ( "userRegister", new User () );
    	 
			return "loginRegister.jsp";
         
		}
		
		session.setAttribute ( "user_id", loggedInUser.getId () );
		
		session.setAttribute ( "name", loggedInUser.getName() );
 
		return "redirect:/books";
     
	}
	
	@GetMapping ( "/logout" )
	public String logout ( HttpSession session ) {
		
		session.invalidate();
		
		return "redirect:/";
		
	}
 	
}