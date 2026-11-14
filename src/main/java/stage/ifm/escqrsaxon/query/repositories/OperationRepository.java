package stage.ifm.escqrsaxon.query.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stage.ifm.escqrsaxon.query.entities.AccountOperation;

public interface OperationRepository extends JpaRepository<AccountOperation,Long> {
}
