package com.example.restaurantrestful.service;

import com.example.restaurantrestful.dto.inputs.payment.GetPaymentByPaymentTypeInput;
import com.example.restaurantrestful.entity.Invoice;
import com.example.restaurantrestful.entity.Payment;
import com.example.restaurantrestful.enums.PaymentTypeEnums;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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

    public Page<Payment> getPaymentByPaymentType(GetPaymentByPaymentTypeInput getPaymentByPaymentTypeInput){

        Pageable pageable = PageRequest.of(getPaymentByPaymentTypeInput.getPage(), getPaymentByPaymentTypeInput.getSize(), Sort.by(Sort.Direction.valueOf(getPaymentByPaymentTypeInput.getSortBy().toString()),"createDate"));
        
        return paymentRepository.findAll(pageable);
    }

    public List<Payment> getPaymentByInvoiceId(String invoiceId){
        Invoice dbInvoice = invoiceService.getInvoiceById(invoiceId);
        List<Payment> paymentList =  paymentRepository.findByInvoiceId(dbInvoice.getId());

        if(paymentList.size() == 0){
            throw CustomException.invoiceHasNotAlreadyPayment();
        }else{
            return paymentList;
        }
    }
}
