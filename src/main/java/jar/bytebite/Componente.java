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
public class Componente extends Conexao {

    Conexao conexao = new Conexao();
    JdbcTemplate con = conexao.getConnection();

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

    }

    public Integer fkComponenteParaConfigCpu() {
        return con.queryForObject("select idComponente from componente where total = ?;", Integer.class, totalCpu);
    }

    public Integer fkComponenteParaConfigRam() {
        return con.queryForObject("select idComponente from componente where total = ?;", Integer.class, ramTotal);
    }

    public Integer fkComponenteParaConfigArmazenamento() {
        return con.queryForObject("select idComponente from componente where total = ?;", Integer.class, armazenamentoTotal);
    }

    public Integer consultarConfig(String id) {
        return con.queryForObject("SELECT COUNT(idConfiguracao) FROM configuracao WHERE fk_maquina = ?", Integer.class, id);
    }

    public void inserirConfiguracao(String id) {

        try {

            con.update("insert into configuracao values (?, ?);",
                    id, fkComponenteParaConfigCpu());
            con.update("insert into configuracao values (?, ?);",
                    id, fkComponenteParaConfigRam());
            con.update("insert into configuracao values (?, ?);",
                    id, fkComponenteParaConfigArmazenamento());
            System.out.println("Deu Certo a inserção de configuração");

        } catch (Exception e) {
            System.out.println("Erro na inserção de configuração");

        }
    }

    public void mostrar() {
        System.out.println(totalCpu);
    }

}
