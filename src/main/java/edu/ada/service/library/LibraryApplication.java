package edu.ada.service.library;

import edu.ada.service.library.model.repository.CommentRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackageClasses = CommentRepository.class)
@EnableJpaRepositories(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CommentRepository.class))
@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

}
