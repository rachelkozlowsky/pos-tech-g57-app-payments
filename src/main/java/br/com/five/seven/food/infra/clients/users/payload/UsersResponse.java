package br.com.five.seven.food.infra.clients.users.payload;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersResponse {

    private String id;
    private String cpf;
    private String name;
    private String email;
    private String phone;
}