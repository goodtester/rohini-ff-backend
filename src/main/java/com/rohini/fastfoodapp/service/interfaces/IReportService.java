/**
 * 
 */
package com.rohini.fastfoodapp.service.interfaces;

import java.text.ParseException;
import java.util.Collection;

import com.rohini.fastfoodapp.dto.*;

/**
 * @author Rohini
 */
public interface IReportService {

	Collection<ReportProductDTO> getRankProducts(Long idProduct, Integer limit, String startDate, String endDate);
	
	Collection<ReportClientDTO> getRankClient(String username, String startDate, String endDate) throws ParseException;
	
	Collection<ReportSalesDTO> getSalesByDate(String startDate, String endDate);

	Collection<ReportSalesMonthlyDTO> getSalesMonthly();

	Collection<ReportSalesWeeklyDTO> getSalesPerWeek() throws ParseException;
	Collection<ReportPayModeDTO> getPayModeQuantity();
}
