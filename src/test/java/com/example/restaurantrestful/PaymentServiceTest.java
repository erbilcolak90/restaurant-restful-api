package com.example.restaurantrestful;

import com.example.restaurantrestful.entity.Payment;
import com.example.restaurantrestful.repository.elastic.PaymentRepository;
import com.example.restaurantrestful.service.InvoiceService;
import com.example.restaurantrestful.service.PaymentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void setUp(){}

    @AfterEach
    void tearDown(){}
}
