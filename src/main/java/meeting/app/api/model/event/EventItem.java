package meeting.app.api.model.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.model.comment.CommentItem;
import meeting.app.api.model.rating.RatingItem;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.*;

@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EventItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "msg.err.event.item.city.is.null")
    private String city;

    @NotBlank(message = "msg.err.event.item.street.is.null")
    private String street;

    @NotNull(message = "msg.err.event.item.date.is.null")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    @DateTimeFormat(pattern = "dd-MM-yyyy dd-MM-yyyy HH:mm")
    private Date date;

    //todo uczestnicy List<User> jako activeParticipants

    @NotBlank(message = "msg.err.event.item.description.is.blank")
    private String description;

    private boolean active;

    @NotNull(message = "msg.err.event.item.maxparticipants.is.null")
    @Min(0)
    private int maxParticipants;

    @OneToMany(mappedBy = "eventItem")
    private List<CommentItem> comments = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryItem categoryId;

    private Integer rating = 0;

    @OneToMany(mappedBy = "eventItem")
    private List<RatingItem> ratings = new ArrayList<>();
}
