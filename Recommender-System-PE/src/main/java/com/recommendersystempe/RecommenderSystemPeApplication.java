package com.recommendersystempe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Recommendation System PE API", version = "1.0", description = "Trabalho de conclusão de curso de Engenharia de Software na Universidade de São Paulo (USP)",
contact = @Contact(name = "Douglas Fragoso", email = "douglas.iff@gmail.com", url = "https://github.com/douglasfragoso"), 
license = @License(name = "MIT License", url = "https://opensource.org/licenses/MIT")))
@SecuritySchemes({@SecurityScheme(name = "Bearer Token", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", description = "Autenticação JWT usando o esquema Bearer")})
public class RecommenderSystemPeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecommenderSystemPeApplication.class, args);
	}

}
