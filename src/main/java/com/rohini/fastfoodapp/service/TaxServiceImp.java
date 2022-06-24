package com.rohini.fastfoodapp.service;

import com.rohini.fastfoodapp.model.Tax;
import com.rohini.fastfoodapp.repository.ITaxRepository;
import com.rohini.fastfoodapp.service.interfaces.ITaxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

/**
 * @author Rohini
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TaxServiceImp implements ITaxService {

    @Autowired
    private ITaxRepository taxRepository;

    @Override
    public Tax create(Tax tax) {
        log.info("Creating tax");
        return taxRepository.save(tax);
    }

    @Override
    public Collection<Tax> read() {
        log.info("list taxes");
        return taxRepository.findAll();
    }

    @Override
    public Tax update(Long idTax, Tax tax) {
        log.info("Updating tax");
        Tax taxOld= taxRepository.findById(idTax).get();
        tax.setIdTax(taxOld.getIdTax());
        return taxRepository.save(tax);
    }

    @Override
    public Boolean delete(Long idTax) {
        log.info("Deleting tax");
        taxRepository.deleteById(idTax);
        return true;
    }

    @Override
    public Boolean exist(Long idTax) {
        log.info("Exist tax");
        taxRepository.existsById(idTax);
        return true;
    }
}
