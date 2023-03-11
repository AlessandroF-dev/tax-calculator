package br.com.taxApi.taxcalculator.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class WorkerAdm {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column (name = "login", nullable = false)
    private String login;
    @Column (name = "password", nullable = false)
    private String password;
}
