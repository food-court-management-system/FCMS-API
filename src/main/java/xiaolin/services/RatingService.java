package xiaolin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaolin.dao.IRatingRepository;
import xiaolin.entities.Rating;

import java.util.List;

@Service
public class RatingService implements IRatingService{

    @Autowired
    IRatingRepository ratingRepository;

    @Override
    public List<Rating> getTotalRatingOfFood(Long foodId) {
        return ratingRepository.getTotalRatingStartOfFood(foodId);
    }

    @Override
    public Rating logRating(Rating rating) {
        return ratingRepository.save(rating);
    }
}
