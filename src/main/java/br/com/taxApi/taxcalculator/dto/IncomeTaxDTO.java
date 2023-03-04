package br.com.taxApi.taxcalculator.dto;

import lombok.Data;

@Data
public class IncomeTaxDTO {
    private double tax;
    private String message;
}
