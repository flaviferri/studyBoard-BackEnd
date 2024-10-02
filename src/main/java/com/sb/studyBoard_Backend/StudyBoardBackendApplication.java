package com.sb.studyBoard_Backend;

import com.sb.studyBoard_Backend.model.PermissionEntity;
import com.sb.studyBoard_Backend.model.RoleEntity;
import com.sb.studyBoard_Backend.model.RoleEnum;
import com.sb.studyBoard_Backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Set;

@SpringBootApplication
public class StudyBoardBackendApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(StudyBoardBackendApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
						.addMapping("/auth/github/callback")
						.allowedOrigins("http://localhost:4001/");
			}
		};
	}

/*	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {
			PermissionEntity createPostitPermission = PermissionEntity.builder()
					.name("CREATE_POSTIT")
					.build();

			PermissionEntity readPostitPermission = PermissionEntity.builder()
					.name("READ_POSTIT")
					.build();

			PermissionEntity updatePostitPermission = PermissionEntity.builder()
					.name("UPDATE_POSTIT")
					.build();

			PermissionEntity deletePostitPermission = PermissionEntity.builder()
					.name("DELETE_POSTIT")
					.build();
			PermissionEntity refactorPermission = PermissionEntity.builder()
					.name("REFACTOR")
					.build();

			*//*Create Role*//*
			RoleEntity roleAdmin = RoleEntity.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permissions(Set.of(createPostitPermission, readPostitPermission, updatePostitPermission, deletePostitPermission, refactorPermission))
					.build();

			RoleEntity roleCreated = RoleEntity.builder()
					.roleEnum(RoleEnum.CREATED)
					.permissions(Set.of(createPostitPermission, readPostitPermission, updatePostitPermission, deletePostitPermission))
					.build();

			RoleEntity roleUser = RoleEntity.builder()
					.roleEnum(RoleEnum.USER)
					.permissions(Set.of(createPostitPermission, readPostitPermission, updatePostitPermission, deletePostitPermission))
					.build();
		};


	}*/
}