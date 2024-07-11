package com.Code.Compiler.models;

import com.Code.Compiler.Enum.Role;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "id")

public class Students implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String grid;
    private String course;
    private String branchCode;

    @Enumerated(EnumType.STRING)
    private Role role = Role.STUDENT;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private List<User> users;

    @ManyToMany(mappedBy = "enrolledStudents" , fetch = FetchType.LAZY)
    private List<Contest> enrolledContests;

    @ManyToMany(mappedBy = "enrolledStudents" , fetch = FetchType.LAZY)
    private List<Contest> attemptedContests;

    @ManyToMany(  fetch = FetchType.LAZY)
    @JoinTable(
            name = "student_solved_questions",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")


    )
    private List<Questions> solvedQuestions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var role = new SimpleGrantedAuthority("ROLE_" + this.role.name());
        return List.of(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public String toString() {
        return "Students{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", grid='" + grid + '\'' +
                ", course='" + course + '\'' +
                ", branchCode='" + branchCode + '\'' +
                ", role=" + role +
                '}';
    }
}
