package br.com.five.seven.food.application.ports.out;

import br.com.five.seven.food.infra.clients.users.payload.UsersResponse;

public interface UsersClientOut {
    UsersResponse getUserByCpf(String cpf);
}
