package space.nixus.pubtrans.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

/**
 * Swagger configuration for setting bearer requirement for controllers.
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Public Transportation API", version = "v1"))
@SecurityScheme(
    name = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenAPIConfig {}
