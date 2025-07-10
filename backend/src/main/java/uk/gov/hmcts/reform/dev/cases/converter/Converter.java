package uk.gov.hmcts.reform.dev.cases.converter;

/**
 * It converts a data type to another
 * @param <FROM>
 * @param <TO>
 */
public interface Converter<FROM, TO> {

  TO convert(FROM fromType);
}
