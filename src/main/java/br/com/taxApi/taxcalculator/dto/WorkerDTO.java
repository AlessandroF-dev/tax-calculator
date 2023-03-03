package br.com.taxApi.taxcalculator.dto;

import lombok.Data;

@Data
public class WorkerDTO {
    private Long id;
    private String name;
    private int age;
    private String login;
    private String password;
    private String office;
    private double salary;
}