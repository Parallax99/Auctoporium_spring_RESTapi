package com.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bean.UserBean;

@Repository
public interface UserRepository extends CrudRepository<UserBean, Integer> {
//	@Query("SELECT u FROM UserBean u JOIN FETCH u.roles where email=?1")
//	@Query(value = "SELECT email FROM users u , roles where u.email= ?1 and u.roleid = role.roleid", nativeQuery = true)
	UserBean findByEmail(String email);

	UserBean findByEmailAndPassword(String Email, String Password);
//	Criteria criteria = session.createCriteria(User.class);
//	criteria.setFetchMode("roles", FetchMode.EAGER);

	UserBean findByUserId(int userId);

}
