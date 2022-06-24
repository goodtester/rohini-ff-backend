package com.rohini.fastfoodapp.dto;

import lombok.Data;

/**
 * @author Rohini
 */
@Data
public class ReportSalesWeeklyDTO {
    private int weekday;
    private double total;
}
