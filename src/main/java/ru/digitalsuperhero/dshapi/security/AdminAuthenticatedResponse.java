package ru.digitalsuperhero.dshapi.security;

import lombok.Data;
import ru.digitalsuperhero.dshapi.dao.domain.Admin;

@Data
public class AdminAuthenticatedResponse extends Admin {
    private final String token;

    public AdminAuthenticatedResponse(Admin admin, String token) {
        this.token = token;
        super.setId(admin.getId());
        super.setUsername(admin.getUsername());
        super.setPassword(admin.getPassword());
    }
}
