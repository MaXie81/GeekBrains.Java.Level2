package ru.geekbrains.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrimitiveAuthService implements AuthService {
    private List<Client> clients;

    public PrimitiveAuthService() {
        clients = new ArrayList<>(Arrays.asList(
                new Client("Вася", "Вася", "pass1"),
                new Client("Жанна", "Жанна", "pass2"),
                new Client("Петя", "Петя", "pass3")
        ));
    }

    @Override
    public void start() {
        System.out.println("Auth started");
    }

    @Override
    public void stop() {
        System.out.println("Auth stopped");
    }

    @Override
    public String getUsernameByLoginPass(String login, String pass) {
        for (Client c : clients) {
            if (c.getLogin().equals(login) && c.getPassword().equals(pass)) return c.getUsername();
        }
        return null;
    }
}
