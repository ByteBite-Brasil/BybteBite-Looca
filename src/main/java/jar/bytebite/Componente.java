/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jar.bytebite;

import com.github.britooo.looca.api.core.Looca;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
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

    private static Logger logger = Logger.getLogger(Componente.class.getName());

    public static void logFormatacao() throws IOException {
        Looca looca = new Looca();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dataFormatada = dateFormat.format(date);
        String sistemaOperacional = looca.getSistema().getSistemaOperacional();

        if (sistemaOperacional.equalsIgnoreCase("Windows")) {
            Path path = Paths.get("C:/Logs-ByteBite/Componentes/");
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            FileHandler fileHadler = new FileHandler(String.format("C:/Logs-ByteBite/Componentes/%s.txt", dataFormatada));
            fileHadler.setFormatter(new Formatter() {
                private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy >> HH:mm:ss");

                public String format(LogRecord record) {

                    StringBuilder builder = new StringBuilder();
                    builder.append(dateFormat.format(new Date())).append(" ");
                    builder.append(record.getLevel()).append(": ");
                    builder.append(record.getMessage()).append(" (");
                    builder.append(record.getSourceClassName()).append(".");
                    builder.append(record.getSourceMethodName()).append(")");
                    builder.append(System.lineSeparator());
                    return builder.toString();
                }
            }
            );
            logger.addHandler(fileHadler);
            logger.setLevel(Level.ALL);
        }

        if (sistemaOperacional.equalsIgnoreCase("Ubuntu")) {
            Path path = Paths.get("/home/ubuntu/Desktop/Logs-ByteBite/Componentes/");
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            FileHandler fileHadler = new FileHandler(String.format("/home/ubuntu/Desktop/Logs-ByteBite/Componentes/%s.txt", dataFormatada));
            fileHadler.setFormatter(new Formatter() {
                private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy >> HH:nn:ss");

                public String format(LogRecord record) {

                    StringBuilder builder = new StringBuilder();
                    builder.append(dateFormat.format(new Date())).append(" ");
                    builder.append(record.getLevel()).append(": ");
                    builder.append(record.getMessage()).append(" (");
                    builder.append(record.getSourceClassName()).append(".");
                    builder.append(record.getSourceMethodName()).append(")");
                    builder.append(System.lineSeparator());
                    return builder.toString();
                }
            }
            );
            logger.addHandler(fileHadler);
            logger.setLevel(Level.ALL);
        }
    }

    public void inserirComponente() {
        try {
            con.update("insert into componente values(?, ?, ?);",
                    totalCpu, "GHz", 1);
            System.out.println("Inseriu um novo componente do tipo 'Cpu'.");
            logger.info("Foi inserido um novo componente do tipo 'CPU'.");

        } catch (Exception e) {
            System.out.println("Componente do tipo 'Cpu' já existente.");
            logger.info("O componente do tipo 'CPU' já existe.");
        }
        try {
            con.update("insert into componente values(?, ?, ?);",
                    ramTotal, "GB", 2);
            System.out.println("Inseriu um novo componente do tipo 'Memória ram'.");
            logger.info("Foi inserido um novo componente do tipo 'Memória RAM'.");

        } catch (Exception e) {
            System.out.println("Componente do tipo 'Memória ram' já existente.");
            logger.info("O componente do tipo 'Memória RAM' já existe.");
        }
        try {
            con.update("insert into componente values(?, ?, ?);",
                    armazenamentoTotal, "GB", 3);
            System.out.println("Inseriu um novo componente do tipo 'Armazenamento'.");
            logger.info("Foi inserido um novo componente do tipo 'Armazenamento'.");

        } catch (Exception e) {
            System.out.println("Componente do tipo 'Armazenamento' já existente.");
            logger.info("O componente do tipo 'Armazenamento' já existe.");
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
            logger.severe("Houve uma falha durante a inserção das configurações.");
        }
    }

    public void mostrar() {
        System.out.println(totalCpu);
    }

}
