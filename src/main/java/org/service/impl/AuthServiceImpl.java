package org.service.impl;

import lombok.NonNull;
import org.entity.Role;
import org.entity.Token;
import org.entity.User;
import org.repository.RoleRepository;
import org.repository.TokenRepository;
import org.repository.TokenUserRepository;
import org.repository.UserRepository;
import org.repository.impl.RoleRepositoryImpl;
import org.repository.impl.TokenRepositoryImpl;
import org.repository.impl.TokenUserRepositoryImpl;
import org.repository.impl.UserRepositoryImpl;
import org.service.AuthService;
import org.service.util.EncryptUtils;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private TokenRepository tokenRepository;

    private TokenUserRepository tokenUserRepository;
    private EncryptUtils encryptUtils;

    private long tokenExpireSecond = 2 * 60 * 60;

    public AuthServiceImpl() {
        this.userRepository = new UserRepositoryImpl();
        this.roleRepository = new RoleRepositoryImpl();
        this.tokenRepository = new TokenRepositoryImpl();
        this.tokenUserRepository = new TokenUserRepositoryImpl();
        this.encryptUtils = new EncryptUtils();
    }

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           TokenRepository tokenRepository, TokenUserRepository tokenUserRepository,
                           EncryptUtils encryptUtils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.tokenUserRepository = tokenUserRepository;
        this.encryptUtils = encryptUtils;
    }

    /**
     * create a user
     * @param userName
     * @param password
     */
    @Override
    public void createUser(@NonNull String userName, @NonNull String password) {
        if(userRepository.userExists(userName)) {
            throw new IllegalArgumentException("User already exists");
        }
        String encryptPassword = encryptUtils.encrypt(password);
        User user = new User(userName, encryptPassword);
        userRepository.addUser(user);
    }

    /**
     * delete a user
     * @param userName
     */
    @Override
    public void deleteUser(@NonNull String userName) {
        if(!userRepository.userExists(userName)) {
            throw new IllegalArgumentException("User doesn't exists");
        }
        String tokenName = tokenUserRepository.getTokenByUser(userName);
        tokenUserRepository.deleteRelationByUser(userName);
        tokenRepository.deleteToken(tokenName);
        userRepository.deleteUser(userName);
    }

    /**
     * create a role
     * @param roleName
     */
    @Override
    public void createRole(@NonNull String roleName) {
        if(roleRepository.roleExists(roleName)) {
            throw new IllegalArgumentException("Role already exists");
        }
        Role role = new Role(roleName);
        roleRepository.addRole(role);
    }

    /**
     * delete a role
     * @param roleName
     */
    @Override
    public void deleteRole(@NonNull String roleName) {
        if(!roleRepository.roleExists(roleName)) {
            throw new IllegalArgumentException("Role doesn't exists");
        }
        roleRepository.deleteRole(roleName);
    }

    /**
     * assign a role to a user
     * @param userName
     * @param roleName
     */
    @Override
    public void addRoleToUser(@NonNull String userName, @NonNull String roleName) {
        if(!userRepository.userExists(userName)) {
            throw new IllegalArgumentException("User doesn't exists");
        }
        if(!roleRepository.roleExists(roleName)) {
            throw new IllegalArgumentException("Role doesn't exists");
        }
        User user = userRepository.getUser(userName);
        user.addRole(roleName);
    }

    /**
     * login with userName and password, update the token for the user
     * @param userName
     * @param password
     * @return token name of the login user
     */
    @Override
    public String authenticate(@NonNull String userName, @NonNull String password) {
        if(!userRepository.userExists(userName)) {
            throw new IllegalArgumentException("User doesn't exists");
        }
        String encryptedPassword = encryptUtils.encrypt(password);
        User user = userRepository.getUser(userName);
        if(!encryptedPassword.equals(user.getPassword())) {
            throw new IllegalArgumentException("Password doesn't match");
        }
        String tokenName = tokenUserRepository.getTokenByUser(userName);
        if(Objects.nonNull(tokenName)) {
            tokenUserRepository.deleteRelationByToken(tokenName);
            tokenRepository.deleteToken(tokenName);
        }
        tokenName = encryptUtils.encrypt(userName) + UUID.randomUUID();
        Token token = new Token(tokenName, OffsetDateTime.now().plusSeconds(tokenExpireSecond));
        tokenRepository.addToken(token);
        tokenUserRepository.addRelation(tokenName, userName);
        return tokenName;
    }

    /**
     * log out/invalidate a token
     * @param tokenName
     */
    @Override
    public void invalidate(@NonNull String tokenName) {
        if(!tokenRepository.tokenExists(tokenName) || !tokenUserRepository.relationExistsByToken(tokenName)) {
            throw new IllegalArgumentException("Token doesn't exist");
        }
        tokenUserRepository.deleteRelationByToken(tokenName);
        tokenRepository.deleteToken(tokenName);
    }

    /**
     * check whether the role is assigned to a certain token's corresponding user
     * @param tokenName
     * @param roleName
     * @return boolean of whether the role belongs to the token's corresponding user
     */
    @Override
    public Boolean checkRole(@NonNull String tokenName, @NonNull String roleName) {
        if(!tokenRepository.tokenExists(tokenName)) {
            throw new IllegalArgumentException("Token doesn't exist");
        }
        Token token = tokenRepository.getToken(tokenName);
        if (OffsetDateTime.now().isAfter(token.getExpiredTime())) {
            throw new IllegalArgumentException("Token is expired");
        }
        String userName = tokenUserRepository.getUserByToken(tokenName);
        User user = userRepository.getUser(userName);
        Set<String> roles = user.getRoles();
        return roles.contains(roleName);
    }

    /**
     * get all roles for a certain token
     * @param tokenName
     * @return set of role names
     */
    @Override
    public Set<String> allRoles(@NonNull String tokenName) {
        if(!tokenRepository.tokenExists(tokenName)) {
            throw new IllegalArgumentException("Token doesn't exist");
        }
        Token token = tokenRepository.getToken(tokenName);
        if (OffsetDateTime.now().isAfter(token.getExpiredTime())) {
            throw new IllegalArgumentException("Token is expired");
        }
        String userName = tokenUserRepository.getUserByToken(tokenName);
        User user = userRepository.getUser(userName);
        Set<String> roles = user.getRoles();
        return roles;
    }

    /**
     * this function is for Junit test only
     * @param seconds
     */
    public void setTokenExpireSecond(long seconds) {
        this.tokenExpireSecond = seconds;
    }
}
