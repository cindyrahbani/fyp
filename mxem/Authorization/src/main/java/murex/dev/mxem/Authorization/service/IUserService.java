package murex.dev.mxem.Authorization.service;


import murex.dev.mxem.Authorization.exception.RoleNotFoundException;
import murex.dev.mxem.Authorization.exception.UserNotFoundException;
import murex.dev.mxem.Authorization.model.Role;
import murex.dev.mxem.Authorization.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IUserService {

    public List<User> findAllUsers();
    public Optional<User> findUserById(Long id) throws UserNotFoundException;
    public User findUserByName(String id) throws UserNotFoundException;
    public Set<Role> findRolesForUser(String id) throws UserNotFoundException;
    public void deleteAllUsers();
    public void deleteUserById(Long id) throws UserNotFoundException;
    public void deleteRolesForUser(Long id) throws UserNotFoundException;
    public Role addRoleForUser(Long userId, Long roleId) throws UserNotFoundException, RoleNotFoundException;
    public User addUser(User user);
    public User updateUser(Long id, User user) throws UserNotFoundException;
    public User updateNameofUser(Long id, String name) throws UserNotFoundException;

}
