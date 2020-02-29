package xiaolin.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tblTypes")
@Setter
@Getter
public class Type implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "type_name")
    private String typeName;
}
