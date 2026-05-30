package ua.chekmaryov.barber_stat.service.clients;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoCreateRequest;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoResponse;
import ua.chekmaryov.barber_stat.dto.clients.ClientDtoUpdateRequest;
import ua.chekmaryov.barber_stat.entity.Client;
import ua.chekmaryov.barber_stat.enums.ClientStatus;
import ua.chekmaryov.barber_stat.exception.BadRequestException;
import ua.chekmaryov.barber_stat.mapper.ClientMapper;
import ua.chekmaryov.barber_stat.repository.ClientRepository;
import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    public void create_ShouldReturnResponse_WhenNoClientByPhone(){
        ClientDtoCreateRequest request = ClientDtoCreateRequest.builder()
                .firstName("John")
                .lastName("Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(null)
                .lastVisitDate(null)
                .notes(null)
                .build();
        Client clientBefore = new Client(null,"John","Marston", "380666666666", LocalDate.of(1873, Month.JUNE,22), ClientStatus.ACTIVE, null,null);
        Client clientAfter = new Client(1L,"John","Marston", "380666666666", LocalDate.of(1873, Month.JUNE,22), ClientStatus.ACTIVE, null,null);
        ClientDtoResponse response = ClientDtoResponse.builder()
                .id(1L)
                .fullName("John Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(ClientStatus.ACTIVE)
                .lastVisitDate(null)
                .notes(null)
                .build();
        when(clientRepository.existsByPhone(request.phone())).thenReturn(false);
        when(clientMapper.dtoToEntity(request)).thenReturn(clientBefore);
        when(clientRepository.save(clientBefore)).thenReturn(clientAfter);
        when(clientMapper.toResponse(clientAfter)).thenReturn(response);

        ClientDtoResponse actualResponse = clientService.create(request);

        assertNotNull(actualResponse);
        assertEquals(response, actualResponse);
        assertEquals(1L,actualResponse.id());

        verify(clientRepository).existsByPhone(anyString());
        verify(clientMapper).toResponse(any(Client.class));
        verify(clientRepository).save(any(Client.class));
        verify(clientMapper).dtoToEntity(any(ClientDtoCreateRequest.class));
    }

    @Test
    public void create_ShouldThrowAlreadyExistsException_WhenNoClientByPhone(){
        ClientDtoCreateRequest request = ClientDtoCreateRequest.builder()
                .firstName("John")
                .lastName("Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(null)
                .lastVisitDate(null)
                .notes(null)
                .build();
        when(clientRepository.existsByPhone(request.phone())).thenReturn(true);

        Exception exception = assertThrows(AlreadyExistsException.class, () -> clientService.create(request));

        String expectedMessage = "Client with " + request.phone() +" already exists";
        String actualMessage = exception.getMessage();
        // 3. ASSERT
        assertTrue(actualMessage.contains(expectedMessage));

        verify(clientRepository).existsByPhone(anyString());
        verify(clientMapper,never()).toResponse(any(Client.class));
        verify(clientRepository,never()).save(any(Client.class));
        verify(clientMapper,never()).dtoToEntity(any(ClientDtoCreateRequest.class));
    }

    @Test
    public void getAll_ShouldReturnPageOfClients_WhenBarbersExists(){
        Pageable pageable = PageRequest.of(0, 10); // Перша сторінка, 10 записів
        Client client = new Client(1L,"John","Marston", "380666666666", LocalDate.of(1873, Month.JUNE,22), ClientStatus.ACTIVE, null,null);// Перша сторінка, 10 записів
        Page<Client> allClients = new PageImpl<>(List.of(client),pageable,1);
        ClientDtoResponse response = ClientDtoResponse.builder()
                .id(1L)
                .fullName("John Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(ClientStatus.ACTIVE)
                .lastVisitDate(null)
                .notes(null)
                .build();

        when(clientRepository.findAll(any(Pageable.class))).thenReturn(allClients);
        when(clientMapper.toResponse(client)).thenReturn(response);

        Page<ClientDtoResponse> result = clientService.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(response, result.getContent().getLast());

        verify(clientRepository).findAll(any(Pageable.class));
        verify(clientMapper).toResponse(any(Client.class));
    }

    @Test
    public void getAll_shouldReturnEmptyPage_whenNoClients(){
        Pageable pageable = PageRequest.of(0, 10); // Перша сторінка, 10 записів

        when(clientRepository.findAll(pageable)).thenReturn(Page.empty());

        Page<ClientDtoResponse> result = clientService.getAll(pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(Collections.emptyList(), result.getContent());

        verify(clientRepository).findAll(any(Pageable.class));
        verify(clientMapper,never()).toResponse(any(Client.class));
    }

    @Test
    public void getById_ShouldReturnClientDtoResponse_WhenBarberById(){
        Long id =1L;
        Client client = new Client(1L,"John","Marston", "380666666666", LocalDate.of(1873, Month.JUNE,22), ClientStatus.ACTIVE, null,null);
        ClientDtoResponse response = ClientDtoResponse.builder()
                .id(1L)
                .fullName("John Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(ClientStatus.ACTIVE)
                .lastVisitDate(null)
                .notes(null)
                .build();

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        when(clientMapper.toResponse(client)).thenReturn(response);

        ClientDtoResponse actualResponse = clientService.getById(id);

        assertNotNull(actualResponse);
        assertEquals(response,actualResponse);
        assertEquals(id, actualResponse.id());

        verify(clientRepository).findById(anyLong());
        verify(clientMapper).toResponse(any(Client.class));
    }

    @Test
    public void getById_ShouldThrowResourceNotFoundException_WhenNoClientById(){
        Long id = 1L;

        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> clientService.getById(id));

        String expectedMessage = "Client not found with id: " + id;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(clientRepository).findById(anyLong());
        verify(clientMapper,never()).toResponse(any(Client.class));
    }

    @Test
    public void updateById_ShouldReturnResponse_WhenClientById(){
        Long id = 1L;
        ClientDtoUpdateRequest request = ClientDtoUpdateRequest.builder()
                .firstName("John")
                .lastName("Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(ClientStatus.BLACKLISTED)
                .lastVisitDate(null)
                .notes(null)
                .build();
        Client client = new Client(null,"John","Marston", "380666666666", LocalDate.of(1873, Month.JUNE,22), ClientStatus.ACTIVE, null,null);
        Client clientBefore = new Client(1L,"John","Marston", "380666666666", LocalDate.of(1873, Month.JUNE,22), ClientStatus.BLACKLISTED, null,null);
        Client clientAfter = new Client(1L,"John","Marston", "380666666666", LocalDate.of(1873, Month.JUNE,22), ClientStatus.BLACKLISTED, null,null);
        ClientDtoResponse response = ClientDtoResponse.builder()
                .id(1L)
                .fullName("John Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(ClientStatus.BLACKLISTED)
                .lastVisitDate(null)
                .notes(null)
                .build();

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        when(clientMapper.dtoUpdateToEntity(request,client)).thenReturn(clientBefore);
        when(clientRepository.save(clientBefore)).thenReturn(clientAfter);
        when(clientMapper.toResponse(clientAfter)).thenReturn(response);

        ClientDtoResponse actualResponse = clientService.updateById(id,request);

        assertNotNull(actualResponse);
        assertEquals(response,actualResponse);

        verify(clientRepository).findById(anyLong());
        verify(clientMapper).dtoUpdateToEntity(any(ClientDtoUpdateRequest.class),any(Client.class));
        verify(clientRepository).save(any(Client.class));
        verify(clientMapper).toResponse(any(Client.class));
    }

    @Test
    public void updateById_ShouldThrowAlreadyExistsException_WhenPhoneFromClientRequestAlreadyExist(){
        Long id = 1L;
        ClientDtoUpdateRequest request = ClientDtoUpdateRequest.builder()
                .firstName("John")
                .lastName("Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(ClientStatus.BLACKLISTED)
                .lastVisitDate(null)
                .notes(null)
                .build();
        Client client = new Client(null,"John","Marston", "380999999999", LocalDate.of(1873, Month.JUNE,22), ClientStatus.ACTIVE, null,null);
        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        when(clientRepository.existsByPhone(request.phone())).thenReturn(true);

        Exception exception = assertThrows(AlreadyExistsException.class, () ->clientService.updateById(id,request));

        String expectedMessage = "Client from request with " + request.phone() +" already exists";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(clientRepository).findById(anyLong());
        verify(clientRepository).existsByPhone(anyString());
        verify(clientMapper,never()).dtoUpdateToEntity(any(ClientDtoUpdateRequest.class),any(Client.class));
        verify(clientRepository,never()).save(any(Client.class));
        verify(clientMapper,never()).toResponse(any(Client.class));
    }

    @Test
    public void updateById_ShouldThrowResourceNotFoundException_WhenNoClientById(){
        Long id = 1L;
        ClientDtoUpdateRequest request = ClientDtoUpdateRequest.builder()
                .firstName("John")
                .lastName("Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(ClientStatus.BLACKLISTED)
                .lastVisitDate(null)
                .notes(null)
                .build();

        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->clientService.updateById(id,request));

        String expectedMessage = "Client not found with id: " + id;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(clientRepository).findById(anyLong());
        verify(clientMapper,never()).dtoUpdateToEntity(any(ClientDtoUpdateRequest.class),any(Client.class));
        verify(clientRepository,never()).save(any(Client.class));
        verify(clientMapper,never()).toResponse(any(Client.class));
    }

    @Test
    public void deleteById_ShouldReturnResponse_WhenClientById(){
        Long id = 1L;
        Client client = new Client(null,"John","Marston", "380666666666", LocalDate.of(1873, Month.JUNE,22), ClientStatus.ACTIVE, null,null);
        ClientDtoResponse response = ClientDtoResponse.builder()
                .id(1L)
                .fullName("John Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(ClientStatus.ARCHIVED)
                .lastVisitDate(null)
                .notes(null)
                .build();
        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        when(clientMapper.toResponse(client)).thenReturn(response);

        ClientDtoResponse actualResponse = clientService.deleteById(id);

        assertNotNull(actualResponse);
        assertEquals(response,actualResponse);
        assertEquals(ClientStatus.ARCHIVED, actualResponse.status());

        verify(clientRepository).findById(anyLong());
        verify(clientMapper).toResponse(any(Client.class));
    }

    @Test
    public void deleteById_ShouldReturnResponse_WhenNoClientById(){
        Long id = 1L;
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class,() -> clientService.deleteById(id));
        String expectedMessage = "Client not found with id: " + id;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(clientRepository).findById(anyLong());
        verify(clientMapper,never()).toResponse(any(Client.class));
    }

    @Test
    public void getByPhone_ShouldReturnResponse_WhenClientByPhone(){
        String phone = "380666666666";
        Client client = new Client(1L,"John","Marston", "380666666666", LocalDate.of(1873, Month.JUNE,22), ClientStatus.ACTIVE, null,null);
        ClientDtoResponse response = ClientDtoResponse.builder()
                .id(1L)
                .fullName("John Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE,22))
                .status(ClientStatus.ACTIVE)
                .lastVisitDate(null)
                .notes(null)
                .build();
        when(clientRepository.findClientByPhone(phone)).thenReturn(Optional.of(client));
        when(clientMapper.toResponse(client)).thenReturn(response);

        ClientDtoResponse actualResponse = clientService.getByPhone(phone);

        assertEquals(response,actualResponse);
        assertEquals(phone,actualResponse.phone());

        verify(clientRepository).findClientByPhone(anyString());
        verify(clientMapper).toResponse(any(Client.class));
    }

    @Test
    public void getByPhone_ShouldThrowResourceNotFoundException_WhenNoClientByPhone(){
        String phone = "380666666666";
        when(clientRepository.findClientByPhone(phone)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class,() -> clientService.getByPhone(phone));
        String expectedMessage = "Client not found with phone: " + phone;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(clientRepository).findClientByPhone(anyString());
        verify(clientMapper,never()).toResponse(any(Client.class));
    }

    @Test
    public void findByFirstNameAndLastName_ShouldReturnPage_WhenClientExist() {
        Pageable pageable = PageRequest.of(0, 10);
        Client client = new Client(1L, "John", "Marston", "380666666666", LocalDate.of(1873, Month.JUNE, 22), ClientStatus.ACTIVE, null, null);
        ClientDtoResponse response = ClientDtoResponse.builder()
                .id(1L)
                .fullName("John Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE, 22))
                .status(ClientStatus.ACTIVE)
                .lastVisitDate(null)
                .notes(null)
                .build();
        Page<Client> clientPage = new PageImpl<>(List.of(client), pageable, 1);


        when(clientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("John", "Marston", pageable)).thenReturn(clientPage);
        when(clientMapper.toResponse(client)).thenReturn(response);

        Page<ClientDtoResponse> actualResponse = clientService.findByFirstNameAndLastName("John", "Marston", pageable);

        assertNotNull(actualResponse);
        assertEquals(1, actualResponse.getTotalElements());
        assertEquals(response, actualResponse.getContent().getLast());

        verify(clientRepository).findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(anyString(), anyString(), eq(pageable));
        verify(clientMapper).toResponse(any(Client.class));
    }

    @Test
    public void findByFirstNameAndLastName_ShouldReturnPage_WhenClientDontExist() {
        Pageable pageable = PageRequest.of(0, 10);

        when(clientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("John", "Marston", pageable)).thenReturn(Page.empty());

        Page<ClientDtoResponse> actualResponse = clientService.findByFirstNameAndLastName("John", "Marston", pageable);

        assertNotNull(actualResponse);
        assertEquals(0, actualResponse.getTotalElements());
        assertTrue(actualResponse.getContent().isEmpty());

        verify(clientRepository).findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(anyString(), anyString(), eq(pageable));
        verify(clientMapper,never()).toResponse(any(Client.class));
    }

    @Test
    public void findByStatusAndLastVisitDateBetween_ShouldReturnPage_WhenClientsExist(){
        Pageable pageable = PageRequest.of(0, 10);
        Client client = new Client(1L, "John", "Marston", "380666666666", LocalDate.of(1873, Month.JUNE, 22), ClientStatus.INACTIVE, LocalDate.of(2025,7,2), null);
        ClientDtoResponse response = ClientDtoResponse.builder()
                .id(1L)
                .fullName("John Marston")
                .phone("380666666666")
                .birthDate(LocalDate.of(1873, Month.JUNE, 22))
                .status(ClientStatus.INACTIVE)
                .lastVisitDate(LocalDate.of(2025,7,2))
                .notes(null)
                .build();
        Page<Client> clientPage = new PageImpl<>(List.of(client), pageable, 1);

        when(clientRepository.findClientsByStatusAndLastVisitDateBetween(ClientStatus.ACTIVE,LocalDate.of(2025,7,1),LocalDate.of(2025,7,5),pageable)).thenReturn(clientPage);
        when(clientMapper.toResponse(client)).thenReturn(response);

        Page<ClientDtoResponse> actualResponse = clientService.findByStatusAndLastVisitDateBetween(ClientStatus.ACTIVE,LocalDate.of(2025,7,1),LocalDate.of(2025,7,5),pageable);

        assertNotNull(actualResponse);
        assertEquals(1, actualResponse.getTotalElements());
        assertEquals(response, actualResponse.getContent().getLast());

        verify(clientRepository).findClientsByStatusAndLastVisitDateBetween(any(ClientStatus.class),any(LocalDate.class),any(LocalDate.class), any(Pageable.class));
        verify(clientMapper).toResponse(any(Client.class));
    }

    @Test
    public void findByStatusAndLastVisitDateBetween_ShouldReturnPage_WhenNoClientsExist(){
        Pageable pageable = PageRequest.of(0, 10);

        when(clientRepository.findClientsByStatusAndLastVisitDateBetween(ClientStatus.ACTIVE,LocalDate.of(2025,7,1),LocalDate.of(2025,7,5),pageable)).thenReturn(Page.empty());

        Page<ClientDtoResponse> actualResponse = clientService.findByStatusAndLastVisitDateBetween(ClientStatus.ACTIVE,LocalDate.of(2025,7,1),LocalDate.of(2025,7,5),pageable);

        assertNotNull(actualResponse);
        assertEquals(0, actualResponse.getTotalElements());
        assertTrue(actualResponse.getContent().isEmpty());

        verify(clientRepository).findClientsByStatusAndLastVisitDateBetween(any(ClientStatus.class),any(LocalDate.class),any(LocalDate.class), any(Pageable.class));
        verify(clientMapper,never()).toResponse(any(Client.class));
    }

    @Test
    public void findByStatusAndLastVisitDateBetween_ShouldThrowBadRequestException_WhenStartDateAfterEndDate(){
        Pageable pageable = PageRequest.of(0, 10);
        LocalDate lastVisitDateAfter = LocalDate.of(2025, 7, 7);
        LocalDate lastVisitDateBefore = LocalDate.of(2025, 7, 5);
        Exception exception = assertThrows(BadRequestException.class, () ->
            clientService.findByStatusAndLastVisitDateBetween(ClientStatus.ACTIVE, lastVisitDateAfter, lastVisitDateBefore,pageable));

        String expectedMessage = "Start date (" + lastVisitDateAfter + ") cannot be after end date (" + lastVisitDateBefore + ")";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(clientRepository,never()).findClientsByStatusAndLastVisitDateBetween(any(ClientStatus.class),any(LocalDate.class),any(LocalDate.class), any(Pageable.class));
        verify(clientMapper,never()).toResponse(any(Client.class));
    }

    @Test
    public void findByStatusAndLastVisitDateBetween_ShouldThrowBadRequestException_WhenDateFromFuture(){
        Pageable pageable = PageRequest.of(0, 10);
        LocalDate lastVisitDateAfter = LocalDate.of(2026, 7, 7);
        LocalDate lastVisitDateBefore = LocalDate.of(2026, 7, 9);
        Exception exception = assertThrows(BadRequestException.class, () ->
                clientService.findByStatusAndLastVisitDateBetween(ClientStatus.ACTIVE, lastVisitDateAfter, lastVisitDateBefore,pageable));

        String expectedMessage = "Search dates cannot be in the future. Today is " + LocalDate.now();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(clientRepository,never()).findClientsByStatusAndLastVisitDateBetween(any(ClientStatus.class),any(LocalDate.class),any(LocalDate.class), any(Pageable.class));
        verify(clientMapper,never()).toResponse(any(Client.class));
    }
}