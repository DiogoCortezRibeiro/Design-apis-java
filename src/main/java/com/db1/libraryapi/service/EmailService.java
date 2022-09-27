package com.db1.libraryapi.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmailService {
    void sendEmails(List<String> emails, String message);
}
