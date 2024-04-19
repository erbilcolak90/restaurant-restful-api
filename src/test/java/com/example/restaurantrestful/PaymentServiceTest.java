package com.example.restaurantrestful;

import com.example.restaurantrestful.dto.inputs.payment.GetPaymentByPaymentTypeInput;
import com.example.restaurantrestful.entity.Payment;
import com.example.restaurantrestful.enums.PaymentTypeEnums;
import com.example.restaurantrestful.enums.SortBy;
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
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentServiceMock;

    @Mock
    private PaymentRepository paymentRepositoryMock;

    @Mock
    private InvoiceService invoiceServiceMock;

    private Payment paymentMock;

    @BeforeEach
    void setUp(){

        paymentMock = new Payment();
        paymentMock.setId("test_id");
        paymentMock.setInvoiceId("test_invoice_id");
        paymentMock.setPrice(100.0);

    }

    @DisplayName("getPaymentById should return valid payment when given id is exist")
    @Test
    void testGetPaymentById_success(){
        String id = "test_id";

        when(paymentRepositoryMock.findById(id)).thenReturn(Optional.ofNullable(paymentMock));

        assertNotNull(paymentServiceMock.getPaymentById(id));

    }

    @DisplayName("getPaymentById should throw custom exception paymentNotFound when given id does not exist")
    @Test
    void testGetPaymentById_paymentNotFound(){
        String id = "test_id";

        when(paymentRepositoryMock.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, ()-> paymentServiceMock.getPaymentById(id));

    }

    @DisplayName("getPaymentByPaymentType should return page payment when given paymentType is exist in paymentTypeEnums at getPaymentByPaymentTypeInput")
    @Test
    void testGetPaymentByPaymentType_success(){
        GetPaymentByPaymentTypeInput getPaymentByPaymentTypeInput = new GetPaymentByPaymentTypeInput(1,5, SortBy.ASC, PaymentTypeEnums.CASH);
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(paymentMock);
        paymentList.add(paymentMock);
        paymentList.add(paymentMock);
        paymentList.add(paymentMock);
        paymentList.add(paymentMock);

        Pageable pageable = PageRequest.of(getPaymentByPaymentTypeInput.getPage(), getPaymentByPaymentTypeInput.getSize(), Sort.by(getPaymentByPaymentTypeInput.getSortBy().toString(),"createDate"));

        Page<Payment> paymentPage =new PageImpl<>(paymentList,pageable,paymentList.size());

        when(paymentRepositoryMock.findAll(any(Pageable.class))).thenReturn(paymentPage);

        Page<Payment> result = paymentServiceMock.getPaymentByPaymentType(getPaymentByPaymentTypeInput);

        assertNotNull(result);
        assertEquals(5,result.getSize());
    }

    @AfterEach
    void tearDown(){}
}
