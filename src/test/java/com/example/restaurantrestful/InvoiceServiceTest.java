package com.example.restaurantrestful;

import com.example.restaurantrestful.entity.Invoice;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.InvoiceRepository;
import com.example.restaurantrestful.service.InvoiceService;
import com.example.restaurantrestful.service.OrderService;
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
        invoiceMock = new Invoice();
        invoiceMock.setId("test_id");
        invoiceMock.setOrderId("test_order_id");
        invoiceMock.setPrice(100.0);
        invoiceMock.setCompleted(false);

    }

    @DisplayName("getInvoiceById should return valid invoice when given id is exist")
    @Test
    void testGetInvoiceById_success(){
        String test_id = "test_id";

        when(invoiceRepositoryMock.findById(test_id)).thenReturn(Optional.ofNullable(invoiceMock));

        assertNotNull(invoiceServiceMock.getInvoiceById(test_id));

    }

    @DisplayName("getInvoiceById should throw custom exception invoiceNotFound when given id does not exist")
    @Test
    void testGetInvoiceById_invoiceNotFound(){
        String test_id = "test_id";

        when(invoiceRepositoryMock.findById(test_id)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, ()-> invoiceServiceMock.getInvoiceById(test_id));
    }


    @AfterEach
    void tearDown(){

    }
}
