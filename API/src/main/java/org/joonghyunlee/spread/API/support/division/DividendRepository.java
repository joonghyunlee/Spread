package org.joonghyunlee.spread.API.support.division;

import org.joonghyunlee.spread.API.support.division.model.DividendEntity;
import org.joonghyunlee.spread.API.support.division.model.DivisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DividendRepository extends JpaRepository<DividendEntity, Long> {

    Long countByDivisionAndUserId(DivisionEntity division, String userId);
}
