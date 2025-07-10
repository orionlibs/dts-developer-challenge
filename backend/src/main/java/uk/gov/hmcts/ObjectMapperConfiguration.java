package uk.gov.hmcts;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class ObjectMapperConfiguration {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new Jackson2ObjectMapperBuilder().serializationInclusion(Include.NON_NULL)
        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
            SerializationFeature.FAIL_ON_EMPTY_BEANS,
            SerializationFeature.FAIL_ON_SELF_REFERENCES)
        .build();
    return mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
  }
}
