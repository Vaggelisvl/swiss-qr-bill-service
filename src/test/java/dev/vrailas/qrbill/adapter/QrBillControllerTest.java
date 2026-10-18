package dev.vrailas.qrbill.adapter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class QrBillControllerTest {

    private static final String VALID_BODY = """
            {
              "creditorIban": "CH44 3199 9123 0008 8901 2",
              "creditor": {"name": "Robert Schneider AG", "street": "Rue du Lac", "buildingNumber": "1268",
                           "postalCode": "2501", "town": "Biel", "country": "CH"},
              "amount": 1949.75,
              "currency": "CHF",
              "reference": {"type": "QRR", "value": "210000000003139471430009017"},
              "additionalInformation": "Invoice 10978"
            }""";

    @Autowired
    MockMvc mvc;

    @Test
    void generatesPayloadAndImage() throws Exception {
        mvc.perform(post("/api/v1/qr-bills").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").value(org.hamcrest.Matchers.startsWith("SPC\n0200\n1\nCH4431999123000889012")))
                .andExpect(jsonPath("$.qrCodePngBase64").isNotEmpty());
    }

    @Test
    void returnsPng() throws Exception {
        mvc.perform(post("/api/v1/qr-bills/image").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
    }

    @Test
    void reportsDomainViolationsAsProblemDetails() throws Exception {
        String body = VALID_BODY.replace("210000000003139471430009017", "210000000003139471430009018");
        mvc.perform(post("/api/v1/qr-bills").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.title").value("Invalid QR-bill"))
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("check digit")));
    }

    @Test
    void reportsUnknownCurrencyAndReferenceType() throws Exception {
        mvc.perform(post("/api/v1/qr-bills").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY.replace("\"CHF\"", "\"USD\"")))
                .andExpect(status().isUnprocessableEntity());
        mvc.perform(post("/api/v1/qr-bills").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY.replace("QRR", "XYZ")))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void acceptsRegularIbanWithoutReference() throws Exception {
        String body = """
                {"creditorIban": "CH9300762011623852957",
                 "creditor": {"name": "Max Muster", "postalCode": "8000", "town": "Zürich", "country": "CH"},
                 "currency": "EUR"}""";
        mvc.perform(post("/api/v1/qr-bills").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").value(org.hamcrest.Matchers.containsString("\nNON\n")));
    }

    @Test
    void rejectsMissingFieldsWithBadRequest() throws Exception {
        mvc.perform(post("/api/v1/qr-bills").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }
}
