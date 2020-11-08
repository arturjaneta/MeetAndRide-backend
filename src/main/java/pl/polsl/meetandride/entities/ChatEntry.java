package pl.polsl.meetandride.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "chat_entries")
public class ChatEntry extends BaseEntity{
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "id", nullable = false)
    private User author;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "trip", referencedColumnName = "id", nullable = false)
    private Trip trip;
}
