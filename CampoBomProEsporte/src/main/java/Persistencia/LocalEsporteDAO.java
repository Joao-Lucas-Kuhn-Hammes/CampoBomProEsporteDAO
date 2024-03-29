package Persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import Models.Esporte;
import Models.Local;
import Models.LocalEsporte;

import com.mysql.jdbc.PreparedStatement;


public class LocalEsporteDAO {
		
		//atributos
		private ConexaoMysql conexao;
		private LocalDAO localDAO = new LocalDAO();
		private EsporteDAO esporteDAO = new EsporteDAO();
		
		//construtor
		public LocalEsporteDAO() {
			super();
			this.conexao = new ConexaoMysql();
		}
		
		//cria um relacionamento entre esporte e local
		public boolean salvar(LocalEsporte le) {
			this.conexao.abrirConexao();
			String sqlInsert = "INSERT INTO local_esportes VALUES(?,null,?)";
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
				statement.setLong(1, le.getLocal().getId());
				statement.setLong(2, le.getEsporte().getId());
				statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			} finally {
				this.conexao.fecharConexao();
			}
			return true;
		}
		
		//exclui relacionamento
		public boolean excluir(LocalEsporte le) {
			this.conexao.abrirConexao();
			String sqlExcluir = "DELETE FROM local_esportes WHERE id_esportes=? AND id_local=?)";
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlExcluir);
				statement.setLong(2, le.getLocal().getId());
				statement.setLong(1, le.getEsporte().getId());
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
		
		//busca no banco todos esportes de um local
		public ArrayList<Esporte> buscarPorLocal(Local local) {
			ArrayList<Esporte> al = new ArrayList<>();
			this.conexao.abrirConexao();
			String sqlBuscarPorId = "SELECT id_esportes FROM local_esportes WHERE id_local =?";
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlBuscarPorId);
				statement.setLong(1, local.getId());
				ResultSet rs = statement.executeQuery();
				// CONVERTER O RESULTSET EM UM OBJETO USUARIO
				while(rs.next()) {
					al.add(esporteDAO.buscarPorId(rs.getLong("id_esportes")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.conexao.fecharConexao();
			}		
			return al;
		}
		
		//busca todos os locais com aquele esporte
		public ArrayList<Local> buscarPorEsporte(Esporte esporte) {
			ArrayList<Local> al = new ArrayList<>();
			this.conexao.abrirConexao();
			String sqlBuscarPorId = "SELECT id_local FROM local_esportes WHERE id_esportes =?";
			try {
				PreparedStatement statement = (PreparedStatement) this.conexao.getConexao().prepareStatement(sqlBuscarPorId);
				statement.setLong(1, esporte.getId());
				ResultSet rs = statement.executeQuery();
				// CONVERTER O RESULTSET EM UM OBJETO USUARIO
				while(rs.next()) {
					al.add(localDAO.buscarPorId(rs.getLong("id_local")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.conexao.fecharConexao();
			}		
			return al;
		}
}
