package toy.order.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import toy.order.domain.member.ex.MyDbException;
import toy.order.domain.member.ex.MyDuplicateKeyException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository
public class MemberRepository {
    private final JdbcTemplate template;

    public MemberRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public Member save(Member member) {
        String sql = "insert into member(login_id, name, password) values (?, ?, ?)";
        template.update(sql, member.getLoginId(), member.getName(), member.getPassword());
        return member;
    }

    public void update(Long memberId, String name, String loginId, String password) {
        String sql = "update member set login_id=?, name=?, password=? where member_id=?";
        template.update(sql, loginId, name, password, memberId);
    }

    public void delete(String loginId) {
        String sql = "delete from member where login_id=?";
        template.update(sql, loginId);
    }

    //----------------------------- 여기까지 누구나 접근 가능 --------------------------
    //----------------------------- 밑에서부터는 관리자 권한 ---------------------------

    //회원 조회
    public Member findByMemberId(Long memberId) {
        String sql = "select * from member where member_id = ?";
        return template.queryForObject(sql, memberRowMapper(), memberId);
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setMemberId(rs.getLong(1));
            member.setLoginId(rs.getString(2));
            member.setName(rs.getString(3));
            member.setPassword(rs.getString(4));
            return member;
        };
    }

    public Optional<Member> findByLoginId(String loginId) {
        String sql = "select * from member where login_id = ?";
        return template.query(sql, memberRowMapper(), loginId).stream().findFirst();
    }

    public List<Member> findAll() {
        String sql = "select * from member";
        return template.query(sql, memberRowMapper());
    }
}
