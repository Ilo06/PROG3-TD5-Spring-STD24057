package ilo.ingredienttd5springstd24057.repository;

import ilo.ingredienttd5springstd24057.entity.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StockMovementRepository {

    private final DataSource dataSource;

    public StockMovementRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public StockValue getStockValueAt(Integer ingredientId, Instant at, UnitEnum unit) {
        String sql = """
                SELECT unit,
                       SUM(
                           CASE
                               WHEN type = 'IN'  THEN  quantity
                               WHEN type = 'OUT' THEN -quantity
                               ELSE 0
                           END
                       ) AS actual_quantity
                FROM stock_movement
                WHERE creation_datetime <= ?
                  AND id_ingredient = ?
                  AND unit = ?::unit
                GROUP BY unit
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.from(at));
            ps.setInt(2, ingredientId);
            ps.setString(3, unit.name());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new StockValue(
                            rs.getDouble("actual_quantity"),
                            UnitEnum.valueOf(rs.getString("unit"))
                    );
                }
                return new StockValue(0.0, unit);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns stock movements for a given ingredient filtered by a datetime range [from, to].
     */
    public List<StockMovement> getStockMovementsByIngredientIdAndDateRange(
            Integer ingredientId, Instant from, Instant to) {

        String sql = """
                SELECT id, creation_datetime, unit, quantity, type
                FROM stock_movement
                WHERE id_ingredient = ?
                  AND creation_datetime >= ?
                  AND creation_datetime <= ?
                ORDER BY creation_datetime ASC
                """;

        List<StockMovement> movements = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ingredientId);
            ps.setTimestamp(2, Timestamp.from(from));
            ps.setTimestamp(3, Timestamp.from(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movements.add(new StockMovement(
                            rs.getInt("id"),
                            rs.getTimestamp("creation_datetime").toInstant(),
                            UnitEnum.valueOf(rs.getString("unit")),
                            rs.getDouble("quantity"),
                            MovementTypeEnum.valueOf(rs.getString("type"))
                    ));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return movements;
    }

    /**
     * Saves a list of stock movement creation requests for a given ingredient,
     * setting creation_datetime to now, and returns the persisted StockMovement objects.
     */
    public List<StockMovement> saveStockMovements(
            Integer ingredientId, List<StockMovementCreateRequest> requests) {

        String insertSql = """
                INSERT INTO stock_movement (id_ingredient, quantity, type, unit, creation_datetime)
                VALUES (?, ?, ?::movement_type, ?::unit, ?)
                RETURNING id, creation_datetime, unit, quantity, type
                """;

        List<StockMovement> saved = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            for (StockMovementCreateRequest req : requests) {
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    Instant now = Instant.now();
                    ps.setInt(1, ingredientId);
                    ps.setDouble(2, req.getQuantity());
                    ps.setString(3, req.getType().name());
                    ps.setString(4, req.getUnit().name());
                    ps.setTimestamp(5, Timestamp.from(now));

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            saved.add(new StockMovement(
                                    rs.getInt("id"),
                                    rs.getTimestamp("creation_datetime").toInstant(),
                                    UnitEnum.valueOf(rs.getString("unit")),
                                    rs.getDouble("quantity"),
                                    MovementTypeEnum.valueOf(rs.getString("type"))
                            ));
                        }
                    }
                }
            }

            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return saved;
    }
}
