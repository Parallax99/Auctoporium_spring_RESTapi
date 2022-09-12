package com.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bean.ResponseBean;
import com.bean.UserBean;
import com.repository.UserRepository;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/admin")
@CrossOrigin
@Log4j2
public class AdminController {
	@Autowired
	UserRepository userRepo;
	@Autowired
	PasswordEncoder bCrypt;

	@GetMapping("/listUsers")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<ResponseBean<List<UserBean>>> listUsers() {
		ResponseBean<List<UserBean>> res = new ResponseBean<>();
		List<UserBean> users = (List<UserBean>) userRepo.findAll();
		if (users == null) {
			res.setMsg("no Users to Show");
			return ResponseEntity.ok(res);
		} else {
			res.setData(users);
			res.setMsg("Retrieved all users Successfully");
			res.setErrorCode(0);
			return ResponseEntity.ok(res);
		}
	}

	@GetMapping("/listDisabledAccounts")
	public ResponseEntity<?> listDisabledAccounts() {
		ResponseBean<List<UserBean>> res = new ResponseBean<>();
		List<UserBean> users = (List<UserBean>) userRepo.findAll();
//		List<UserBean> disabledAccounts = new ArrayList<UserBean>();
//		Consumer<?> isFalse = t ->t==0; 
//		users.stream().forEach(isFalse);
//			
		res.setData(disabledAccounts);
		res.setErrorCode(0);
		res.setMsg("Retrieved Disabled Accounts");
		return ResponseEntity.ok(res);
	}

	@PostMapping("/verify")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<?> verifyUserByEmail(@RequestBody UserBean user) {
		ResponseBean<UserBean> res = new ResponseBean<>();
		UserBean dbUser = userRepo.findByEmail(user.getEmail());
		if (dbUser == null) {
			res.setData(null);
			res.setMsg("No such User Exists");
			res.setErrorCode(-1);
			return ResponseEntity.badRequest().body(res);
		} else if (dbUser.getActive()) {
			res.setMsg("email is already Verified");
			res.setErrorCode(0);
			return ResponseEntity.badRequest().body(res);
		} else {
			dbUser.setActive(true);
			userRepo.save(dbUser);
			res.setMsg("Account verified");
			res.setErrorCode(0);
			return ResponseEntity.ok(res);
		}
	}

	@PostMapping("/disable")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<?> disableUserByEmail(@RequestBody UserBean user) {
		ResponseBean<UserBean> res = new ResponseBean<>();
		UserBean dbUser = userRepo.findByEmail(user.getEmail());
		if (dbUser == null) {
			res.setData(null);
			res.setMsg("No such User Exists");
			res.setErrorCode(-1);
			return ResponseEntity.badRequest().body(res);
		} else if (!dbUser.getActive()) {
			res.setMsg("email is already disabled");
			res.setErrorCode(-1);
			return ResponseEntity.badRequest().body(res);
		} else {
			dbUser.setActive(false);
			userRepo.save(dbUser);
			res.setMsg("Account disabled");
			res.setErrorCode(0);
			return ResponseEntity.ok(res);
		}
	}
}
