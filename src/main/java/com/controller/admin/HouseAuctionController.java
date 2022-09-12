package com.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bean.HouseBean;
import com.bean.ResponseBean;
import com.repository.HouseRepository;
import com.repository.ProductRepository;

import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin
@Log4j2
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class HouseAuctionController {
	@Autowired
	ProductRepository productRepo;
	@Autowired
	HouseRepository houseRepo;
//	@Autowired
//	ResponseBean<HouseBean> res;

	@PostMapping("/addHouse")
	public ResponseEntity<?> addProduct(@RequestBody HouseBean housebean) {
		ResponseBean<HouseBean> res = new ResponseBean<>();
		if (housebean == null) {
			res.setData(housebean);
			res.setMsg("House Added");
			res.setErrorCode(-1);
			return ResponseEntity.badRequest().body(res);
		} else {
			houseRepo.save(housebean);
			res.setData(housebean);
			res.setMsg("House Added Successfully");
			res.setErrorCode(0);
			return ResponseEntity.ok(res);
		}
	}

	@GetMapping("listHouses")
	public ResponseEntity<?> listHouses() {
		ResponseBean<List<HouseBean>> res = new ResponseBean<>();
		List<HouseBean> houses = houseRepo.findAll();
		if (houses == null) {
			res.setData(null);
			res.setMsg("NO Houses to list");
			res.setErrorCode(-1);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
		} else {
			res.setData(houses);
			res.setMsg("List Retrieved");
			res.setErrorCode(0);
			return ResponseEntity.ok(res);
		}
	}
}
