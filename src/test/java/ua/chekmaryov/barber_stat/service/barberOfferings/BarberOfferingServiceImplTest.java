//package ua.chekmaryov.barber_stat.service.barberOfferings;
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
//import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoCreateRequest;
//import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoResponse;
//import ua.chekmaryov.barber_stat.dto.barberOffering.BarberOfferingDtoUpdateRequest;
//import ua.chekmaryov.barber_stat.entity.Barber;
//import ua.chekmaryov.barber_stat.entity.BarberOffering;
//import ua.chekmaryov.barber_stat.entity.Offer;
//import ua.chekmaryov.barber_stat.enums.BarberRole;
//import ua.chekmaryov.barber_stat.enums.BarberStatus;
//import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
//import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
//import ua.chekmaryov.barber_stat.mapper.BarberOfferingMapper;
//import ua.chekmaryov.barber_stat.repository.BarberOfferingRepository;
//import ua.chekmaryov.barber_stat.repository.BarberRepository;
//import ua.chekmaryov.barber_stat.repository.OfferRepository;
//import ua.chekmaryov.barber_stat.service.barberofferings.BarberOfferingServiceImpl;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.Month;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class BarberOfferingServiceImplTest {
//
//    @Mock
//    private BarberOfferingRepository barberOfferingRepository;
//
//    @Mock
//    private BarberRepository barberRepository;
//
//    @Mock
//    private OfferRepository offerRepository;
//
//    @Mock
//    private BarberOfferingMapper mapper;
//
//    @InjectMocks
//    private BarberOfferingServiceImpl service;
//
//    @BeforeEach
//    void setUp() {
//    }
//
//    @Test
//    public void create_ShouldReturnResponse_WhenIsValid(){
//        BarberOfferingDtoCreateRequest request = BarberOfferingDtoCreateRequest.builder()
//                .barberId(1L)
//                .offerId(1L)
//                .price(BigDecimal.valueOf(400))
//                .customTime(30)
//                .build();
//        Barber barber = new Barber(1L,"Arthur","Morgan","380666666666", LocalDate.of(1868, Month.JUNE,22), BarberStatus.ACTIVE, BarberRole.TOP,50,null);
//        Offer offer = new Offer(1L,"Haircut");
//        BarberOffering barberOfferingBefore = new BarberOffering(null,barber,offer, BigDecimal.valueOf(400),30);
//        BarberOffering barberOfferingAfter = new BarberOffering(1L,barber,offer, BigDecimal.valueOf(400),30);
//        BarberOfferingDtoResponse response = BarberOfferingDtoResponse.builder()
//                .id(barberOfferingAfter.getId())
//                .barberId(1L)
//                .barberFullName("Arthur Morgan")
//                .offerId(1L)
//                .offerName("Haircut")
//                .price(BigDecimal.valueOf(400))
//                .customTime(30)
//                .build();
//
//        when(barberRepository.findById(anyLong())).thenReturn(Optional.of(barber));
//        when(offerRepository.findById(anyLong())).thenReturn(Optional.of(offer));
//        when(barberOfferingRepository.existsByBarber_IdAndOffer_Id(anyLong(),anyLong())).thenReturn(false);
//        when(mapper.dtoToEntity(request,barber,offer)).thenReturn(barberOfferingBefore);
//        when(barberOfferingRepository.save(barberOfferingBefore)).thenReturn(barberOfferingAfter);
//        when(mapper.toResponse(barberOfferingAfter)).thenReturn(response);
//
//        BarberOfferingDtoResponse actualResponse = service.create(request);
//
//        assertNotNull(actualResponse);
//        assertEquals(response, actualResponse);
//
//        verify(barberRepository).findById(anyLong());
//        verify(offerRepository).findById(anyLong());
//        verify(barberOfferingRepository).existsByBarber_IdAndOffer_Id(anyLong(),anyLong());
//        verify(mapper).dtoToEntity(request,barber,offer);
//        verify(barberOfferingRepository).save(barberOfferingBefore);
//        verify(mapper).toResponse(barberOfferingAfter);
//    }
//
//    @Test
//    public void create_ShouldThrowResourceNotFoundException_WhenNoBarberById(){
//        BarberOfferingDtoCreateRequest request = BarberOfferingDtoCreateRequest.builder()
//                .barberId(1L)
//                .offerId(1L)
//                .price(BigDecimal.valueOf(400))
//                .customTime(30)
//                .build();
//
//        when(barberRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.create(request));
//        String expectedMessage = "Barber not found with id: " + request.barberId();
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//
//        verify(barberRepository).findById(anyLong());
//        verify(offerRepository,never()).findById(anyLong());
//        verify(barberOfferingRepository,never()).existsByBarber_IdAndOffer_Id(anyLong(),anyLong());
//        verify(mapper,never()).dtoToEntity(any(BarberOfferingDtoCreateRequest.class),any(Barber.class),any(Offer.class));
//        verify(barberOfferingRepository,never()).save(any(BarberOffering.class));
//        verify(mapper,never()).toResponse(any(BarberOffering.class));
//    }
//
//    @Test
//    public void create_ShouldThrowResourceNotFoundException_WhenNoOfferById(){
//        BarberOfferingDtoCreateRequest request = BarberOfferingDtoCreateRequest.builder()
//                .barberId(1L)
//                .offerId(1L)
//                .price(BigDecimal.valueOf(400))
//                .customTime(30)
//                .build();
//        Barber barber = new Barber(1L,"Arthur","Morgan","380666666666", LocalDate.of(1868, Month.JUNE,22), BarberStatus.ACTIVE, BarberRole.TOP,50,null);
//
//        when(barberRepository.findById(anyLong())).thenReturn(Optional.of(barber));
//        when(offerRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.create(request));
//        String expectedMessage = "Offer not found with id: " + request.offerId();
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//
//        verify(barberRepository).findById(anyLong());
//        verify(offerRepository).findById(anyLong());
//        verify(barberOfferingRepository,never()).existsByBarber_IdAndOffer_Id(anyLong(),anyLong());
//        verify(mapper,never()).dtoToEntity(any(BarberOfferingDtoCreateRequest.class),any(Barber.class),any(Offer.class));
//        verify(barberOfferingRepository,never()).save(any(BarberOffering.class));
//        verify(mapper,never()).toResponse(any(BarberOffering.class));
//    }
//
//    @Test
//    public void create_ShouldThrowResourceNotFoundException_WhenKeyAlreadyExists(){
//        BarberOfferingDtoCreateRequest request = BarberOfferingDtoCreateRequest.builder()
//                .barberId(1L)
//                .offerId(1L)
//                .price(BigDecimal.valueOf(400))
//                .customTime(30)
//                .build();
//        Barber barber = new Barber(1L,"Arthur","Morgan","380666666666", LocalDate.of(1868, Month.JUNE,22), BarberStatus.ACTIVE, BarberRole.TOP,50,null);
//        Offer offer = new Offer(1L,"Haircut");
//
//        when(barberRepository.findById(anyLong())).thenReturn(Optional.of(barber));
//        when(offerRepository.findById(anyLong())).thenReturn(Optional.of(offer));
//        when(barberOfferingRepository.existsByBarber_IdAndOffer_Id(barber.getId(),offer.getId())).thenReturn(true);
//
//        Exception exception = assertThrows(AlreadyExistsException.class, () -> service.create(request));
//        String expectedMessage = "Offer already exists with this BarberId " + barber.getId() + " and OfferId " + offer.getId();
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//
//        verify(barberRepository).findById(anyLong());
//        verify(offerRepository).findById(anyLong());
//        verify(barberOfferingRepository).existsByBarber_IdAndOffer_Id(anyLong(),anyLong());
//        verify(mapper,never()).dtoToEntity(any(BarberOfferingDtoCreateRequest.class),any(Barber.class),any(Offer.class));
//        verify(barberOfferingRepository,never()).save(any(BarberOffering.class));
//        verify(mapper,never()).toResponse(any(BarberOffering.class));
//    }
//
//    @Test
//    public void getAll_ShouldReturnPageOfResponses_WhenBarberOfferingsExist(){
//        Pageable pageable = PageRequest.of(0, 10);
//        Barber barber = new Barber(1L,"Arthur","Morgan","380666666666", LocalDate.of(1868, Month.JUNE,22), BarberStatus.ACTIVE, BarberRole.TOP,50,null);
//        Offer offer = new Offer(1L,"Haircut");
//        BarberOffering barberOffering = new BarberOffering(1L,barber,offer, BigDecimal.valueOf(400),30);
//        BarberOfferingDtoResponse response = BarberOfferingDtoResponse.builder()
//                .id(barberOffering.getId())
//                .barberId(1L)
//                .barberFullName("Arthur Morgan")
//                .offerId(1L)
//                .offerName("Haircut")
//                .price(BigDecimal.valueOf(400))
//                .customTime(30)
//                .build();
//        Page<BarberOffering> barberOfferingsPage = new PageImpl<>(List.of(barberOffering), pageable, 1);
//
//        when(barberOfferingRepository.findAll(pageable)).thenReturn(barberOfferingsPage);
//        when(mapper.toResponse(any(BarberOffering.class))).thenReturn(response);
//
//        Page<BarberOfferingDtoResponse> actualResponse = service.getAll(pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(1,actualResponse.getTotalElements());
//        assertEquals(response,actualResponse.getContent().getLast());
//
//        verify(barberOfferingRepository).findAll(any(Pageable.class));
//        verify(mapper).toResponse(any(BarberOffering.class));
//    }
//
//    @Test
//    public void getAll_ShouldReturnEmptyPage_WhenNoBarberOfferingsExist(){
//        Pageable pageable = PageRequest.of(0, 10);
//
//        when(barberOfferingRepository.findAll(pageable)).thenReturn(Page.empty());
//
//        Page<BarberOfferingDtoResponse> actualResponse = service.getAll(pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(0,actualResponse.getTotalElements());
//        assertEquals(Collections.emptyList(),actualResponse.getContent());
//
//        verify(barberOfferingRepository).findAll(any(Pageable.class));
//        verify(mapper,never()).toResponse(any(BarberOffering.class));
//    }
//
//    @Test
//    public void getById_ShouldReturnResponse_WhenBarberOfferingExist(){
//        Barber barber = new Barber(1L,"Arthur","Morgan","380666666666", LocalDate.of(1868, Month.JUNE,22), BarberStatus.ACTIVE, BarberRole.TOP,50,null);
//        Offer offer = new Offer(1L,"Haircut");
//        BarberOffering barberOffering = new BarberOffering(1L,barber,offer, BigDecimal.valueOf(400),30);
//        BarberOfferingDtoResponse response = BarberOfferingDtoResponse.builder()
//                .id(barberOffering.getId())
//                .barberId(1L)
//                .barberFullName("Arthur Morgan")
//                .offerId(1L)
//                .offerName("Haircut")
//                .price(BigDecimal.valueOf(400))
//                .customTime(30)
//                .build();
//
//        when(barberOfferingRepository.findById(anyLong())).thenReturn(Optional.of(barberOffering));
//        when(mapper.toResponse(any(BarberOffering.class))).thenReturn(response);
//
//        BarberOfferingDtoResponse actualResponse = service.getById(1L);
//
//        assertNotNull(actualResponse);
//        assertEquals(response,actualResponse);
//
//        verify(barberOfferingRepository).findById(anyLong());
//        verify(mapper).toResponse(any(BarberOffering.class));
//    }
//
//    @Test
//    public void getById_ShouldThrowResourceNotFoundException_WhenNoBarberOfferingExist(){
//        when(barberOfferingRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.getById(1L));
//        String expectedMessage = "No barber offering with ID:" + 1L;
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//
//        verify(barberOfferingRepository).findById(anyLong());
//        verify(mapper,never()).toResponse(any(BarberOffering.class));
//    }
//
//    @Test
//    public void updateById_ShouldReturnResponse_WhenBarberOfferingExistById(){
//        Long id = 1L;
//        BarberOfferingDtoUpdateRequest request = BarberOfferingDtoUpdateRequest.builder()
//                .price(BigDecimal.valueOf(500))
//                .build();
//        Barber barber = new Barber(id,"Arthur","Morgan","380666666666", LocalDate.of(1868, Month.JUNE,22), BarberStatus.ACTIVE, BarberRole.TOP,50,null);
//        Offer offer = new Offer(id,"Haircut");
//        BarberOffering barberOfferingBefore = new BarberOffering(null,barber,offer, BigDecimal.valueOf(400),30);
//        BarberOffering barberOfferingUpdated = new BarberOffering(id,barber,offer, BigDecimal.valueOf(500),30);
//        BarberOfferingDtoResponse response = BarberOfferingDtoResponse.builder()
//                .id(barberOfferingUpdated.getId())
//                .barberId(id)
//                .barberFullName("Arthur Morgan")
//                .offerId(id)
//                .offerName("Haircut")
//                .price(BigDecimal.valueOf(500))
//                .customTime(30)
//                .build();
//
//        when(barberOfferingRepository.findById(anyLong())).thenReturn(Optional.of(barberOfferingBefore));
//        when(mapper.dtoUpdateToEntity(any(BarberOfferingDtoUpdateRequest.class),any(BarberOffering.class))).thenReturn(barberOfferingUpdated);
//        when(barberOfferingRepository.save(any(BarberOffering.class))).thenReturn(barberOfferingUpdated);
//        when(mapper.toResponse(any(BarberOffering.class))).thenReturn(response);
//
//        BarberOfferingDtoResponse actualResponse = service.updateById(id,request);
//
//        assertNotNull(actualResponse);
//        assertEquals(response,actualResponse);
//        assertEquals(BigDecimal.valueOf(500),actualResponse.price());
//
//
//        verify(barberOfferingRepository).findById(anyLong());
//        verify(mapper).dtoUpdateToEntity(any(BarberOfferingDtoUpdateRequest.class),any(BarberOffering.class));
//        verify(barberOfferingRepository).save(any(BarberOffering.class));
//        verify(mapper).toResponse(any(BarberOffering.class));
//    }
//
//    @Test
//    public void updateById_ShouldThrow_WhenNoBarberOfferingExistById(){
//        Long id = 1L;
//        BarberOfferingDtoUpdateRequest request = BarberOfferingDtoUpdateRequest.builder()
//                .price(BigDecimal.valueOf(500))
//                .build();
//
//        when(barberOfferingRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.updateById(id,request));
//        String expectedMessage = "No barber offering with ID:" + id;
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//
//
//        verify(barberOfferingRepository).findById(anyLong());
//        verify(mapper,never()).dtoUpdateToEntity(any(BarberOfferingDtoUpdateRequest.class),any(BarberOffering.class));
//        verify(barberOfferingRepository,never()).save(any(BarberOffering.class));
//        verify(mapper,never()).toResponse(any(BarberOffering.class));
//    }
//
//    @Test
//    public void deleteById_ShouldReturnResponse_WhenBarberOfferingExistById(){
//        Long id = 1L;
//
//        when(barberOfferingRepository.existsById(id)).thenReturn(true);
//
//        boolean response = service.deleteById(id);
//
//        assertTrue(response);
//
//        verify(barberOfferingRepository).existsById(id);
//        verify(barberOfferingRepository).deleteById(id);
//    }
//
//    @Test
//    public void deleteById_ShouldThrowResourceNotFoundException_WhenNoBarberOfferingExistById(){
//        Long id = 1L;
//
//        when(barberOfferingRepository.existsById(id)).thenReturn(false);
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.deleteById(id));
//        String expectedMessage = "No barber offering with ID:" + id;
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//
//        verify(barberOfferingRepository).existsById(id);
//        verify(barberOfferingRepository,never()).deleteById(id);
//    }
//
//    @Test
//    public void findByBarberIdAndOfferId_ShouldReturnResponse_WhenKeyExist(){
//        Long id = 1L;
//        Barber barber = new Barber(id,"Arthur","Morgan","380666666666", LocalDate.of(1868, Month.JUNE,22), BarberStatus.ACTIVE, BarberRole.TOP,50,null);
//        Offer offer = new Offer(id,"Haircut");
//        BarberOffering barberOffering = new BarberOffering(null,barber,offer, BigDecimal.valueOf(400),30);
//        BarberOfferingDtoResponse response = BarberOfferingDtoResponse.builder()
//                .id(id)
//                .barberId(id)
//                .barberFullName("Arthur Morgan")
//                .offerId(id)
//                .offerName("Haircut")
//                .price(BigDecimal.valueOf(500))
//                .customTime(30)
//                .build();
//
//
//        when(barberOfferingRepository.findByBarberIdAndOfferId(barber.getId(),offer.getId())).thenReturn(Optional.of(barberOffering));
//        when(mapper.toResponse(barberOffering)).thenReturn(response);
//
//        BarberOfferingDtoResponse actualResponse = service.findByBarberIdAndOfferId(barber.getId(),offer.getId());
//
//        assertNotNull(actualResponse);
//        assertEquals(response,actualResponse);
//
//        verify(barberOfferingRepository).findByBarberIdAndOfferId(anyLong(),anyLong());
//        verify(mapper).toResponse(any(BarberOffering.class));
//    }
//
//    @Test
//    public void findByBarberIdAndOfferId_ShouldThrowResourceNotFoundException_WhenKeyDontExist(){
//        Long id = 1L;
//        Barber barber = new Barber(id,"Arthur","Morgan","380666666666", LocalDate.of(1868, Month.JUNE,22), BarberStatus.ACTIVE, BarberRole.TOP,50,null);
//        Offer offer = new Offer(id,"Haircut");
//
//        when(barberOfferingRepository.findByBarberIdAndOfferId(barber.getId(),offer.getId())).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.findByBarberIdAndOfferId(barber.getId(),offer.getId()));
//        String expectedMessage = "No barber offering with barberID " + barber.getId() + "and offerID " + offer.getId();
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//
//        verify(barberOfferingRepository).findByBarberIdAndOfferId(anyLong(),anyLong());
//        verify(mapper,never()).toResponse(any(BarberOffering.class));
//    }
//
//    @Test
//    public void findByBarber_Id_ShouldReturnPageOfResponses_WhenBarberOfferingExists(){
//        Pageable pageable = PageRequest.of(0, 10);
//        Long id = 1L;
//        Barber barber = new Barber(id,"Arthur","Morgan","380666666666", LocalDate.of(1868, Month.JUNE,22), BarberStatus.ACTIVE, BarberRole.TOP,50,null);
//        Offer offer = new Offer(id,"Haircut");
//        BarberOffering barberOffering = new BarberOffering(null,barber,offer, BigDecimal.valueOf(400),30);
//        BarberOfferingDtoResponse response = BarberOfferingDtoResponse.builder()
//                .id(id)
//                .barberId(id)
//                .barberFullName("Arthur Morgan")
//                .offerId(id)
//                .offerName("Haircut")
//                .price(BigDecimal.valueOf(500))
//                .customTime(30)
//                .build();
//        Page<BarberOffering> barberOfferingsPage = new PageImpl<>(List.of(barberOffering), pageable, 1);
//
//
//        when(barberOfferingRepository.findBarberOfferingsByBarber_Id(barber.getId(),pageable)).thenReturn(barberOfferingsPage);
//        when(mapper.toResponse(any(BarberOffering.class))).thenReturn(response);
//
//        Page<BarberOfferingDtoResponse> actualResponse = service.findByBarber_Id(barber.getId(),pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(1,actualResponse.getTotalElements());
//        assertEquals(response,actualResponse.getContent().getLast());
//
//        verify(barberOfferingRepository).findBarberOfferingsByBarber_Id(anyLong(),any(Pageable.class));
//        verify(mapper).toResponse(any(BarberOffering.class));
//    }
//
//    @Test
//    public void findByBarber_Id_ShouldReturnEmptyPage_WhenNoBarberOfferingExists(){
//        Pageable pageable = PageRequest.of(0, 10);
//        Long id = 1L;
//        Barber barber = new Barber(id,"Arthur","Morgan","380666666666", LocalDate.of(1868, Month.JUNE,22), BarberStatus.ACTIVE, BarberRole.TOP,50,null);
//
//        when(barberOfferingRepository.findBarberOfferingsByBarber_Id(barber.getId(),pageable)).thenReturn(Page.empty());
//
//        Page<BarberOfferingDtoResponse> actualResponse = service.findByBarber_Id(barber.getId(),pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(0,actualResponse.getTotalElements());
//        assertEquals(Collections.emptyList(),actualResponse.getContent());
//
//        verify(barberOfferingRepository).findBarberOfferingsByBarber_Id(anyLong(),any(Pageable.class));
//        verify(mapper,never()).toResponse(any(BarberOffering.class));
//    }
//
//    @Test
//    public void findByOffer_Id_ShouldReturnPageOfResponses_WhenBarberOfferingExists(){
//        Pageable pageable = PageRequest.of(0, 10);
//        Long id = 1L;
//        Barber barber = new Barber(id,"Arthur","Morgan","380666666666", LocalDate.of(1868, Month.JUNE,22), BarberStatus.ACTIVE, BarberRole.TOP,50,null);
//        Offer offer = new Offer(id,"Haircut");
//        BarberOffering barberOffering = new BarberOffering(null,barber,offer, BigDecimal.valueOf(400),30);
//        BarberOfferingDtoResponse response = BarberOfferingDtoResponse.builder()
//                .id(id)
//                .barberId(id)
//                .barberFullName("Arthur Morgan")
//                .offerId(id)
//                .offerName("Haircut")
//                .price(BigDecimal.valueOf(500))
//                .customTime(30)
//                .build();
//        Page<BarberOffering> barberOfferingsPage = new PageImpl<>(List.of(barberOffering), pageable, 1);
//
//
//        when(barberOfferingRepository.findBarberOfferingsByOffer_Id(offer.getId(),pageable)).thenReturn(barberOfferingsPage);
//        when(mapper.toResponse(any(BarberOffering.class))).thenReturn(response);
//
//        Page<BarberOfferingDtoResponse> actualResponse = service.findByOffer_Id(offer.getId(),pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(1,actualResponse.getTotalElements());
//        assertEquals(response,actualResponse.getContent().getLast());
//
//        verify(barberOfferingRepository).findBarberOfferingsByOffer_Id(anyLong(),any(Pageable.class));
//        verify(mapper).toResponse(any(BarberOffering.class));
//    }
//
//    @Test
//    public void findByOffer_Id_ShouldReturnEmptyPage_WhenNoBarberOfferingExists(){
//        Pageable pageable = PageRequest.of(0, 10);
//        Long id = 1L;
//        Offer offer = new Offer(id,"Haircut");
//
//        when(barberOfferingRepository.findBarberOfferingsByOffer_Id(offer.getId(),pageable)).thenReturn(Page.empty());
//
//        Page<BarberOfferingDtoResponse> actualResponse = service.findByOffer_Id(offer.getId(),pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(0,actualResponse.getTotalElements());
//        assertEquals(Collections.emptyList(),actualResponse.getContent());
//
//        verify(barberOfferingRepository).findBarberOfferingsByOffer_Id(anyLong(),any(Pageable.class));
//        verify(mapper,never()).toResponse(any(BarberOffering.class));
//    }
//
//    }