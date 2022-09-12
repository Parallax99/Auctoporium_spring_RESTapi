package com.seed;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.bean.CategoryBean;
import com.bean.RoleBean;
import com.repository.CategoryRepository;
import com.repository.RoleRepository;
import com.repository.UserRepository;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class IntialRoleSeed {
	@Autowired
	RoleRepository roleRepo;
	@Autowired
	UserRepository userRepo;
	@Autowired
	PasswordEncoder bCrypt;
	@Autowired
	CategoryRepository catRepo;

	@PostConstruct
	void createRoles() {
		List<RoleBean> role = roleRepo.findByRoleName("admin");
		List<RoleBean> role1 = roleRepo.findByRoleName("user");
		if (role.isEmpty() && role1.isEmpty()) {
			RoleBean roleu = new RoleBean();
			roleu.setRoleName("user");
			roleRepo.save(roleu);

			RoleBean rolea = new RoleBean();
			rolea.setRoleName("admin");
			roleRepo.save(rolea);
			System.out.println("Role Added.....");

		} else {
			System.out.println("Role Already Added....");
		}
		CategoryBean housing = catRepo.findByCategoryName("Housing");
		CategoryBean electronics = catRepo.findByCategoryName("Electronics");
		CategoryBean antiques = catRepo.findByCategoryName("Antiques");
		if (housing == null) {
			CategoryBean house = new CategoryBean();
			house.setCategoryName("Housing");
			catRepo.save(house);
		}
		if (electronics == null) {
			CategoryBean Electronics = new CategoryBean();
			Electronics.setCategoryName("Electronics");
			catRepo.save(Electronics);
		}
		if (antiques == null) {
			CategoryBean Antique = new CategoryBean();
			Antique.setCategoryName("Antiques");
			catRepo.save(Antique);
		}
		if (housing != null && electronics != null && antiques != null) {
			log.info("Categories Already Added");
		}
	}

//	@PostConstruct
//	void createSuperAdmin() {
//		UserBean user = userRepo.findByEmail("admin@gmail.com");
//		log.info(user);
//		if (user == null) {
//			UserBean newUser = new UserBean();
//			newUser.setActive(true);
//			newUser.setEmail("admin@gmail.com");
//			newUser.setPassword(bCrypt.encode("admin"));
//			newUser.setFirstName("admin");
//			newUser.setGender("robo");
//			newUser.setLastName("online");
//			List<RoleBean> roles = new ArrayList<>();
//			roles.add((RoleBean) roleRepo.findByRoleName("admin"));
//			roles.add((RoleBean) roleRepo.findByRoleName("user"));
//			newUser.setRoles(roles);
//			userRepo.save(newUser);
//			log.info("super admin added");
//		} else {
//			log.info("Super Admin already Exists");
//		}
//	}

}
