package com.example.restaurantrestful.repository.elastic;

import com.example.restaurantrestful.entity.Payment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PaymentRepository extends ElasticsearchRepository<Payment, String> {

}
