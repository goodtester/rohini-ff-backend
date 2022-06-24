/**
 *
 */
package com.rohini.fastfoodapp.service;

import com.rohini.fastfoodapp.dto.OrdersDTO;
import com.rohini.fastfoodapp.enumeration.StatusOrder;
import com.rohini.fastfoodapp.model.Additional;
import com.rohini.fastfoodapp.model.Orders;
import com.rohini.fastfoodapp.model.Product;
import com.rohini.fastfoodapp.repository.IAdditionalRepository;
import com.rohini.fastfoodapp.repository.IOrdersRepository;
import com.rohini.fastfoodapp.repository.IProductRepository;
import com.rohini.fastfoodapp.service.interfaces.IOrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Rohini
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrdersServiceImp implements IOrdersService {

    @Autowired
    private final IOrdersRepository ordersRepository;
    @Autowired
    private final IProductRepository productRepository;
    @Autowired
    private final IAdditionalRepository additionalRepository;

    @Override
    public OrdersDTO create(Orders orders) {
        log.info("Saving new order");
        orders.setStatusOrder(StatusOrder.NEW);

        return convertOrderToDTO(ordersRepository.save(orders));
    }

    @Override
    public OrdersDTO update(Long id, Orders orders) {
        log.info("Updating orders with id: " + id);
        Orders ordersOld = ordersRepository.findById(id).get();
        orders.setIdOrder(ordersOld.getIdOrder());
        return convertOrderToDTO(ordersRepository.save(orders));
    }

    @Override
    public Boolean updateStatus(Long id, StatusOrder statusOrder) {
        log.info("Updating updateStatus with bill id: " + id);
        Collection<Orders> ordersOld = ordersRepository.findByIdBill(id);
        try {
            ordersOld.forEach(element -> {
                element.setStatusOrder(statusOrder);
                ordersRepository.save(element);
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean delete(Long idOrders) {
        log.info("Deleting the order with id: " + idOrders);
        if (ordersRepository.existsById(idOrders)) {
            ordersRepository.deleteById(idOrders);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean exist(Long idOrders) {
        log.info("Searching order by id: " + idOrders);
        return ordersRepository.existsById(idOrders);
    }

    public Orders findByIdOrder(Long idOrders) {
        log.info("Searching order by id: " + idOrders);
        return ordersRepository.findById(idOrders).get();
    }

    private OrdersDTO convertOrderToDTO(Orders orders) {
        int totalAdditional = 0;
        if (orders.getProduct().getIdProduct() != null) {
            Product product = productRepository.findByIdProduct(orders.getProduct().getIdProduct());
            if (orders.getAdditional() != null) {
                Collection<Additional> additionals = orders.getAdditional();

                for (Additional additional : additionals) {
                    Additional additionalRequest = additionalRepository
                            .findByIdAdditional(additional.getIdAdditional());
                    totalAdditional += additionalRequest.getPrice();
                }
            }
            orders.setTotal((product.getPrice() * orders.getAmount()) + (totalAdditional * orders.getAmount()));

        } else {
            return null;
        }

        OrdersDTO Order = new OrdersDTO();
        Order.setIdOrder(orders.getIdOrder());
        Order.setStatusOrder(orders.getStatusOrder());
        Order.setAmount(orders.getAmount());

        Order.setTotal(orders.getTotal());
        //this is important because this method update the totalPrice
        ordersRepository.setTotalPriceBill(orders.getBill().getIdBill());

        Order.setProduct(orders.getProduct());

        Collection<Additional> additional = new ArrayList<Additional>();
        additional.addAll(orders.getAdditional());
        Order.setAdditional(additional);

        return Order;
    }



}
