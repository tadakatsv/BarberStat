package ua.chekmaryov.barber_stat.mapper;

import org.springframework.stereotype.Component;
import ua.chekmaryov.barber_stat.dto.salaries.SalaryDtoResponse;
import ua.chekmaryov.barber_stat.dto.salaries.SalaryDtoUpdateRequest;
import ua.chekmaryov.barber_stat.entity.Barber;
import ua.chekmaryov.barber_stat.entity.Salary;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class SalaryMapper {
//    public Salary dtoToEntity(SalaryDtoCreateRequest request, Barber barber){
//        return updateEntityFromDto(request,barber, new Salary());
//    }

    public Salary dtoUpdateToEntity(SalaryDtoUpdateRequest request, Salary toUpdate) {
        return updateEntityFromDto(request, toUpdate);
    }

    public Salary dtoToEntity(LocalDateTime newStart, LocalDateTime newEnd, Barber barber, BigDecimal sum){
        return updateEntityFromDto(newStart,newEnd,barber,sum, new Salary());
    }

    private Salary updateEntityFromDto(LocalDateTime newStart, LocalDateTime newEnd, Barber barber, BigDecimal sum, Salary toUpdate) {
        toUpdate.setBarber(barber);
        toUpdate.setPeriodStart(newStart.toLocalDate());
        toUpdate.setPeriodEnd(newEnd.toLocalDate());
        toUpdate.setTotalSum(sum);
        return toUpdate;
    }

//    private Salary updateEntityFromDto(SalaryDtoCreateRequest request, Barber barber, Salary toUpdate) {
//        toUpdate.setBarber(barber);
//        toUpdate.setPeriodStart(request.periodStart());
//        toUpdate.setPeriodEnd(request.periodEnd());
//        toUpdate.setTotalSum(request.totalSum());
//        if (request.status() != null)toUpdate.setStatus(request.status());
//        return toUpdate;
//    }

    private Salary updateEntityFromDto(SalaryDtoUpdateRequest request, Salary toUpdate) {
        if(request.totalSum() != null) toUpdate.setTotalSum(request.totalSum());
        if(request.status() != null) toUpdate.setStatus(request.status());
        return toUpdate;
    }

    public SalaryDtoResponse toResponse(Salary salary) {
        if (salary == null) return null;
        return SalaryDtoResponse.builder()
                .id(salary.getId())
                .barberId(salary.getBarber().getId())
                .fullNameBarber(salary.getBarber().getFirstName() + " " + salary.getBarber().getLastName())
                .periodStart(salary.getPeriodStart())
                .periodEnd(salary.getPeriodEnd())
                .totalSum(salary.getTotalSum())
                .status(salary.getStatus())
                .build();
    }

}
