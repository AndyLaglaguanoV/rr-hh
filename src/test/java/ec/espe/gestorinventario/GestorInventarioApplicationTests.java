package ec.espe.gestorinventario;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "langchain4j.open-ai.chat-model.api-key=test-key")
class GestorInventarioApplicationTests {

    @Test
    void contextLoads() {
    }
}