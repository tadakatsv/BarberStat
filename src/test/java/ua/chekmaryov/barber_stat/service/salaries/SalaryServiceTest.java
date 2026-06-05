//package ua.chekmaryov.barber_stat.service.salaries;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import ua.chekmaryov.barber_stat.dto.salaries.SalaryDtoResponse;
//import ua.chekmaryov.barber_stat.dto.salaries.SalaryDtoUpdateRequest;
//import ua.chekmaryov.barber_stat.entity.Barber;
//import ua.chekmaryov.barber_stat.entity.Salary;
//import ua.chekmaryov.barber_stat.enums.BarberRole;
//import ua.chekmaryov.barber_stat.enums.BarberStatus;
//import ua.chekmaryov.barber_stat.enums.SalaryStatus;
//import ua.chekmaryov.barber_stat.exception.BadRequestException;
//import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
//import ua.chekmaryov.barber_stat.mapper.SalaryMapper;
//import ua.chekmaryov.barber_stat.repository.BarberRepository;
//import ua.chekmaryov.barber_stat.repository.SalaryRepository;
//import ua.chekmaryov.barber_stat.repository.VisitRepository;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.Month;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class SalaryServiceTest {
//    @Mock
//    private SalaryMapper mapper;
//
//    @Mock
//    private SalaryRepository salaryRepository;
//
//    @Mock
//    private VisitRepository visitRepository;
//
//    @Mock
//    private BarberRepository barberRepository;
//
//    @InjectMocks
//    private SalaryService service;
//
//    private Long id;
//    private Barber barber;
//    private Salary salary;
//    private Pageable pageable;
//    private Page<Salary> salariesPage;
//    private SalaryDtoResponse response;
//    private LocalDate start;
//    private LocalDate end;
//    private BigDecimal sumSalary;
//    private Salary salaryWithoutId;
//    private SalaryDtoResponse responseNoId;
//
//
//    @BeforeEach
//    void setUp() {
//        id = 1L;
//        barber = new Barber(id,"Arthur","Morgan","380666666666", LocalDate.of(1868, Month.JUNE,22), BarberStatus.ACTIVE, BarberRole.TOP,50,null);
//        start = LocalDate.of(2026, 1, 1);
//        end = LocalDate.of(2026, 1, 30);
//        sumSalary = BigDecimal.valueOf(400);
//        salaryWithoutId = new Salary(id,barber, start, end, sumSalary, SalaryStatus.PENDING);
//        salary = new Salary(id,barber, start, end, sumSalary, SalaryStatus.PENDING);
//        pageable = PageRequest.of(0, 10);
//        salariesPage = new PageImpl<>(List.of(salary), pageable, 1);
//        response = SalaryDtoResponse.builder()
//                .id(id)
//                .barberId(id)
//                .barberFullName(barber.getFirstName() + " " + barber.getLastName())
//                .periodStart(salary.getPeriodStart())
//                .periodEnd(salary.getPeriodEnd())
//                .totalSum(salary.getTotalSum())
//                .status(salary.getStatus())
//                .build();
//        responseNoId = SalaryDtoResponse.builder()
//                .id(id)
//                .barberId(id)
//                .barberFullName(barber.getFirstName() + " " + barber.getLastName())
//                .periodStart(salary.getPeriodStart())
//                .periodEnd(salary.getPeriodEnd())
//                .totalSum(salary.getTotalSum())
//                .status(salary.getStatus())
//                .build();
//    }
//
//    @Test
//    public void getAll_ShouldReturnPageOfResponses_WhenSalaryExist(){
//        when(salaryRepository.findAll(pageable)).thenReturn(salariesPage);
//        when(mapper.toResponse(any(Salary.class))).thenReturn(response);
//
//        Page<SalaryDtoResponse> salaries = service.getAll(pageable);
//
//        assertNotNull(salaries);
//        assertEquals(response,salaries.getContent().getLast());
//
//        verify(salaryRepository).findAll(any(Pageable.class));
//        verify(mapper).toResponse(any(Salary.class));
//    }
//
//    @Test
//    public void getAll_ShouldReturnEmptyPage_WhenNoSalaryExist(){
//        when(salaryRepository.findAll(pageable)).thenReturn(Page.empty());
//
//        Page<SalaryDtoResponse> salaries = service.getAll(pageable);
//
//        assertNotNull(salaries);
//        assertEquals(Collections.emptyList(),salaries.getContent());
//
//        verify(salaryRepository).findAll(any(Pageable.class));
//        verify(mapper,never()).toResponse(any(Salary.class));
//    }
//
//    @Test
//    public void getById_ShouldReturnResponse_WhenSalaryExist(){
//        when(salaryRepository.findById(id)).thenReturn(Optional.of(salary));
//        when(mapper.toResponse(any(Salary.class))).thenReturn(response);
//
//        SalaryDtoResponse actualResponse = service.getById(id);
//
//        assertNotNull(actualResponse);
//        assertEquals(response,actualResponse);
//
//        verify(salaryRepository).findById(anyLong());
//        verify(mapper).toResponse(any(Salary.class));
//    }
//
//    @Test
//    public void getById_ShouldThrowResourceNotFoundException_WhenNoSalaryExist(){
//        when(salaryRepository.findById(id)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class,() -> service.getById(id));
//        String expectedMessage = "No salary record by this id:" + id;
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//
//        verify(salaryRepository).findById(anyLong());
//        verify(mapper,never()).toResponse(any(Salary.class));
//    }
//
//    @Test
//    public void updateById_ShouldReturnResponse_WhenSalaryExistById(){
//        SalaryDtoUpdateRequest updateRequest = SalaryDtoUpdateRequest.builder()
//                .totalSum(BigDecimal.valueOf(500))
//                .status(SalaryStatus.PAID)
//                .build();
//        Salary updatedSalary = new Salary(salary.getId(),salary.getBarber(),salary.getPeriodStart(),salary.getPeriodEnd(),updateRequest.totalSum(),updateRequest.status());
//        SalaryDtoResponse updatedResponse = SalaryDtoResponse.builder()
//                .id(updatedSalary.getId())
//                .barberId(updatedSalary.getBarber().getId())
//                .barberFullName(updatedSalary.getBarber().getFirstName() + " " + updatedSalary.getBarber().getLastName())
//                .periodStart(updatedSalary.getPeriodStart())
//                .periodEnd(updatedSalary.getPeriodEnd())
//                .totalSum(updatedSalary.getTotalSum())
//                .status(updatedSalary.getStatus())
//                .build();
//
//        when(salaryRepository.findById(anyLong())).thenReturn(Optional.of(salary));
//        when(mapper.dtoUpdateToEntity(updateRequest,salary)).thenReturn(updatedSalary);
//        when(salaryRepository.save(any(Salary.class))).thenReturn(updatedSalary);
//        when(mapper.toResponse(any(Salary.class))).thenReturn(updatedResponse);
//
//        SalaryDtoResponse actualResponse = service.updateById(id,updateRequest);
//
//        assertNotNull(actualResponse);
//        assertEquals(updatedResponse,actualResponse);
//        assertEquals(SalaryStatus.PAID,actualResponse.status());
//        assertEquals(BigDecimal.valueOf(500),actualResponse.totalSum());
//
//        verify(salaryRepository).findById(anyLong());
//        verify(mapper).dtoUpdateToEntity(any(SalaryDtoUpdateRequest.class),any(Salary.class));
//        verify(salaryRepository).save(any(Salary.class));
//        verify(mapper).toResponse(any(Salary.class));
//    }
//
//    @Test
//    public void updateById_ShouldThrowResourceNotFoundException_WhenNoSalaryExistById(){
//        SalaryDtoUpdateRequest updateRequest = SalaryDtoUpdateRequest.builder()
//                .totalSum(BigDecimal.valueOf(500))
//                .status(SalaryStatus.PAID)
//                .build();
//
//        when(salaryRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//
//        Exception exception = assertThrows(ResourceNotFoundException.class,() -> service.updateById(id,updateRequest));
//        String expectedMessage = "No salary record by this id:" + id;
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//
//        verify(salaryRepository).findById(anyLong());
//        verify(mapper,never()).dtoUpdateToEntity(any(SalaryDtoUpdateRequest.class),any(Salary.class));
//        verify(salaryRepository,never()).save(any(Salary.class));
//        verify(mapper,never()).toResponse(any(Salary.class));
//    }
//
//    @Test
//    public void findByBarber_Id_ShouldReturnPageOfResponses_WhenBarberExistsById(){
//        when(barberRepository.existsById(anyLong())).thenReturn(true);
//        when(salaryRepository.findSalariesByBarber_Id(id,pageable)).thenReturn(salariesPage);
//        when(mapper.toResponse(salary)).thenReturn(response);
//
//        Page<SalaryDtoResponse> actualResponse = service.findByBarber_Id(id,pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(response,actualResponse.getContent().getLast());
//        assertEquals(1,actualResponse.getTotalElements());
//
//        verify(barberRepository).existsById(anyLong());
//        verify(salaryRepository).findSalariesByBarber_Id(anyLong(),any(Pageable.class));
//        verify(mapper).toResponse(any(Salary.class));
//    }
//
//    @Test
//    public void findByBarber_Id_ShouldReturnEmptyPage_WhenBarberExistsByIdButNoSalaries(){
//        when(barberRepository.existsById(anyLong())).thenReturn(true);
//        when(salaryRepository.findSalariesByBarber_Id(id,pageable)).thenReturn(Page.empty());
//        Page<SalaryDtoResponse> actualResponse = service.findByBarber_Id(id,pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(Collections.emptyList(),actualResponse.getContent());
//        assertEquals(0,actualResponse.getTotalElements());
//
//        verify(barberRepository).existsById(anyLong());
//        verify(salaryRepository).findSalariesByBarber_Id(anyLong(),any(Pageable.class));
//        verify(mapper, never()).toResponse(any(Salary.class));
//    }
//
//    @Test
//    public void findByBarber_Id_ShouldThrowResourceNotFoundException_WhenBarberDontExistsById(){
//        when(barberRepository.existsById(anyLong())).thenReturn(false);
//
//        Exception exception = assertThrows(ResourceNotFoundException.class,() -> service.findByBarber_Id(id,pageable));
//        String expectedMessage = "Barber not found with id: " + id;
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);//лучше так делать, потому что в случае ошибки что у именно ошибка дала
//
//        verify(barberRepository).existsById(anyLong());
//        verify(salaryRepository,never()).findSalariesByBarber_Id(anyLong(),any(Pageable.class));
//        verify(mapper, never()).toResponse(any(Salary.class));
//    }
//
//    @Test
//    public void checkSumSalaryForBarber_ShouldReturnResponse_WhenAllValid(){
//        when(barberRepository.findById(id)).thenReturn(Optional.of(barber));
//        when(visitRepository.sumSalaryForBarber(id,start.atStartOfDay(),end.atTime(LocalTime.MAX))).thenReturn(Optional.of(sumSalary));
//        when(mapper.dtoToEntity(start.atStartOfDay(),end.atTime(LocalTime.MAX),barber,sumSalary)).thenReturn(salaryWithoutId);
//        when(mapper.toResponse(salaryWithoutId)).thenReturn(responseNoId);
//
//        SalaryDtoResponse actualResponse = service.checkSumSalaryForBarber(id,start.atStartOfDay(),end.atTime(LocalTime.MAX));
//
//        assertNotNull(actualResponse);
//        assertEquals(actualResponse,responseNoId);
//        assertEquals(BigDecimal.valueOf(400), actualResponse.totalSum());
//
//        verify(barberRepository).findById(anyLong());
//        verify(visitRepository).sumSalaryForBarber(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class));
//        verify(mapper).dtoToEntity(any(LocalDateTime.class),any(LocalDateTime.class),any(Barber.class),any(BigDecimal.class));
//        verify(mapper).toResponse(any(Salary.class));
//    }
//
//    @Test
//    public void checkSumSalaryForBarber_ShouldReturnResponse_WhenEndDateIsBeforeStart(){
//        Exception exception = assertThrows(BadRequestException.class,() ->service.checkSumSalaryForBarber(id,end.atTime(LocalTime.MAX),start.atStartOfDay()));
//        String expectedMessage = "Start date (" + end.atTime(LocalTime.MAX) + ") cannot be after end date (" + start.atStartOfDay() + ")";
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(barberRepository,never()).findById(anyLong());
//        verify(visitRepository,never()).sumSalaryForBarber(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class));
//        verify(mapper,never()).dtoToEntity(any(LocalDateTime.class),any(LocalDateTime.class),any(Barber.class),any(BigDecimal.class));
//        verify(mapper,never()).toResponse(any(Salary.class));
//    }
//
//    @Test
//    public void checkSumSalaryForBarber_ShouldReturnResponse_WhenNoBarberExistById(){
//        when(barberRepository.findById(id)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class,() ->service.checkSumSalaryForBarber(id,start.atStartOfDay(),end.atTime(LocalTime.MAX)));
//        String expectedMessage = "Barber not found with id: " + id;
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(barberRepository).findById(anyLong());
//        verify(visitRepository,never()).sumSalaryForBarber(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class));
//        verify(mapper,never()).dtoToEntity(any(LocalDateTime.class),any(LocalDateTime.class),any(Barber.class),any(BigDecimal.class));
//        verify(mapper,never()).toResponse(any(Salary.class));
//    }
//
//    @Test
//    public void saveSumSalaryForBarber_ShouldReturnResponse_WhenAllValid(){
//        when(barberRepository.findById(id)).thenReturn(Optional.of(barber));
//        when(visitRepository.sumSalaryForBarber(id,start.atStartOfDay(),end.atTime(LocalTime.MAX))).thenReturn(Optional.of(sumSalary));
//        when(mapper.dtoToEntity(start.atStartOfDay(),end.atTime(LocalTime.MAX),barber,sumSalary)).thenReturn(salaryWithoutId);
//        when(salaryRepository.save(salaryWithoutId)).thenReturn(salary);
//        when(mapper.toResponse(salary)).thenReturn(response);
//
//        SalaryDtoResponse actualResponse = service.saveSumSalaryForBarber(id,start.atStartOfDay(),end.atTime(LocalTime.MAX));
//
//        assertNotNull(actualResponse);
//        assertEquals(actualResponse,response);
//        assertEquals(BigDecimal.valueOf(400), actualResponse.totalSum());
//
//        verify(barberRepository).findById(anyLong());
//        verify(visitRepository).sumSalaryForBarber(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class));
//        verify(mapper).dtoToEntity(any(LocalDateTime.class),any(LocalDateTime.class),any(Barber.class),any(BigDecimal.class));
//        verify(mapper).toResponse(any(Salary.class));
//    }
//
//    @Test
//    public void saveSumSalaryForBarber_ShouldReturnResponse_WhenEndDateIsBeforeStart(){
//
//        Exception exception = assertThrows(BadRequestException.class,() ->service.saveSumSalaryForBarber(id,end.atTime(LocalTime.MAX),start.atStartOfDay()));
//        String expectedMessage = "Start date (" + end.atTime(LocalTime.MAX) + ") cannot be after end date (" + start.atStartOfDay() + ")";
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(barberRepository, never()).findById(anyLong());
//        verify(visitRepository, never()).sumSalaryForBarber(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class));
//        verify(mapper, never()).dtoToEntity(any(LocalDateTime.class),any(LocalDateTime.class),any(Barber.class),any(BigDecimal.class));
//        verify(mapper, never()).toResponse(any(Salary.class));
//    }
//
//
//    @Test
//    public void saveSumSalaryForBarber_ShouldReturnResponse_WhenNoBarberExistById(){
//        when(barberRepository.findById(id)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class,() ->service.saveSumSalaryForBarber(id,start.atStartOfDay(),end.atTime(LocalTime.MAX)));
//        String expectedMessage = "Barber not found with id: " + id;
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(barberRepository).findById(anyLong());
//        verify(visitRepository,never()).sumSalaryForBarber(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class));
//        verify(mapper,never()).dtoToEntity(any(LocalDateTime.class),any(LocalDateTime.class),any(Barber.class),any(BigDecimal.class));
//        verify(mapper,never()).toResponse(any(Salary.class));
//    }
//}