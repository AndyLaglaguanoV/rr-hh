package ec.espe.gestorinventario.controller;

import dev.langchain4j.model.chat.ChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IaController.class)
class IaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChatModel chatModel;

    @Test
    void chatDevuelveLaRespuestaDelModelo() throws Exception {
        when(chatModel.chat("Hola")).thenReturn("Respuesta simulada");

        mockMvc.perform(post("/api/ia/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"Hola\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("Respuesta simulada"));
    }

    @Test
    void chatConMensajeVacioDevuelve400() throws Exception {
        mockMvc.perform(post("/api/ia/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}