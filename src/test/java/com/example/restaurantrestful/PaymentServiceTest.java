package com.example.restaurantrestful;

import com.example.restaurantrestful.entity.Payment;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.PaymentRepository;
import com.example.restaurantrestful.service.InvoiceService;
import com.example.restaurantrestful.service.PaymentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private InvoiceService invoiceService;

    private Payment payment;

    @BeforeEach
    void setUp(){

        payment = new Payment();
        payment.setId("test_id");
        payment.setInvoiceId("test_invoice_id");
        payment.setPrice(100.0);

    }

    @DisplayName("getPaymentById should return valid payment when given id is exist")
    @Test
    void testGetPaymentById_success(){
        String id = "test_id";

        when(paymentRepository.findById(id)).thenReturn(Optional.ofNullable(payment));

        assertNotNull(paymentService.getPaymentById(id));

    }

    @DisplayName("getPaymentById should throw custom exception paymentNotFound when given id does not exist")
    @Test
    void testGetPaymentById_paymentNotFound(){
        String id = "test_id";

        when(paymentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, ()-> paymentService.getPaymentById(id));

    }

    @AfterEach
    void tearDown(){}
}
