package jar.bytebite;

import com.github.britooo.looca.api.core.Looca;
import java.io.File;
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
import org.springframework.jdbc.core.JdbcTemplate;
import twilio.bytebite.TwilioSms;
import java.util.logging.Logger;

/**
 *
 * @author ViniciusJesus
 */
public class Alerta extends Conexao {

    TwilioSms twilio = new TwilioSms();
    Conexao conexao = new Conexao();
    JdbcTemplate con = conexao.getConnection();
    //Criticiade id 1= moderado 2= crítico

    private static Logger logger = Logger.getLogger(Login.class.getName());

    public static void logFormatacao() throws IOException {
        Looca looca = new Looca();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dataFormatada = dateFormat.format(date);
        String sistemaOperacional = looca.getSistema().getSistemaOperacional();

        if (sistemaOperacional.equalsIgnoreCase("Windows")) {
            Path path = Paths.get("C:/Logs-ByteBite/Alertas/");
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            FileHandler fileHadler = new FileHandler(String.format("C:/Logs-ByteBite/Alertas/%s.txt", dataFormatada));
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
            Path path = Paths.get("/home/ubuntu/Desktop/Logs-ByteBite/Alertas/");
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            FileHandler fileHadler = new FileHandler(String.format("/home/ubuntu/Desktop/Logs-ByteBite/Alertas/%s.txt", dataFormatada));
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

    public Integer fkJanela(String data, String hora) {
        return con.queryForObject("select idJanelas from [dbo].[janelas] where data_ = ? and hora = ?;", Integer.class, data, hora);
    }

    public void alertaModerado(Integer fkLog, Integer descricao, String data, String hora) {
        try {
            con.update("insert into alerta values (?, ?, ?, ?);", fkLog, 1, descricao, fkJanela(data, hora));
            System.out.println("Alerta emitido.");
            logger.info("Os alertas moderados foram emitidos com sucesso.");

        } catch (Exception e) {
            System.out.println("Erro ao inserir alertas.");
            logger.severe("Houve erro ao inserir os alertas moderados.");
        }
    }

    public void alertaCritico(Integer fkLog, Integer descricao, String data, String hora) {
        try {
            con.update("insert into alerta values (?, ?, ?, ?);", fkLog, 2, descricao, fkJanela(data, hora));
            System.out.println("Alerta emitido.");
            logger.info("Os alertas críticos foram emitidos com sucesso.");

        } catch (Exception e) {
            System.out.println("Erro ao inserir alertas.");
            logger.severe("Houve erro ao inserir os alertas críticos.");
        }
//            twilio.enviaSms("Alerta critico na máquina xxx");
    }

}
