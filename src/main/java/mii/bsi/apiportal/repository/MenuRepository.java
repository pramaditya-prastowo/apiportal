package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.Groups;
import mii.bsi.apiportal.domain.Menu;
import mii.bsi.apiportal.domain.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByRole(Roles roles);

    @Query(value="select * from bsi_menu_api_portal where permission_name = ? and menu_id = ?", nativeQuery = true)
    Menu findOneByPermissionNameAndMenuId(String stringParam, Long longParam);

    @Query(value="select * from bsi_menu_api_portal where permission_name = ? ", nativeQuery = true)
    Menu findOneByPermissionName(String stringParam);

    @Query(value="select * from bsi_menu_api_portal where permission_name IN ( ? )", nativeQuery = true)
    List<Menu> findByPermissionList(String permissionList);

    List<Menu> findByPermissionNameInOrderBySequenceMenuAsc(Collection<String> permission);

    List<Menu> findByShowOnApproval(boolean showApproval);
    Menu findByPermissionName(String permission);
}