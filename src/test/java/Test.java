import com.fasterxml.jackson.annotation.JsonFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static java.lang.reflect.Array.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class) // Integra el soporte de Spring con JUnit 5
@SpringBootTest // Carga el contexto completo de la aplicación
@AutoConfigureMockMvc // Configura automáticamente MockMvc
public class Test {

    @Autowired
    private MockMvc mvc; // MockMvc inyectado automáticamente

   @org.junit.jupiter.api.Test
    public void cuandoOAuth2Login_thenAuthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/endpoint-protegido")
                        .with(oauth2Login())) // Simula un usuario autenticado mediante OAuth2
                .andExpect(status().isOk()); // Verifica que el estado HTTP sea 200 OK
    }
}