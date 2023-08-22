package tr.com.cemalaydin.dashboard.enums.converters;


import tr.com.cemalaydin.dashboard.enums.Status;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.EnumUtils;

@Converter(autoApply = true)
public class StatusConverter  implements AttributeConverter<Status, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Status en) {
        return en != null ? en.getId(): null;
    }

    @Override
    public Status convertToEntityAttribute(Integer id) {
        return EnumUtils.getEnumList(Status.class).stream().filter(x-> x.getId()== id).findFirst().orElse(null);
    }
}
