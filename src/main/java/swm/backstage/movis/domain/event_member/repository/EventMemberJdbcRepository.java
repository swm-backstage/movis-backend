package swm.backstage.movis.domain.event_member.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import swm.backstage.movis.domain.event_member.EventMember;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EventMemberJdbcRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void bulkSave(List<EventMember> memberList) {
        String sql = "INSERT INTO event_member " +
                "(uuid, amount_to_pay, is_paid ,member_id, event_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                EventMember member = memberList.get(i);
                ps.setString(1, member.getUuid());
                ps.setLong(2, member.getAmountToPay());
                ps.setBoolean(3, member.getIsPaid());
                ps.setLong(4, member.getMember().getId());
                ps.setLong(5, member.getEvent().getId());
                ps.setObject(6, LocalDateTime.now());
                ps.setObject(7, LocalDateTime.now());
            }

            @Override
            public int getBatchSize() {
                return memberList.size();
            }
        });
    }
}
