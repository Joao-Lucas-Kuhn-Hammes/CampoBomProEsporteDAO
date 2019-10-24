package Persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import Models.Local;

import com.mysql.jdbc.PreparedStatement;

public class LocalDAO {
		 
		private ConexaoMysql conexao;
		private UsuarioDAO usuDAO = new UsuarioDAO();
		
		
		public LocalDAO() {
			super();
			this.conexao = new ConexaoMysql();
		}
		// Salvar
		public Local salvar(Local l) {
			this.conexao.abrirConexao();
			String sqlInsert = "INSERT INTO local VALUES(?,?,?,null,?,?";
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
				statement.setDouble(1, l.getLatitude());
				statement.setString(2, l.getDescricao());
				statement.setString(3, l.getImagem());
				statement.setDouble(1, l.getLongitude());
				statement.setLong(1, l.getUsuario().getId());
				statement.executeUpdate();
				ResultSet rs = statement.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					l.setId(id);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.conexao.fecharConexao();
			}
			return l;
		}
		
		public Local editar(Local l) {
			this.conexao.abrirConexao();
			String sqlUpdate = "UPDATE local SET longitude=?, descricao=?, imagem=?, latitute=?, id_usuario=? WHERE id_local=?";
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlUpdate);
				statement.setDouble(1, l.getLongitude());
				statement.setString(2, l.getDescricao());
				statement.setString(3, l.getImagem());
				statement.setDouble(4, l.getLatitude());
				statement.setLong(5, l.getUsuario().getId());
				statement.setLong(6, l.getId());
				/*int linhasAfetadas = */statement.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.conexao.fecharConexao();
			}
			return l;
		}
	//	
	//	
		public boolean excluir(int id) {
			
			this.conexao.abrirConexao();
			String sqlExcluir = "DELETE FROM equipamentos_local WHERE id_local=?";
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlExcluir);
				statement.setInt(1, id);
				int linhasAfetadas = statement.executeUpdate();
				if(linhasAfetadas>0) {
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.conexao.fecharConexao();
			}
			
			this.conexao.abrirConexao();
			sqlExcluir = "DELETE FROM local_esportes WHERE id_local=?";
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlExcluir);
				statement.setInt(1, id);
				int linhasAfetadas = statement.executeUpdate();
				if(linhasAfetadas>0) {
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.conexao.fecharConexao();
			}
			
			this.conexao.abrirConexao();
			sqlExcluir = "DELETE FROM local WHERE id_local=?";
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlExcluir);
				statement.setInt(1, id);
				int linhasAfetadas = statement.executeUpdate();
				if(linhasAfetadas>0) {
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.conexao.fecharConexao();
			}
			return false;
		
	}
		public Local buscarPorId(long id) {
			this.conexao.abrirConexao();
			String sqlBuscarPorId = "SELECT * FROM local WHERE id_local=?";
			Local l = null;
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlBuscarPorId);
				statement.setLong(1, id);
				ResultSet rs = statement.executeQuery();
				// CONVERTER O RESULTSET EM UM OBJETO USUARIO
	 			if(rs.next()) {
					l = new Local();
					l.setId(rs.getLong("id_local"));
					l.setDescricao(rs.getString("descricao"));
					l.setUsuario(usuDAO.buscarPorId(rs.getLong("id_usuario")));
					l.setLongitude(rs.getDouble("longitude"));
					l.setLatitude(rs.getDouble("latitude"));
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.conexao.fecharConexao();
			}		
			return l;
		}
		public ArrayList<Local> buscarTodos() {
			ArrayList<Local> al = new ArrayList<>();
			this.conexao.abrirConexao();
			String sqlBuscarPorId = "SELECT * FROM local";
			Local l = null;
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlBuscarPorId);

				ResultSet rs = statement.executeQuery();
				// CONVERTER O RESULTSET EM UM OBJETO USUARIO
	 			while(rs.next()) {
					l = new Local();
					l.setId(rs.getLong("id_local"));
					l.setDescricao(rs.getString("descricao"));
					l.setUsuario(usuDAO.buscarPorId(rs.getLong("id_usuario")));
					l.setLongitude(rs.getDouble("longitude"));
					l.setLatitude(rs.getDouble("latitude"));
					al.add(l);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.conexao.fecharConexao();
			}		
			return al;
		}
}