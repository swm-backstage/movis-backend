package swm.backstage.movis.domain.member.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import swm.backstage.movis.domain.member.Member;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class MemberJdbcRepository {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void bulkSave(List<Member> memberList) {
        String sql = "INSERT INTO member " +
                "(ulid, name, phone_no ,is_enrolled, is_deleted, club_id, created_at, updated_at, deleted_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Member member = memberList.get(i);
                ps.setString(1, member.getUlid());
                ps.setString(2, member.getName());
                ps.setString(3, member.getPhoneNo());
                ps.setBoolean(4, member.getIsEnrolled());
                ps.setBoolean(5, member.getIsDeleted());
                ps.setString(6, member.getClub().getUlid());
                ps.setObject(7, LocalDateTime.now());
                ps.setObject(8, LocalDateTime.now());
                ps.setObject(9, null);

            }

            @Override
            public int getBatchSize() {
                return memberList.size();
            }
        });
    }
}
