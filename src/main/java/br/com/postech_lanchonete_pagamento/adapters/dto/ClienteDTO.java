package br.com.postech_lanchonete_pagamento.adapters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("unused")

@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {
    private String nome;

    private String sobrenome;

    private String cpf;

    private String email;

}