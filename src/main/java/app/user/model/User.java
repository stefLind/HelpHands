package app.user.model;

import app.campaign.model.Campaign;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    private String firstName;

    private String surname;

    private String lastName;

    private String town;

    private String address;

    private String companyName;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType type;

    private boolean isActive;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private LocalDateTime updatedOn;

    @Lob
    @Column(length = Integer.MAX_VALUE)
    private byte[] profilePictureData;

    // ManyToOne relationship to define the company
    @ManyToOne
    private User company;

    // OneToMany relationship to define the employees
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> employees = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "creator")
    private List<Campaign> campaigns = new ArrayList<>();

    public String generateBase64Image() {
        return Base64.getEncoder().encodeToString(this.profilePictureData);
    }
}
