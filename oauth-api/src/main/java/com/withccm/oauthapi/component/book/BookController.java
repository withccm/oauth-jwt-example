package com.withccm.oauthapi.component.book;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.withccm.oauthapi.common.ApiResponse;
import com.withccm.oauthapi.common.ApiResponseCode;
import com.withccm.oauthapi.common.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

	private final BookRepository bookRepository;

	@GetMapping
	public ApiResponse<List<Book>> getBooks() {
		List<Book> books = bookRepository.findAll();
		log.debug("books === {}", books);
		return ApiResponse.success(books);
	}

	@GetMapping("/error")
	public List<Book> getBookError() {
		throw new CustomException(ApiResponseCode.SERVER_ERROR);
	}
}
