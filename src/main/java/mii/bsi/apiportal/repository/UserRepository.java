package mii.bsi.apiportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mii.bsi.apiportal.domain.User;

public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = "SELECT NEXTVAL(bsi_user_api_portal_seq)", nativeQuery = true)
    public String getUserSequence();

    public User findByEmail(String paramString);
}
