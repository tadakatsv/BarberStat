package ua.chekmaryov.barber_stat.controller.clients;

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
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoResponse;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoUpdateRequest;
import ua.chekmaryov.barber_stat.enums.ClientStatus;
import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
import ua.chekmaryov.barber_stat.exception.BadRequestException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
import ua.chekmaryov.barber_stat.service.clients.ClientServiceImpl;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc()
@WebMvcTest(ClientController.class)
public class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private ClientServiceImpl clientService;

    @Test
    public void create_shouldReturn201_WhenCreateValidClient() throws Exception{
        ClientDtoCreateRequest request = ClientDtoCreateRequest.builder()
                .firstName("John")
                .lastName("Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(null)
                .lastVisitDate(null)
                .notes(null)
                .build();
        ClientDtoResponse response = ClientDtoResponse.builder()
                .id(1L)
                .fullName("John Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(ClientStatus.ACTIVE)
                .lastVisitDate(null)
                .notes(null)
                .build();

        when(clientService.create(request))
                .thenReturn(response);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/clients")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("John Marston"))
                .andExpect(jsonPath("$.id").value(1L));

        verify(clientService).create(Mockito.any(ClientDtoCreateRequest.class));
    }

    @Test
    public void create_shouldReturn400_WhenLastNameIsBlank() throws Exception{
        ClientDtoCreateRequest request = ClientDtoCreateRequest.builder()
                .firstName("John")
                .lastName("")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(null)
                .lastVisitDate(null)
                .notes(null)
                .build();


        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/clients")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(clientService,never()).create(Mockito.any(ClientDtoCreateRequest.class));
    }

    @Test
    public void getAll_shouldReturn200_WhenClientsExist() throws Exception{
        ClientDtoResponse response = ClientDtoResponse.builder()
                .id(1L)
                .fullName("John Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(ClientStatus.ACTIVE)
                .lastVisitDate(null)
                .notes(null)
                .build();
        Page<ClientDtoResponse> clientPage = new PageImpl<>(List.of(response));

        when(clientService.getAll(any(Pageable.class))).thenReturn(clientPage);

        mockMvc.perform(get("/api/v1/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fullName").value("John Marston"));

        verify(clientService).getAll(any(Pageable.class));
    }

    @Test
    public void getAll_shouldReturn200_WhenNoClientsExist() throws Exception{
        when(clientService.getAll(any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(clientService).getAll(any(Pageable.class));
    }

    @Test
    public void getById_shouldReturn200_WhenClientExistsById() throws Exception{
        Long id = 1L;
        ClientDtoResponse response = ClientDtoResponse.builder()
                .id(1L)
                .fullName("John Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(ClientStatus.ACTIVE)
                .lastVisitDate(null)
                .notes(null)
                .build();

        when(clientService.getById(id)).thenReturn(response);

        mockMvc.perform(get("/api/v1/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
        verify(clientService).getById(anyLong());
    }

    @Test
    public void getById_shouldReturn404_WhenClientNotFoundById() throws Exception{
        Long id = 1L;

        when(clientService.getById(id)).thenThrow(new ResourceNotFoundException("Client not found with id: " + id));

        mockMvc.perform(get("/api/v1/clients/1"))
                .andExpect(status().isNotFound());
        verify(clientService).getById(anyLong());
    }

    @Test
    public void updateById_shouldReturn200_WhenUpdateValidClient() throws Exception{
        Long id = 1L;
        ClientDtoUpdateRequest request = ClientDtoUpdateRequest.builder()
                .firstName("John")
                .lastName("Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(null)
                .lastVisitDate(null)
                .notes(null)
                .build();

        ClientDtoResponse response = ClientDtoResponse.builder()
                .id(1L)
                .fullName("John Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(ClientStatus.ACTIVE)
                .lastVisitDate(null)
                .notes(null)
                .build();

        String json = objectMapper.writeValueAsString(request);


        when(clientService.updateById(id,request)).thenReturn(response);


        mockMvc.perform(put("/api/v1/clients/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        verify(clientService).updateById(anyLong(),eq(request));
    }

    @Test
    public void updateById_shouldReturn404_WhenNoClientById() throws Exception{
        Long id = 1L;
        ClientDtoUpdateRequest request = ClientDtoUpdateRequest.builder()
                .firstName("John")
                .lastName("Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(null)
                .lastVisitDate(null)
                .notes(null)
                .build();

        String json = objectMapper.writeValueAsString(request);


        when(clientService.updateById(id,request)).thenThrow(new ResourceNotFoundException("Client not found with id: " + id));


        mockMvc.perform(put("/api/v1/clients/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(clientService).updateById(anyLong(),eq(request));
    }

    @Test
    public void updateById_shouldReturn409_WhenNoClientById() throws Exception{
        Long id = 1L;
        ClientDtoUpdateRequest request = ClientDtoUpdateRequest.builder()
                .firstName("John")
                .lastName("Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(null)
                .lastVisitDate(null)
                .notes(null)
                .build();

        String json = objectMapper.writeValueAsString(request);


        when(clientService.updateById(id,request)).thenThrow(new AlreadyExistsException("Client from request with " + request.phone() +" already exists"));


        mockMvc.perform(put("/api/v1/clients/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        verify(clientService).updateById(anyLong(),eq(request));
    }

    @Test
    public void deleteById_shouldReturn200_WhenDeleteClientExists() throws Exception{
        Long id = 1L;
        ClientDtoResponse response = ClientDtoResponse.builder()
                .id(1L)
                .fullName("John Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(ClientStatus.ARCHIVED)
                .lastVisitDate(null)
                .notes(null)
                .build();

        when(clientService.deleteById(id)).thenReturn(response);

        mockMvc.perform(delete("/api/v1/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ClientStatus.ARCHIVED.name()));

        verify(clientService).deleteById(anyLong());
    }

    @Test
    public void deleteById_shouldReturn404_WhenDeleteClientDontExists() throws Exception{
        Long id = 1L;

        when(clientService.deleteById(id)).thenThrow(new ResourceNotFoundException("Client not found with id: " + id));

        mockMvc.perform(delete("/api/v1/clients/1"))
                .andExpect(status().isNotFound());

        verify(clientService).deleteById(anyLong());
    }

    @Test
    public void getByPhone_shouldReturn200_WhenClientExistsByPhone() throws Exception{
        ClientDtoResponse response = ClientDtoResponse.builder()
                .id(1L)
                .fullName("John Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(ClientStatus.ACTIVE)
                .lastVisitDate(null)
                .notes(null)
                .build();

        when(clientService.getByPhone("380666666666")).thenReturn(response);

        mockMvc.perform(get("/api/v1/clients/by-phone?phone=380666666666"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("380666666666"));

        verify(clientService).getByPhone(anyString());
    }

    @Test
    public void getByPhone_shouldReturn404_WhenClientDontExistsByPhone() throws Exception{
        String phone = "380666666666";

        when(clientService.getByPhone(phone)).thenThrow(new ResourceNotFoundException("Client not found with phone: " + phone));

        mockMvc.perform(get("/api/v1/clients/by-phone?phone=380666666666"))
                .andExpect(status().isNotFound());

        verify(clientService).getByPhone(anyString());
    }

    @Test
    public void findByFirstNameAndLastName_shouldReturn200_WhenSearchByNames() throws Exception{
        ClientDtoResponse response = ClientDtoResponse.builder()
                .id(1L)
                .fullName("John Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(ClientStatus.ACTIVE)
                .lastVisitDate(null)
                .notes(null)
                .build();

        Page<ClientDtoResponse> clientPage = new PageImpl<>(List.of(response));


        when(clientService.findByFirstNameAndLastName(eq("John"),eq("Marston"),any(Pageable.class))).thenReturn(clientPage);

        mockMvc.perform(get("/api/v1/clients/by-first-name-and-second-name?firstName=John&lastName=Marston"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fullName").value("John Marston"));

        verify(clientService).findByFirstNameAndLastName(anyString(),anyString(),any(Pageable.class));
    }

    @Test
    public void findByFirstNameAndLastName_shouldReturnEmptyPage_WhenNoClients() throws Exception{

        when(clientService.findByFirstNameAndLastName(eq("John"),eq("Marston"),any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/clients/by-first-name-and-second-name?firstName=John&lastName=Marston"))
                .andExpect(status().isOk());

        verify(clientService).findByFirstNameAndLastName(anyString(),anyString(),any(Pageable.class));
    }

    @Test
    public void findByStatusAndLastVisitDateBetween_shouldReturn200_WhenSearchByStatusAndDates() throws Exception{
        ClientDtoResponse response = ClientDtoResponse.builder()
                .id(1L)
                .fullName("John Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(ClientStatus.ACTIVE)
                .lastVisitDate(LocalDate.of(2025,7,2))
                .notes(null)
                .build();

        Page<ClientDtoResponse> clientPage = new PageImpl<>(List.of(response));


        when(clientService.findByStatusAndLastVisitDateBetween(any(ClientStatus.class),any(LocalDate.class),any(LocalDate.class),any(Pageable.class))).thenReturn(clientPage);

        mockMvc.perform(get("/api/v1/clients/search/by-visit-date-and-status?clientStatus=ACTIVE&lastVisitDateAfter=2025-07-01&lastVisitDateBefore=2025-07-05"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].lastVisitDate").value("2025-07-02"));

        verify(clientService).findByStatusAndLastVisitDateBetween(any(ClientStatus.class),any(LocalDate.class),any(LocalDate.class),any(Pageable.class));
    }

    @Test
    public void findByStatusAndLastVisitDateBetween_shouldReturn404_WhenWrongDateLogic() throws Exception{
        LocalDate lastVisitDateAfter = LocalDate.of(2025,7,5);
        LocalDate lastVisitDateBefore = LocalDate.of(2025,7,1);

        when(clientService.findByStatusAndLastVisitDateBetween(any(ClientStatus.class),any(LocalDate.class),any(LocalDate.class),any(Pageable.class))).thenThrow(new BadRequestException("Start date (" + lastVisitDateAfter + ") cannot be after end date (" + lastVisitDateBefore + ")"));

        mockMvc.perform(get("/api/v1/clients/search/by-visit-date-and-status?clientStatus=ACTIVE&lastVisitDateAfter=2025-07-05&lastVisitDateBefore=2025-07-01"))
                .andExpect(status().isNotFound());

        verify(clientService).findByStatusAndLastVisitDateBetween(any(ClientStatus.class),any(LocalDate.class),any(LocalDate.class),any(Pageable.class));
    }

    @Test
    public void findByStatusAndLastVisitDateBetween_shouldReturn404_WhenDatesFromFuture() throws Exception{

        when(clientService.findByStatusAndLastVisitDateBetween(any(ClientStatus.class),any(LocalDate.class),any(LocalDate.class),any(Pageable.class))).thenThrow(new BadRequestException("Search dates cannot be in the future. Today is " + LocalDate.now()));

        mockMvc.perform(get("/api/v1/clients/search/by-visit-date-and-status?clientStatus=ACTIVE&lastVisitDateAfter=2025-07-05&lastVisitDateBefore=2025-07-01"))
                .andExpect(status().isNotFound());

        verify(clientService).findByStatusAndLastVisitDateBetween(any(ClientStatus.class),any(LocalDate.class),any(LocalDate.class),any(Pageable.class));
    }
}