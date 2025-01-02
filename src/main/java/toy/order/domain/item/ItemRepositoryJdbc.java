package toy.order.domain.item;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ItemRepositoryJdbc implements ItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public ItemRepositoryJdbc(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Item save(Item item) {
        String sql = "insert into item (item_name, price, quantity, member_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, item.getItemName(), item.getPrice(), item.getQuantity(), item.getMemberId());
        return item;
    }

    @Override
    public void update(Long itemId, Item itemParam) {
        String sql = "update item set item_name=?, price = ?, quantity = ? where item_id = ?";
        jdbcTemplate.update(sql, itemParam.getItemName(), itemParam.getPrice(), itemParam.getQuantity(), itemParam.getItemId());
    }

    @Override
    public void updateCnt(Item item, Integer quantity) {
        String sql = "update item set quantity = ? where item_id = ?";
        jdbcTemplate.update(sql, quantity, item.getItemId());
    }

    @Override
    public void delete(Long itemId) {
        String sql = "delete from item where item_id = ?";
        jdbcTemplate.update(sql, itemId);
    }

    @Override
    public Long findItemIdByItemNameAndMemberId(String itemName, Long memberId) {
        String sql = """
        SELECT i.item_id 
        FROM item i
        JOIN member m ON i.member_id = m.member_id
        WHERE i.item_name = ? AND m.member_id = ?
    """;
        return jdbcTemplate.queryForObject(sql, Long.class, itemName, memberId);
    }

    @Override
    public Long findMemberIdByItemId(Long itemId){
        String sql = """
        SELECT m.member_id
        FROM member m
        JOIN item i
        ON i.member_id = m.member_id
        WHERE i.item_id = ?
        """;
        return jdbcTemplate.queryForObject(sql, Long.class, itemId);
    }

    @Override
    public Item findByItemId(Long itemId){
        String sql = "select * from item where item_id = ?";
        return jdbcTemplate.queryForObject(sql, itemRowMapper(), itemId);
    };


    //쿼리 실행 결과를 객체로 매핑하여 반환
    private RowMapper<Item> itemRowMapper() {
        return (rs, rowNum) -> {
            Item item = new Item();
            item.setItemId(rs.getLong(1));
            item.setItemName(rs.getString(2));
            item.setPrice(rs.getInt(3));
            item.setQuantity(rs.getInt(4));
            item.setMemberId(rs.getLong(5));
            return item;
        };
    }

    @Override
    public List<Item> findAll() {
        String sql = "select * from item";
        return jdbcTemplate.query(sql, itemRowMapper());
    }
}
