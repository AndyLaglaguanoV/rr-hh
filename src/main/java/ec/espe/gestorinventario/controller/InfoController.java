package ec.espe.gestorinventario.controller;

import ec.espe.gestorinventario.config.AppInfoProperties;
import ec.espe.gestorinventario.dto.InfoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api")
public class InfoController {

    private final AppInfoProperties props;

    public InfoController(AppInfoProperties props) {
        this.props = props;
    }

    @GetMapping("/info")
    public InfoResponse info() {
        var dev = props.getDeveloper();
        return new InfoResponse(
                props.getName(),
                props.getDescription(),
                props.getVersion(),
                props.getEnvironment(),
                new InfoResponse.DeveloperInfo(dev.getName(), dev.getEmail()),
                Instant.now());
    }
}