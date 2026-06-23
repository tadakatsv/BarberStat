package ua.chekmaryov.barber_stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.chekmaryov.barber_stat.entity.Salary;

@Repository
public interface SalaryRepository extends JpaRepository<Salary,Long> {
}
