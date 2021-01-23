package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

//@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OptionContract {

    @Id private String contractSymbol; //"SPY210104C00245000",

    private String underlyingSymbol; //"SPY",
    private LocalDate expiration; //UTC: Monday 4 January 2021 00:00:00
    @Enumerated(EnumType.STRING) private OptionType optionType;
    private Double strike; //":245.0,

    private String currency; //"USD",
    private String contractSize; //"REGULAR",

    List<OptionQuote> quotes;

    protected boolean businessKeyIsSet() {
        return     this.underlyingSymbol != null
                && this.expiration != null
                && this.optionType != null
                && this.strike != null;
    }

    protected String getContractSymbolFromBusinessKey() { //"SPY210104C00245000"
        String symbolPart = this.underlyingSymbol;
        String datePart = this.expiration.format(DateTimeFormatter.ofPattern("yyMMdd"));
        String typePart = this.optionType == OptionType.CALL ? "C" : "P";
        String strikePart = String.format("%1$08.0f", this.strike * 1000);
        return symbolPart + datePart + typePart + strikePart;
    }

    protected synchronized void setBusinessKeyFieldsFromContractSymbol() { //SPY 210104 C 00245000
        int symbolLenght = contractSymbol.length() - 6 - 1 - 8;
        this.underlyingSymbol = contractSymbol.substring(0, symbolLenght);
        this.expiration = LocalDate.parse(contractSymbol.substring(symbolLenght, symbolLenght+6), DateTimeFormatter.ofPattern("yyMMdd"));
        this.optionType = contractSymbol.substring(symbolLenght + 6, symbolLenght + 6 + 1).equals("C") ? OptionType.CALL : OptionType.PUT;
        this.strike = Double.parseDouble(contractSymbol.substring(symbolLenght + 6 + 1)) / 1000;
    }

    public enum OptionType {
        CALL,
        PUT
    }

    /** Uses custom builder class */
    public static OptionContractBuilder builder() {
        return new CustomOptionContractBuilder();
    }

    /**
     * Custom  builder class that adds PK / BK verification / calculation on build.
     *
     * Calculate PK (contractSymbol) from BK if all BK fields are set,
     * otherwise calculate BK from contractSymbol (PK).
     */
    private static class CustomOptionContractBuilder extends OptionContractBuilder {
        @Override
        public OptionContract build() {
            OptionContract optionContract = super.build();
            if (optionContract.businessKeyIsSet()) {
                if (optionContract.contractSymbol == null) {
                    optionContract.contractSymbol = optionContract.getContractSymbolFromBusinessKey();
                } else {
                    if (! optionContract.contractSymbol.equals(optionContract.getContractSymbolFromBusinessKey())) {
                        throw new RuntimeException("Fields of BK and contractSymbol do not match: " + optionContract.toString());
                    }
                }
            } else {
                if (optionContract.contractSymbol == null) { throw new RuntimeException("Both BK and contractSymbol are not set."); }
                optionContract.setBusinessKeyFieldsFromContractSymbol();
            }
            return optionContract;
        }

    }

}
