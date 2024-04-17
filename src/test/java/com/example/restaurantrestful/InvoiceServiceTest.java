package com.example.restaurantrestful;

import com.example.restaurantrestful.entity.Invoice;
import com.example.restaurantrestful.repository.elastic.InvoiceRepository;
import com.example.restaurantrestful.service.InvoiceService;
import com.example.restaurantrestful.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @InjectMocks
    private InvoiceService invoiceServiceMock;

    @Mock
    private OrderService orderServiceMock;

    @Mock
    private InvoiceRepository invoiceRepositoryMock;

    private Invoice invoiceMock;

    @BeforeEach
    void setUp(){

    }

    @AfterEach
    void tearDown(){

    }
}
