package br.com.taxApi.taxcalculator.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "id")
    private  Long id;
    @Column (name = "name", nullable = false)
    private  String name;
    @Column (name = "age", nullable = false)
    private  int age;
    @Column (name = "email", nullable = false)
    private  String email;
    @Column (name = "password", nullable = false)
    private  String password;
    @Column (name = "office", nullable = false)
    private  String office;
    @Column (name = "salary", nullable = false)
    private double salary;
    @Column (name = "active")
    private boolean active;
}
