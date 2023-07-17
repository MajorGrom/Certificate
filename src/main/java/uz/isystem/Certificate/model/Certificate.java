package uz.isystem.Certificate.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = ("certificate"))
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = ("user_id"), insertable = false, updatable = false)
    private User user;
    @Column(name = ("user_id"))
    private Integer userId;
    @ManyToOne
    @JoinColumn(name = ("direction_id"), insertable = false, updatable = false)
    private Direction direction;
    @Column(name = "direction_id")
    private Integer directionId;
    @Column(name = "finished_ad")
    private LocalDate finishedAd;
    private String token;
    private String url;
    private String urlQR;
    private String path;
    private Boolean status;
    @Column(name = ("created_at"))
    private LocalDate createdAT;
    @Column(name = ("deleted_At"))
    private LocalDate deletedAt;


}
