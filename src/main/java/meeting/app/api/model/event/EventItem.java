package meeting.app.api.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

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

    //todo komentarze -> powiazanie z CommentItem
}
