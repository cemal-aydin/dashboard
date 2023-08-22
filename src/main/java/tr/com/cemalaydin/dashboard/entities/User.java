package tr.com.cemalaydin.dashboard.entities;

import tr.com.cemalaydin.dashboard.base.BaseEntity;
import tr.com.cemalaydin.dashboard.enums.AuthProvider;
import tr.com.cemalaydin.dashboard.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "app_user")
@Where(clause = "status >=0")
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {
    @NotNull
    @Column(nullable = false,unique = true)
    private String username;
    @Email
    @Column(nullable = false,unique = true)
    private String email;
    private String phone;
    private String password;
    @NotNull
    @Column(nullable = false)
    private String fullName;
    private String roles;
    @NotNull
    @Column(nullable = false)
    private AuthProvider provider;
    private String providerId;
    private String pictureUrl;
    private String locale;
    private Boolean emailValidated = false;
    private String emailValidationCode;
    @Column(name = "EMAIL_VALID_SENT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date emailValidationCodeSentDate;

    public User(User user) {
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String[] roles =getRoles() != null ? getRoles().split(",") : new String[]{"ROLE_USER"};
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = Arrays.stream(roles).map(p -> new SimpleGrantedAuthority(p)).toList();
        //SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_TEMP");
        return simpleGrantedAuthorities;
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
        return Status.ACTIVE.equals(this.getStatus());
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public Boolean getEmailValidated() {
        return emailValidated;
    }

    public void setEmailValidated(Boolean emailValidated) {
        this.emailValidated = emailValidated;
    }

    public String getEmailValidationCode() {
        return emailValidationCode;
    }

    public void setEmailValidationCode(String emailValidationCode) {
        this.emailValidationCode = emailValidationCode;
    }

    public Date getEmailValidationCodeSentDate() {
        return emailValidationCodeSentDate;
    }

    public void setEmailValidationCodeSentDate(Date emailValidationCodeSentDate) {
        this.emailValidationCodeSentDate = emailValidationCodeSentDate;
    }
}
