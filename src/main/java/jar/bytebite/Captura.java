package jar.bytebite;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.sistema.Sistema;
import com.github.britooo.looca.api.group.temperatura.Temperatura;
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
public class Captura extends Conexao {

    Conexao conexao = new Conexao();
    ConexaoMySQL ConexaoMySQL = new ConexaoMySQL();

    JdbcTemplate con = conexao.getConnection();
    JdbcTemplate conMySQL = ConexaoMySQL.getConnectionMySQL();

    Alerta alerta = new Alerta();

    Looca looca = new Looca();
    Sistema sistema = looca.getSistema();
    Memoria memoria = looca.getMemoria();
    Processador cpu = looca.getProcessador();
    DiscoGrupo discoGrupo = looca.getGrupoDeDiscos();
    Temperatura temperatura = looca.getTemperatura();
    double scale = Math.pow(10, 2);

    //        Processador
    Double porcUso = cpu.getUso();
    double porcUsoCpu = Math.round(porcUso * scale) / scale;

    Long LongCpu = looca.getProcessador().getFrequencia();
    double c = LongCpu.doubleValue();
    Double cpuBites = c / 1000000000;
    double totalCpu = Math.round(cpuBites * scale) / scale;

    Double temperaturaAntes = (Math.random() * 35) + 45;
    double temperaturaCpu = Math.round(temperaturaAntes * scale) / scale;

//        Memória Ram
    Long longMemoriaD = memoria.getDisponivel();
    double d = longMemoriaD.doubleValue();
    Double memoriaDisponivelBites = d / (1024 * 1024 * 1024);
    double ramDisponivel = Math.round(memoriaDisponivelBites * scale) / scale;

    Long longMemoriaU = memoria.getEmUso();
    double u = longMemoriaU.doubleValue();
    Double memoriaEmUsoBites = u / (1024 * 1024 * 1024);
    double ramEmUso = Math.round(memoriaEmUsoBites * scale) / scale;

    Double ramTotalSemFormatar = Double.valueOf(looca.getMemoria().getTotal());
    Double ramTotalSemFormatado = ramTotalSemFormatar / 1073141824.00;
    Double ramTotal = Math.round(ramTotalSemFormatado * scale) / scale;

//        Janelas
    Integer janelasTotal = looca.getGrupoDeJanelas().getTotalJanelas();

//        Armazenamento
    Long longArmazenamento = discoGrupo.getTamanhoTotal();
    double a = longArmazenamento.doubleValue();
    Double armazenamentoBites = a / (1024 * 1024 * 1024);
    double armazenamentoTotal = Math.round(armazenamentoBites * scale) / scale;

    Double armazenamentoEmUsoSemFormatar = Double.valueOf(discoGrupo.getDiscos().get(0).getBytesDeLeitura());
    Double armazenamentoEmUsoSemFormatado = armazenamentoEmUsoSemFormatar / 1000000000.00;
    Double armazenamentoEmUso = Math.round(armazenamentoEmUsoSemFormatado * scale) / scale;

    private static Logger logger = Logger.getLogger(Captura.class.getName());

