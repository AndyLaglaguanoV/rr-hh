package ec.espe.gestorinventario.controller;

import ec.espe.gestorinventario.config.AppInfoProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InfoController.class)
@EnableConfigurationProperties
@Import(AppInfoProperties.class)
@TestPropertySource(properties = {
        "app.info.name=Gestor de Inventario",
        "app.info.description=API de prueba",
        "app.info.version=1.0.0",
        "app.info.environment=test",
        "app.info.developer.name=Dayanara",
        "app.info.developer.email=dayanara@espe.edu.ec"
})
class InfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void infoDevuelve200YEstructuraCorrecta() throws Exception {
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gestor de Inventario"))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.environment").value("test"))
                .andExpect(jsonPath("$.developer.email")
                        .value(matchesPattern("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")));
    }
}