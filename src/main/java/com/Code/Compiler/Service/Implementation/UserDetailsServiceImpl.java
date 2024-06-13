package com.Code.Compiler.Service.Implementation;


import com.Code.Compiler.Repository.UserRepository;
import com.Code.Compiler.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        System.out.println("username"+ username);
        System.out.println("user"+ user.toString());

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }
}