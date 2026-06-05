//package ua.chekmaryov.barber_stat.service.visits;
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
//import ua.chekmaryov.barber_stat.dto.visits.VisitDtoCreateRequest;
//import ua.chekmaryov.barber_stat.dto.visits.VisitDtoResponse;
//import ua.chekmaryov.barber_stat.dto.visits.VisitDtoUpdateRequest;
//import ua.chekmaryov.barber_stat.entity.*;
//import ua.chekmaryov.barber_stat.enums.BarberRole;
//import ua.chekmaryov.barber_stat.enums.BarberStatus;
//import ua.chekmaryov.barber_stat.enums.ClientStatus;
//import ua.chekmaryov.barber_stat.enums.VisitStatus;
//import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
//import ua.chekmaryov.barber_stat.exception.BadRequestException;
//import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
//import ua.chekmaryov.barber_stat.mapper.VisitMapper;
//import ua.chekmaryov.barber_stat.repository.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.Month;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class VisitServiceImplTest {
//
//    @Mock
//    private BarberRepository barberRepository;
//
//    @Mock
//    private ClientRepository clientRepository;
//
//    @Mock
//    private OfferRepository offerRepository;
//
//    @Mock
//    private VisitRepository visitRepository;
//
//    @Mock
//    private BarberOfferingRepository barberOfferingRepository;
//
//    @Mock
//    private VisitMapper mapper;
//
//    @InjectMocks
//    private VisitServiceImpl service;
//
//    private Long id;
//    private LocalDateTime visitTime;
//    private VisitDtoCreateRequest request;
//    private Barber barber;
//    private Client client;
//    private Offer offer;
//    private BarberOffering barberOffering;
//    private Visit visitBeforeRepository;
//    private Visit visitAfterRepository;
//    private VisitDtoResponse response;
//    private Pageable pageable;
//    private LocalDateTime start;
//    private LocalDateTime end;
//    private Page<Visit> visitsPage;
//
//
//    @BeforeEach
//    void setUp() {
//        id = 1L;
//        pageable = PageRequest.of(0, 10);
//        visitTime = LocalDateTime.of(2030, 7, 2, 22, 0);
//        request = VisitDtoCreateRequest.builder()
//                .clientId(id)
//                .barberId(id)
//                .offerId(id)
//                .visitTime(visitTime)
//                .build();
//        barber = new Barber(id,"Arthur","Morgan","380666666666", LocalDate.of(1868, Month.JUNE,22), BarberStatus.ACTIVE, BarberRole.TOP,50,null);
//        client = new Client(id,"John","Marston","380000000000", LocalDate.of(1873, Month.JUNE,22), ClientStatus.ACTIVE, null,null);
//        offer = new Offer(id,"Haircut");
//        barberOffering = new BarberOffering(id,barber,offer, BigDecimal.valueOf(400),30);
//        visitBeforeRepository = new Visit(null,client,barber,offer,visitTime, barberOffering.getPrice(),barber.getSalaryPercent(), VisitStatus.PLANNED,barberOffering.getCustomTime(),null);
//        visitAfterRepository = new Visit(id,client,barber,offer,visitTime, barberOffering.getPrice(),barber.getSalaryPercent(), VisitStatus.PLANNED,barberOffering.getCustomTime(),null);
//        response = VisitDtoResponse.builder()
//                .id(visitAfterRepository.getId())
//                .clientId(visitAfterRepository.getClient().getId())
//                .clientFullName(visitAfterRepository.getClient().getFirstName() + " " + visitAfterRepository.getClient().getLastName())
//                .barberId(visitAfterRepository.getBarber().getId())
//                .barberFullName(visitAfterRepository.getBarber().getFirstName() + " " + visitAfterRepository.getBarber().getLastName())
//                .offerId(visitAfterRepository.getOffer().getId())
//                .offerName(visitAfterRepository.getOffer().getName())
//                .visitTime(visitAfterRepository.getVisitTime())
//                .actualPrice(visitAfterRepository.getActualPrice())
//                .status(visitAfterRepository.getStatus())
//                .durationMinutes(visitAfterRepository.getDurationMinutes())
//                .build();
//        start = LocalDateTime.of(2030, 7, 1, 22, 0);
//        end = LocalDateTime.of(2030, 7, 3, 22, 0);
//        visitsPage = new PageImpl<>(List.of(visitAfterRepository), pageable, 1);
//
//    }
//
//    @Test
//    public void create_ShouldReturnResponse_WhenAllValid(){
//        LocalDateTime visitTimeEnd = request.visitTime().plusMinutes(barberOffering.getCustomTime());
//
//        when(barberRepository.findById(id)).thenReturn(Optional.of(barber));
//        when(offerRepository.findById(id)).thenReturn(Optional.of(offer));
//        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
//        when(barberOfferingRepository.findByBarberIdAndOfferId(barber.getId(),offer.getId())).thenReturn(Optional.of(barberOffering));
//        when(visitRepository.hasOverlappingVisit(barber.getId(),request.visitTime(),visitTimeEnd)).thenReturn(false);
//        when(mapper.dtoToEntity(request,client,barber,offer,barberOffering)).thenReturn(visitBeforeRepository);
//        when(visitRepository.save(visitBeforeRepository)).thenReturn(visitAfterRepository);
//        when(mapper.toResponse(visitAfterRepository)).thenReturn(response);
//
//        VisitDtoResponse actualResponse = service.create(request);
//
//        assertNotNull(actualResponse);
//        assertEquals(response,actualResponse);
//
//        verify(barberRepository).findById(anyLong());
//        verify(offerRepository).findById(anyLong());
//        verify(clientRepository).findById(anyLong());
//        verify(barberOfferingRepository).findByBarberIdAndOfferId(anyLong(),anyLong());
//        verify(visitRepository).hasOverlappingVisit(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class));
//        verify(mapper).dtoToEntity(any(VisitDtoCreateRequest.class),any(Client.class),any(Barber.class),any(Offer.class),any(BarberOffering.class));
//        verify(visitRepository).save(any(Visit.class));
//        verify(mapper).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void create_ShouldThrowResourceNotFoundException_WhenNoBarberById(){
//        when(barberRepository.findById(id)).thenReturn(Optional.empty());
//
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.create(request));
//        String expectedMessage = "Barber not found with id: " + request.barberId();
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(barberRepository).findById(anyLong());
//        verify(offerRepository,never()).findById(anyLong());
//        verify(clientRepository,never()).findById(anyLong());
//        verify(barberOfferingRepository,never()).findByBarberIdAndOfferId(anyLong(),anyLong());
//        verify(visitRepository,never()).hasOverlappingVisit(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class));
//        verify(mapper,never()).dtoToEntity(any(VisitDtoCreateRequest.class),any(Client.class),any(Barber.class),any(Offer.class),any(BarberOffering.class));
//        verify(visitRepository,never()).save(any(Visit.class));
//        verify(mapper,never()).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void create_ShouldThrowResourceNotFoundException_WhenNoOfferById(){
//        when(barberRepository.findById(id)).thenReturn(Optional.of(barber));
//        when(offerRepository.findById(id)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.create(request));
//        String expectedMessage = "Offer not found with id: " + request.offerId();
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(barberRepository).findById(anyLong());
//        verify(offerRepository).findById(anyLong());
//        verify(clientRepository,never()).findById(anyLong());
//        verify(barberOfferingRepository,never()).findByBarberIdAndOfferId(anyLong(),anyLong());
//        verify(visitRepository,never()).hasOverlappingVisit(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class));
//        verify(mapper,never()).dtoToEntity(any(VisitDtoCreateRequest.class),any(Client.class),any(Barber.class),any(Offer.class),any(BarberOffering.class));
//        verify(visitRepository,never()).save(any(Visit.class));
//        verify(mapper,never()).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void create_ShouldThrowResourceNotFoundException_WhenNoClientById(){
//        when(barberRepository.findById(id)).thenReturn(Optional.of(barber));
//        when(offerRepository.findById(id)).thenReturn(Optional.of(offer));
//        when(clientRepository.findById(id)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.create(request));
//        String expectedMessage = "Client not found with id: " + request.clientId();
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(barberRepository).findById(anyLong());
//        verify(offerRepository).findById(anyLong());
//        verify(clientRepository).findById(anyLong());
//        verify(barberOfferingRepository,never()).findByBarberIdAndOfferId(anyLong(),anyLong());
//        verify(visitRepository,never()).hasOverlappingVisit(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class));
//        verify(mapper,never()).dtoToEntity(any(VisitDtoCreateRequest.class),any(Client.class),any(Barber.class),any(Offer.class),any(BarberOffering.class));
//        verify(visitRepository,never()).save(any(Visit.class));
//        verify(mapper,never()).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void create_ShouldThrowResourceNotFoundException_WhenNoBarberOfferingByBarberIdAndOfferId(){
//        when(barberRepository.findById(id)).thenReturn(Optional.of(barber));
//        when(offerRepository.findById(id)).thenReturn(Optional.of(offer));
//        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
//        when(barberOfferingRepository.findByBarberIdAndOfferId(barber.getId(),offer.getId())).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.create(request));
//        String expectedMessage = "Barber " + barber.getFirstName() + " " + barber.getLastName() + " don't have this offer " + offer.getName();
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(barberRepository).findById(anyLong());
//        verify(offerRepository).findById(anyLong());
//        verify(clientRepository).findById(anyLong());
//        verify(barberOfferingRepository).findByBarberIdAndOfferId(anyLong(),anyLong());
//        verify(visitRepository, never()).hasOverlappingVisit(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class));
//        verify(mapper, never()).dtoToEntity(any(VisitDtoCreateRequest.class),any(Client.class),any(Barber.class),any(Offer.class),any(BarberOffering.class));
//        verify(visitRepository, never()).save(any(Visit.class));
//        verify(mapper, never()).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void create_ShouldThrowBadRequestException_WhenRequestVisitInPast(){
//        request = VisitDtoCreateRequest.builder()
//                .clientId(id)
//                .barberId(id)
//                .offerId(id)
//                .visitTime(LocalDateTime.of(2025,2,2,22,0))
//                .build();
//
//        when(barberRepository.findById(id)).thenReturn(Optional.of(barber));
//        when(offerRepository.findById(id)).thenReturn(Optional.of(offer));
//        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
//        when(barberOfferingRepository.findByBarberIdAndOfferId(barber.getId(),offer.getId())).thenReturn(Optional.of(barberOffering));
//
//        Exception exception = assertThrows(BadRequestException.class, () -> service.create(request));
//        String expectedMessage = "You can't make a visit on past " + request.visitTime();
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(barberRepository).findById(anyLong());
//        verify(offerRepository).findById(anyLong());
//        verify(clientRepository).findById(anyLong());
//        verify(barberOfferingRepository).findByBarberIdAndOfferId(anyLong(),anyLong());
//        verify(visitRepository,never()).hasOverlappingVisit(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class));
//        verify(mapper,never()).dtoToEntity(any(VisitDtoCreateRequest.class),any(Client.class),any(Barber.class),any(Offer.class),any(BarberOffering.class));
//        verify(visitRepository,never()).save(any(Visit.class));
//        verify(mapper,never()).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void create_ShouldThrowAlreadyExistsException_WhenVisitTimeAlreadyBooked(){
//        LocalDateTime visitTimeEnd = request.visitTime().plusMinutes(barberOffering.getCustomTime());
//
//        when(barberRepository.findById(id)).thenReturn(Optional.of(barber));
//        when(offerRepository.findById(id)).thenReturn(Optional.of(offer));
//        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
//        when(barberOfferingRepository.findByBarberIdAndOfferId(barber.getId(),offer.getId())).thenReturn(Optional.of(barberOffering));
//        when(visitRepository.hasOverlappingVisit(barber.getId(),request.visitTime(),visitTimeEnd)).thenReturn(true);
//
//        Exception exception = assertThrows(AlreadyExistsException.class, () -> service.create(request));
//        String expectedMessage = "Barber already booked on this time " + request.visitTime();
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(barberRepository).findById(anyLong());
//        verify(offerRepository).findById(anyLong());
//        verify(clientRepository).findById(anyLong());
//        verify(barberOfferingRepository).findByBarberIdAndOfferId(anyLong(),anyLong());
//        verify(visitRepository).hasOverlappingVisit(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class));
//        verify(mapper,never()).dtoToEntity(any(VisitDtoCreateRequest.class),any(Client.class),any(Barber.class),any(Offer.class),any(BarberOffering.class));
//        verify(visitRepository,never()).save(any(Visit.class));
//        verify(mapper,never()).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void getAll_ShouldReturnPageOfResponses_WhenVisitsExist(){
//        when(visitRepository.findVisitsByStatusAndVisitTimeBetween(VisitStatus.PLANNED,LocalDateTime.of(2029, 7, 2, 22, 0),LocalDateTime.of(2031, 7, 2, 22, 0),pageable)).thenReturn(visitsPage);
//        when(mapper.toResponse(visitAfterRepository)).thenReturn(response);
//
//        Page<VisitDtoResponse> actualResponse = service.getAllByStatusAndBetweenTwoDates(VisitStatus.PLANNED,LocalDateTime.of(2029, 7, 2, 22, 0),LocalDateTime.of(2031, 7, 2, 22, 0),pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(1,actualResponse.getTotalElements());
//        assertEquals(response,actualResponse.getContent().getLast());
//
//        verify(visitRepository).findVisitsByStatusAndVisitTimeBetween(any(VisitStatus.class),any(LocalDateTime.class),any(LocalDateTime.class),any(Pageable.class));
//        verify(mapper).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void getAll_ShouldReturnEmptyPage_WhenNoVisitsExist(){
//        when(visitRepository.findVisitsByStatusAndVisitTimeBetween(VisitStatus.PLANNED,LocalDateTime.of(2029, 7, 2, 22, 0),LocalDateTime.of(2031, 7, 2, 22, 0),pageable)).thenReturn(Page.empty());
//
//        Page<VisitDtoResponse> actualResponse = service.getAllByStatusAndBetweenTwoDates(VisitStatus.PLANNED,LocalDateTime.of(2029, 7, 2, 22, 0),LocalDateTime.of(2031, 7, 2, 22, 0),pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(0,actualResponse.getTotalElements());
//        assertEquals(Collections.emptyList(),actualResponse.getContent());
//
//        verify(visitRepository).findVisitsByStatusAndVisitTimeBetween(any(VisitStatus.class),any(LocalDateTime.class),any(LocalDateTime.class),any(Pageable.class));
//        verify(mapper,never()).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void getById_ShouldReturnResponse_WhenVisitExistById(){
//        when(visitRepository.findById(id)).thenReturn(Optional.of(visitAfterRepository));
//        when(mapper.toResponse(visitAfterRepository)).thenReturn(response);
//
//        VisitDtoResponse actualResponse = service.getById(id);
//
//        assertNotNull(actualResponse);
//        assertEquals(response,actualResponse);
//
//        verify(visitRepository).findById(anyLong());
//        verify(mapper).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void getById_ShouldThrowResourceNotFoundException_WhenNoVisitExistById(){
//        when(visitRepository.findById(id)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.getById(id));
//        String expectedMessage = "No visit by that id " + id;
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(visitRepository).findById(anyLong());
//        verify(mapper,never()).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void updateById_ShouldReturnResponse_WhenVisitTimeIsAvailable(){
//        VisitDtoUpdateRequest updateRequest = VisitDtoUpdateRequest.builder()
//                .visitTime(LocalDateTime.of(2030, 7, 2, 22, 20))
//                .build();
//        Visit visitUpdated = new Visit(id,client,barber,offer,updateRequest.visitTime(), barberOffering.getPrice(),barber.getSalaryPercent(), VisitStatus.PLANNED,barberOffering.getCustomTime(),null);
//        VisitDtoResponse updatedResponse = VisitDtoResponse.builder()
//                .id(visitUpdated.getId())
//                .clientId(visitUpdated.getClient().getId())
//                .clientFullName(visitUpdated.getClient().getFirstName() + " " + visitUpdated.getClient().getLastName())
//                .barberId(visitUpdated.getBarber().getId())
//                .barberFullName(visitUpdated.getBarber().getFirstName() + " " + visitUpdated.getBarber().getLastName())
//                .offerId(visitUpdated.getOffer().getId())
//                .offerName(visitUpdated.getOffer().getName())
//                .visitTime(visitUpdated.getVisitTime())
//                .actualPrice(visitUpdated.getActualPrice())
//                .actualBarberPercentage(visitUpdated.getActualBarberPercentage())
//                .status(visitUpdated.getStatus())
//                .durationMinutes(visitUpdated.getDurationMinutes())
//                .notes(visitUpdated.getNotes())
//                .build();
//
//        when(visitRepository.findById(id)).thenReturn(Optional.of(visitAfterRepository));
//        when(visitRepository.hasOverlappingVisitButForHimself(anyLong(),anyLong(),any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(false);
//        when(mapper.dtoUpdateToEntity(updateRequest, visitAfterRepository)).thenReturn(visitUpdated);
//        when(visitRepository.save(any(Visit.class))).thenReturn(visitUpdated);
//        when(mapper.toResponse(visitUpdated)).thenReturn(updatedResponse);
//
//        VisitDtoResponse actualResponse = service.updateById(id,updateRequest);
//        assertNotNull(actualResponse);
//        assertEquals(updatedResponse,actualResponse);
//        assertEquals(updateRequest.visitTime(),actualResponse.visitTime());
//
//        verify(visitRepository).findById(anyLong());
//        verify(visitRepository).hasOverlappingVisitButForHimself(anyLong(),anyLong(),any(LocalDateTime.class),any(LocalDateTime.class));
//        verify(mapper).dtoUpdateToEntity(any(VisitDtoUpdateRequest.class),any(Visit.class));
//        verify(visitRepository).save(any(Visit.class));
//        verify(mapper).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void updateById_ShouldReturnResponse_WhenStatusInRequest(){
//        VisitDtoUpdateRequest updateRequest = VisitDtoUpdateRequest.builder()
//                .status(VisitStatus.COMPLETED)
//                .build();
//        Visit visitUpdated = new Visit(id,client,barber,offer,visitTime, barberOffering.getPrice(),barber.getSalaryPercent(), updateRequest.status(),barberOffering.getCustomTime(),null);
//        VisitDtoResponse updatedResponse = VisitDtoResponse.builder()
//                .id(visitUpdated.getId())
//                .clientId(visitUpdated.getClient().getId())
//                .clientFullName(visitUpdated.getClient().getFirstName() + " " + visitUpdated.getClient().getLastName())
//                .barberId(visitUpdated.getBarber().getId())
//                .barberFullName(visitUpdated.getBarber().getFirstName() + " " + visitUpdated.getBarber().getLastName())
//                .offerId(visitUpdated.getOffer().getId())
//                .offerName(visitUpdated.getOffer().getName())
//                .visitTime(visitUpdated.getVisitTime())
//                .actualPrice(visitUpdated.getActualPrice())
//                .actualBarberPercentage(visitUpdated.getActualBarberPercentage())
//                .status(visitUpdated.getStatus())
//                .durationMinutes(visitUpdated.getDurationMinutes())
//                .notes(visitUpdated.getNotes())
//                .build();
//
//        when(visitRepository.findById(id)).thenReturn(Optional.of(visitAfterRepository));
//        when(mapper.dtoUpdateToEntity(updateRequest, visitAfterRepository)).thenReturn(visitUpdated);
//        when(visitRepository.save(any(Visit.class))).thenReturn(visitUpdated);
//        when(mapper.toResponse(visitUpdated)).thenReturn(updatedResponse);
//
//        VisitDtoResponse actualResponse = service.updateById(id,updateRequest);
//        assertNotNull(actualResponse);
//        assertEquals(updatedResponse,actualResponse);
//        assertEquals(updateRequest.status(),actualResponse.status());
//        assertEquals(client.getLastVisitDate(), updatedResponse.visitTime().toLocalDate());
//
//        verify(visitRepository).findById(anyLong());
//        verify(visitRepository,never()).hasOverlappingVisitButForHimself(anyLong(),anyLong(),any(LocalDateTime.class),any(LocalDateTime.class));
//        verify(mapper).dtoUpdateToEntity(any(VisitDtoUpdateRequest.class),any(Visit.class));
//        verify(visitRepository).save(any(Visit.class));
//        verify(mapper).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void updateById_ShouldReturnResponse_WhenNoVisitById(){
//        VisitDtoUpdateRequest updateRequest = VisitDtoUpdateRequest.builder()
//                .status(VisitStatus.COMPLETED)
//                .build();
//
//        when(visitRepository.findById(id)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.updateById(id,updateRequest));
//        String expectedMessage = "No visit by that id " + id;
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//
//        verify(visitRepository).findById(anyLong());
//        verify(visitRepository,never()).hasOverlappingVisitButForHimself(anyLong(),anyLong(),any(LocalDateTime.class),any(LocalDateTime.class));
//        verify(mapper,never()).dtoUpdateToEntity(any(VisitDtoUpdateRequest.class),any(Visit.class));
//        verify(visitRepository,never()).save(any(Visit.class));
//        verify(mapper,never()).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void updateById_ShouldReturnResponse_WhenRequestTimeOverlapOthers(){
//        VisitDtoUpdateRequest updateRequest = VisitDtoUpdateRequest.builder()
//                .visitTime(LocalDateTime.of(2030, 7, 2, 22, 20))
//                .build();
//
//        when(visitRepository.findById(id)).thenReturn(Optional.of(visitAfterRepository));
//        when(visitRepository.hasOverlappingVisitButForHimself(anyLong(),anyLong(),any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(true);
//
//
//        Exception exception = assertThrows(AlreadyExistsException.class, () -> service.updateById(id,updateRequest));
//        String expectedMessage = "Barber already booked on this time " + updateRequest.visitTime();
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(visitRepository).findById(anyLong());
//        verify(visitRepository).hasOverlappingVisitButForHimself(anyLong(),anyLong(),any(LocalDateTime.class),any(LocalDateTime.class));
//        verify(mapper,never()).dtoUpdateToEntity(any(VisitDtoUpdateRequest.class),any(Visit.class));
//        verify(visitRepository,never()).save(any(Visit.class));
//        verify(mapper,never()).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void cancelById_ShouldReturnResponse_WhenVisitById(){
//        VisitDtoResponse updatedResponse = VisitDtoResponse.builder()
//                .id(visitAfterRepository.getId())
//                .clientId(visitAfterRepository.getClient().getId())
//                .clientFullName(visitAfterRepository.getClient().getFirstName() + " " + visitAfterRepository.getClient().getLastName())
//                .barberId(visitAfterRepository.getBarber().getId())
//                .barberFullName(visitAfterRepository.getBarber().getFirstName() + " " + visitAfterRepository.getBarber().getLastName())
//                .offerId(visitAfterRepository.getOffer().getId())
//                .offerName(visitAfterRepository.getOffer().getName())
//                .visitTime(visitAfterRepository.getVisitTime())
//                .actualPrice(visitAfterRepository.getActualPrice())
//                .actualBarberPercentage(visitAfterRepository.getActualBarberPercentage())
//                .status(VisitStatus.CANCELLED)
//                .durationMinutes(visitAfterRepository.getDurationMinutes())
//                .notes(visitAfterRepository.getNotes())
//                .build();
//
//        when(visitRepository.findById(id)).thenReturn(Optional.of(visitAfterRepository));
//        when(mapper.toResponse(visitAfterRepository)).thenReturn(updatedResponse);
//
//        VisitDtoResponse actualResponse = service.cancelVisitById(id);
//
//        assertNotNull(actualResponse);
//        assertEquals(updatedResponse,actualResponse);
//        assertEquals(VisitStatus.CANCELLED,actualResponse.status());
//
//        verify(visitRepository).findById(anyLong());
//        verify(mapper).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void cancelById_ShouldThrowResourceNotFoundException_WhenNoVisitById(){
//
//        when(visitRepository.findById(id)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.cancelVisitById(id));
//
//        String expectedMessage = "No visit by that id " + id;
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(visitRepository).findById(anyLong());
//        verify(mapper,never()).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void findVisitsByClient_IdAndStatus_ShouldReturnPageOfResponses_WhenVisitsExistById(){
//
//        when(visitRepository.findVisitsByClient_IdAndStatus(id,VisitStatus.COMPLETED,pageable)).thenReturn(visitsPage);
//        when(mapper.toResponse(visitAfterRepository)).thenReturn(response);
//
//        Page<VisitDtoResponse> actualResponse = service.findVisitsByClient_IdAndStatus(id,VisitStatus.COMPLETED,pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(1,actualResponse.getTotalElements());
//        assertEquals(response,actualResponse.getContent().getLast());
//
//        verify(visitRepository).findVisitsByClient_IdAndStatus(anyLong(),any(VisitStatus.class),any(Pageable.class));
//        verify(mapper).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void findVisitsByClient_IdAndStatus_ShouldReturnEmptyPage_WhenNoVisitsExistById(){
//
//        when(visitRepository.findVisitsByClient_IdAndStatus(id,VisitStatus.COMPLETED,pageable)).thenReturn(Page.empty());
//
//        Page<VisitDtoResponse> actualResponse = service.findVisitsByClient_IdAndStatus(id,VisitStatus.COMPLETED,pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(0,actualResponse.getTotalElements());
//        assertEquals(Collections.emptyList(),actualResponse.getContent());
//
//        verify(visitRepository).findVisitsByClient_IdAndStatus(anyLong(),any(VisitStatus.class),any(Pageable.class));
//        verify(mapper,never()).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void findByClientIdAndVisitTimeBetween_ShouldReturnPageOfResponses_WhenVisitsById(){
//
//        when(visitRepository.findByClientIdAndVisitTimeBetween(client.getId(),start,end,pageable)).thenReturn(visitsPage);
//        when(mapper.toResponse(visitAfterRepository)).thenReturn(response);
//
//        Page<VisitDtoResponse> actualResponse = service.findByClientIdAndVisitTimeBetween(client.getId(),start,end,pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(1,actualResponse.getTotalElements());
//        assertEquals(response,actualResponse.getContent().getLast());
//
//        verify(visitRepository).findByClientIdAndVisitTimeBetween(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class),any(Pageable.class));
//        verify(mapper).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void findByClientIdAndVisitTimeBetween_ShouldReturnEmptyPage_WhenNoVisitsById(){
//
//        when(visitRepository.findByClientIdAndVisitTimeBetween(client.getId(),start,end,pageable)).thenReturn(Page.empty());
//
//        Page<VisitDtoResponse> actualResponse = service.findByClientIdAndVisitTimeBetween(client.getId(),start,end,pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(0,actualResponse.getTotalElements());
//        assertEquals(Collections.emptyList(),actualResponse.getContent());
//
//        verify(visitRepository).findByClientIdAndVisitTimeBetween(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class),any(Pageable.class));
//        verify(mapper,never()).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void findByClientIdAndVisitTimeBetween_ShouldThrowBadRequestException_WhenNoVisitsById(){
//
//        Exception exception = assertThrows(BadRequestException.class,() -> service.findByClientIdAndVisitTimeBetween(client.getId(),end,start,pageable));
//        String expectedMessage = "Start date (" + end + ") cannot be after end date (" + start + ")";
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(visitRepository,never()).findByClientIdAndVisitTimeBetween(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class),any(Pageable.class));
//        verify(mapper,never()).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void findByVisitTimeBetween_ShouldReturnPageOfResponses_WhenVisitsById(){
//
//        when(visitRepository.findByVisitTimeBetween(start,end,pageable)).thenReturn(visitsPage);
//        when(mapper.toResponse(visitAfterRepository)).thenReturn(response);
//
//        Page<VisitDtoResponse> actualResponse = service.findByVisitTimeBetween(start,end,pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(1,actualResponse.getTotalElements());
//        assertEquals(response,actualResponse.getContent().getLast());
//
//        verify(visitRepository).findByVisitTimeBetween(any(LocalDateTime.class),any(LocalDateTime.class),any(Pageable.class));
//        verify(mapper).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void findByVisitTimeBetween_ShouldReturnPageOfResponses_WhenNoVisitsById(){
//
//        when(visitRepository.findByVisitTimeBetween(start,end,pageable)).thenReturn(Page.empty());
//
//        Page<VisitDtoResponse> actualResponse = service.findByVisitTimeBetween(start,end,pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(0,actualResponse.getTotalElements());
//        assertEquals(Collections.emptyList(),actualResponse.getContent());
//
//        verify(visitRepository).findByVisitTimeBetween(any(LocalDateTime.class),any(LocalDateTime.class),any(Pageable.class));
//        verify(mapper,never()).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void findByVisitTimeBetween_ShouldThrowBadRequestException_WhenNoVisitsById(){
//
//        Exception exception = assertThrows(BadRequestException.class,() -> service.findByVisitTimeBetween(end,start,pageable));
//        String expectedMessage = "Start date (" + end + ") cannot be after end date (" + start + ")";
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(visitRepository,never()).findByVisitTimeBetween(any(LocalDateTime.class),any(LocalDateTime.class),any(Pageable.class));
//        verify(mapper,never()).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void findByBarberIdAndVisitTimeBetween_ShouldReturnPageOfResponses_WhenVisitsById(){
//
//        when(visitRepository.findByBarberIdAndVisitTimeBetween(id,start,end,pageable)).thenReturn(visitsPage);
//        when(mapper.toResponse(visitAfterRepository)).thenReturn(response);
//
//        Page<VisitDtoResponse> actualResponse = service.findByBarberIdAndVisitTimeBetween(id,start,end,pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(1,actualResponse.getTotalElements());
//        assertEquals(response,actualResponse.getContent().getLast());
//
//        verify(visitRepository).findByBarberIdAndVisitTimeBetween(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class),any(Pageable.class));
//        verify(mapper).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void findByBarberIdAndVisitTimeBetween_ShouldReturnPageOfResponses_WhenNoVisitsById(){
//
//        when(visitRepository.findByBarberIdAndVisitTimeBetween(id,start,end,pageable)).thenReturn(Page.empty());
//
//        Page<VisitDtoResponse> actualResponse = service.findByBarberIdAndVisitTimeBetween(id,start,end,pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(0,actualResponse.getTotalElements());
//        assertEquals(Collections.emptyList(),actualResponse.getContent());
//
//        verify(visitRepository).findByBarberIdAndVisitTimeBetween(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class),any(Pageable.class));
//        verify(mapper,never()).toResponse(any(Visit.class));
//    }
//
//    @Test
//    public void findByBarberIdAndVisitTimeBetween_ShouldThrowBadRequestException_WhenNoVisitsById(){
//
//        Exception exception = assertThrows(BadRequestException.class,() -> service.findByBarberIdAndVisitTimeBetween(id,end,start,pageable));
//        String expectedMessage = "Start date (" + end + ") cannot be after end date (" + start + ")";
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(visitRepository,never()).findByBarberIdAndVisitTimeBetween(anyLong(),any(LocalDateTime.class),any(LocalDateTime.class),any(Pageable.class));
//        verify(mapper,never()).toResponse(any(Visit.class));
//    }
//
//}