package JoyBundler.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import JoyBundler.models.BabyNames;
import JoyBundler.models.User;
import JoyBundler.services.BabyNamesService;
import JoyBundler.services.JointService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class BabyNamesController {

	private final BabyNamesService babynamesService;
	
	private final JointService jointService;
	
	public BabyNamesController ( BabyNamesService babynamesService, JointService jointService ) {
		
		this.babynamesService = babynamesService;
		
		this.jointService = jointService;
		
	}
	
	@GetMapping ( "/names" )
	public String homePage ( HttpSession session, Model model ) {
		
		if ( session.getAttribute ( "user_id" ) != null ) {
			
			model.addAttribute ( "allBabyNames", this.babynamesService.findAllNames () );
			
			Long userId = (Long) session.getAttribute ( "user_id" );
			
			model.addAttribute ( "currentUser", this.jointService.findUserById ( userId ) );
			
			return "dashboard.jsp";
			
		}
		
		return "redirect:/";
		
	}
	
	@PostMapping ( "/names/upvote/{userId}/{babynameId}" )
	public String upvote ( @PathVariable ( "userId" ) Long userId, @PathVariable ( "babynameId" ) Long nameId ) {
		
		this.jointService.upvote ( userId, nameId );
		
		return "redirect:/names";
		
	}
	
	@PostMapping ( "/names/upvote/show/{userId}/{nameId}" )
	public String upvoteSHow ( @PathVariable ( "userId" ) Long userId, @PathVariable ( "nameId" ) Long nameId ) {
		
		this.jointService.upvote ( userId, nameId );
		
		return "redirect:/names/{nameId}";
		
	}
	
	@PostMapping ( "/names/downvote/{userId}/{nameId}" )
	public String downvote ( @PathVariable ( "userId" ) Long userId, @PathVariable ( "nameId" ) Long nameId ) {
		
		this.jointService.downvote ( userId, nameId );
		
		return "redirect:/names";
		
	}
	
	@GetMapping ( "/names/new" )
	public String newNamePage ( HttpSession session, @ModelAttribute ( "newName" ) BabyNames newName ) {
		
		if ( session.getAttribute ( "user_id" ) != null ) {
			
			return "newName.jsp";
			
		}
		
		return "redirect:/";
		
	}
	
	@PostMapping ( "/names/new" ) 
	public String addName ( @Valid @ModelAttribute ( "newName" ) BabyNames newName, BindingResult bindingResult,
			HttpSession session ) {
		
		
		if ( bindingResult.hasErrors () ) {
			
			return "newName.jsp";
			
		}
		
		Long userId = (Long) session.getAttribute ( "user_id" );
		
		User currentUser = this.jointService.findUserById ( userId );
		
		List<User> users = new ArrayList<User> ();
		
		users.add ( currentUser );
		
		newName.setUsers ( users );
		
		this.babynamesService.create ( newName, bindingResult);		
		
		if ( bindingResult.hasErrors () ) {
			
			return "newName.jsp";
			
		}
		
		return "redirect:/names";
		
	}
	
	@GetMapping ( "/names/{nameId}" )
	public String showDetailsPage ( @PathVariable ( "nameId" ) Long nameId, HttpSession session, Model model ) {
		
		if ( session.getAttribute ( "user_id" ) != null ) {
		
			model.addAttribute ( "babyname", this.babynamesService.findOneName ( nameId ) );
			
			Long userId = (Long) session.getAttribute ( "user_id" );
			
			model.addAttribute ( "currentUser", this.jointService.findUserById ( userId ) );
			
			return "show.jsp";
			
		}
		
		return "redirect:/";
		
	}
	
	@GetMapping ( "/names/edit/{nameId}" )
	public String editPage ( @PathVariable ( "nameId" ) Long nameId, 
			@ModelAttribute ( "updateBabyName" ) BabyNames updateBabyName,
			Model model,
			HttpSession session ) {
		
		if ( session.getAttribute ( "user_id" ) != null ) {
			
			BabyNames babyname = this.babynamesService.findOneName ( nameId );
			
			model.addAttribute ( "babyname", babyname );
			
			return "edit.jsp";
			
		}
		
		return "redirect:/";
		
	}
	
	@PatchMapping ( "/names/edit/{nameId}" )
	public String addEdits ( @PathVariable ( "nameId" ) Long nameId,
			@Valid @ModelAttribute ( "updateBabyName" ) BabyNames updateBabyName, 
			BindingResult bindingResult,			
			Model model) {
		
		if ( bindingResult.hasErrors () ) {
			
			model.addAttribute ( "babyname", this.babynamesService.findOneName ( nameId ) );
			
			return "edit.jsp";
			
		}		
		
		
		
		this.babynamesService.update ( updateBabyName );
		
		return "redirect:/names/{nameId}";
		
	}
	
	@DeleteMapping ("/names/{nameId}")
	public String deleteName ( @PathVariable ( "nameId" ) Long nameId ) {
		
		BabyNames babyname = this.babynamesService.findOneName ( nameId );
		
		babyname.setUsers ( null );
		
		this.babynamesService.delete ( nameId );
		
		return "redirect:/names";
		
	}
	
	
}





























































