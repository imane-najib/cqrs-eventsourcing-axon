package stage.ifm.escqrsaxon.query.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stage.ifm.escqrsaxon.query.entities.Account;

public interface AccountRepository extends JpaRepository<Account, String> {
}
