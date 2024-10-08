package swm.backstage.movis.domain.auth.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {

    @Override
    public String convertToDatabaseColumn(RoleType roleType) {

        return roleType.getRole();
    }

    @Override
    public RoleType convertToEntityAttribute(String dbData) {

        return RoleType.ofRole(dbData);
    }
}
