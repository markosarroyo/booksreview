package com.booksreview.bookservice.client;

import com.booksreview.bookservice.dto.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;
    private static final String USER_SERVICE_URL = "http://localhost:8080/api/users";

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getAuthorName(String authorId) {
        try {
            UserDTO user = restTemplate.getForObject(
                    USER_SERVICE_URL + "/" + authorId, UserDTO.class
            );
            return user != null ? user.getName() : "Desconocido";
        } catch (Exception e) {
            return "Desconocido";
        }
    }
}
