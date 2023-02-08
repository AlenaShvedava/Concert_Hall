package pl.solvd.concerthall.dao.impl;

import pl.solvd.concerthall.dao.interfacesDAO.IProgramHasGenreDAO;
import pl.solvd.concerthall.dao.mysql.MySqlDAO;
import pl.solvd.concerthall.entities.ProgramHasGenre;
import pl.solvd.concerthall.utils.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProgramHasGenreDAOImpl extends MySqlDAO implements IProgramHasGenreDAO {
    private static final ConnectionPool instance = ConnectionPool.getInstance();
    private static final Connection connection = instance.getConnection();
    private static final String GET_ALL_PROGRAM_HAS_GENRE_QUERY = "SELECT * FROM program_has_genre";
    private static final String GET_PROGRAM_HAS_GENRE_QUERY = "SELECT * FROM program_has_genre WHERE program_id = ?";
    private static final String INSERT_PROGRAM_HAS_GENRE_QUERY = "INSERT INTO program_has_genre (program_id, genre_id) VALUES(?, ?)";
    private static final String UPDATE_PROGRAM_HAS_GENRE_QUERY = "UPDATE program_has_genre SET genre_id = ? WHERE program_id = ?";
    private static final String DELETE_PROGRAM_HAS_GENRE_QUERY = "DELETE FROM program_has_genre WHERE program_id = ?";

    @Override
    public ProgramHasGenre addEntity(ProgramHasGenre entity) throws Exception {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_PROGRAM_HAS_GENRE_QUERY)) {
            ps.setLong(1, entity.getProgramId());
            ps.setLong(2, entity.getGenreId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return entity;
    }

    @Override
    public List<ProgramHasGenre> updateEntity(ProgramHasGenre entity) throws Exception {
        List<ProgramHasGenre> updatedProgramHasGenre = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_PROGRAM_HAS_GENRE_QUERY)) {
            ps.setLong(1, entity.getGenreId());
            ps.setLong(2, entity.getProgramId());
            ps.executeUpdate();
            updatedProgramHasGenre = getAll();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return updatedProgramHasGenre;
    }

    @Override
    public void deleteEntity(Long programId) throws Exception {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_PROGRAM_HAS_GENRE_QUERY)) {
            ps.setLong(1, programId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public List<ProgramHasGenre> getAll() throws Exception {
        List<ProgramHasGenre> programHasGenre = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(GET_ALL_PROGRAM_HAS_GENRE_QUERY)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Long programId = rs.getLong("program_id");
                    Long genreId = rs.getLong("genre_id");
                    programHasGenre.add(new ProgramHasGenre(programId, genreId));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            return programHasGenre;
        }
    }

    @Override
    public List<ProgramHasGenre> getAllProgramHasGenreBy(Predicate<ProgramHasGenre> predicate) throws Exception {
        List<ProgramHasGenre> programHasGenreList = getAll();
        programHasGenreList = programHasGenreList.stream().filter(predicate).collect(Collectors.toList());
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return programHasGenreList;
    }

    @Override
    public void getEntityByProgramIdAndGenreId(Long programId, Long genreId) throws Exception {
        ProgramHasGenre programHasGenre = new ProgramHasGenre();
        try (PreparedStatement ps = connection.prepareStatement(GET_PROGRAM_HAS_GENRE_QUERY)) {
            ps.setLong(1, programId);
            ps.setLong(2, genreId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    programHasGenre.setProgramId(rs.getLong(1));
                    programHasGenre.setGenreId(rs.getLong(2));
                    System.out.println(programHasGenre.getProgramId() + "," + programHasGenre.getGenreId());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
