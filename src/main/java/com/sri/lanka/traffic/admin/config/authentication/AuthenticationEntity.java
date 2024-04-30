package com.sri.lanka.traffic.admin.config.authentication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sri.lanka.traffic.admin.common.dto.common.LoginMngrDTO;

import lombok.Data;

@Data
public class AuthenticationEntity extends LoginMngrDTO implements UserDetails{
	
    String userId;				//담당자 아이디
    String userPswd;				//담당자 비밀번호
    String userAuth;				//담당자 권한
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = Arrays.asList(userAuth);
        List<GrantedAuthority> list = new ArrayList<>();
        roles.forEach(role -> list.add(new SimpleGrantedAuthority("ROLE_" + role)));
        return list;
    }

    public AuthenticationEntity(LoginMngrDTO authLoginDTO) {
        BeanUtils.copyProperties(authLoginDTO, this);
    }
    
    @Override
    public String getPassword() {
        return userPswd;
    }

    @Override
    public String getUsername() {
        return userId;
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
}
