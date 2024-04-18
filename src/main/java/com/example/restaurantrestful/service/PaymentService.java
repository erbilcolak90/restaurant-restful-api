package com.example.restaurantrestful.service;

import com.example.restaurantrestful.entity.Payment;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final InvoiceService invoiceService;

    public PaymentService(PaymentRepository paymentRepository, InvoiceService invoiceService) {
        this.paymentRepository = paymentRepository;
        this.invoiceService = invoiceService;
    }

    public Payment getPaymentById(String id){

        return paymentRepository.findById(id).orElseThrow(CustomException::paymentNotFound);
    }
}
