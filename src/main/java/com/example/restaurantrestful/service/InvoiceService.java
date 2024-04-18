package com.example.restaurantrestful.service;

import com.example.restaurantrestful.dto.inputs.invoice.GetInvoiceByDateRangeInput;
import com.example.restaurantrestful.entity.Invoice;
import com.example.restaurantrestful.entity.Order;
import com.example.restaurantrestful.enums.PaymentTypeEnums;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final OrderService orderService;

    public InvoiceService(InvoiceRepository invoiceRepository, OrderService orderService) {
        this.invoiceRepository = invoiceRepository;
        this.orderService = orderService;
    }

    public Invoice getInvoiceById(String id) {
        return invoiceRepository.findById(id).orElseThrow(CustomException::invoiceNotFound);
    }

    public Invoice getInvoiceByOrderId(String orderId) {
        return invoiceRepository.findByOrderId(orderId).orElseThrow(CustomException::invoiceNotFound);
    }

    public List<Invoice> getInvoiceByDateRange(GetInvoiceByDateRangeInput getInvoiceByDateRangeInput) {
        Date startDate = Date.from(getInvoiceByDateRangeInput.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(getInvoiceByDateRangeInput.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        return invoiceRepository.findByDateRange(startDate, endDate);
    }

    @Transactional
    public Invoice createInvoice(String orderId) {
        Order dbOrder = orderService.getOrderById(orderId);

        Invoice dbInvoice = invoiceRepository.findByOrderId(orderId).orElse(new Invoice());
        if (dbInvoice.getOrderId() != dbOrder.getId()) {

            dbInvoice.setOrderId(dbOrder.getId());
            dbInvoice.setPrice(dbOrder.getTotalPrice());
            dbInvoice.setPayment(new HashMap<>());

            Invoice invoice = invoiceRepository.save(dbInvoice);

            return invoice;
        } else {
            throw CustomException.orderHasAlreadyInvoiced(dbInvoice.getId());
        }
    }

    @Transactional
    public boolean completeInvoice(String id) {
        Invoice dbInvoice = invoiceRepository.findById(id).orElseThrow(CustomException::invoiceNotFound);

        if (dbInvoice.isCompleted()) {
            throw CustomException.invoicePaymentIsAlreadyCompleted(id);
        } else {
            int total = 0;
            for (Map.Entry<PaymentTypeEnums, Double> item : dbInvoice.getPayment().entrySet()) {
                total += item.getValue();
            }
            if (total == dbInvoice.getPrice()) {
                dbInvoice.setCompleted(true);
                dbInvoice.setUpdateDate(new Date());
                invoiceRepository.save(dbInvoice);

                return true;
            } else {
                double balance = dbInvoice.getPrice() - total;
                throw CustomException.paymentNotCompleted(balance);
            }
        }
    }

    @Transactional
    public boolean deleteInvoice(String id){
        Invoice dbInvoice = invoiceRepository.findById(id).orElseThrow(CustomException::invoiceNotFound);

        if(dbInvoice.isDeleted()){
            throw CustomException.invoiceIsAlreadyDeleted();
        }else{
            dbInvoice.setDeleted(true);
            dbInvoice.setUpdateDate(new Date());
            invoiceRepository.save(dbInvoice);

            return true;
        }
    }
}
