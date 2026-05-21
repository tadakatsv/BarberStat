package ua.chekmaryov.barber_stat.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoResponse;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoUpdateRequest;
import ua.chekmaryov.barber_stat.entity.Barber;
import ua.chekmaryov.barber_stat.enums.BarberRole;
import ua.chekmaryov.barber_stat.enums.BarberStatus;
import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
import ua.chekmaryov.barber_stat.mapper.BarberMapper;
import ua.chekmaryov.barber_stat.repository.BarberRepository;
import ua.chekmaryov.barber_stat.service.barbers.BarberServiceImpl;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BarberServiceImplTest {

    @Mock
    private BarberRepository barberRepository;

    @Mock
    private BarberMapper barberMapper;

    @InjectMocks
    private BarberServiceImpl barberService;

    @Test
    public void getAll_ShouldReturnPageOfResponses_WhenBarbersExist(){
        // 1. ARRANGE
        Pageable pageable = PageRequest.of(0, 10); // Перша сторінка, 10 записів

        Barber barber = new Barber(1L,"Артур","Морган","380666666666",LocalDate.of(1868, Month.JUNE,22),BarberStatus.ACTIVE,BarberRole.TOP,50,null);

        Page<Barber> barberPage = new PageImpl<>(List.of(barber), pageable, 1);

        BarberDtoResponse response =
                BarberDtoResponse.builder()
                        .id(1L)
                        .fullName("Артур Морган")
                        .phone("380666666666")
                        .birthDate(LocalDate.of(1868, Month.JUNE,22))
                        .status(BarberStatus.ACTIVE)
                        .role(BarberRole.TOP)
                        .salaryPercent(50)
                        .notes(null)
                        .build();

        when(barberRepository.findAll(pageable)).thenReturn(barberPage);
        when(barberMapper.toResponse(barber)).thenReturn(response);

        // 2. ACT
        Page<BarberDtoResponse> result = barberService.getAll(pageable);

        // 3. ASSERT
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(response, result.getContent().getLast());

        verify(barberRepository, times(1)).findAll(pageable);
        verify(barberMapper, times(1)).toResponse(any(Barber.class));
    }

    @Test
    public void getAll_ShouldReturnEmpty_WhenBarbersDontExist(){
        // 1. ARRANGE
        Pageable pageable = PageRequest.of(0, 10); // Перша сторінка, 10 записів

        when(barberRepository.findAll(pageable)).thenReturn(Page.empty());

        // 2. ACT
        Page<BarberDtoResponse> result = barberService.getAll(pageable);

        // 3. ASSERT
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(barberRepository, times(1)).findAll(pageable);
        verify(barberMapper, times(0)).toResponse(any(Barber.class));
    }

    @Test
    public void create_ShouldReturnBarberDtoResponse_whenNoBarberByPhone(){
        // 1. ARRANGE
        BarberDtoCreateRequest request = BarberDtoCreateRequest.builder()
                .firstName("Артур")
                .lastName("Морган")
                .phone("380666666666")
                .birthDate(LocalDate.of(1868, Month.JUNE,22))
                .status(null)
                .role(BarberRole.TOP)
                .salaryPercent(50)
                .notes(null)
                .build();
        Barber barberBefore = new Barber(null,"Артур","Морган","380666666666",LocalDate.of(1868, Month.JUNE,22),BarberStatus.ACTIVE,BarberRole.TOP,50,null);
        Barber barberAfter = new Barber(1L,"Артур","Морган","380666666666",LocalDate.of(1868, Month.JUNE,22),BarberStatus.ACTIVE,BarberRole.TOP,50,null);
        BarberDtoResponse response =
                BarberDtoResponse.builder()
                        .id(1L)
                        .fullName("Артур Морган")
                        .phone("380666666666")
                        .birthDate(LocalDate.of(1868, Month.JUNE,22))
                        .status(BarberStatus.ACTIVE)
                        .role(BarberRole.TOP)
                        .salaryPercent(50)
                        .notes(null)
                        .build();

        when(barberRepository.existsByPhone(request.phone())).thenReturn(false);
        when(barberMapper.dtoToEntity(request)).thenReturn(barberBefore);
        when(barberRepository.save(barberBefore)).thenReturn(barberAfter);
        when(barberMapper.toResponse(barberAfter)).thenReturn(response);

        // 2. ACT
        BarberDtoResponse result = barberService.create(request);

        // 3. ASSERT
        assertNotNull(result);
        assertEquals(response, result);

        verify(barberRepository, times(1)).existsByPhone(request.phone());
        verify(barberMapper, times(1)).dtoToEntity(request);
        verify(barberRepository, times(1)).save(barberBefore);
        verify(barberMapper, times(1)).toResponse(barberAfter);
    }

    @Test
    public void create_ShouldThrowAlreadyExistsException_WhenPhoneAlreadyRegistered(){
        // 1. ARRANGE
        BarberDtoCreateRequest request = BarberDtoCreateRequest.builder()
                .firstName("Артур")
                .lastName("Морган")
                .phone("380666666666")
                .birthDate(LocalDate.of(1868, Month.JUNE,22))
                .status(null)
                .role(BarberRole.TOP)
                .salaryPercent(50)
                .notes(null)
                .build();

        when(barberRepository.existsByPhone(request.phone())).thenReturn(true);


        // 2. ACT
        Exception exception = assertThrows(AlreadyExistsException.class, () -> barberService.create(request));
        String expectedMessage = "Barber with " + request.phone() +" already exists";
        String actualMessage = exception.getMessage();
        // 3. ASSERT
        assertTrue(actualMessage.contains(expectedMessage));

        verify(barberRepository, times(1)).existsByPhone(request.phone());
        verify(barberMapper, times(0)).dtoToEntity(request);
        verify(barberRepository, times(0)).save(any(Barber.class));
        verify(barberMapper, times(0)).toResponse(any(Barber.class));
    }

    @Test
    public void getById_ShouldReturnDtoResponse_WhenBarberById(){
        // 1. ARRANGE
        Long id = 1L;
        Barber barber= new Barber(id,"Артур","Морган","380666666666",LocalDate.of(1868, Month.JUNE,22),BarberStatus.ACTIVE,BarberRole.TOP,50,null);
        BarberDtoResponse response =
                BarberDtoResponse.builder()
                        .id(1L)
                        .fullName("Артур Морган")
                        .phone("380666666666")
                        .birthDate(LocalDate.of(1868, Month.JUNE,22))
                        .status(BarberStatus.ACTIVE)
                        .role(BarberRole.TOP)
                        .salaryPercent(50)
                        .notes(null)
                        .build();
        when(barberRepository.findById(id)).thenReturn(Optional.of(barber));
        when(barberMapper.toResponse(barber)).thenReturn(response);
        // 2. ACT
        BarberDtoResponse actualResponse = barberService.getById(id);

        // 3. ASSERT
        assertNotNull(actualResponse);
        assertEquals(response,actualResponse);

        verify(barberRepository, times(1)).findById(id);
        verify(barberMapper, times(1)).toResponse(barber);

    }

    @Test
    public void getById_ShouldReturnDtoResponse_WhenNoBarberById(){
        // 1. ARRANGE
        Long id = 1L;

        when(barberRepository.findById(id)).thenReturn(Optional.empty());

        // 2. ACT
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> barberService.getById(id));
        String expectedMessage = "Barber not found with id: " + id;
        String actualMessage = exception.getMessage();

        // 3. ASSERT
        assertTrue(actualMessage.contains(expectedMessage));

        verify(barberRepository, times(1)).findById(anyLong());
        verify(barberMapper, times(0)).toResponse(any(Barber.class));
    }

    @Test
    public void updateById_ShouldReturnDtoResponse_WhenBarberById(){
        // 1. ARRANGE
        Long id = 1L;
        Barber barberBefore = new Barber(1L,"Артур","Морган","380666666666",LocalDate.of(1868, Month.JUNE,22),BarberStatus.ACTIVE,BarberRole.MIDDLE,50,null);
        BarberDtoUpdateRequest request = BarberDtoUpdateRequest.builder()
                .firstName("Артур")
                .lastName("Морган")
                .phone("380666666666")
                .birthDate(LocalDate.of(1868, Month.JUNE,22))
                .status(BarberStatus.VACATION)
                .role(BarberRole.TOP)
                .salaryPercent(50)
                .notes(null)
                .build();
        Barber barberAfter = new Barber(id,"Артур","Морган","380666666666",LocalDate.of(1868, Month.JUNE,22),BarberStatus.VACATION,BarberRole.TOP,50,null);
        Barber barberFromRepostiry = new Barber(1L,"Артур","Морган","380666666666",LocalDate.of(1868, Month.JUNE,22),BarberStatus.VACATION,BarberRole.TOP,50,null);
        BarberDtoResponse response =
                BarberDtoResponse.builder()
                        .id(1L)
                        .fullName("Артур Морган")
                        .phone("380666666666")
                        .birthDate(LocalDate.of(1868, Month.JUNE,22))
                        .status(BarberStatus.VACATION)
                        .role(BarberRole.TOP)
                        .salaryPercent(50)
                        .notes(null)
                        .build();


        when(barberRepository.findById(id)).thenReturn(Optional.of(barberBefore));
        when(barberMapper.dtoUpdateToEntity(request,barberBefore)).thenReturn(barberAfter);
        when(barberRepository.save(barberAfter)).thenReturn(barberFromRepostiry);
        when(barberMapper.toResponse(barberFromRepostiry)).thenReturn(response);

        // 2. ACT
        BarberDtoResponse actualResponse = barberService.updateById(id,request);

        // 3. ASSERT
        assertNotNull(actualResponse);
        assertEquals(response,actualResponse);

        verify(barberRepository).findById(anyLong());
        verify(barberMapper).dtoUpdateToEntity(request,barberBefore);
        verify(barberRepository).save(any(Barber.class));
        verify(barberMapper).toResponse(barberFromRepostiry);
    }

    @Test
    public void updateById_ShouldThrowAlreadyExistsException_WhenPhoneFromBarbeRequestAlreadyExist(){
        // 1. ARRANGE
        Long id = 1L;
        BarberDtoUpdateRequest request = BarberDtoUpdateRequest.builder()
                .firstName("Артур")
                .lastName("Морган")
                .phone("380666666666")
                .birthDate(LocalDate.of(1868, Month.JUNE,22))
                .status(BarberStatus.VACATION)
                .role(BarberRole.TOP)
                .salaryPercent(50)
                .notes(null)
                .build();
        Barber barberBefore = new Barber(1L,"Артур","Морган","380666946699",LocalDate.of(1868, Month.JUNE,22),BarberStatus.ACTIVE,BarberRole.MIDDLE,50,null);

        when(barberRepository.findById(id)).thenReturn(Optional.of(barberBefore));
        when(barberRepository.existsByPhone(request.phone())).thenReturn(true);
        // 2. ACT
        Exception exception = assertThrows(AlreadyExistsException.class, () -> barberService.updateById(id,request));
        String expectedMessage = "Barber from request with " + request.phone() +" already exists";
        String actualMessage = exception.getMessage();

        // 3. ASSERT
        assertTrue(actualMessage.contains(expectedMessage));

        verify(barberRepository, times(1)).findById(anyLong());
        verify(barberRepository).existsByPhone(anyString());
        verify(barberMapper, times(0)).toResponse(any(Barber.class));
        verify(barberRepository, times(0)).save(any(Barber.class));
        verify(barberMapper, times(0)).toResponse(any(Barber.class));
    }

    @Test
    public void updateById_ShouldReturnDtoResponse_WhenNoBarberById(){
        // 1. ARRANGE
        Long id = 1L;
        BarberDtoUpdateRequest request = BarberDtoUpdateRequest.builder()
                .firstName("Артур")
                .lastName("Морган")
                .phone("380666666666")
                .birthDate(LocalDate.of(1868, Month.JUNE,22))
                .status(BarberStatus.VACATION)
                .role(BarberRole.TOP)
                .salaryPercent(50)
                .notes(null)
                .build();

        when(barberRepository.findById(id)).thenReturn(Optional.empty());
        // 2. ACT
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> barberService.updateById(id,request));
        String expectedMessage = "Barber not found with id: " + id;
        String actualMessage = exception.getMessage();
        // 3. ASSERT
        assertTrue(actualMessage.contains(expectedMessage));

        verify(barberRepository, times(1)).findById(anyLong());
        verify(barberMapper, times(0)).toResponse(any(Barber.class));
        verify(barberRepository, times(0)).save(any(Barber.class));
        verify(barberMapper, times(0)).toResponse(any(Barber.class));
    }

    @Test
    public void deleteById_ShouldReturnDtoResponse_WhenBarberById(){
        // 1. ARRANGE
        Long id = 1L;
        Barber barber= new Barber(id,"Артур","Морган","380666666666",LocalDate.of(1868, Month.JUNE,22),BarberStatus.VACATION,BarberRole.TOP,50,null);
        BarberDtoResponse response =
                BarberDtoResponse.builder()
                        .id(1L)
                        .fullName("Артур Морган")
                        .phone("380666666666")
                        .birthDate(LocalDate.of(1868, Month.JUNE,22))
                        .status(BarberStatus.FIRED)
                        .role(BarberRole.TOP)
                        .salaryPercent(50)
                        .notes(null)
                        .build();

        when(barberRepository.findById(id)).thenReturn(Optional.of(barber));
        when(barberMapper.toResponse(barber)).thenReturn(response);
        // 2. ACT
        BarberDtoResponse actualResponse = barberService.deleteById(id);
        // 3. ASSERT
        assertNotNull(actualResponse);
        assertEquals(response,actualResponse);
        assertEquals(BarberStatus.FIRED, actualResponse.status());

        verify(barberRepository).findById(anyLong());
        verify(barberMapper).toResponse(barber);
    }

    @Test
    public void deleteById_ShouldReturnDtoResponse_WhenNoBarberById(){
        // 1. ARRANGE
        Long id = 1L;

        when(barberRepository.findById(id)).thenReturn(Optional.empty());
        // 2. ACT
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> barberService.deleteById(id));
        String expectedMessage = "Barber not found with id: " + id;
        String actualMessage = exception.getMessage();
        // 3. ASSERT
        assertTrue(actualMessage.contains(expectedMessage));

        verify(barberRepository, times(1)).findById(anyLong());
        verify(barberMapper, times(0)).toResponse(any(Barber.class));
    }

    @Test
    public void findByFirstNameAndLastName_ShouldReturnPageOfResponses_WhenBarbersExist(){
        // 1. ARRANGE
        Pageable pageable = PageRequest.of(0, 10);

        Barber barber= new Barber(1L,"Артур","Морган","380666666666",LocalDate.of(1868, Month.JUNE,22),BarberStatus.VACATION,BarberRole.TOP,50,null);

        Page<Barber> barberPage = new PageImpl<>(List.of(barber), pageable, 2);

        BarberDtoResponse response =
                BarberDtoResponse.builder()
                        .id(1L)
                        .fullName("Артур Морган")
                        .phone("380666666666")
                        .birthDate(LocalDate.of(1868, Month.JUNE,22))
                        .status(BarberStatus.ACTIVE)
                        .role(BarberRole.TOP)
                        .salaryPercent(50)
                        .notes(null)
                        .build();

        when(barberRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("Артур","Морган",pageable)).thenReturn(barberPage);
        when(barberMapper.toResponse(barber)).thenReturn(response);

        // 2. ACT
        Page<BarberDtoResponse> result = barberService.findByFirstNameAndLastName("Артур","Морган",pageable);

        // 3. ASSERT
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(response, result.getContent().getLast());

        verify(barberRepository, times(1)).findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(anyString(),anyString(),eq(pageable));
        verify(barberMapper, times(1)).toResponse(any(Barber.class));
    }

    @Test
    public void findByFirstNameAndLastName_ShouldReturnEmpty_WhenBarbersDontExist(){
        // 1. ARRANGE
        Pageable pageable = PageRequest.of(0, 10); // Перша сторінка, 10 записів

        when(barberRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("Артур","Морган",pageable)).thenReturn(Page.empty());

        // 2. ACT
        Page<BarberDtoResponse> result = barberService.findByFirstNameAndLastName("Артур","Морган",pageable);

        // 3. ASSERT
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(barberRepository, times(1)).findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(anyString(),anyString(),eq(pageable));
        verify(barberMapper, times(0)).toResponse(any(Barber.class));
    }

    @Test
    public void findByStatus_ShouldReturnPageOfResponses_WhenBarbersExist(){
        // 1. ARRANGE
        Pageable pageable = PageRequest.of(0, 10);

        Barber barber= new Barber(1L,"Артур","Морган","380666666666",LocalDate.of(1868, Month.JUNE,22),BarberStatus.VACATION,BarberRole.TOP,50,null);

        Page<Barber> barberPage = new PageImpl<>(List.of(barber), pageable, 2);

        BarberDtoResponse response =
                BarberDtoResponse.builder()
                        .id(1L)
                        .fullName("Артур Морган")
                        .phone("380666666666")
                        .birthDate(LocalDate.of(1868, Month.JUNE,22))
                        .status(BarberStatus.ACTIVE)
                        .role(BarberRole.TOP)
                        .salaryPercent(50)
                        .notes(null)
                        .build();

        when(barberRepository.findBarbersByStatusIs(BarberStatus.ACTIVE,pageable)).thenReturn(barberPage);
        when(barberMapper.toResponse(barber)).thenReturn(response);

        // 2. ACT
        Page<BarberDtoResponse> result = barberService.findByStatus(BarberStatus.ACTIVE,pageable);

        // 3. ASSERT
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(response, result.getContent().getLast());

        verify(barberRepository, times(1)).findBarbersByStatusIs(any(BarberStatus.class),eq(pageable));
        verify(barberMapper, times(1)).toResponse(any(Barber.class));
    }

    @Test
    public void findByStatus_ShouldReturnEmpty_WhenBarbersDontExist(){
        // 1. ARRANGE
        Pageable pageable = PageRequest.of(0, 10); // Перша сторінка, 10 записів

        when(barberRepository.findBarbersByStatusIs(BarberStatus.ACTIVE,pageable)).thenReturn(Page.empty());

        // 2. ACT
        Page<BarberDtoResponse> result = barberService.findByStatus(BarberStatus.ACTIVE,pageable);

        // 3. ASSERT
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(barberRepository, times(1)).findBarbersByStatusIs(any(BarberStatus.class),eq(pageable));
        verify(barberMapper, times(0)).toResponse(any(Barber.class));
    }
}

