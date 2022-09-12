package com.controller.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bean.CategoryBean;
import com.bean.ResponseBean;
import com.repository.CategoryRepository;

import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin
@Log4j2
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class CategoryController {
	@Autowired
	CategoryRepository catRepo;

	@PostMapping("/addCategory")
	public ResponseEntity<?> addCategory(@RequestBody CategoryBean category) {
		ResponseBean<CategoryBean> res = new ResponseBean<>();
		if (category == null) {
			res.setData(category);
			res.setMsg("Please Enter Some Category");
			res.setErrorCode(-1);
			return ResponseEntity.badRequest().body(res);
		} else if (catRepo.findByCategoryName(category.getCategoryName()) != null) {
			res.setData(category);
			res.setMsg("This Category already Exists");
			res.setErrorCode(0);
			return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
		} else {
			catRepo.save(category);
			res.setData(category);
			res.setMsg("Category Added Successfully");
			res.setErrorCode(0);
			return ResponseEntity.ok(res);
		}
	}

	@GetMapping("/listCategories")
	public ResponseEntity<?> listCategories() {
		return ResponseEntity.ok(catRepo.findAll());
	}

	@PutMapping("/updateCategory")
	public ResponseEntity<?> updateCategory(@RequestBody CategoryBean catbean) {
		Optional<CategoryBean> category = catRepo.findById(catbean.getCategoryId());
		ResponseBean<CategoryBean> res = new ResponseBean<>();
		if (category != null) {
			category.get().setCategoryName(catbean.getCategoryName());
			catRepo.save(category.get());
			res.setData(catbean);
			res.setMsg("Catgeory Updated");
			res.setErrorCode(0);
			return ResponseEntity.ok(res);
		} else {
			res.setData(catbean);
			res.setMsg("Category doesn't exists");
			res.setErrorCode(-1);
			return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(res);
		}
	}

	@DeleteMapping("/deleteCategory/{categoryId}")
	public ResponseEntity<?> deleteCategory(@PathVariable("categoryId") int categoryId) {
		ResponseBean<CategoryBean> res = new ResponseBean<>();
		if (catRepo.findById(categoryId).isEmpty()) {
			res.setMsg("There is no Such Category ");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		} else {
			catRepo.deleteById(categoryId);
			res.setMsg("Category Successfully deleted");
			res.setErrorCode(0);
			return ResponseEntity.ok(res);
		}
	}
}
