package com.jaszczurki.justbuild_it_web_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

import static com.jaszczurki.justbuild_it_web_portal.entity.constants.ApplicationConstants.PASSWORD_FORMAT;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserAuthDto {

    private Long dtoUserId;
    @NotBlank
    private String dtoUsername;
    @NotBlank
    @Pattern(regexp = PASSWORD_FORMAT)
    private String dtoPassword;
    private Set<GrantedAuthority> dtoAuthorities;

    public UserAuthDto() {
        this.dtoUserId = 0L;
        this.dtoUsername = "";
        this.dtoPassword = "";
        this.dtoAuthorities = null;
    }
}
