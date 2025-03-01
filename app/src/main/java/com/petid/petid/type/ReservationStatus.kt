package com.petid.petid.type

enum class ReservationStatus {
    ALL,
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED;

    companion object {
        fun toValue(value: String): ReservationStatus? {
            return entries.find { it.name == value }
        }
    }
}