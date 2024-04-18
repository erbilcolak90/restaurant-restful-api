package com.example.restaurantrestful.repository.elastic;

import com.example.restaurantrestful.entity.Invoice;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends ElasticsearchRepository<Invoice, String> {

    @Query("{\"bool\" : {\"must\" : {\"match\" : \"orderId\" : \"?0\"}}}")
    Optional<Invoice> findByOrderId(String orderId);

    @Query("{\"bool\":{\"must\":[{\"range\":{\"createDate\":{\"gte\": \"?0\", \"lte\": \"?1\"}}}]}}")
    List<Invoice> findByDateRange(Date startDate, Date endDate);
}
