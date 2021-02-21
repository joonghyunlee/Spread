package org.joonghyunlee.spread.API.support.division;

import org.joonghyunlee.spread.API.support.division.model.DivisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DivisionRepository extends JpaRepository<DivisionEntity, String> {
}
