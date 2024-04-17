package com.example.restaurantrestful.repository.elastic;

import com.example.restaurantrestful.entity.Invoice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface InvoiceRepository extends ElasticsearchRepository<Invoice, String> {
}
