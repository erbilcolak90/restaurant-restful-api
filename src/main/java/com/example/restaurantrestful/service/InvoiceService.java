package com.example.restaurantrestful.service;

import com.example.restaurantrestful.entity.Invoice;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.InvoiceRepository;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final OrderService orderService;

    public InvoiceService(InvoiceRepository invoiceRepository, OrderService orderService) {
        this.invoiceRepository = invoiceRepository;
        this.orderService = orderService;
    }

    public Invoice getInvoiceById(String id){
        return invoiceRepository.findById(id).orElseThrow(CustomException::invoiceNotFound);
    }

    public Invoice getInvoiceByOrderId(String orderId){
        return invoiceRepository.findByOrderId(orderId).orElseThrow(CustomException::invoiceNotFound);
    }
}
