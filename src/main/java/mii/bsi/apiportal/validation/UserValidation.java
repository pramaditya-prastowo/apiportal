package mii.bsi.apiportal.validation;

import io.jsonwebtoken.Claims;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.domain.model.Roles;
import mii.bsi.apiportal.repository.UserRepository;
import mii.bsi.apiportal.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserValidation {

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private UserRepository userRepository;

    public static boolean isAdminByClaim(Claims claim){
        if(!(claim.get("role").equals(Roles.SUPER_ADMIN.toString()) || claim.get("role").equals(Roles.ADMIN.toString()))){
            return false;
        }
        return true;
    }

    public static boolean isAdminByUser(User user){
        if(!(user.getAuthPrincipal().equals(Roles.SUPER_ADMIN) || user.getAuthPrincipal().equals(Roles.ADMIN))){
            return false;
        }
        return true;
    }

    public User getUserFromToken(String token){
        final String username = jwtUtility.getUsernameFromToken(token);
        User user = userRepository.findByEmail(username);
        return user;
    }

    public boolean isAdmin(String token){
        final Claims claim = jwtUtility.getAllClaimsFromToken(token);
        if(!isAdminByClaim(claim)){
            return false;
        }

        final String username = jwtUtility.getUsernameFromToken(token);
        User user = userRepository.findByEmail(username);

        if(!isAdminByUser(user)){
            return false;
        }

        return true;

    }
}
