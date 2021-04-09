package meeting.app.api.model.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import meeting.app.api.model.event.EventItem;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date date;

    //todo author

    private String content;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private EventItem eventItem;
}
