package application.domen.webapi.services.mappers.meeting;

import application.domen.webapi.models.commons.MeetingInfo;
import application.domen.webapi.models.requests.NewMeetingInfo;
import application.domen.webapi.models.responses.MeetingInfoResult;
import application.domen.webapi.services.repository.entities.MeetingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface IMeetingMapper {
    public MeetingInfo toInfo(MeetingEntity entity);
    public MeetingEntity toEntity(NewMeetingInfo model);
    public MeetingInfoResult toDto(MeetingEntity entity);
}
