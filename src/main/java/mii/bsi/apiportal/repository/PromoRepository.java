package mii.bsi.apiportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mii.bsi.apiportal.domain.Promo;
import org.springframework.data.jpa.repository.Query;

public interface PromoRepository extends JpaRepository<Promo, Long> {
    @Query(value = "SELECT * from bsi_promo_api_portal where id=?1 ", nativeQuery = true)
    public Promo findByIdPromo(Long paramLong);
}
