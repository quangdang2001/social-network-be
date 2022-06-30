package com.cnpm.socialmedia;

import com.cnpm.socialmedia.repo.MessageRepo;
import com.cnpm.socialmedia.repo.PostRepo;
import com.cnpm.socialmedia.repo.UserRepo;
import com.cnpm.socialmedia.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class SocialmediaApplicationTests {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private UserService userService;
	@Autowired
	private PostRepo postRepo;
	@Autowired
	private MessageRepo messageRepo;


	@Test
	public void test(){
		Pageable pageable = PageRequest.of(0,10);
		System.out.println(messageRepo.findSenderChat(Long.parseLong("1"),pageable));

	}

}
