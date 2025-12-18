package br.com.five.seven.food.domain.model;

import br.com.five.seven.food.infra.utils.FoodUtils;
import java.time.LocalDateTime;

public class Users extends TimeAt {

    private String id;
    private String cpf;
    private String name;
    private String email;
    private String phone;

    public Users(String id, String cpf, String name, String email, String phone) {
        this.id = id;
        this.cpf = cpf;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Users() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = FoodUtils.limparString(cpf);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

abstract class TimeAt {

    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}