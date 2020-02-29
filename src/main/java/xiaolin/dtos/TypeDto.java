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

}
