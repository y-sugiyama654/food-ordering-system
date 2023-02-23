package org.example.order.service.domain.ports.output.repository;

import org.example.order.service.domain.entiry.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {

    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}
