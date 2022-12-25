package me.ketlas.comptecqrses.query.repositories;

import me.ketlas.comptecqrses.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {

}
