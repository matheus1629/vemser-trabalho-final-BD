package com.dbc.repository;

import com.dbc.exceptions.BancoDeDadosException;
import com.dbc.model.Especialidade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadeRepository implements Repositorio<Integer, Especialidade>{

    @Override
    public Integer getProximoId(Connection connection) throws BancoDeDadosException {
        try {
            String sql = "SELECT seq_especialidade.nextval mysequence from DUAL";
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery(sql);

            if (res.next()) {
                return res.getInt("mysequence");
            }

            return null;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e.getCause());
        }
    }

    @Override
    public Especialidade adicionar(Especialidade especialidade) throws BancoDeDadosException {
        Connection con = null;
        try {
            con = ConexaoBancoDeDados.getConnection();

            Integer proximoId = this.getProximoId(con);
            especialidade.setIdEspecialidade(proximoId);

            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO ESPECIALIDADE\n" +
                    "(id_especialidade, valor, nome) VALUES (?, ?, ?)" );

            PreparedStatement stmt = con.prepareStatement(sql.toString());

            stmt.setInt(1, especialidade.getIdEspecialidade());
            stmt.setDouble(2, especialidade.getValor());
            stmt.setString(3, especialidade.getNome());

            int res = stmt.executeUpdate();
            System.out.println("adicionarEspecialidade.res=" + res);
            return especialidade;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e.getCause());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean remover(Integer id) throws BancoDeDadosException {
        Connection con = null;
        try {
            con = ConexaoBancoDeDados.getConnection();

            String sql = "DELETE FROM ESPECIALIDADE WHERE ID_ESPECIALIDADE = ?";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, id);

            // Executa-se a consulta
            int res = stmt.executeUpdate();
            System.out.println("removerEspecialidadePorId.res=" + res);

            return res > 0;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e.getCause());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean editar(Integer id, Especialidade especialidade) throws BancoDeDadosException {
        Connection con = null;
        try {
            con = ConexaoBancoDeDados.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE ESPECIALIDADE SET \n");

            if (especialidade.getValor() != 0) {
                sql.append(" valor = ?,");
            }
            if (especialidade.getNome() != null) {
                sql.append(" nome = ?,");
            }

            sql.deleteCharAt(sql.length() - 1); //remove o ultimo ','
            sql.append(" WHERE id_especialidade = ? ");

            PreparedStatement stmt = con.prepareStatement(sql.toString());

            int index = 1;

            if (especialidade.getValor() != 0) {
                stmt.setDouble(index++, especialidade.getValor());
            }
            if (especialidade.getNome() != null) {
                stmt.setString(index++, especialidade.getNome());
            }

            stmt.setInt(index, id);

            // Executa-se a consulta
            int res = stmt.executeUpdate();
            System.out.println("editarContato.res=" + res);

            return res > 0;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e.getCause());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Especialidade> listar() throws BancoDeDadosException {
        List<Especialidade> listaespecialidades = new ArrayList<>();
        Connection con = null;
        try {
            con = ConexaoBancoDeDados.getConnection();
            Statement stmt = con.createStatement();

            String sql = "SELECT * " +
                    "       FROM ESPECIALIDADE E " ;

            // Executa-se a consulta
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                Especialidade especialidade = getEspecialidadesFromResultSet(res);
                listaespecialidades.add(especialidade);
            }
            return listaespecialidades;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e.getCause());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Especialidade getEspecialidadesFromResultSet(ResultSet res) throws SQLException {
        Especialidade especialidade = new Especialidade();
        especialidade.setIdEspecialidade(res.getInt("id_Especialidade"));
        especialidade.setValor(res.getDouble("valor"));
        especialidade.setNome(res.getString("nome"));
        return especialidade;
    }

}
