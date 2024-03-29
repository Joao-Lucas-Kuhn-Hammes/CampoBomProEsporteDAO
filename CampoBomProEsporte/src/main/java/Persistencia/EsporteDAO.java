package Persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Models.Esporte;

import com.mysql.jdbc.PreparedStatement;

public class EsporteDAO {
		
		//atributos
		private ConexaoMysql conexao;
		private UsuarioDAO usuDAO = new UsuarioDAO();
		
		//construtores
		public EsporteDAO() {
			super();
			this.conexao = new ConexaoMysql();
		}
		
		//salvar no banco novo esporte
		public Esporte salvar(Esporte es) {
			this.conexao.abrirConexao();
			String sqlInsert = "INSERT INTO esportes VALUES(null,?,?,?,?)";
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
				statement.setString(1, es.getDescricao());
				statement.setLong(2, 0);
				statement.setLong(3, es.getUsuario().getId());
				statement.setString(4, es.getNome());
				statement.executeUpdate();
				ResultSet rs = statement.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					es.setId(id);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.conexao.fecharConexao();
			}
			return es;
		}
		
		//edita no banco um esporte existente
		public Esporte editar(Esporte es) {
			this.conexao.abrirConexao();
			String sqlUpdate = "UPDATE esportes SET descricao=?, id_usuario=?, id_pin=?, nome=? WHERE id_esportes=?";
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlUpdate);
				statement.setString(1, es.getDescricao());
				statement.setLong(2, es.getUsuario().getId());
				statement.setLong(3, 0);
				statement.setString(4, es.getNome());
				statement.setLong(5, es.getId());
				/*int linhasAfetadas =*/ statement.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.conexao.fecharConexao();
			}
			return es;
		}
		
		//exclui esporte no banco
		public boolean excluir(long id) {
			
			//deleta seus relacionamentos
			this.conexao.abrirConexao();
			String sqlExcluir = "DELETE FROM local_esportes WHERE id_esporte=?";
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlExcluir);
				statement.setLong(1, id);
				int linhasAfetadas = statement.executeUpdate();
				if(linhasAfetadas>0) {
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.conexao.fecharConexao();
			}
			
			//agora exclui no banco
			this.conexao.abrirConexao();
			sqlExcluir = "DELETE FROM esportes WHERE id_esportes=?";
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlExcluir);
				statement.setLong(1, id);
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
		
		//busca esporte no banco por id
		public Esporte buscarPorId(long id) {
			this.conexao.abrirConexao();
			String sqlBuscarPorId = "SELECT * FROM esportes WHERE id_esportes=?";
			Esporte es = null;
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlBuscarPorId);
				statement.setLong(1, id);
				ResultSet rs = statement.executeQuery();
				// CONVERTER O RESULTSET EM UM OBJETO USUARIO
	 			if(rs.next()) {
					es = new Esporte();
					es.setId(rs.getLong("id_esportes"));
					es.setDescricao(rs.getString("descricao"));
					es.setUsuario(usuDAO.buscarPorId(rs.getLong("id_usuario")));
					es.setPin(rs.getLong("id_pin"));
					es.setNome(rs.getString("nome"));
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.conexao.fecharConexao();
			}		
			return es;
		}
		
		//busca todos
		public ArrayList<Esporte> buscarTodos() {
			this.conexao.abrirConexao();
			ArrayList<Esporte> al = new ArrayList<>();
			String sqlBuscarPorId = "SELECT * FROM esportes";
			Esporte es = null;
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlBuscarPorId);
				ResultSet rs = statement.executeQuery();
				// CONVERTER O RESULTSET EM UM OBJETO USUARIO
	 			while(rs.next()) {
					es = new Esporte();
					es.setId(rs.getLong("id_esportes"));
					es.setDescricao(rs.getString("descricao"));
					es.setUsuario(usuDAO.buscarPorId(rs.getLong("id_usuario")));
					es.setPin(rs.getLong("id_pin"));
					es.setNome(rs.getString("nome"));
					al.add(es);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.conexao.fecharConexao();
			}		
			return al;
		}
		
		//busca todos esporte criados com um usuario
		public ArrayList<Esporte> buscarTodosUsuario(Long id) {
			this.conexao.abrirConexao();
			ArrayList<Esporte> al = new ArrayList<>();
			String sqlBuscarPorId = "SELECT * FROM esportes WHERE id_usuario =?";
			Esporte es = null;
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlBuscarPorId);
				statement.setLong(1, id);
				ResultSet rs = statement.executeQuery();
				// CONVERTER O RESULTSET EM UM OBJETO USUARIO
	 			while(rs.next()) {
					es = new Esporte();
					es.setId(rs.getLong("id_esportes"));
					es.setDescricao(rs.getString("descricao"));
					es.setUsuario(usuDAO.buscarPorId(rs.getLong("id_usuario")));
					es.setPin(rs.getLong("id_pin"));
					al.add(es);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.conexao.fecharConexao();
			}		
			return al;
		}
}

