package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.entities.Rating;

import java.util.List;

@Service
public interface IRatingService {

    List<Rating> getTotalRatingOfFood(Long foodId);

    Rating logRating(Rating rating);
}
