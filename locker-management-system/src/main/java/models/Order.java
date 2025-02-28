package models;

import enums.OrderSize;
import enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

  /*
  orderId,
  productId,
  orderStatus,
  createdAt,
  completedAt,
   */

  private Integer orderId;
  private Integer userId;
  private Integer productId;
  private OrderStatus orderStatus;
  private LocalDateTime createdAt;
  private LocalDateTime completedAt;
  private String location;

  private OrderSize orderSize;


  public void completeOrder() {
    this.orderStatus = OrderStatus.DELIVERED;
    this.completedAt = LocalDateTime.now();
  }

}
