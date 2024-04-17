package com.example.restaurantrestful.repository.elastic;

import com.example.restaurantrestful.entity.Invoice;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface InvoiceRepository extends ElasticsearchRepository<Invoice, String> {

    @Query("{\"bool\" : {\"must\" : {\"match\" : \"orderId\" : \"?0\"}}}")
    Optional<Invoice> findByOrderId(String orderId);
}
