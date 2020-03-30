package xiaolin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaolin.dao.ITypeRepository;
import xiaolin.entities.Type;

@Service
public class TypeService implements ITypeService{

    @Autowired
    ITypeRepository typeRepository;

    @Override
    public Type getTypeBaseOnTypeName(String typeName) {
        return typeRepository.getTypeBaseOnTypeName(typeName);
    }

    @Override
    public Type addNewTypeToFoodCourt(Type newType) {
        return typeRepository.save(newType);
    }
}
