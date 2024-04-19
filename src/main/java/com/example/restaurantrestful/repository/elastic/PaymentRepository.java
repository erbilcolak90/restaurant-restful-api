package com.example.restaurantrestful.repository.elastic;

import com.example.restaurantrestful.entity.Payment;
import com.example.restaurantrestful.enums.PaymentTypeEnums;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PaymentRepository extends ElasticsearchRepository<Payment, String> {

    @Query("{\"bool\": {\"must\": {\"match\" : {\"paymentType\": \"?0\"}}, \"sort\": [{\"createDate\": {\"order\": \"asc\"}}]}}")
    List<Payment> findByPaymentType(PaymentTypeEnums paymentTypeEnums);

    @Query("{\"bool\": {\"must\" : {\"match\" : {\"invoiceId\": \"?0\"}}}}")
    List<Payment> findByInvoiceId(String invoiceId);
}
