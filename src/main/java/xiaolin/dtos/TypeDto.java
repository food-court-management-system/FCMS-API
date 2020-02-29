package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;
import xiaolin.entities.Type;

@Getter
@Setter
public class TypeDto {

    private Long typeId;
    private String typeName;

    public TypeDto() { }

    public Type mapToType(TypeDto typeDto) {
        Type result = new Type();
        if (typeDto.getTypeId() != null) {
            result.setId(typeDto.getTypeId());
        }
        if (typeDto.getTypeName() != null) {
            result.setTypeName(typeDto.getTypeName());
        }
        return result;
    }
}
