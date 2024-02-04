package com.gilberto.invoiceapi.controllers;

import com.gilberto.invoiceapi.exceptions.ApiException;
import com.gilberto.invoiceapi.exceptions.InvoiceNotFoundException;
import com.gilberto.invoiceapi.models.dto.requests.InvoiceForm;
import com.gilberto.invoiceapi.services.implementations.InvoiceService;
import static com.gilberto.invoiceapi.utils.JsonConvertionUtils.asJsonString;
import com.gilberto.invoiceapi.utils.ModelUtil;
import java.util.Collections;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@ExtendWith(MockitoExtension.class)
public class InvoiceControllerTest {
  
  private static final String INVOICE_API_URL_PATH       = "/api/v1/invoice";
  public static final  String QUERY_PARAMS               = "?pageSize=10&pageNumber=0&statusList" +
      "=APPROVED,CANCELLED&search=search&startDate=2024-01-01&endDate=2024-01-31";
  public static final  String ISSUER_TAX_ID              = "issuerTaxId";
  public static final  long   INVOICE_ID                 = 1L;
  public static final  long   INVALID_INVOICE_ID         = 2L;
  public static final  String INVOICE_API_SUBPATH_CANCEL = "/cancel";
  
  private MockMvc mockMvc;
  
  @Mock
  private InvoiceService invoiceService;
  
  @InjectMocks
  private InvoiceController invoiceController;
  
