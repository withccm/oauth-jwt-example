package com.withccm.oauthapi.component.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.withccm.oauthapi.common.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final UserRepository userRepository;

	@GetMapping("/myProfile")
	public ApiResponse<User> getMyProfile() {
		org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Long id = Long.valueOf(principal.getUsername());// username 은 id임
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("사용자가 없습니다."));
		log.info("principal === {}, user === {}", principal, user);

		return ApiResponse.success(user);
	}
}
