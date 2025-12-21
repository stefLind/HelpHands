package app.campaign.model;

import app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.UUID;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User creator;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, length = 250)
    private String shortDescription;

    private String location;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CampaignStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CampaignType type;

    private LocalTime startTime;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalTime endTime;

    @Column(nullable = false)
    private LocalDate endDate;

    private Integer peopleNeeded;

    private String foodNeeded;

    private String thingsNeeded;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private LocalDateTime updatedOn;

    @Lob
    @Column(length = Integer.MAX_VALUE)
    private byte[] pictureData;

    public String generateBase64Image() {
        return Base64.getEncoder().encodeToString(this.pictureData);
    }
}
