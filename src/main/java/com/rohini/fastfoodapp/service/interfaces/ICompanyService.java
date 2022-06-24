package com.rohini.fastfoodapp.service.interfaces;

import com.rohini.fastfoodapp.model.Company;

import java.util.Collection;

/**
 * @author Rohini
 */
public interface ICompanyService {

    Company create(Company company);

    Collection<Company> list();

    Company update(Long idCompany, Company company);

    Boolean delete(Long idCompany);

    Boolean exist(Long idCompany);

}
