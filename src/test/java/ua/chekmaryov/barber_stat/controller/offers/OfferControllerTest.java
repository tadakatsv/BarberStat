package ua.chekmaryov.barber_stat.controller.offers;

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
import ua.chekmaryov.barber_stat.controller.OfferController;
import ua.chekmaryov.barber_stat.dto.offers.OfferDtoRequest;
import ua.chekmaryov.barber_stat.dto.offers.OfferDtoResponse;
import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;
import ua.chekmaryov.barber_stat.service.offers.OfferServiceImpl;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc()
@WebMvcTest(OfferController.class)
public class OfferControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private OfferServiceImpl offerService;

    @Test
    public void create_shouldReturn201_WhenCreateValidOffer() throws Exception{
        OfferDtoRequest request = new OfferDtoRequest("Haircut");
        OfferDtoResponse response = new OfferDtoResponse(1L,"Haircut");

        when(offerService.create(request)).thenReturn(response);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/offers")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Haircut"))
                .andExpect(jsonPath("$.id").value(1L));

        verify(offerService).create(Mockito.any(OfferDtoRequest.class));
    }

    @Test
    public void create_shouldReturn400_WhenNameIsBlank() throws Exception{
        OfferDtoRequest request = new OfferDtoRequest("");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/offers")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(offerService,never()).create(Mockito.any(OfferDtoRequest.class));
    }

    @Test
    public void getAll_shouldReturn200_WhenClientsExist() throws Exception{
        OfferDtoResponse response = new OfferDtoResponse(1L,"Haircut");
        Page<OfferDtoResponse> offerPage = new PageImpl<>(List.of(response));

        when(offerService.getAll(any(Pageable.class))).thenReturn(offerPage);

        mockMvc.perform(get("/api/v1/offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Haircut"));

        verify(offerService).getAll(any(Pageable.class));
    }

    @Test
    public void getAll_shouldReturn200_WhenNoClientsExist() throws Exception{
        when(offerService.getAll(any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(offerService).getAll(any(Pageable.class));
    }

    @Test
    public void getById_shouldReturn200_WhenOfferExistsById() throws Exception{
        Long id = 1L;
        OfferDtoResponse response = new OfferDtoResponse(id,"Haircut");

        when(offerService.getById(id)).thenReturn(response);

        mockMvc.perform(get("/api/v1/offers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
        verify(offerService).getById(anyLong());
    }

    @Test
    public void getById_shouldReturn404_WhenOfferNotFoundById() throws Exception{
        Long id = 1L;

        when(offerService.getById(id)).thenThrow(new ResourceNotFoundException("Client not found with id: " + id));

        mockMvc.perform(get("/api/v1/offers/1"))
                .andExpect(status().isNotFound());
        verify(offerService).getById(anyLong());
    }

    @Test
    public void update_shouldReturn200_WhenUpdateValidOffer() throws Exception{
        Long id = 1L;
        OfferDtoRequest request = new OfferDtoRequest("Haircut");
        OfferDtoResponse response = new OfferDtoResponse(id,"Haircut");

        when(offerService.updateById(anyLong(),eq(request))).thenReturn(response);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/offers/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Haircut"))
                .andExpect(jsonPath("$.id").value(id));

        verify(offerService).updateById(anyLong(),any(OfferDtoRequest.class));
    }

    @Test
    public void update_shouldReturn409_WhenNameAlreadyExists() throws Exception{
        OfferDtoRequest request = new OfferDtoRequest("Haircut");

        when(offerService.updateById(anyLong(),eq(request))).thenThrow(new AlreadyExistsException("Offer by name" + request.name() + "already exists"));

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/offers/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        verify(offerService).updateById(anyLong(),any(OfferDtoRequest.class));
    }

    @Test
    public void update_shouldReturn404_WhenNoOfferById() throws Exception{
        long id = 1L;
        OfferDtoRequest request = new OfferDtoRequest("Haircut");

        when(offerService.updateById(anyLong(),eq(request))).thenThrow(new ResourceNotFoundException("Offer not found with id: " + id));

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/offers/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(offerService).updateById(anyLong(),any(OfferDtoRequest.class));
    }

    @Test
    public void findByName_shouldReturn200_WhenClientsExist() throws Exception{
        OfferDtoResponse response = new OfferDtoResponse(1L,"Haircut");
        Page<OfferDtoResponse> offerPage = new PageImpl<>(List.of(response));

        when(offerService.findByName(anyString(), any(Pageable.class))).thenReturn(offerPage);

        mockMvc.perform(get("/api/v1/offers/search/by-name?name=Haircut"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Haircut"));

        verify(offerService).findByName(anyString(),any(Pageable.class));
    }

    @Test
    public void findByName_shouldReturn200_WhenNoClientsExist() throws Exception{
        when(offerService.findByName(anyString(),any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/offers/search/by-name?name=Haircut"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(offerService).findByName(anyString(),any(Pageable.class));
    }

    }