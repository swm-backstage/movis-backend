package swm.backstage.movis.domain.auth.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import swm.backstage.movis.domain.auth.PlatformType;
import swm.backstage.movis.domain.auth.dto.AuthenticationPrincipalDetails;
import swm.backstage.movis.domain.club_user.ClubUser;
import swm.backstage.movis.domain.club_user.service.ClubUserService;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event.service.EventService;
import swm.backstage.movis.domain.member.service.MemberService;
import swm.backstage.movis.domain.auth.RoleType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AuthorityFilter extends OncePerRequestFilter {

    private final EventService eventService;
    private final ClubUserService clubUserService;
    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    public AuthorityFilter(EventService eventService, ClubUserService clubUserService, MemberService memberService) {
        this.eventService = eventService;
        this.clubUserService = clubUserService;
        this.memberService = memberService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticationPrincipalDetails principal = (AuthenticationPrincipalDetails) authentication.getPrincipal();

        String identifier = principal.getIdentifier();
        String platformType = principal.getPlatformType();
        String clubId = extractClubIdFromRequest(request);

        String role = RoleType.ROLE_AUTHENTICATED.value();

        if (StringUtils.hasText(clubId)) {

            if (platformType.equals(PlatformType.APP.value())){
                //앱을 사용하는 총무(운영진)일 경우, ClubUser에서 권한 조회
                ClubUser clubUser = clubUserService.getClubUser(identifier, clubId);
                if (clubUser != null) {
                    role = clubUser.getRole();
                }
            } else if (platformType.equals(PlatformType.WEB.value())) {
                //웹을 사용하는 회원일 경우, Club에 해당 Member의 존재 여부 조회

                //TODO: 조회 기능 구현
                role = RoleType.ROLE_MEMBER.value();
            }
        }

        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                List.of(new SimpleGrantedAuthority(role)));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String extractClubIdFromRequest(HttpServletRequest request) throws IOException {

        String clubId = extractClubIdFromRequestParam(request);
        if(StringUtils.hasText(clubId)){
            return clubId;
        }

        return extractClubIdFromRequestBody(request);
    }

    private String extractClubIdFromRequestParam(HttpServletRequest request) throws IOException {
        String clubId = request.getParameter("clubId");
        String eventId = request.getParameter("eventId");

        if (StringUtils.hasText(clubId)) {
            return clubId;
        }
        if (StringUtils.hasText(eventId)) {

            Event event = eventService.getEventByUuid(eventId);
            if (event != null && event.getClub() != null) {
                return event.getClub().getUuid();
            }
        }

        return null;
    }

    private String extractClubIdFromRequestBody(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String jsonBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        if (StringUtils.hasText(jsonBody)) {
            JsonNode jsonNode = objectMapper.readTree(jsonBody);

            if (jsonNode.has("clubId")) {
                return jsonNode.get("clubId").toString();
            } else if (jsonNode.has("eventId")) {
                Event event = eventService.getEventByUuid(jsonNode.get("eventId").toString());
                if (event != null && event.getClub() != null) {
                    return event.getClub().getUuid();
                }
            }
        }

        return null;
    }
}
