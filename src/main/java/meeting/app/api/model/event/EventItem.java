package meeting.app.api.model.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.model.comment.CommentItem;

import javax.persistence.*;
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

    private String city;

    private String street;

    private Date date;

    //todo uczestnicy List<User> jako activeParticipants

    private String description;

    private boolean active;

    private int maxParticipants;

    @OneToMany(mappedBy = "eventItem")
    private List<CommentItem> comments = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryItem categoryId;
}
