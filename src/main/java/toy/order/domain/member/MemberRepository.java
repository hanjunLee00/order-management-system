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
        String sql = "insert into member(login_id, name, password, uuid) values (?, ?, ?, ?)";

        // 로그 추가: SQL 실행 전 Member 객체 필드 출력
        System.out.println("Executing SQL Insert with Member: " +
                "LoginId=" + member.getLoginId() +
                ", Name=" + member.getName() +
                ", Password=" + member.getPassword() +
                ", UUID=" + member.getUuid());

        template.update(sql, member.getLoginId(), member.getName(), member.getPassword(), member.getUuid());

        // 로그 추가: SQL 실행 후 확인
        System.out.println("Member saved successfully with UUID: " + member.getUuid());

        return member;
    }


    public void update(String uuid, String name, String loginId, String password) {
        String sql = "update member set login_id=?, name=?, password=? where uuid=?";
        template.update(sql, loginId, name, password, uuid);
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

    public Member findByUuid(String uuid) {
        String sql = "select * from member where uuid = ?";
        return template.queryForObject(sql, memberRowMapper(), uuid);

    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setMemberId(rs.getLong(1));
            member.setLoginId(rs.getString(2));
            member.setName(rs.getString(3));
            member.setPassword(rs.getString(4));
            member.setUuid(rs.getString(5));
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
