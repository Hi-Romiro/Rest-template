package ru.romiro;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.romiro.models.Employee;

import java.util.Objects;

public class RestApiClient {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        String baseUrl = "http://94.198.50.185:7081/api/users";
        HttpHeaders headers = new HttpHeaders();

        // 1. GET-запрос для получения списка пользователей и сохранение Cookie
        ResponseEntity<String> getResponse = restTemplate.getForEntity(baseUrl, String.class);
        String cookie = String.join(";", Objects.requireNonNull(getResponse.getHeaders().get("Set-Cookie")));
        headers.set("Cookie", cookie);
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println("Cookie получен: " + cookie);

        // 2. POST-запрос: добавление нового пользователя
        Employee employee = new Employee(3L, "James", "Brown", (byte) 30);
        HttpEntity<Employee> postRequest = new HttpEntity<>(employee, headers);
        String postResponse = restTemplate.postForObject(baseUrl, postRequest, String.class);
        System.out.println("Ответ после POST: " + postResponse);

        // 3. PUT-запрос: обновление пользователя
        employee.setName("Thomas");
        employee.setLastName("Shelby");
        HttpEntity<Employee> putRequest = new HttpEntity<>(employee, headers);
        ResponseEntity<String> putResponse = restTemplate.exchange(baseUrl, HttpMethod.PUT, putRequest, String.class);
        System.out.println("Ответ после PUT: " + putResponse.getBody());

        // 4. DELETE-запрос: удаление пользователя
        String deleteUrl = baseUrl + "/3";
        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers);
        ResponseEntity<String> deleteResponse = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, deleteRequest, String.class);
        System.out.println("Ответ после DELETE: " + deleteResponse.getBody());

        // 5. Получение итогового кода
        String finalCode = postResponse + putResponse.getBody() + deleteResponse.getBody();
        System.out.println("Итоговый код: " + finalCode);
    }
}