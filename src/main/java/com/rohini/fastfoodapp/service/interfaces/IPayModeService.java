/**
 * 
 */
package com.rohini.fastfoodapp.service.interfaces;

import java.util.Collection;

import com.rohini.fastfoodapp.model.PayMode;

/**
 * @author Rohini
 */
public interface IPayModeService {

	PayMode create(PayMode payMode);

	PayMode update(Long id, PayMode payMode);

	Boolean delete(Long idPayMode);

	Collection<PayMode> list();

	Boolean exist(Long idPayMode);

}
