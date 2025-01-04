package toy.order.domain.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Slf4j
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

    @Override
    public Double findPriceByItemId(Long itemId){
        String sql = "select price from item where item_id = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, itemId);
    }

    @Override
    public List<Item> findItems(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        // MapSqlParameterSource로 변경
        MapSqlParameterSource param = new MapSqlParameterSource();
        if (StringUtils.hasText(itemName)) {
            param.addValue("itemName", itemName);
        }
        if (maxPrice != null) {
            param.addValue("maxPrice", maxPrice);
        }

        String sql = "select item_id, item_name, price, quantity, member_id from item";

        // 동적 쿼리 구성
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }

        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%',:itemName,'%')";
            andFlag = true;
        }

        if (maxPrice != null) {
            if (andFlag) {
                sql += " and";
            }
            sql += " price <= :maxPrice";
        }

        log.info("sql={}", sql);

        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        return namedJdbcTemplate.query(sql, param, itemRowMapper());
    }


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
