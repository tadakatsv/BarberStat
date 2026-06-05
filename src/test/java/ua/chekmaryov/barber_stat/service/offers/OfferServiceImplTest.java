//package ua.chekmaryov.barber_stat.service.offers;
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
//import ua.chekmaryov.barber_stat.dto.offers.OfferDtoRequest;
//import ua.chekmaryov.barber_stat.dto.offers.OfferDtoResponse;
//import ua.chekmaryov.barber_stat.entity.Offer;
//import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
//import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
//import ua.chekmaryov.barber_stat.mapper.OfferMapper;
//import ua.chekmaryov.barber_stat.repository.OfferRepository;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class OfferServiceImplTest {
//
//    @Mock
//    private OfferMapper offerMapper;
//
//    @Mock
//    private OfferRepository offerRepository;
//
//    @InjectMocks
//    private OfferService offerService;
//
//    private Long id;
//    private String name;
//    private OfferDtoRequest request;
//    private Offer offerBefore;
//    private Offer offerAfter;
//    private OfferDtoResponse response;
//    private String newName;
//    private Pageable pageable;
//    private Page<Offer> offerPage;
//
//
//    @BeforeEach
//    void setUp() {
//        id=1L;
//        name = "Haircut";
//        request = new OfferDtoRequest(name);
//        offerBefore = new Offer(null, name);
//        offerAfter = new Offer(1L, name);
//        response = new OfferDtoResponse(1L, name);
//        newName = "Premium haircut";
//        pageable = PageRequest.of(0, 10);
//        offerPage = new PageImpl<>(List.of(offerAfter), pageable, 1);
//
//    }
//
//    @Test
//    public void create_ShouldReturnResponse_WhenNoOfferByName(){
//        when(offerRepository.existsOfferByName(name)).thenReturn(false);
//        when(offerMapper.dtoToEntity(request)).thenReturn(offerBefore);
//        when(offerRepository.save(offerBefore)).thenReturn(offerAfter);
//        when(offerMapper.toResponse(offerAfter)).thenReturn(response);
//
//        OfferDtoResponse actualResponse = offerService.create(request);
//
//        assertNotNull(actualResponse);
//        assertEquals(response,actualResponse);
//
//        verify(offerRepository).existsOfferByName(anyString());
//        verify(offerRepository).save(any(Offer.class));
//        verify(offerMapper).dtoToEntity(any(OfferDtoRequest.class));
//        verify(offerMapper).toResponse(any(Offer.class));
//    }
//
//    @Test
//    public void create_ShouldThrowAlreadyExistsException_WhenOfferByName(){
//        when(offerRepository.existsOfferByName(name)).thenReturn(true);
//
//        Exception exception = assertThrows(AlreadyExistsException.class, () -> offerService.create(request));
//        String expectedMessage = "Offer by name" + request.name() + "already exists";
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(offerRepository).existsOfferByName(anyString());
//        verify(offerRepository,never()).save(any(Offer.class));
//        verify(offerMapper,never()).dtoToEntity(any(OfferDtoRequest.class));
//        verify(offerMapper,never()).toResponse(any(Offer.class));
//    }
//
//    @Test
//    public void getAll_ShouldReturnPageOfResponses_WhenOffersExist(){
//
//        when(offerRepository.findAll(any(Pageable.class))).thenReturn(offerPage);
//        when(offerMapper.toResponse(offerAfter)).thenReturn(response);
//
//        Page<OfferDtoResponse> actualResponse = offerService.getAll(pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(1, actualResponse.getTotalElements());
//        assertEquals(response, actualResponse.getContent().getLast());
//
//        verify(offerRepository).findAll(any(Pageable.class));
//        verify(offerMapper).toResponse(any(Offer.class));
//    }
//
//    @Test
//    public void getAll_ShouldReturnEmptyPage_WhenNoOffersExist(){
//        Pageable pageable = PageRequest.of(0, 10);
//
//        when(offerRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());
//
//        Page<OfferDtoResponse> actualResponse = offerService.getAll(pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(0, actualResponse.getTotalElements());
//        assertEquals(Collections.emptyList(), actualResponse.getContent());
//
//        verify(offerRepository).findAll(any(Pageable.class));
//        verify(offerMapper,never()).toResponse(any(Offer.class));
//    }
//
//    @Test
//    public void getById_ShouldReturnEmptyPage_WhenOffersById(){
//        when(offerRepository.findById(id)).thenReturn(Optional.of(offerAfter));
//        when(offerMapper.toResponse(offerAfter)).thenReturn(response);
//
//        OfferDtoResponse actualResponse = offerService.getById(id);
//
//        assertNotNull(actualResponse);
//        assertEquals(response,actualResponse);
//
//        verify(offerRepository).findById(anyLong());
//        verify(offerMapper).toResponse(any(Offer.class));
//    }
//
//    @Test
//    public void getById_ShouldReturnEmptyPage_WhenNoOffersById(){
//        when(offerRepository.findById(id)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> offerService.getById(id));
//        String expectedMessage = "Offer not found with id: " + id;
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(offerRepository).findById(anyLong());
//        verify(offerMapper,never()).toResponse(any(Offer.class));
//    }
//
//    @Test
//    public void updateById_ShouldReturnEmptyPage_WhenOfferByIdANDNoOfferByRequestName(){
//        OfferDtoRequest request = new OfferDtoRequest(newName);
//        Offer offerOld = new Offer(id, name);
//        Offer offerNew = new Offer(id,newName);
//
//        OfferDtoResponse response = new OfferDtoResponse(id, newName);
//
//        when(offerRepository.findById(id)).thenReturn(Optional.of(offerOld));
//        when(offerRepository.existsOfferByName(request.name().trim())).thenReturn(false);
//        when(offerMapper.dtoUpdateToEntity(request,offerOld)).thenReturn(offerNew);
//        when(offerRepository.save(offerNew)).thenReturn(offerNew);
//        when(offerMapper.toResponse(offerNew)).thenReturn(response);
//
//        OfferDtoResponse actualResponse = offerService.updateById(id,request);
//
//        assertNotNull(actualResponse);
//        assertEquals(response,actualResponse);
//        assertEquals(newName,actualResponse.name());
//
//        verify(offerRepository).findById(anyLong());
//        verify(offerRepository).existsOfferByName(anyString());
//        verify(offerRepository).save(any(Offer.class));
//        verify(offerMapper).dtoUpdateToEntity(any(OfferDtoRequest.class),any(Offer.class));
//        verify(offerMapper).toResponse(any(Offer.class));
//    }
//
//    @Test
//    public void updateById_ShouldThrowResourceNotFoundException_WhenNoOfferById(){
//        OfferDtoRequest request = new OfferDtoRequest(newName);
//
//        when(offerRepository.findById(id)).thenReturn(Optional.empty());
//
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> offerService.updateById(id,request));
//        String expectedMessage = "Offer not found with id: " + id;
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(offerRepository).findById(anyLong());
//        verify(offerRepository,never()).existsOfferByName(anyString());
//        verify(offerRepository,never()).save(any(Offer.class));
//        verify(offerMapper,never()).dtoUpdateToEntity(any(OfferDtoRequest.class),any(Offer.class));
//        verify(offerMapper,never()).toResponse(any(Offer.class));
//    }
//
//    @Test
//    public void updateById_ShouldThrowAlreadyExistsException_WhenOfferByRequestName(){
//        OfferDtoRequest request = new OfferDtoRequest(newName);
//        Offer offerOld = new Offer(id, name);
//
//        when(offerRepository.findById(id)).thenReturn(Optional.of(offerOld));
//        when(offerRepository.existsOfferByName(request.name().trim())).thenReturn(true);
//
//        Exception exception = assertThrows(AlreadyExistsException.class, () -> offerService.updateById(id,request));
//        String expectedMessage = "Offer by name" + request.name() + "already exists";
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage,actualMessage);
//
//        verify(offerRepository).findById(anyLong());
//        verify(offerRepository).existsOfferByName(anyString());
//        verify(offerRepository, never()).save(any(Offer.class));
//        verify(offerMapper, never()).dtoUpdateToEntity(any(OfferDtoRequest.class),any(Offer.class));
//        verify(offerMapper, never()).toResponse(any(Offer.class));
//    }
//
//    @Test
//    public void findByName_ShouldReturnPageOfResponses_WhenOffersExist(){
//
//        when(offerRepository.findByNameContainingIgnoreCase(name,pageable)).thenReturn(offerPage);
//        when(offerMapper.toResponse(offerAfter)).thenReturn(response);
//
//        Page<OfferDtoResponse> actualResponse = offerService.findByName(name,pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(1, actualResponse.getTotalElements());
//        assertEquals(response, actualResponse.getContent().getLast());
//
//        verify(offerRepository).findByNameContainingIgnoreCase(anyString(),any(Pageable.class));
//        verify(offerMapper).toResponse(any(Offer.class));
//    }
//
//    @Test
//    public void findByName_ShouldReturnEmptyPage_WhenNoOffersExist(){
//        when(offerRepository.findByNameContainingIgnoreCase(name,pageable)).thenReturn(Page.empty());
//
//        Page<OfferDtoResponse> actualResponse = offerService.findByName(name,pageable);
//
//        assertNotNull(actualResponse);
//        assertEquals(0, actualResponse.getTotalElements());
//        assertEquals(Collections.emptyList(), actualResponse.getContent());
//
//        verify(offerRepository).findByNameContainingIgnoreCase(anyString(),any(Pageable.class));
//        verify(offerMapper,never()).toResponse(any(Offer.class));
//    }
//}