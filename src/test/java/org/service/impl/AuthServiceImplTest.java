package org.service.impl;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.service.AuthService;
import org.service.util.EncryptUtils;

import java.util.UUID;

class AuthServiceImplTest {
    private final static String USER_A = "userA";
    private final static String PASSWORD_A = "passwordA";
    private final static String ROLE_A = "roleA";
    private final static String ROLE_B = "roleB";
    private static final String PASSWORD_B = "passwordB";
    private static final String TOKEN_A = EncryptUtils.encrypt(USER_A) + UUID.randomUUID();

    private AuthService authService = new AuthServiceImpl();

    @Test
    void createUser() {
        // successfully add user A
        authService.createUser(USER_A, PASSWORD_A);

        // throw exception when user already exists
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.createUser(USER_A, PASSWORD_A);
        });
    }

    @Test
    void deleteUser() {
        // throw exception when user A not exists
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.deleteUser(USER_A);
        });

        authService.createUser(USER_A, PASSWORD_A);
        authService.deleteUser(USER_A);
        // throw exception as user A has already been deleted in previous step
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.deleteUser(USER_A);
        });
    }

    @Test
    void createRole() {
        // successfully create role A
        authService.createRole(ROLE_A);

        // throw exception when role already exists
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.createRole(ROLE_A);
        });
    }

    @Test
    void deleteRole() {
        // throw exception when role not exists
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.deleteRole(ROLE_A);
        });

        authService.createRole(ROLE_A);
        authService.deleteRole(ROLE_A);
        // throw exception as role A has already been deleted in previous step
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.deleteRole(ROLE_A);
        });
    }

    @Test
    void addRoleToUser() {
        // throw exception as user A not exists
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.addRoleToUser(USER_A, ROLE_A);
        });
        authService.createUser(USER_A, PASSWORD_A);
        // throw exception as role A not exists
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.addRoleToUser(USER_A, ROLE_A);
        });
        authService.createRole(ROLE_A);
        authService.addRoleToUser(USER_A, ROLE_A);
    }

    @Test
    void authenticate() {
        // throw exception as user A not exists
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.authenticate(USER_A, PASSWORD_A);
        });
        authService.createUser(USER_A, PASSWORD_A);

        // throw exception as password B does not match
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.authenticate(USER_A, PASSWORD_B);
        });
        String token1 = authService.authenticate(USER_A, PASSWORD_A);
        Assert.assertNotNull(token1);

        String token2 = authService.authenticate(USER_A, PASSWORD_A);
        Assert.assertNotNull(token2);

        // authenticate func should update the token
        Assert.assertNotEquals(token1, token2);
    }

    @Test
    void invalidate() {
        // throw exception as token A not exists
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.invalidate(TOKEN_A);
        });
        authService.createUser(USER_A, PASSWORD_A);

        String token = authService.authenticate(USER_A, PASSWORD_A);
        authService.invalidate(token);

        // throw exception as token has already been invalidate
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.invalidate(token);
        });
    }

    @Test
    void checkRole() {
        // throw exception as token A not exists
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.checkRole(TOKEN_A, ROLE_A);
        });
        authService.createUser(USER_A, PASSWORD_A);
        String token = authService.authenticate(USER_A, PASSWORD_A);
        // role A hasn't been assigned to user A
        Assert.assertFalse(authService.checkRole(token, ROLE_A));
        authService.createRole(ROLE_A);
        authService.addRoleToUser(USER_A, ROLE_A);
        // role A has been assigned to user A
        Assert.assertTrue(authService.checkRole(token, ROLE_A));
        // role B hasn't been assigned to user A
        Assert.assertFalse(authService.checkRole(token, ROLE_B));

        authService.deleteUser(USER_A);
        // throw exception as token has been deleted together with user A
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.invalidate(token);
        });
    }

    @Test
    void allRoles() {
        // throw exception as token A not exists
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.allRoles(TOKEN_A);
        });
        authService.createUser(USER_A, PASSWORD_A);
        String token = authService.authenticate(USER_A, PASSWORD_A);
        // none of role has been assigned to user A
        Assert.assertEquals(0, authService.allRoles(token).size());
        authService.createRole(ROLE_A);
        authService.addRoleToUser(USER_A, ROLE_A);
        // role A has been assigned to user A
        Assert.assertEquals(1, authService.allRoles(token).size());
        Assert.assertEquals(ROLE_A, authService.allRoles(token).toArray()[0]);
        authService.createRole(ROLE_B);
        authService.addRoleToUser(USER_A, ROLE_B);
        // role A and role B have been assigned to user A
        Assert.assertEquals(2, authService.allRoles(token).size());
        authService.addRoleToUser(USER_A, ROLE_B);
        // duplicate role B will not be added
        Assert.assertEquals(2, authService.allRoles(token).size());
    }

    @Test
    void testExpiration(){
        authService.setTokenExpireSecond(2L);
        authService.createUser(USER_A, PASSWORD_A);
        String token = authService.authenticate(USER_A, PASSWORD_A);
        authService.createRole(ROLE_A);
        authService.addRoleToUser(USER_A, ROLE_A);
        // role A has been assigned to user A
        Assert.assertEquals(1, authService.allRoles(token).size());
        Assert.assertTrue(authService.checkRole(token, ROLE_A));

        try {
            Thread.sleep(2000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        // token should have been expired
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.allRoles(token);
        });
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.checkRole(token, ROLE_A);
        });
    }

}