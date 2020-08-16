package com.winsun.upssm.service;

public interface userService {

    String login(String username, String password);

    void registered(String username, String password);
}