  @BeforeEach
  public void setUp() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(invoiceController)
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
        .build();
  }
  
  // POST
  @Test
  public void whenPOSTIsCalledThenCreatAnInvoice() throws Exception {
    // Given
    var invoiceForm = ModelUtil.getInvoiceForm();
    var invoiceDTO  = ModelUtil.getInvoiceDTO();
    
    // When
    when(this.invoiceService.create(invoiceForm))
        .thenReturn(invoiceDTO);
    
    // Then
    this.mockMvc.perform(post(INVOICE_API_URL_PATH)
            .contentType(APPLICATION_JSON)
            .content(asJsonString(invoiceForm)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(invoiceDTO.id().intValue())))
        .andExpect(jsonPath("$.number", is(invoiceDTO.number())))
        .andExpect(jsonPath("$.accessKey", is(invoiceDTO.accessKey())));
  }
  
  @Test
  public void whenPOSTIsCalledWithoutRequiredFieldThenBadRequestStatusMustBeReturned() throws Exception {
    // Given
    var invoiceForm = new InvoiceForm(null, null, null,
        null, null, null);
    
    // Then
    this.mockMvc.perform(post(INVOICE_API_URL_PATH)
            .contentType(APPLICATION_JSON)
            .content(asJsonString(invoiceForm)))
        .andExpect(status().isBadRequest());
  }
  
  @Test
  public void whenPOSTIsCalledToCreateInvoiceWithInvalidItemsThenBadRequestStatusMustBeReturned() throws Exception {
    // Given
    var invoiceForm = ModelUtil.getInvoiceForm();
    
    // When
    when(this.invoiceService.create(invoiceForm))
        .thenThrow(ApiException.class);
    
    // Then
    this.mockMvc.perform(post(INVOICE_API_URL_PATH)
            .contentType(APPLICATION_JSON)
            .content(asJsonString(invoiceForm)))
        .andExpect(status().isBadRequest());
  }
  
  // GET ALL
  @Test
  public void whenGETIsCalledToListInvoicesThenReturnAnInvoiceList() throws Exception {
    // Given
    var invoiceFilter = ModelUtil.getInvoiceFilter();
    var invoiceDTO    = ModelUtil.getInvoiceDTO();
    
    // When
    when(this.invoiceService.listAll(ISSUER_TAX_ID, invoiceFilter))
        .thenReturn(Collections.singletonList(invoiceDTO));
    
    // Then
    this.mockMvc.perform(get(INVOICE_API_URL_PATH + QUERY_PARAMS)
            .contentType(APPLICATION_JSON)
            .header("issuerTaxId", ISSUER_TAX_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", is(invoiceDTO.id().intValue())))
        .andExpect(jsonPath("$[0].number", is(invoiceDTO.number())))
        .andExpect(jsonPath("$[0].accessKey", is(invoiceDTO.accessKey())));
  }
  
  @Test
  public void whenGETIsCalledToListInvoicesThenReturnAnEmptyList() throws Exception {
    // Given
    var invoiceFilter = ModelUtil.getInvoiceFilter();
    
    // When
    when(this.invoiceService.listAll(ISSUER_TAX_ID, invoiceFilter))
        .thenReturn(Collections.emptyList());
    
    // Then
    this.mockMvc.perform(get(INVOICE_API_URL_PATH + QUERY_PARAMS)
            .contentType(APPLICATION_JSON)
            .header("issuerTaxId", ISSUER_TAX_ID))
        .andExpect(status().isOk());
  }
  
  // GET ID
  @Test
  public void whenGETIsCalledWithValidIdThenAnInvoiceMustBeReturned() throws Exception {
    // Given
    var invoiceDTO = ModelUtil.getInvoiceDTO();
    
    // When
    when(this.invoiceService.findById(ISSUER_TAX_ID, INVOICE_ID))
        .thenReturn(invoiceDTO);
    
    // Then
    this.mockMvc.perform(get(INVOICE_API_URL_PATH + "/" + INVOICE_ID)
            .contentType(APPLICATION_JSON)
            .header("issuerTaxId", ISSUER_TAX_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(invoiceDTO.id().intValue())))
        .andExpect(jsonPath("$.number", is(invoiceDTO.number())))
        .andExpect(jsonPath("$.accessKey", is(invoiceDTO.accessKey())));
  }
  
  @Test
  public void whenGETIsCalledWithInValidIdThenNotFoundStatusMustBeReturned() throws Exception {
    // When
    when(this.invoiceService.findById(ISSUER_TAX_ID, INVALID_INVOICE_ID))
        .thenThrow(InvoiceNotFoundException.class);
    
    // Then
    this.mockMvc.perform(get(INVOICE_API_URL_PATH + "/" + INVALID_INVOICE_ID)
            .contentType(APPLICATION_JSON)
            .header("issuerTaxId", ISSUER_TAX_ID))
        .andExpect(status().isNotFound());
  }
  
  // PATCH
  @Test
  public void whenPATCHIsCalledWithValidIdThenCancelInvoice() throws Exception {
    // Given
    var invoiceDTO = ModelUtil.getInvoiceDTO();
    
    // When
    when(this.invoiceService.cancelInvoice(ISSUER_TAX_ID, INVOICE_ID))
        .thenReturn(invoiceDTO);
    
    // Then
    this.mockMvc.perform(patch(INVOICE_API_URL_PATH + "/" + INVOICE_ID + INVOICE_API_SUBPATH_CANCEL)
            .contentType(APPLICATION_JSON)
            .header("issuerTaxId", ISSUER_TAX_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(invoiceDTO.id().intValue())))
        .andExpect(jsonPath("$.number", is(invoiceDTO.number())))
        .andExpect(jsonPath("$.accessKey", is(invoiceDTO.accessKey())));
  }
  
  @Test
  public void whenPATCHIsCalledWithInValidIdThenNotFoundStatusMustBeReturned() throws Exception {
    // When
    when(this.invoiceService.cancelInvoice(ISSUER_TAX_ID, INVALID_INVOICE_ID))
        .thenThrow(InvoiceNotFoundException.class);
    
    // Then
    this.mockMvc.perform(patch(INVOICE_API_URL_PATH + "/" + INVALID_INVOICE_ID + INVOICE_API_SUBPATH_CANCEL)
            .contentType(APPLICATION_JSON)
            .header("issuerTaxId", ISSUER_TAX_ID))
        .andExpect(status().isNotFound());
  }
  
}
