package meeting.app.api.model.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import meeting.app.api.model.event.EventItem;
import meeting.app.api.model.user.UserEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotNull(message = "msg.err.comment.item.date.is.null")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    @DateTimeFormat(pattern = "dd-MM-yyyy dd-MM-yyyy HH:mm")
    private Date date;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity userEntity;

    @NotBlank(message = "msg.err.comment.item.content.is.blank")
    private String content;

    @Transient
    private Long userId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private EventItem eventItem;

    @Transient
    private Long eventId;
}
