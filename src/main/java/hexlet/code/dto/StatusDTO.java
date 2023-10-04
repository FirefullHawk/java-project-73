package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusDTO {

    private long id;

    @NotBlank(message = "Field 'name' must not be empty!")
    private String name;

    private Date createdAt;

    public StatusDTO (String name){
        this.name = name;
    }
}