package com.db1.libraryapi.utils;

import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    private List<String> erros;

    public ApiErrors(BindingResult bindingResult) {
        this.erros = new ArrayList<>();
        bindingResult.getAllErrors().forEach(e -> {
            this.erros.add(e.getDefaultMessage());
        });
    }

    public ApiErrors(ResponseStatusException ex) {
        this.erros = Arrays.asList(ex.getReason());
    }

    public List<String> getErros() {
        return erros;
    }
}
