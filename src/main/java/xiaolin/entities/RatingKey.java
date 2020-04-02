package xiaolin.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class RatingKey implements Serializable {
    @Column(name = "customer_id")
    Long customerId;

    @Column(name = "food_id")
    Long foodId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RatingKey)) return false;
        RatingKey that = (RatingKey) o;
        return Objects.equals(customerId, that.getCustomerId()) &&
                Objects.equals(foodId, that.getFoodId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, foodId);
    }
}
