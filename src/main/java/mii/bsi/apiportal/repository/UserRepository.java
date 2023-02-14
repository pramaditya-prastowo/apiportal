package mii.bsi.apiportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mii.bsi.apiportal.domain.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = "SELECT NEXTVAL('bsi_user_api_portal_seq')", nativeQuery = true)
    public String getUserSequence();

    public User findByEmail(String paramString);

//    public User findById(String paramString);

    @Query(value = "SELECT * from bsi_user_api_portal  where account_inactive = false", nativeQuery = true)
    public List<User> findByAccountActive();

    @Query(value="select count(id) from bsi_user_api_portal where auth_principal = ?1", nativeQuery = true)
    long countUserByAuthPrincipal(String paramString);

    @Query(value = "SELECT * from bsi_user_api_portal where account_inactive = true", nativeQuery = true)
    public List<User> findByAccountInActive();

    @Query(value = "SELECT * from bsi_user_api_portal where group_id = ?1", nativeQuery = true)
    public List<User> findByGroupId(long paramLong);
}
