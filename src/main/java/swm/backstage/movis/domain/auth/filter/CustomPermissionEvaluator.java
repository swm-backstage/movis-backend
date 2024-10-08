package swm.backstage.movis.domain.auth.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import swm.backstage.movis.domain.auth.enums.PlatformType;
import swm.backstage.movis.domain.auth.dto.AuthenticationPrincipalDetails;
import swm.backstage.movis.domain.club_user.service.ClubUserService;
import swm.backstage.movis.domain.event.service.EventService;
import swm.backstage.movis.domain.member.service.MemberService;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final ClubUserService clubUserService;
    private final MemberService memberService;
    private final EventService eventService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if(authentication == null || targetId == null || targetType == null || permission == null) {
            throw new BaseException("PreAuthorize 매개변수의 형식이 일치하지 않습니다. ", ErrorCode.INTERNAL_SERVER_ERROR);
        }
        String clubId = this.getClubIdFromTargetId(targetId.toString(), targetType);
        if (clubId == null){
            return false;
        }

        return this.validateRole(authentication, clubId, permission);
    }

    private String getClubIdFromTargetId(String targetId, String targetType) {
        if(targetType.equals("clubId")){
            return targetId;
        } else if(targetType.equals("eventId")){
            return eventService.getEventByUuid(targetId).getClub().getUuid();
        }
        return null;
    }

    private boolean validateRole(Authentication authentication, String clubId, Object permission) {
        AuthenticationPrincipalDetails principal = (AuthenticationPrincipalDetails) authentication.getPrincipal();
        String identifier = principal.getIdentifier();
        String platformType = principal.getPlatformType();

        Set<String> permissions = new HashSet<>();
        if (permission instanceof String) {
            permissions.add((String) permission);
        } else if (permission instanceof Collection<?>) {
            permissions.addAll((Collection<String>) permission);
        }

        if (platformType.equals(PlatformType.APP.value())){
            return permissions.contains(clubUserService.getClubUser(identifier, clubId).getRoleType().getRole());
        } else if (platformType.equals(PlatformType.WEB.value())){
            return memberService.existedByNameAndClubUuid(identifier, clubId);
        }

        return false;
    }
}