    public static void logFormatacao() throws IOException {
        Looca looca = new Looca();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dataFormatada = dateFormat.format(date);
        String sistemaOperacional = looca.getSistema().getSistemaOperacional();

        if (sistemaOperacional.equalsIgnoreCase("Windows")) {
            Path path = Paths.get("C:/Logs-ByteBite/Capturas/");
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            FileHandler fileHadler = new FileHandler(String.format("C:/Logs-ByteBite/Capturas/%s.txt", dataFormatada));
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
            Path path = Paths.get("/home/ubuntu/Desktop/Logs-ByteBite/Capturas/");
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            FileHandler fileHadler = new FileHandler(String.format("/home/ubuntu/Desktop/Logs-ByteBite/Capturas/%s.txt", dataFormatada));
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

    public Integer retornarFkConfigCpu(String id, String senha) {
        return con.queryForObject("select idConfiguracao from configuracao as c join maquina as m on c.fk_maquina = m.idMaquina join componente as comp on c.fk_componente = comp.idComponente where m.idMaquina = ? and m.senha = ? and comp.total = ?; ", Integer.class, id, senha, totalCpu);
    }

    public Integer retornarFkConfigRam(String id, String senha) {
        return con.queryForObject("select idConfiguracao from configuracao as c join maquina as m on c.fk_maquina = m.idMaquina join componente as comp on c.fk_componente = comp.idComponente where m.idMaquina = ? and m.senha = ? and comp.total = ?; ", Integer.class, id, senha, ramTotal);
    }

    public Integer retornarFkConfigArmazenamento(String id, String senha) {
        return con.queryForObject("select idConfiguracao from configuracao as c join maquina as m on c.fk_maquina = m.idMaquina join componente as comp on c.fk_componente = comp.idComponente where m.idMaquina = ? and m.senha = ? and comp.total = ?; ", Integer.class, id, senha, armazenamentoTotal);
    }

    public Integer retornarIdLog(Double medicao, String data, String hora) {
        return con.queryForObject("select idLog from log_captura where data_ = ? and hora = ? and medicao = ?;", Integer.class, data, hora, medicao);
    }

    public void inserirNoBanco(String id, String senha, String data, String hora) {
        try {

            con.update("insert into log_captura values(?, ?, ?, ?, ?);",
                    data, hora, porcUsoCpu, retornarFkConfigCpu(id, senha), 1);

            con.update("insert into log_captura values(?, ?, ?, ?, ?);",
                    data, hora, temperaturaCpu, retornarFkConfigCpu(id, senha), 2);

            System.out.println("Inseriu no banco os dados da CPU");

            con.update("insert into log_captura values(?, ?, ?, ?, ?);",
                    data, hora, ramEmUso, retornarFkConfigRam(id, senha), 1);

            System.out.println("Inseriu no banco os dados da mamória ram");

            con.update("insert into janelas values(?, ?, ?);",
                    data, hora, janelasTotal);

            System.out.println("Inseriu no banco os dados das janelas");

            con.update("insert into log_captura values(?, ?, ?, ?, ?);",
                    data, hora, armazenamentoEmUso, retornarFkConfigArmazenamento(id, senha), 1);

            System.out.println("Inseriu no banco os dados do armazenamento");
            System.out.println(porcUsoCpu);
            System.out.println(temperaturaCpu);
            System.out.println(ramEmUso);
            System.out.println(armazenamentoEmUso);
            verificarAlertas(data, hora);

        } catch (Exception e) {
            System.out.println("Erro ao inserir dados.");
            System.out.println(e);
        }
    }

    public void verificarAlertas(String data, String hora) {
//        1 Uso da CPU está alto.
//        2 uso da CPU está em estado crítico.
//        3 A temperatura da CPU está alta.
//        4 A temperatura da CPU está em estado crítico.
//        5 O uso da memória ram está alto.
//        6 O uso da memória ram está em estado crítico.
//        7 O uso do armazenamento está alto.
//        8 O uso do armazenamento está em estado crítico.

        //Moderado
        if ((porcUsoCpu * 100 / totalCpu) >= 70 && (porcUsoCpu * 100 / totalCpu) < 90) {
            alerta.alertaModerado(retornarIdLog(porcUsoCpu, data, hora), 1, data, hora);
        }
        if (temperaturaCpu >= 65 && temperaturaCpu < 71) {
            alerta.alertaModerado(retornarIdLog(temperaturaCpu, data, hora), 3, data, hora);
        }
        if ((ramEmUso * 100 / ramTotal) >= 80 && (ramEmUso * 100 / ramTotal) < 90) {
            alerta.alertaModerado(retornarIdLog(ramEmUso, data, hora), 5, data, hora);
        }
        if ((armazenamentoEmUso * 100 / armazenamentoTotal) >= 70 && (armazenamentoEmUso * 100 / armazenamentoTotal) < 90) {
            alerta.alertaModerado(retornarIdLog(armazenamentoEmUso, data, hora), 7, data, hora);
        }
        //Crítico

        if ((porcUsoCpu * 100 / totalCpu) >= 90) {
            alerta.alertaCritico(retornarIdLog(porcUsoCpu, data, hora), 2, data, hora);
        }
        if (temperaturaCpu >= 71) {
            alerta.alertaCritico(retornarIdLog(temperaturaCpu, data, hora), 4, data, hora);
        }
        if ((ramEmUso * 100 / ramTotal) >= 90) {
            alerta.alertaCritico(retornarIdLog(ramEmUso, data, hora), 6, data, hora);
        }
        if ((armazenamentoEmUso * 100 / armazenamentoTotal) >= 99) {
            alerta.alertaCritico(retornarIdLog(armazenamentoEmUso, data, hora), 8, data, hora);
        }

    }

    public void inserirNoBancoMySQL(String id, String senha, String data, String hora) {
        try {

            conMySQL.update("insert into log_captura( data_, hora, medicao, fk_configuracao, fk_tipo_log) values(?, ?, ?, ?, ?);",
                    data, hora, porcUsoCpu, retornarFkConfigCpu(id, senha), 1);

            conMySQL.update("insert into log_captura ( data_, hora, medicao, fk_configuracao, fk_tipo_log) values(?, ?, ?, ?, ?);",
                    data, hora, temperaturaCpu, retornarFkConfigCpu(id, senha), 2);

            System.out.println("Inseriu no banco MySQL os dados da CPU");
            logger.info("Os dados da CPU foram inseridos no banco MySQL");

            conMySQL.update("insert into log_captura ( data_, hora, medicao, fk_configuracao, fk_tipo_log) values(?, ?, ?, ?, ?);",
                    data, hora, ramEmUso, retornarFkConfigRam(id, senha), 1);

            System.out.println("Inseriu no banco MySQL os dados da mamória ram");
            logger.info("Os dados da mamória RAM foram inseridos no banco MySQL");

            conMySQL.update("insert into log_captura ( data_, hora, medicao, fk_configuracao, fk_tipo_log) values(?, ?, ?, ?, ?);",
                    data, hora, armazenamentoEmUso, retornarFkConfigArmazenamento(id, senha), 1);

            System.out.println("Inseriu no banco MySQL os dados do armazenamento");
            logger.info("Os dados do armazenamento foram inseridos no banco MySQL");

        } catch (Exception e) {
            System.out.println("Erro ao inserir dados no banco MySQL.");
            logger.severe("Houve uma falha durante a inserção dos dados em relação ao MySQL.");
            System.out.println(e);
        }
    }

    public void mostrarInfoSistema() {

        System.out.println(sistema.toString());
    }

}
