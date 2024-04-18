package com.example.restaurantrestful;

import co.elastic.clients.util.DateTime;
import com.example.restaurantrestful.dto.inputs.invoice.GetInvoiceByDateRangeInput;
import com.example.restaurantrestful.entity.Invoice;
import com.example.restaurantrestful.entity.Order;
import com.example.restaurantrestful.enums.PaymentTypeEnums;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

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
    void setUp() {
        invoiceMock = new Invoice();
        invoiceMock.setId("test_id");
        invoiceMock.setOrderId("test_order_id");
        invoiceMock.setPrice(100.0);
        invoiceMock.setCompleted(false);
        invoiceMock.setPayment(new HashMap<>());

    }

    @DisplayName("getInvoiceById should return valid invoice when given id is exist")
    @Test
    void testGetInvoiceById_success() {
        String test_id = "test_id";

        when(invoiceRepositoryMock.findById(test_id)).thenReturn(Optional.ofNullable(invoiceMock));

        assertNotNull(invoiceServiceMock.getInvoiceById(test_id));

    }

    @DisplayName("getInvoiceById should throw custom exception invoiceNotFound when given id does not exist")
    @Test
    void testGetInvoiceById_invoiceNotFound() {
        String test_id = "test_id";

        when(invoiceRepositoryMock.findById(test_id)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> invoiceServiceMock.getInvoiceById(test_id));
    }

    @DisplayName("getInvoiceByOrderId should return valid invoice when given id is exist")
    @Test
    void testGetInvoiceByOrderId_success() {
        String order_id = "test_order_id";

        when(invoiceRepositoryMock.findByOrderId(order_id)).thenReturn(Optional.ofNullable(invoiceMock));

        assertNotNull(invoiceServiceMock.getInvoiceByOrderId(order_id));

    }

    @DisplayName("getInvoiceByOrderId should throw custom exception invoiceNotFound when given order id does not exist")
    @Test
    void testGetInvoiceByOrderId_invoiceNotFound() {
        String order_id = "test_order_id";

        when(invoiceRepositoryMock.findByOrderId(order_id)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> invoiceServiceMock.getInvoiceByOrderId(order_id));
    }

    @DisplayName("getInvoiceByDateRange should return list invoice when given string startDate and endDate")
    @Test
    void testGetInvoiceByDateRange_success() {

        LocalDate startDate = LocalDate.of(2024, 3, 24);
        LocalDate endDate = LocalDate.of(2024, 5, 20);
        GetInvoiceByDateRangeInput getInvoiceByDateRangeInput = new GetInvoiceByDateRangeInput(startDate, endDate);

        Date start_date = Date.from(getInvoiceByDateRangeInput.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end_date = Date.from(getInvoiceByDateRangeInput.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

        Invoice invoice = new Invoice();
        invoice.setCreateDate(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        List<Invoice> invoiceList = Arrays.asList(invoice);

        when(invoiceRepositoryMock.findByDateRange(start_date, end_date)).thenReturn(invoiceList);

        List<Invoice> result = invoiceServiceMock.getInvoiceByDateRange(getInvoiceByDateRangeInput);

        assertNotNull(result);
    }

    @DisplayName("createInvoice should return valid invoice when given orderId is exist and order hasn't yet invoice")
    @Test
    void testCreateInvoice_success() {
        String order_id = "test_order_id";
        Order order = new Order();
        order.setId("test_order_id");
        order.setTotalPrice(100.0);

        when(orderServiceMock.getOrderById(order_id)).thenReturn(order);
        when(invoiceRepositoryMock.save(any(Invoice.class))).thenReturn(invoiceMock);

        Invoice result = invoiceServiceMock.createInvoice(order_id);

        assertNotNull(result);
    }

    @DisplayName("createInvoice should throw custom exception orderHasAlreadyInvoiced when given orderId is exist and order has already invoice")
    @Test
    void testCreateInvoice_orderHasAlreadyInvoiced() {
        String order_id = "test_order_id";
        Order order = new Order();
        order.setId("test_order_id");
        order.setTotalPrice(100.0);
        when(orderServiceMock.getOrderById(order_id)).thenReturn(order);
        when(invoiceRepositoryMock.findByOrderId(order_id)).thenReturn(Optional.ofNullable(invoiceMock));

        CustomException exception = assertThrows(CustomException.class, () -> invoiceServiceMock.createInvoice(order_id));

        assertEquals("Order has already invoiced. Invoice id : " + invoiceMock.getId(), exception.getMessage());
    }

    @DisplayName("completeInvoice should return true when given invoice id is exist and invoice completed already false")
    @Test
    void testCompleteInvoice_success() {
        String id = "test_id";
        invoiceMock.getPayment().put(PaymentTypeEnums.CASH, 50.0);
        invoiceMock.getPayment().put(PaymentTypeEnums.CREDIT_CARD, 50.0);

        when(invoiceRepositoryMock.findById(id)).thenReturn(Optional.ofNullable(invoiceMock));

        assertTrue(invoiceServiceMock.completeInvoice(id));
    }

    @DisplayName("completeInvoice should throw custom exception invoiceNotFound when given invoice id does not exist")
    @Test
    void testCompleteInvoice_invoiceNotFound() {
        String id = "test_id";

        when(invoiceRepositoryMock.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> invoiceServiceMock.completeInvoice(id));
    }

    @DisplayName("completeInvoice should throw custom exception invoicePaymentIsAlreadyCompleted when given invoice id is exist and invoice is completed true")
    @Test
    void testCompleteInvoice_invoicePaymentIsAlreadyCompleted() {
        invoiceMock.setCompleted(true);
        String id = "test_id";

        when(invoiceRepositoryMock.findById(id)).thenReturn(Optional.ofNullable(invoiceMock));

        assertThrows(CustomException.class, () -> invoiceServiceMock.completeInvoice(id));
    }

    @DisplayName("completeInvoice should throw custom exception paymentNotCompleted when given id is exist and invoice completed already false but payment total less then invoice price")
    @Test
    void testCompleteInvoice_paymentNotCompleted() {
        String id = "test_id";
        invoiceMock.getPayment().put(PaymentTypeEnums.CASH, 50.0);

        when(invoiceRepositoryMock.findById(id)).thenReturn(Optional.ofNullable(invoiceMock));

        assertThrows(CustomException.class, () -> invoiceServiceMock.completeInvoice(id));
    }


    @AfterEach
    void tearDown() {

    }
}
