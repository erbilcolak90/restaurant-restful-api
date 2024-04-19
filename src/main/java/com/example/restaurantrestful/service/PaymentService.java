package com.example.restaurantrestful.service;

import com.example.restaurantrestful.dto.inputs.payment.GetPaymentByPaymentTypeInput;
import com.example.restaurantrestful.dto.inputs.payment.MakePaymentInput;
import com.example.restaurantrestful.dto.payloads.PaymentPayload;
import com.example.restaurantrestful.entity.Invoice;
import com.example.restaurantrestful.entity.Payment;
import com.example.restaurantrestful.enums.PaymentTypeEnums;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.InvoiceRepository;
import com.example.restaurantrestful.repository.elastic.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final InvoiceService invoiceService;

    private final InvoiceRepository invoiceRepository;

    public PaymentService(PaymentRepository paymentRepository, InvoiceService invoiceService, InvoiceRepository invoiceRepository) {
        this.paymentRepository = paymentRepository;
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
    }

    public Payment getPaymentById(String id) {

        return paymentRepository.findById(id).orElseThrow(CustomException::paymentNotFound);
    }

    public Page<Payment> getPaymentByPaymentType(GetPaymentByPaymentTypeInput getPaymentByPaymentTypeInput) {

        Pageable pageable = PageRequest.of(getPaymentByPaymentTypeInput.getPage(), getPaymentByPaymentTypeInput.getSize(), Sort.by(Sort.Direction.valueOf(getPaymentByPaymentTypeInput.getSortBy().toString()), "createDate"));

        return paymentRepository.findAll(pageable);
    }

    public List<Payment> getPaymentByInvoiceId(String invoiceId) {
        Invoice dbInvoice = invoiceService.getInvoiceById(invoiceId);
        List<Payment> paymentList = paymentRepository.findByInvoiceId(dbInvoice.getId());

        if (paymentList.isEmpty()) {
            throw CustomException.invoiceHasNotAlreadyPayment();
        } else {
            return paymentList;
        }
    }

    @Transactional
    public List<PaymentPayload> makePayment(MakePaymentInput makePaymentInput) {
        Invoice dbInvoice = invoiceService.getInvoiceById(makePaymentInput.getInvoiceId());
        double invoiceTotalPrice = dbInvoice.getPrice();
        double inputTotalPrice = 0.0;
        List<PaymentPayload> paymentPayloads = new ArrayList<>();

        for(Map.Entry<PaymentTypeEnums, Double> item : makePaymentInput.getPaymentList().entrySet()){
            inputTotalPrice += item.getValue();
        }

        if(invoiceTotalPrice == inputTotalPrice){
            for (Map.Entry<PaymentTypeEnums, Double> paymentItem : makePaymentInput.getPaymentList().entrySet()) {
                dbInvoice.getPayment().put(paymentItem.getKey(), paymentItem.getValue());
                Payment payment = new Payment();
                payment.setInvoiceId(dbInvoice.getId());
                payment.setPaymentType(paymentItem.getKey());
                payment.setPrice(paymentItem.getValue());
                payment.setCompleted(true);
                Payment savedPayment = paymentRepository.save(payment);
                paymentPayloads.add(PaymentPayload.convert(savedPayment));

            }
            dbInvoice.setCompleted(true);
            dbInvoice.setUpdateDate(new Date());
            invoiceRepository.save(dbInvoice);

            return paymentPayloads;
        }else{
            throw CustomException.paymentAmountLessThanInvoiceAmount(inputTotalPrice,invoiceTotalPrice);
        }

    }
}
