package br.com.taxApi.taxcalculator.dto;

import lombok.Data;

@Data
public class UtiliterCalculator {
    private Long id;
    private double tax;
    private String message;
}
