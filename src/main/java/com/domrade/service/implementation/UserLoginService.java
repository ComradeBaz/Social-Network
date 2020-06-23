package com.domrade.service.implementation;

import com.domrade.service.interfaces.IUserService;
import com.domrade.domain.Role;
import com.domrade.domain.User;
import com.domrade.jsf.common.SessionMB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

//@Service("userLoginService")
public class UserLoginService implements UserDetailsService{

    private static final Logger LOGGER = Logger.getLogger(SessionMB.class);
    
    @Autowired
    private IUserService userService;

    /**
     * Finds user by username which is in this case email
     *
     * @param email user email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException("User " + email + " not found");

        List<Role> roles = userService.getAllRoles(user);
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.toString()));
        }
        boolean enabled = user.isEnabled();
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = !user.isLocked();
        LOGGER.log(Level.INFO, ">>>>>>>>>>>>>>>>>>>" + user.getPassword());
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
