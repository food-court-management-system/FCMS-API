package xiaolin.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tblUsers")
@Getter
@Setter
public class User implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "last_name")
    private String lName;

    @Column(name = "first_name")
    private String fName;

    @Column(name = "age")
    private int age;

    @ManyToOne(targetEntity = FoodStall.class,fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "food_stall_id")
    private FoodStall foodStall;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "is_active")
    private boolean isActive;

}
