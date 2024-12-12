package com.manda.agenda.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.manda.agenda.dto.UserDTO;
import com.manda.agenda.mappers.UserMapper;
import com.manda.agenda.models.User1;
import com.manda.agenda.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private String prenom;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    UserMapper userMapper;

    @SuppressWarnings("null")
    public void saveUser(UserDTO user) {
        // System.out.println("LastName dans userService" + user.getLastName());
        userRepository.save(userMapper.toUser(user));
    }

    public UserDTO getUser(final Integer id) {
        @SuppressWarnings("null")
        Optional<User1> user = userRepository.findById(id);
        if (user.isPresent()) {
            return userMapper.toUserDTO(new User1(user.get().getId(), user.get().getLastName(),
                    user.get().getFirstName(), user.get().getUsername(), user.get().getPassword(), user.get().getRole(),
                    user.get().getWhoCreated(), user.get().getDateCreated(), user.get().getWhoModified(),
                    user.get().getDateModified(), user.get().getFirstConnection(), user.get().getStatut()));
        } else {
            return new UserDTO();
        }

    }

    public UserDTO modifierUser(UserDTO userDTO) {
        return userRepository.findById(userDTO.getId()).map(user -> {
            user.setId(userDTO.getId());
            user.setLastName(userDTO.getLastName());
            user.setFirstName(userDTO.getFirstName());
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
            user.setRole(userDTO.getRole());
            user.setWhoCreated(user.getWhoCreated());
            user.setDateCreated(user.getDateCreated());
            user.setWhoModified(userDTO.getWhoModified());
            user.setDateModified(userDTO.getDateModified());
            user.setFirstConnection(userDTO.getFirstConnection());
            user.setStatut(userDTO.getStatut());
            return userMapper.toUserDTO(userRepository.save(user));
        }).orElseThrow(() -> new RuntimeException("Cet utilisateur n'est pas dans la base"));
    }

    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTOs = null;
        try {

            userDTOs = new ArrayList<UserDTO>();

            for (User1 user1 : userRepository.findAll()) {
                userDTOs.add(userMapper.toUserDTO(user1));
                System.out.println("LastName dans userService==========================:" + user1.getLastName());
            }
        } catch (Exception ex) {

        }
        return userDTOs;
    }

    public UserDTO userConnection(String login, String password) {
        User1 user1 = userRepository.userByPassword(login, password);
        UserDTO userDTO = userMapper.toUserDTO(user1);
        // UserDTO userDTO = new UserDTO();
        // userDTO.setUsername(login);
        // userDTO.setPassword(password);
        // userDTO.setRole("ADMIN");
        if (userDTO != null) {
            // System.out.println("UserDTO different de null" + "Bienvenue" +
            // userDTO.getIdUserGroup());
        } else {
            System.out.println("UserDTO est null");
        }
        return userDTO;
    }

    @SuppressWarnings("unused")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        // User1 user = new User1();
        // user.setUsername(username);
        // user.setRole("ADMIN");
        // user.setPassword(new PasswordEncryptionService().encrypPassword("admirl2"));

        User1 user = userRepository.findByUsername(username);
        // User1 user = new User1();
        // user.setUsername("agenda");
        // user.setPassword(new PasswordEncryptionService().encrypPassword("agenda"));
        // user.setRole("ADMIN");
        // System.out.println("===========================Username:" +
        // user.getUsername());

        // User user = userRepository.findByUsername(username);

        if (user == null) {
            System.out.println("User est null===========================dkddkdkd");
            throw new UsernameNotFoundException("User not found");
        }
        prenom = user.getFirstName();
        // return
        // org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
        // .password(user.getPassword()).roles(user.getRole())
        // .build();

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                getAuthorities(user));

    }

    public Collection<? extends GrantedAuthority> getAuthorities(User1 user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));
        return authorities;
    }

    // public Collection<? extends GrantedAuthority> getFirstConnection(User1 user)
    // {
    // Set<GrantedAuthority> authorities = new HashSet<>();
    // authorities.add(new SimpleGrantedAuthority(user.getFirstConnection()));
    // return authorities;
    // }

    // public User userConnection(String login, String password) {
    // User user1 = userRepository.userByPassword(login, password);
    // return user1;
    // }

    public String getPrenomUser() {
        return prenom;
    }

    @Transactional
    public int modifierPassword(String password, String username) {
        return userRepository.updatePassword(password, username);
    }

}
