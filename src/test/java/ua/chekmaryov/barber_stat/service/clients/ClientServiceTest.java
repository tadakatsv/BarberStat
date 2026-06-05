//package ua.chekmaryov.barber_stat.service.clients;
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
//import ua.chekmaryov.barber_stat.dto.clients.ClientDtoCreateRequest;
//import ua.chekmaryov.barber_stat.dto.clients.ClientDtoResponse;
//import ua.chekmaryov.barber_stat.dto.clients.ClientDtoUpdateRequest;
//import ua.chekmaryov.barber_stat.entity.Client;
//import ua.chekmaryov.barber_stat.enums.ClientStatus;
//import ua.chekmaryov.barber_stat.exception.BadRequestException;
//import ua.chekmaryov.barber_stat.mapper.ClientMapper;
//import ua.chekmaryov.barber_stat.repository.ClientRepository;
//import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
//import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
//
//import java.time.LocalDate;
//import java.time.Month;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class ClientServiceTest {
//
//    @Mock
//    private ClientRepository clientRepository;
//
//    @Mock
//    private ClientMapper clientMapper;
//
//    @InjectMocks
//    private ClientService clientService;
//
//    private ClientDtoCreateRequest request;
//    private Client clientBefore;
//    private Client clientAfter;
//    private ClientDtoResponse response;
//    private Pageable pageable;
//    private Page<Client> allClients;
//    private Long id;
//
//    @BeforeEach
//    void setUp() {
//        id = 1L;
//        request = ClientDtoCreateRequest.builder()
//                .firstName("John")
//                .lastName("Marston")
//                .phone("380666666666")
//                .birthDate(LocalDate.of(1873, Month.JUNE,22))
//                .status(null)
//                .lastVisitDate(null)
//                .notes(null)
//                .build();
//        clientBefore = new Client(null,"John","Marston", "380666666666", LocalDate.of(1873, Month.JUNE,22), ClientStatus.ACTIVE, null,null);
//        clientAfter = new Client(1L,"John","Marston", "380666666666", LocalDate.of(1873, Month.JUNE,22), ClientStatus.ACTIVE, null,null);
//        response = ClientDtoResponse.builder()
//                .id(1L)
//                .fullName("John Marston")
//                .phone("380666666666")
//                .birthDate(LocalDate.of(1873, Month.JUNE,22))
//                .status(ClientStatus.ACTIVE)
//                .lastVisitDate(null)
//                .notes(null)
//                .build();
//        pageable = PageRequest.of(0, 10);
//        allClients = new PageImpl<>(List.of(clientAfter),pageable,1);
//    }
//
//    @Test
//    public void create_ShouldReturnResponse_WhenNoClientByPhone(){
//        when(clientRepository.existsByPhone(request.phone())).thenReturn(false);
//        when(clientMapper.dtoToEntity(request)).thenReturn(clientBefore);
//        when(clientRepository.save(clientBefore)).thenReturn(clientAfter);
//        when(clientMapper.toResponse(clientAfter)).thenReturn(response);
//
//        ClientDtoResponse actualResponse = clientService.create(request);
//
//        assertNotNull(actualResponse);
//        assertEquals(response, actualResponse);
//        assertEquals(1L,actualResponse.id());
//
//        verify(clientRepository).existsByPhone(anyString());
//        verify(clientMapper).toResponse(any(Client.class));
//        verify(clientRepository).save(any(Client.class));
//        verify(clientMapper).dtoToEntity(any(ClientDtoCreateRequest.class));
//    }
//
//    @Test
//    public void create_ShouldThrowAlreadyExistsException_WhenNoClientByPhone(){
//        when(clientRepository.existsByPhone(request.phone())).thenReturn(true);
//
//        Exception exception = assertThrows(AlreadyExistsException.class, () -> clientService.create(request));
//
//        String expectedMessage = "Client with " + request.phone() +" already exists";
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//
//        verify(clientRepository).existsByPhone(anyString());
//        verify(clientMapper,never()).toResponse(any(Client.class));
//        verify(clientRepository,never()).save(any(Client.class));
//        verify(clientMapper,never()).dtoToEntity(any(ClientDtoCreateRequest.class));
//    }
//
//    @Test
//    public void getAll_ShouldReturnPageOfClients_WhenBarbersExists(){
//        when(clientRepository.findAll(any(Pageable.class))).thenReturn(allClients);
//        when(clientMapper.toResponse(clientAfter)).thenReturn(response);
//
//        Page<ClientDtoResponse> result = clientService.getAll(pageable);
//
//        assertNotNull(result);
//        assertEquals(1, result.getTotalElements());
//        assertEquals(response, result.getContent().getLast());
//
//        verify(clientRepository).findAll(any(Pageable.class));
//        verify(clientMapper).toResponse(any(Client.class));
//    }
//
//    @Test
//    public void getAll_shouldReturnEmptyPage_whenNoClients(){
//        when(clientRepository.findAll(pageable)).thenReturn(Page.empty());
//
//        Page<ClientDtoResponse> result = clientService.getAll(pageable);
//
//        assertNotNull(result);
//        assertEquals(0, result.getTotalElements());
//        assertEquals(Collections.emptyList(), result.getContent());
//
//        verify(clientRepository).findAll(any(Pageable.class));
//        verify(clientMapper,never()).toResponse(any(Client.class));
//    }
//
//    @Test
//    public void getById_ShouldReturnClientDtoResponse_WhenBarberById(){
//        Long id =1L;
//
//        when(clientRepository.findById(id)).thenReturn(Optional.of(clientAfter));
//        when(clientMapper.toResponse(clientAfter)).thenReturn(response);
//
//        ClientDtoResponse actualResponse = clientService.getById(id);
//
//        assertNotNull(actualResponse);
//        assertEquals(response,actualResponse);
//        assertEquals(id, actualResponse.id());
//
//        verify(clientRepository).findById(anyLong());
//        verify(clientMapper).toResponse(any(Client.class));
//    }
//
//    @Test
//    public void getById_ShouldThrowResourceNotFoundException_WhenNoClientById(){
//        when(clientRepository.findById(id)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> clientService.getById(id));
//
//        String expectedMessage = "Client not found with id: " + id;
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//
//        verify(clientRepository).findById(anyLong());
//        verify(clientMapper,never()).toResponse(any(Client.class));
//    }
//
//    @Test
//    public void updateById_ShouldReturnResponse_WhenClientById() {
//        ClientDtoUpdateRequest updateRequest = ClientDtoUpdateRequest.builder()
//                .firstName("John")
//                .lastName("Marston")
//                .phone("380666666666")
//                .birthDate(LocalDate.of(1873, Month.JUNE, 22))
//                .status(ClientStatus.BLACKLISTED)
//                .lastVisitDate(null)
//                .notes(null)
//                .build();
//
//        Client clientToSave = new Client(id, "John", "Marston", "380666666666",
//                LocalDate.of(1873, Month.JUNE, 22), ClientStatus.BLACKLISTED, null, null);
//
//        ClientDtoResponse updatedResponse = ClientDtoResponse.builder()
//                .id(id)
//                .fullName("John Marston")
//                .phone("380666666666")
//                .birthDate(LocalDate.of(1873, Month.JUNE, 22))
//                .status(ClientStatus.BLACKLISTED)
//                .lastVisitDate(null)
//                .notes(null)
//                .build();
//
//        when(clientRepository.findById(id)).thenReturn(Optional.of(clientBefore));
//
//        when(clientMapper.dtoUpdateToEntity(updateRequest, clientBefore)).thenReturn(clientToSave);
//
//        when(clientRepository.save(clientToSave)).thenReturn(clientToSave);
//
//        when(clientMapper.toResponse(clientToSave)).thenReturn(updatedResponse);
//
//        ClientDtoResponse actualResponse = clientService.updateById(id, updateRequest);
//
//        assertNotNull(actualResponse);
//        assertEquals(updatedResponse, actualResponse);
//
//        verify(clientRepository).findById(id);
//        verify(clientMapper).dtoUpdateToEntity(updateRequest, clientBefore);
//        verify(clientRepository).save(clientToSave);
//        verify(clientMapper).toResponse(clientToSave);
//    }
//
//    @Test
//    public void updateById_ShouldThrowAlreadyExistsException_WhenPhoneFromClientRequestAlreadyExist(){
//        ClientDtoUpdateRequest request = ClientDtoUpdateRequest.builder()
//                .firstName("John")
//                .lastName("Marston")
//                .phone("380666666666")
//                .birthDate(LocalDate.of(1873, Month.JUNE,22))
//                .status(ClientStatus.BLACKLISTED)
//                .lastVisitDate(null)
//                .notes(null)
//                .build();
//        Client client = new Client(null,"John","Marston", "380999999999", LocalDate.of(1873, Month.JUNE,22), ClientStatus.ACTIVE, null,null);
//        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
//        when(clientRepository.existsByPhone(request.phone())).thenReturn(true);
//
//        Exception exception = assertThrows(AlreadyExistsException.class, () ->clientService.updateById(id,request));
//
//        String expectedMessage = "Client from request with " + request.phone() +" already exists";
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//
//        verify(clientRepository).findById(anyLong());
//        verify(clientRepository).existsByPhone(anyString());
//        verify(clientMapper,never()).dtoUpdateToEntity(any(ClientDtoUpdateRequest.class),any(Client.class));
//        verify(clientRepository,never()).save(any(Client.class));
//        verify(clientMapper,never()).toResponse(any(Client.class));
//    }
//
//    @Test
//    public void updateById_ShouldThrowResourceNotFoundException_WhenNoClientById(){
//        ClientDtoUpdateRequest request = ClientDtoUpdateRequest.builder()
//                .firstName("John")
//                .lastName("Marston")
//                .phone("380666666666")
//                .birthDate(LocalDate.of(1873, Month.JUNE,22))
//                .status(ClientStatus.BLACKLISTED)
//                .lastVisitDate(null)
//                .notes(null)
//                .build();
//
//        when(clientRepository.findById(id)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () ->clientService.updateById(id,request));
//
//        String expectedMessage = "Client not found with id: " + id;
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//
//        verify(clientRepository).findById(anyLong());
//        verify(clientMapper,never()).dtoUpdateToEntity(any(ClientDtoUpdateRequest.class),any(Client.class));
//        verify(clientRepository,never()).save(any(Client.class));
//        verify(clientMapper,never()).toResponse(any(Client.class));
//    }
//
//    @Test
//    public void deleteById_ShouldReturnResponse_WhenClientById(){
//        ClientDtoResponse response = ClientDtoResponse.builder()
//                .id(1L)
//                .fullName("John Marston")
//                .phone("380666666666")
//                .birthDate(LocalDate.of(1873, Month.JUNE,22))
//                .status(ClientStatus.ARCHIVED)
//                .lastVisitDate(null)
//                .notes(null)
//                .build();
//        when(clientRepository.findById(id)).thenReturn(Optional.of(clientBefore));
//        when(clientMapper.toResponse(clientBefore)).thenReturn(response);
//
//        ClientDtoResponse actualResponse = clientService.deleteById(id);
//
//        assertNotNull(actualResponse);
//        assertEquals(response,actualResponse);
//        assertEquals(ClientStatus.ARCHIVED, actualResponse.status());
//
//        verify(clientRepository).findById(anyLong());
//        verify(clientMapper).toResponse(any(Client.class));
//    }
//
//    @Test
//    public void deleteById_ShouldReturnResponse_WhenNoClientById(){
//        when(clientRepository.findById(id)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class,() -> clientService.deleteById(id));
//        String expectedMessage = "Client not found with id: " + id;
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//
//        verify(clientRepository).findById(anyLong());
//        verify(clientMapper,never()).toResponse(any(Client.class));
//    }
//
//    @Test
//    public void getByPhone_ShouldReturnResponse_WhenClientByPhone(){
//        String phone = "380666666666";
//        when(clientRepository.findClientByPhone(phone)).thenReturn(Optional.of(clientAfter));
//        when(clientMapper.toResponse(clientAfter)).thenReturn(response);
//
//        ClientDtoResponse actualResponse = clientService.getByPhone(phone);
//
//        assertEquals(response,actualResponse);
//        assertEquals(phone,actualResponse.phone());
//
//        verify(clientRepository).findClientByPhone(anyString());
//        verify(clientMapper).toResponse(any(Client.class));
//    }
//
//    @Test
//    public void getByPhone_ShouldThrowResourceNotFoundException_WhenNoClientByPhone(){
//        String phone = "380666666666";
//        when(clientRepository.findClientByPhone(phone)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class,() -> clientService.getByPhone(phone));
//        String expectedMessage = "Client not found with phone: " + phone;
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//
//        verify(clientRepository).findClientByPhone(anyString());
//        verify(clientMapper,never()).toResponse(any(Client.class));
//    }
//
//    @Test
//    public void findByFirstNameAndLastName_ShouldReturnPage_WhenClientExist() {
//        Client client = new Client(1L, "John", "Marston", "380666666666", LocalDate.of(1873, Month.JUNE, 22), ClientStatus.ACTIVE, null, null);
//        ClientDtoResponse response = ClientDtoResponse.builder()
//                .id(1L)
//                .fullName("John Marston")
//                .phone("380666666666")
//                .birthDate(LocalDate.of(1873, Month.JUNE, 22))
//                .status(ClientStatus.ACTIVE)
//                .lastVisitDate(null)
//                .notes(null)
//                .build();
//        Page<Client> clientPage = new PageImpl<>(List.of(client), pageable, 1);
//
//
//        when(clientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("John", "Marston", pageable)).thenReturn(clientPage);
//        when(clientMapper.toResponse(client)).thenReturn(response);
//
//        Page<ClientDtoResponse> actualResponse = clientService.findByFirstNameAndLastName("John", "Marston", pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(1, actualResponse.getTotalElements());
//        assertEquals(response, actualResponse.getContent().getLast());
//
//        verify(clientRepository).findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(anyString(), anyString(), eq(pageable));
//        verify(clientMapper).toResponse(any(Client.class));
//    }
//
//    @Test
//    public void findByFirstNameAndLastName_ShouldReturnPage_WhenClientDontExist() {
//
//        when(clientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("John", "Marston", pageable)).thenReturn(Page.empty());
//
//        Page<ClientDtoResponse> actualResponse = clientService.findByFirstNameAndLastName("John", "Marston", pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(0, actualResponse.getTotalElements());
//        assertTrue(actualResponse.getContent().isEmpty());
//
//        verify(clientRepository).findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(anyString(), anyString(), eq(pageable));
//        verify(clientMapper,never()).toResponse(any(Client.class));
//    }
//
//    @Test
//    public void findByStatusAndLastVisitDateBetween_ShouldReturnPage_WhenClientsExist(){
//        Client client = new Client(1L, "John", "Marston", "380666666666", LocalDate.of(1873, Month.JUNE, 22), ClientStatus.ACTIVE, LocalDate.of(2025,7,2), null);
//        ClientDtoResponse response = ClientDtoResponse.builder()
//                .id(1L)
//                .fullName("John Marston")
//                .phone("380666666666")
//                .birthDate(LocalDate.of(1873, Month.JUNE, 22))
//                .status(ClientStatus.INACTIVE)
//                .lastVisitDate(LocalDate.of(2025,7,2))
//                .notes(null)
//                .build();
//        Page<Client> clientPage = new PageImpl<>(List.of(client), pageable, 1);
//
//        when(clientRepository.findClientsByStatusAndLastVisitDateBetween(ClientStatus.ACTIVE,LocalDate.of(2025,7,1),LocalDate.of(2025,7,5),pageable)).thenReturn(clientPage);
//        when(clientMapper.toResponse(client)).thenReturn(response);
//
//        Page<ClientDtoResponse> actualResponse = clientService.findByStatusAndLastVisitDateBetween(ClientStatus.ACTIVE,LocalDate.of(2025,7,1),LocalDate.of(2025,7,5),pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(1, actualResponse.getTotalElements());
//        assertEquals(response, actualResponse.getContent().getLast());
//
//        verify(clientRepository).findClientsByStatusAndLastVisitDateBetween(any(ClientStatus.class),any(LocalDate.class),any(LocalDate.class), any(Pageable.class));
//        verify(clientMapper).toResponse(any(Client.class));
//    }
//
//    @Test
//    public void findByStatusAndLastVisitDateBetween_ShouldReturnPage_WhenNoClientsExist(){
//        when(clientRepository.findClientsByStatusAndLastVisitDateBetween(ClientStatus.ACTIVE,LocalDate.of(2025,7,1),LocalDate.of(2025,7,5),pageable)).thenReturn(Page.empty());
//
//        Page<ClientDtoResponse> actualResponse = clientService.findByStatusAndLastVisitDateBetween(ClientStatus.ACTIVE,LocalDate.of(2025,7,1),LocalDate.of(2025,7,5),pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(0, actualResponse.getTotalElements());
//        assertTrue(actualResponse.getContent().isEmpty());
//
//        verify(clientRepository).findClientsByStatusAndLastVisitDateBetween(any(ClientStatus.class),any(LocalDate.class),any(LocalDate.class), any(Pageable.class));
//        verify(clientMapper,never()).toResponse(any(Client.class));
//    }
//
//    @Test
//    public void findByStatusAndLastVisitDateBetween_ShouldThrowBadRequestException_WhenStartDateAfterEndDate(){
//        LocalDate lastVisitDateAfter = LocalDate.of(2025, 7, 7);
//        LocalDate lastVisitDateBefore = LocalDate.of(2025, 7, 5);
//        Exception exception = assertThrows(BadRequestException.class, () ->
//            clientService.findByStatusAndLastVisitDateBetween(ClientStatus.ACTIVE, lastVisitDateAfter, lastVisitDateBefore,pageable));
//
//        String expectedMessage = "Start date (" + lastVisitDateAfter + ") cannot be after end date (" + lastVisitDateBefore + ")";
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//
//        verify(clientRepository,never()).findClientsByStatusAndLastVisitDateBetween(any(ClientStatus.class),any(LocalDate.class),any(LocalDate.class), any(Pageable.class));
//        verify(clientMapper,never()).toResponse(any(Client.class));
//    }
//
//    @Test
//    public void findByStatusAndLastVisitDateBetween_ShouldThrowBadRequestException_WhenDateFromFuture(){
//        LocalDate lastVisitDateAfter = LocalDate.now().plusDays(1);
//        LocalDate lastVisitDateBefore = LocalDate.now().plusDays(3);
//        Exception exception = assertThrows(BadRequestException.class, () ->
//                clientService.findByStatusAndLastVisitDateBetween(ClientStatus.ACTIVE, lastVisitDateAfter, lastVisitDateBefore,pageable));
//
//        String expectedMessage = "Search dates cannot be in the future. Today is " + LocalDate.now();
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//
//        verify(clientRepository,never()).findClientsByStatusAndLastVisitDateBetween(any(ClientStatus.class),any(LocalDate.class),any(LocalDate.class), any(Pageable.class));
//        verify(clientMapper,never()).toResponse(any(Client.class));
//    }
//}