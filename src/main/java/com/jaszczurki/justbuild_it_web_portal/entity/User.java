package com.jaszczurki.justbuild_it_web_portal.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "company")
    private String company;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false, unique = true)
    private String password;

    @Column(name = "telephone_number")
    private String telephoneNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Offer> offers = new ArrayList<>();

    @ElementCollection(targetClass = SimpleGrantedAuthority.class)
    @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"))
    private Set<GrantedAuthority> authorities;

    public User(Long userId, String firstName, String lastName, String company, String emailAddress, String telephoneNumber, List<Offer> offers) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.emailAddress = emailAddress;
        this.telephoneNumber = telephoneNumber;
        this.addOffers(offers);
    }

    public User(String username, String password, Set<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public void addOffers(List<Offer> offers) {offers.forEach(this::addOffer);}

    public void addOffer(Offer offer) {this.offers.add(offer);}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(company, user.company) && Objects.equals(emailAddress, user.emailAddress) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(telephoneNumber, user.telephoneNumber) && Objects.equals(offers, user.offers) && Objects.equals(authorities, user.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, firstName, lastName, company, emailAddress, username, password, telephoneNumber, offers, authorities);
    }
}
