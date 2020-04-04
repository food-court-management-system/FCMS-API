package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.entities.Type;

import java.util.List;

@Service
public interface ITypeService {

    Type getTypeBaseOnTypeName(String typeName);

    Type addNewTypeToFoodCourt(Type newType);

    List<Type> getAllTypesInFoodCourt();
}
