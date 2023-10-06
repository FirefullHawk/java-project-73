package hexlet.code.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

import static jakarta.persistence.GenerationType.AUTO;
import static jakarta.persistence.TemporalType.TIMESTAMP;

@Entity
@Getter
@Setter
@Table(name = "statuses")
@NoArgsConstructor
@AllArgsConstructor
public class Status {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 1_000)
    @Column(unique = true)
    private String name;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "status")
    private List<Task> tasks;

    public Status(final Long id) {
        this.id = id;
    }
}
