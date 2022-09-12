package com.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bean.FileBean;
import com.bean.LoginBean;
import com.bean.ResponseBean;
import com.bean.RoleBean;
import com.bean.UserBean;
import com.repository.RoleRepository;
import com.repository.UserRepository;
import com.service.FileServices.FileStorageService;
import com.service.JWTutils.JwtTokenManager;
import com.service.aspect.LogExecutionTime;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/session")
@CrossOrigin
public class SessionController {
	@Autowired
	UserRepository userRepo;
	@Autowired
	RoleRepository roleRepo;
	@Autowired
	PasswordEncoder bCrypt;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtTokenManager jwtTokenManager;
	@Autowired
	FileStorageService fileStorage;

	@PostMapping("/SignUp")
	public ResponseEntity<?> userSignUp(@RequestBody UserBean user) {
		log.info("hello");
//		System.out.println(userRepo.findByEmail(user.getEmail()));
//		if (user == null) {
//			res.setData(user);
//			res.setMsg("Null or Invalid Data");
//			res.setErrorCode(-1);
//			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(res);
//		} else 
		ResponseBean<UserBean> res = new ResponseBean<UserBean>();
		UserBean dbUser = userRepo.findByEmail(user.getEmail());
		if (dbUser != null) {
			log.info("email already taken");
			res.setData(user);
			res.setMsg("Email Already Exists");
			log.info(res);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		} else {
			if (user.getEmail().equalsIgnoreCase("admin@admin.com")) {
				List<RoleBean> role = roleRepo.findByRoleName("admin");
				role.add(roleRepo.findByRoleName("user").get(0));
				user.setRoles(role);
				user.setActive(true);
			} else {
				user.setRoles(roleRepo.findByRoleName("user"));
			}
			log.info("email not taken");
			user.setPassword(bCrypt.encode(user.getPassword()));
			userRepo.save(user);
			res.setData(user);
			res.setMsg("SignedUp Successfully");
			res.setErrorCode(0);
			return ResponseEntity.ok(res);
		}
	}

	@PostMapping("/login")
	@LogExecutionTime
	public ResponseEntity<?> userLogin(@RequestBody LoginBean loginbean) {
		ResponseBean<LoginBean> res = new ResponseBean<LoginBean>();
		UserBean user = userRepo.findByEmail(loginbean.getEmail());

		if (user == null || !bCrypt.matches(loginbean.getPassword(), user.getPassword())) {
			res.setData(loginbean);
			res.setMsg("Invalid Credentials");
			log.info("here");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
		} else {
			log.info("above auth");
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginbean.getEmail(), loginbean.getPassword()));
			log.info(authentication.isAuthenticated());
//			user.setAuthToken();
//			userRepo.save(user);
			if (authentication.isAuthenticated()) {
				log.info("token");
				String token = jwtTokenManager.generateJwtToken(loginbean.getEmail(),
						userRepo.findByEmail(loginbean.getEmail()).getRoles());
				ResponseBean<UserBean> dbRes = new ResponseBean<>();
//				JSONParser parser = new JSONParser(token);
//				JSONPObject json = (JSONPObject) parser.parse();
//				AuthTokenBean auth = new AuthTokenBean();
//				auth.setAuthToken(token);
//				ResponseBean<AuthTokenBean> resA = new ResponseBean<>();
				dbRes.setData(user);
				dbRes.setMsg(token);
				dbRes.setErrorCode(0);
				return ResponseEntity.ok(dbRes);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
			}
		}
	}

	@PostMapping("/uploadFile")
	public ResponseEntity<?> uploadProfilePic(@RequestParam("file") MultipartFile file) {
		String fileName = fileStorage.storeFile(file);
		ResponseBean<FileBean> res = new ResponseBean<>();
		FileBean fileBean = new FileBean();
		fileBean.setFileName(fileName);
		fileBean.setFileType(file.getContentType());
		fileBean.setSize(file.getSize());
		res.setData(fileBean);
		res.setErrorCode(0);
		return ResponseEntity.ok(res);
	}

	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request)
			throws Exception {
		Resource resource = fileStorage.loadFileAsResource(fileName);
		String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		if (contentType == null) {
			contentType = "application/octet-stream";
			System.out.println("hre");
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
						"attachment;fileName=\"" + resource.getFilename() + "\"")
				.body(resource);

	}
}
