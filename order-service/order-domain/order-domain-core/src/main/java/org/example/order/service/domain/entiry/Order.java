package org.example.order.service.domain.entiry;

import org.example.domain.entity.AggregateRoot;
import org.example.domain.valueobject.*;
import org.example.order.service.domain.exception.OrderDomainException;
import org.example.order.service.domain.valueobject.OrderItemId;
import org.example.order.service.domain.valueobject.StreetAddress;
import org.example.order.service.domain.valueobject.TrackingId;

import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
     private final StreetAddress deliveryAddress;
     private final Money price;
     private final List<OrderItem> items;

     private TrackingId trackingId;
     private OrderStatus orderStatus;
     private List<String> failureMessages;

    /**
     * 注文の初期化.
     */
    public void initializeOrder() {
         setId(new OrderId(UUID.randomUUID()));
         trackingId = new TrackingId(UUID.randomUUID());
         orderStatus = OrderStatus.PENDING;
         initializeOrderItems();
     }

    /**
     * 注文のバリデーション.
     */
    public void validateOrder() {
        validateInitialOrder();
        validateTotalPriceOrder();
        validateItemsPrice();
     }

    /**
     * 注文ステータスと注文IDのバリデーション.
     */
    private void validateInitialOrder() {
        if (orderStatus != null || getId() != null) {
            throw new OrderDomainException("Order is not in correct state for initialization.");
        }
     }

    /**
     * 注文合計金額のバリデーション.
     */
    private void validateTotalPriceOrder() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero.");
        }
     }

    /**
     * 商品リストの価格のバリデーション.
     */
    private void validateItemsPrice() {
        Money orderItemTotal = items.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

        if (!price.equals(orderItemTotal)) {
            throw new OrderDomainException(String.format("Total price: %.2f is not equal to Order items total: %.2f",
                    price.getAmount(), orderItemTotal.getAmount()));
        }
     }

    /**
     * 商品価格のバリデーション.
     *
     * @param orderItem 注文商品
     */
    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException(String.format("Order item price: %.2f is not valid product. %s",
                    orderItem.getPrice().getAmount(), orderItem.getProduct().getId().getValue()));
        }
    }

    /**
     * 注文商品リストの初期化.
     */
    private void initializeOrderItems() {
         long itemId = 1;
         for (OrderItem orderItem: items) {
             orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
         }
     }

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    public static Builder builder() {
        return new Builder();
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
