package br.com.five.seven.food.infra.clients.users.service;

import br.com.five.seven.food.application.ports.out.UsersClientOut;
import br.com.five.seven.food.infra.clients.users.http.UsersClient;
import br.com.five.seven.food.infra.clients.users.payload.UsersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UsersClientService implements UsersClientOut {

    private final UsersClient usersClient;

    @Override
    public UsersResponse getUserByCpf(String cpf) {
        return usersClient.getUserByCpf(cpf).getBody();
    }
}
