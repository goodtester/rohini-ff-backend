package com.rohini.fastfoodapp.service.interfaces;

import com.rohini.fastfoodapp.model.Tax;

import java.util.Collection;

/**
 * @author Rohini
 */
public interface ITaxService {
    Tax create(Tax tax);

    Collection<Tax> read();

    Tax update(Long idTax, Tax tax);

    Boolean delete(Long idTax);

    Boolean exist(Long idTax);
}
