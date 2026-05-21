package ua.chekmaryov.barber_stat.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoResponse;
import ua.chekmaryov.barber_stat.dto.barbers.BarberDtoUpdateRequest;
import ua.chekmaryov.barber_stat.enums.BarberRole;
import ua.chekmaryov.barber_stat.enums.BarberStatus;
import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
import ua.chekmaryov.barber_stat.service.barbers.BarberServiceImpl;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc()
@WebMvcTest(BarberController.class)
public class BarberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private BarberServiceImpl barberService;

    @Test
    public void create_shouldReturn201_WhenCreateValidBarber() throws Exception{
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

        when(barberService.create(request))
                .thenReturn(response);


        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/barbers")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Артур Морган"))
                .andExpect(jsonPath("$.id").value(1L));

        verify(barberService).create(Mockito.any(BarberDtoCreateRequest.class));
    }

    @Test
    public void create_shouldReturn400_WhenFirstNameIsBlank() throws Exception {
        BarberDtoCreateRequest invalidRequest = BarberDtoCreateRequest.builder()
                .firstName("") // Порожнє ім'я
                .lastName("Морган")
                .phone("380666666666")
                .build();

        String json = objectMapper.writeValueAsString(invalidRequest);
        mockMvc.perform(post("/api/v1/barbers")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // Чекаємо 400 помилку

        verify(barberService, times(0)).create(any());
    }

    @Test
    public void create_shouldReturn400_WhenSalaryPercentIsHigherThan100() throws Exception {
        BarberDtoCreateRequest invalidRequest = BarberDtoCreateRequest.builder()
                .firstName("Arthur")
                .lastName("Morgan")
                .phone("380666666666")
                .salaryPercent(120)
                .build();

        String json = objectMapper.writeValueAsString(invalidRequest);
        mockMvc.perform(post("/api/v1/barbers")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(barberService, times(0)).create(any());
    }

    @Test
    public void create_shouldReturn409_WhenBarberPhoneAlreadyExists() throws Exception{
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

        when(barberService.create(request))
                .thenThrow(new AlreadyExistsException("Barber with " + request.phone() +" already exists"));

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/barbers")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()); // Чекаємо 400 помилку
    }

    @Test
    public void getAll_shouldReturn200_WhenBarbersExist() throws Exception{
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
        Page<BarberDtoResponse> barberPage = new PageImpl<>(List.of(response));

        when(barberService.getAll(any(Pageable.class))).thenReturn(barberPage);

        mockMvc.perform(get("/api/v1/barbers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fullName").value("Артур Морган"));

        verify(barberService).getAll(any(Pageable.class));
    }

    @Test
    public void getAll_shouldReturn200_WhenBarbersDontExist() throws Exception{
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

        when(barberService.getById(id)).thenReturn(response);

        mockMvc.perform(get("/api/v1/barbers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
        verify(barberService).getById(anyLong());
    }

    @Test
    public void getById_shouldReturn404_WhenBarberNotFoundById() throws Exception{
        Long id = 1L;

        when(barberService.getById(id))
                .thenThrow(new ResourceNotFoundException("Barber not found with id: " + id));

        mockMvc.perform(get("/api/v1/barbers/1"))
                .andExpect(status().isNotFound());
        verify(barberService).getById(anyLong());
    }

    @Test
    public void updateById_shouldReturn200_WhenUpdateValidBarber() throws Exception{
        Long id = 1L;
        BarberDtoUpdateRequest request = BarberDtoUpdateRequest.builder()
                .firstName("Артур")
                .lastName("Морган")
                .phone("380666666666")
                .birthDate(LocalDate.of(1868, Month.JUNE,22))
                .status(null)
                .role(BarberRole.TOP)
                .salaryPercent(50)
                .notes(null)
                .build();

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

        String json = objectMapper.writeValueAsString(request);


        when(barberService.updateById(id,request)).thenReturn(response);


        mockMvc.perform(put("/api/v1/barbers/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        verify(barberService).updateById(anyLong(),eq(request));
    }

    @Test
    public void shouldSoftDeleteById() throws Exception{
        Long id = 1L;
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

        when(barberService.deleteById(id)).thenReturn(response);

        mockMvc.perform(delete("/api/v1/barbers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(BarberStatus.FIRED.name()));

        verify(barberService).deleteById(anyLong());
    }

    @Test
    public void shouldReturnBarbersByFirstName() throws Exception{
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
        Page<BarberDtoResponse> barberPage = new PageImpl<>(List.of(response));

        when(barberService.findByFirstNameAndLastName(eq("Артур"),eq("Морган"), any(Pageable.class))).thenReturn(barberPage);

        mockMvc.perform(get("/api/v1/barbers/search/by-first-name-and-second-name?firstName=Артур&lastName=Морган"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fullName").value("Артур Морган"));

        verify(barberService).findByFirstNameAndLastName(anyString(),anyString(),any(Pageable.class));
    }

    @Test
    public void findByStatus_shouldReturn200_WhenSearchByStatus() throws Exception{
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
        Page<BarberDtoResponse> barberPage = new PageImpl<>(List.of(response));

        when(barberService.findByStatus(eq(BarberStatus.ACTIVE), any(Pageable.class))).thenReturn(barberPage);

        mockMvc.perform(get("/api/v1/barbers/search/by-status?status=ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].status").value(BarberStatus.ACTIVE.name()));

        verify(barberService).findByStatus(eq(BarberStatus.ACTIVE), any(Pageable.class));
    }
}