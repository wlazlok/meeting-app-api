package meeting.app.api.model.category;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CategoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Lob
    private byte[] image;
}
