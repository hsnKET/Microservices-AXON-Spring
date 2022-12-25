package me.ketlas.comptecqrses.query.repositories;

import me.ketlas.comptecqrses.query.entities.Account;
import me.ketlas.comptecqrses.query.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository
        extends JpaRepository<Operation,String> {
}
