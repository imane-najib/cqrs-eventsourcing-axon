package stage.ifm.escqrsaxon.query.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stage.ifm.escqrsaxon.query.entities.AccountOperation;

import java.util.List;

public interface OperationRepository extends JpaRepository<AccountOperation,Long> {
    List<AccountOperation> findByAccountId(String Id);
}
