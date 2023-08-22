package tr.com.cemalaydin.dashboard.enums.converters;


import tr.com.cemalaydin.dashboard.enums.AuthProvider;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.EnumUtils;

@Converter(autoApply = true)
public class AuthProviderConverter implements AttributeConverter<AuthProvider, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AuthProvider en) {
        return en != null ? en.getId(): null;
    }

    @Override
    public AuthProvider convertToEntityAttribute(Integer id) {
        return EnumUtils.getEnumList(AuthProvider.class).stream().filter(x-> x.getId()== id).findFirst().orElse(null);
    }
}
