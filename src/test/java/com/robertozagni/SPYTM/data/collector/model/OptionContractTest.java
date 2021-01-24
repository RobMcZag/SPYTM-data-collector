package com.robertozagni.SPYTM.data.collector.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OptionContractTest {

    @Test
    void can_build_an_option_contract() {
        OptionContract optionContract = testOptionContract();
        assertNotNull(optionContract);
        assertEquals("SPY210104C00245000", optionContract.getContractSymbol());
        assertEquals("REGULAR", optionContract.getContractSize());
        assertEquals("USD", optionContract.getCurrency());
    }
    @Test
    void can_calculate_contract_symbol_from_bk(){
        OptionContract optionContract = testOptionContract();
        assertEquals("SPY210104C00245000", optionContract.getContractSymbolFromBusinessKey());
    }
    @Test
    void can_calculate_business_key_from_contract_symbol__SPY() {
        OptionContract optionContract = new OptionContract();
        optionContract.setContractSymbol("SPY210104C00245000");
        optionContract.setBusinessKeyFieldsFromContractSymbol();
        assertEquals("SPY", optionContract.getUnderlyingSymbol());
        assertEquals(LocalDate.parse("2021-01-04"), optionContract.getExpiration());
        assertEquals(OptionContract.OptionType.CALL, optionContract.getOptionType());
        assertEquals(245.0, optionContract.getStrike());
    }
    @Test
    void can_calculate_business_key_from_contract_symbol__AM() {
        OptionContract optionContract = new OptionContract();
        optionContract.setContractSymbol("AM221124P00007500");
        optionContract.setBusinessKeyFieldsFromContractSymbol();
        assertEquals("AM", optionContract.getUnderlyingSymbol());
        assertEquals(LocalDate.parse("2022-11-24"), optionContract.getExpiration());
        assertEquals(OptionContract.OptionType.PUT, optionContract.getOptionType());
        assertEquals(7.5, optionContract.getStrike());
    }
    @Test
    void can_calculate_business_key_from_contract_symbol__DOCU() {
        OptionContract optionContract = new OptionContract();
        optionContract.setContractSymbol("DOCU230124C12345678");
        optionContract.setBusinessKeyFieldsFromContractSymbol();
        assertEquals("DOCU", optionContract.getUnderlyingSymbol());
        assertEquals(LocalDate.parse("2023-01-24"), optionContract.getExpiration());
        assertEquals(OptionContract.OptionType.CALL, optionContract.getOptionType());
        assertEquals(12345.678, optionContract.getStrike());
    }

    @Test
    void can_build_with_contract_symbol__DOCU() {
        OptionContract optionContract = OptionContract.builder()
                .contractSymbol("DOCU230124C12345678")
                .build();
        assertEquals("DOCU", optionContract.getUnderlyingSymbol());
        assertEquals(LocalDate.parse("2023-01-24"), optionContract.getExpiration());
        assertEquals(OptionContract.OptionType.CALL, optionContract.getOptionType());
        assertEquals(12345.678, optionContract.getStrike());
    }
    @Test
    void can_build_with_BK__DOCU() {
        OptionContract optionContract = OptionContract.builder()
                .underlyingSymbol("DOCU")
                .expiration(LocalDate.parse("2023-01-24"))
                .optionType(OptionContract.OptionType.CALL)
                .strike(12345.678)
                .contractSize("REGULAR")
                .currency("USD")
                .build();
        assertEquals("DOCU230124C12345678", optionContract.getContractSymbol());
        assertEquals("DOCU", optionContract.getUnderlyingSymbol());
        assertEquals(LocalDate.parse("2023-01-24"), optionContract.getExpiration());
        assertEquals(OptionContract.OptionType.CALL, optionContract.getOptionType());
        assertEquals(12345.678, optionContract.getStrike());
        assertEquals("REGULAR", optionContract.getContractSize());
        assertEquals("USD", optionContract.getCurrency());
    }
    @Test
    void pk_bk_mismatch_throws_runtime_exception() {
        assertThrows( RuntimeException.class,
            () -> OptionContract.builder()
                        .contractSymbol("AM230124C12345678")
                        .underlyingSymbol("DOCU")
                        .expiration(LocalDate.parse("2023-01-24"))
                        .optionType(OptionContract.OptionType.CALL)
                        .strike(12345.678)
                        .build()
        );
    }

    private OptionContract testOptionContract() {
        return new OptionContract(
                "SPY210104C00245000",
                "SPY", LocalDate.parse("2021-01-04"), OptionContract.OptionType.CALL, 245.0,
                "USD", "REGULAR", null);
    }

}