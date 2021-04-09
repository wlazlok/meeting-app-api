package meeting.app.api.model.category;

import lombok.Data;
import meeting.app.api.model.event.EventItem;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
public class CategoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String cloudinaryId;

    @OneToMany(mappedBy = "categoryId")
    private List<EventItem> events = new ArrayList<>();
}
