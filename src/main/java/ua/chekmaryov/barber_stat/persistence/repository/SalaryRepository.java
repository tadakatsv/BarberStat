package ua.chekmaryov.barber_stat.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.chekmaryov.barber_stat.persistence.entity.Salary;

@Repository
public interface SalaryRepository extends JpaRepository<Salary,Long> {
}
