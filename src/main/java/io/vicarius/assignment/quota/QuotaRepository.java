package io.vicarius.assignment.quota;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotaRepository extends CrudRepository<Quota, String> {
}
