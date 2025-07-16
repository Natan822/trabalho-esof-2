package catalog_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import catalog_service.dto.UserDTO;

@Component
public class UserClient {

    private final RestTemplate restTemplate;

    public UserClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDTO getUserById(Long id) {
        return restTemplate.getForObject("http://localhost:8081/users/" + id, UserDTO.class);
    }
}
