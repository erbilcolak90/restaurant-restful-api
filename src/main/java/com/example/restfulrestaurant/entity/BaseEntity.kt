package com.example.restfulrestaurant.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import java.util.*

@MappedSuperclass
abstract class BaseEntity {

    @Column(name = "create_date", nullable = false)
    var createDate : Date = Date()
    @Column(name = "update_date", nullable = false)
    var updateDate : Date = Date()
    @Column(name = "is_deleted", nullable = false)
    var isDeleted : Boolean = false
}