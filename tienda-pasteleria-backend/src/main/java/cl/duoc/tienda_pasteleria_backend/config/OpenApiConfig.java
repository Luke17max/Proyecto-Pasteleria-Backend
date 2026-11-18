package cl.duoc.tienda_pasteleria_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Pastelería Mil Sabores - DSY1104")
                        .version("v1.0")
                        .description("Documentación interactiva de la API REST para la gestión de productos, órdenes, usuarios y autenticación de la Pastelería Mil Sabores.")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo - Duoc UC")
                                .email("soporte@pasteleriamilsabores.cl"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
