package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.entities.Type;

@Service
public interface ITypeService {

    Type getTypeBaseOnTypeName(String typeName);

    Type addNewTypeToFoodCourt(Type newType);
}
