package models;

import enums.Gender;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class Human {
    private Integer id;
    private String name;
    private Gender gender;
    private String email;
}
