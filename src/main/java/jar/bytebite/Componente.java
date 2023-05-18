/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jar.bytebite;

import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author ViniciusJesus
 */
public class Componente {

    Conexao conexao = new Conexao();
    ConexaoMySQL ConexaoMySQL = new ConexaoMySQL();
    
    JdbcTemplate con = conexao.getConnection();
    JdbcTemplate conMySQL = ConexaoMySQL.getConnectionMySQL();
    
    Looca looca = new Looca();
    double scale = Math.pow(10, 2);

    Double ramTotalSemFormatar = Double.valueOf(looca.getMemoria().getTotal());
    Double ramTotalSemFormatado = ramTotalSemFormatar / 1073141824.00;
    Double ramTotal = Math.round(ramTotalSemFormatado * scale) / scale;

    Long longArmazenamento = looca.getGrupoDeDiscos().getTamanhoTotal();
    double a = longArmazenamento.doubleValue();
    Double armazenamentoBites = a / (1024 * 1024 * 1024);
    double armazenamentoTotal = Math.round(armazenamentoBites * scale) / scale;

    Long LongCpu = looca.getProcessador().getFrequencia();
    double c = LongCpu.doubleValue();
    Double cpuBites = c / 1000000000;
    double totalCpu = Math.round(cpuBites * scale) / scale;

    public void inserirComponente() {
        try {
            con.update("insert into componente values(?, ?, ?);",
                    totalCpu, "GHz", 1);
            System.out.println("Inseriu um novo componente do tipo 'Cpu'.");

        } catch (Exception e) {
            System.out.println("Componente do tipo 'Cpu' já existente.");
        }
        try {
            con.update("insert into componente values(?, ?, ?);",
                    ramTotal, "GB", 2);
            System.out.println("Inseriu um novo componente do tipo 'Memória ram'.");

        } catch (Exception e) {
            System.out.println("Componente do tipo 'Memória ram' já existente.");
        }
        try {
            con.update("insert into componente values(?, ?, ?);",
                    armazenamentoTotal, "GB", 3);
            System.out.println("Inseriu um novo componente do tipo 'Armazenamento'.");

        } catch (Exception e) {
            System.out.println("Componente do tipo 'Armazenamento' já existente.");
        }
        //MYSQL
        try {
            conMySQL.update("insert into componente (total, unidadeMedida, fk_tipo_componente) values(?, ?, ?);",
                    totalCpu, "GHz", 1);
            System.out.println("Inseriu um novo componente do tipo 'Cpu'.");

        } catch (Exception e) {
            System.out.println("Componente do tipo 'Cpu' já existente.");
            
        }
        try {
            conMySQL.update("insert into componente (total, unidadeMedida, fk_tipo_componente) values(?, ?, ?);",
                    ramTotal, "GB", 2);
            System.out.println("Inseriu um novo componente do tipo 'Memória ram'.");

        } catch (Exception e) {
            System.out.println("Componente do tipo 'Memória ram' já existente.");
            
        }
        try {
            conMySQL.update("insert into componente(total, unidadeMedida, fk_tipo_componente) values(?, ?, ?);",
                    armazenamentoTotal, "GB", 3);
            System.out.println("Inseriu um novo componente do tipo 'Armazenamento'.");

        } catch (Exception e) {
            System.out.println("Componente do tipo 'Armazenamento' já existente.");
            
        }
    }

    public Integer FkComponenteParaConfigCpu() {
        return con.queryForObject("select idComponente from componente where total = ?;", Integer.class, totalCpu);
    }

    public Integer FkComponenteParaConfigRam() {
        return con.queryForObject("select idComponente from componente where total = ?;", Integer.class, ramTotal);
    }

    public Integer FkComponenteParaConfigArmazenamento() {
        return con.queryForObject("select idComponente from componente where total = ?;", Integer.class, armazenamentoTotal);
    }
    
    public Integer FkComponenteParaConfigCpuMySQL() {
        return conMySQL.queryForObject("select idComponente from componente where total = ?;", Integer.class, totalCpu);
    }

    public Integer FkComponenteParaConfigRamMySQL() {
        return conMySQL.queryForObject("select idComponente from componente where total = ?;", Integer.class, ramTotal);
    }

    public Integer FkComponenteParaConfigArmazenamentoMySQL() {
        return conMySQL.queryForObject("select idComponente from componente where total = ?;", Integer.class, armazenamentoTotal);
    }

    public Integer ConsultarConfig(String id) {
        return conMySQL.queryForObject("SELECT COUNT(idConfiguracao) FROM configuracao WHERE fk_maquina = ?", Integer.class, id);
    }
    
    public void inserirConfiguracao(String id) {
        
        try {
            
            
            con.update("insert into configuracao values (?, ?);",
                    id, FkComponenteParaConfigCpu());
            con.update("insert into configuracao values (?, ?);",
                    id, FkComponenteParaConfigRam());
            con.update("insert into configuracao values (?, ?);",
                    id, FkComponenteParaConfigArmazenamento());
            System.out.println("Deu Certo a inserção de configuração");
            
           

            conMySQL.update("insert into configuracao (fk_maquina, fk_componente) values (?, ?);",
                    id, FkComponenteParaConfigCpuMySQL());
            conMySQL.update("insert into configuracao (fk_maquina, fk_componente) values (?, ?);",
                    id, FkComponenteParaConfigRamMySQL());
            conMySQL.update("insert into configuracao (fk_maquina, fk_componente) values (?, ?);",
                    id, FkComponenteParaConfigArmazenamentoMySQL());
            System.out.println("Deu Certo a inserção de configuração");

        } catch (Exception e) {
            System.out.println("Erro na inserção de configuração");
        }
    }

    public void mostrar() {
        System.out.println(totalCpu);
    }

}
