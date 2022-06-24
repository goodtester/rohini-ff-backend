/**
 *
 */
package com.rohini.fastfoodapp.service;

import com.rohini.fastfoodapp.dto.BillUserDTO;
import com.rohini.fastfoodapp.dto.OrdersDTO;
import com.rohini.fastfoodapp.dto.UserBillOrdersDTO;
import com.rohini.fastfoodapp.dto.validation.UserForBillDTO;
import com.rohini.fastfoodapp.enumeration.StatusBill;
import com.rohini.fastfoodapp.enumeration.StatusOrder;
import com.rohini.fastfoodapp.model.Additional;
import com.rohini.fastfoodapp.model.Bill;
import com.rohini.fastfoodapp.model.Orders;
import com.rohini.fastfoodapp.model.PayMode;
import com.rohini.fastfoodapp.repository.IBillRepository;
import com.rohini.fastfoodapp.repository.IOrdersRepository;
import com.rohini.fastfoodapp.service.interfaces.IBillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Rohini
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BillServiceImp implements IBillService {

    @Autowired
    private final IOrdersRepository ordersRepository;
    @Autowired
    private IBillRepository billRepository;

    @Override
    public BillUserDTO create(Bill bill) {
        log.info("Saving new bill");
        if (bill.getUser().getIdUser() == null) {
            bill.setUser(null);
        }
        bill.setStatusBill(StatusBill.PENDING);
        return convertBillToDTO(billRepository.save(bill));
    }

    @Override
    public BillUserDTO update(Long idBill, Bill bill) {
        log.info("Updating bill with id: " + idBill);
        int totalOrder = 0;
        Bill billOld = billRepository.findByIdBill(idBill);
        if (bill.getStatusBill().equals(StatusBill.PAID)) {
            Collection<Orders> orders = ordersRepository.findByIdBill(idBill);
            for (Orders order : orders) {
                totalOrder += order.getTotal();
            }
            billOld.setTotalPrice(totalOrder);
        }
        billOld.setPayMode(bill.getPayMode());
        billOld.setNoTable(bill.getNoTable());
        billOld.setStatusBill(bill.getStatusBill());
        billOld.setDate(bill.getDate());
        return convertBillToDTO(billRepository.save(billOld));
    }

    @Override
    public BillUserDTO updateStatusBill(Long idBill, StatusBill statusBill) {
        log.info("Updating bill with id: " + idBill);
        Bill billOld = billRepository.findByIdBill(idBill);
        billOld.setStatusBill(statusBill);
        return convertBillToDTO(billRepository.save(billOld));
    }

    @Override
    public Boolean delete(Long idBill) {
        log.info("Deleting the bill id: " + idBill);
        if (billRepository.existsById(idBill)) {
            Bill bill = billRepository.findById(idBill).get();
            bill.setStatusBill(StatusBill.DELETED);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean exist(Long idBill) {
        log.info("Searching bills by id: " + idBill);
        return billRepository.existsById(idBill);
    }

    @Override
    public UserBillOrdersDTO findByIdBill(Long idBill) {
        return convertBillOrderToDTO(billRepository.findByIdBill(idBill));
    }

    @Override
    public Collection<UserBillOrdersDTO> findByOrder(
            StatusOrder statusOrder, String startDate, String endDate) {
        try {
            log.info("Searching bills by StatusOrder DateBetween" + startDate);
            return billRepository
                    .findByDateBetweenAndStatusOrder(
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate),
                            statusOrder.ordinal())
                    .stream()
                    .map(this::convertBillOrderToDTO)
                    .collect(Collectors.toList());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<UserBillOrdersDTO> findByNewIdUser(
            String username, StatusBill statusBill, String startDate, String endDate) {
        if (username != null && statusBill != null && startDate != null && endDate != null) {
            try {
                log.info("Searching bills by User StatusBill DateBetween");
                return billRepository
                        .findByIdUserAndStatusBillAndDateBetween(
                                username,
                                statusBill.ordinal(),
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate),
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate))
                        .stream()
                        .map(this::convertBillOrderToDTO)
                        .collect(Collectors.toList());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (username == null && statusBill == null && startDate == null && endDate == null) {
            log.info("Searching bills");
            return billRepository.findAll().stream()
                    .map(this::convertBillOrderToDTO)
                    .collect(Collectors.toList());
        }
        if (username != null && statusBill == null && startDate == null && endDate == null) {
            log.info("Searching bills by User");
            return billRepository.findByIdUser(username).stream()
                    .map(this::convertBillOrderToDTO)
                    .collect(Collectors.toList());
        }
        if (username == null && statusBill != null && startDate == null && endDate == null) {
            log.info("Searching bills by StatusBill");
            return billRepository.findByStatusBill(statusBill).stream()
                    .map(this::convertBillOrderToDTO)
                    .collect(Collectors.toList());
        }
        if (username != null && statusBill != null && startDate == null && endDate == null) {
            log.info("Searching bills by User StatusBill");
            return billRepository.findByIdUserAndStatusBill(username, statusBill.ordinal()).stream()
                    .map(this::convertBillOrderToDTO)
                    .collect(Collectors.toList());
        }
        if (username == null && statusBill == null && startDate != null && endDate != null) {
            try {
                log.info("Searching bills by DateBetween");
                return billRepository
                        .findByDateBetween(
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate),
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate))
                        .stream()
                        .map(this::convertBillOrderToDTO)
                        .collect(Collectors.toList());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (username != null && statusBill == null && startDate != null && endDate != null) {
            try {
                log.info("Searching bills by User DateBetween");
                return billRepository
                        .findByDateBetweenAndIdUser(
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate),
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate),
                                username)
                        .stream()
                        .map(this::convertBillOrderToDTO)
                        .collect(Collectors.toList());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (username == null && statusBill != null && startDate != null && endDate != null) {
            try {
                log.info("Searching bills by StatusBill DateBetween");
                return billRepository
                        .findByDateBetweenAndStatusBill(
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate),
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate),
                                statusBill)
                        .stream()
                        .map(this::convertBillOrderToDTO)
                        .collect(Collectors.toList());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    private BillUserDTO convertBillToDTO(Bill bill) {

        BillUserDTO billUser = new BillUserDTO();
        billUser.setIdBill(bill.getIdBill());
        billUser.setIdTransaction(bill.getIdTransaction());
        billUser.setNoTable(bill.getNoTable());
        billUser.setTotalPrice(bill.getTotalPrice());

        convertPartBillDTO(bill, billUser);

        return billUser;
    }

    private UserBillOrdersDTO convertBillOrderToDTO(Bill bill) {
        UserBillOrdersDTO billOrder = new UserBillOrdersDTO();

        BillUserDTO billUser = new BillUserDTO();
        billUser.setIdBill(bill.getIdBill());
        billUser.setNoTable(bill.getNoTable());

        convertPartBillDTO(bill, billUser);
        Collection<OrdersDTO> orders =
                ordersRepository.findByIdBill(bill.getIdBill()).stream()
                        .map(this::convertOrderToDTO)
                        .collect(Collectors.toList());

        int totalPrice = 0;

        for (OrdersDTO order : orders) {
            totalPrice += order.getTotal();
        }
        billUser.setTotalPrice(totalPrice);
        billOrder.setBillUserDTO(billUser);
        billOrder.setOrdersDTO(orders);
        return billOrder;
    }

    private OrdersDTO convertOrderToDTO(Orders orders) {

        OrdersDTO billOrder = new OrdersDTO();
        billOrder.setIdOrder(orders.getIdOrder());
        billOrder.setStatusOrder(orders.getStatusOrder());
        billOrder.setAmount(orders.getAmount());
        billOrder.setTotal(orders.getTotal());

        billOrder.setProduct(orders.getProduct());

        Collection<Additional> additional = new ArrayList<Additional>();
        additional.addAll(orders.getAdditional());
        billOrder.setAdditional(additional);

        return billOrder;
    }

    private void convertPartBillDTO(Bill bill, BillUserDTO billUser) {
        if (bill.getUser() != null) {
            UserForBillDTO userForBill = new UserForBillDTO();
            userForBill.setIdUser(bill.getUser().getIdUser());
            userForBill.setUrlImage(bill.getUser().getUrlImage());
            userForBill.setUsername(bill.getUser().getUsername());
            userForBill.setName(bill.getUser().getName());
            billUser.setUserForBill(userForBill);
        }

        PayMode payMode = new PayMode();
        payMode.setIdPayMode(bill.getPayMode().getIdPayMode());
        payMode.setName(bill.getPayMode().getName());
        payMode.setStatus(bill.getPayMode().getStatus());

        billUser.setPayMode(payMode);

        billUser.setDate(bill.getDate());
        billUser.setStatusBill(bill.getStatusBill());
    }
}
