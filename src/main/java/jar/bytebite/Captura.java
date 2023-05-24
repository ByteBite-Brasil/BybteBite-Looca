package jar.bytebite;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.sistema.Sistema;
import com.github.britooo.looca.api.group.temperatura.Temperatura;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author ViniciusJesus
 */
public class Captura {

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
//        Double temperaturaCpu = temperatura.getTemperatura();
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

//        Long longArmazenamentoEmUso = discoGrupo.getDiscos().get(0).getBytesDeLeitura();
//        double aEmUso = longArmazenamentoEmUso.doubleValue();
//        Double armazenamentoEmUsoBites = aEmUso / (1024*1024*1024);
//        double armazenamentoEmUso = Math.round(armazenamentoEmUsoBites*scale)/scale;
    Double armazenamentoEmUsoSemFormatar = Double.valueOf(discoGrupo.getDiscos().get(0).getBytesDeLeitura());
    Double armazenamentoEmUsoSemFormatado = armazenamentoEmUsoSemFormatar / 1000000000.00;
    Double armazenamentoEmUso = Math.round(armazenamentoEmUsoSemFormatado * scale) / scale;
//
//    public void mostrar() {
//
//        System.out.println("Processador Uso:");
//        System.out.println(porcUsoCpu);
//        System.out.println("Temperatura processador:");
//        System.out.println(temperaturaCpu);
//        System.out.println("Memória RAM total/disponivel/uso:");
//        System.out.println(ramTotal);
//        System.out.println(ramDisponivel);
//        System.out.println(ramEmUso);
//        System.out.println("Total janelas:");
//        System.out.println(janelasTotal);
//        System.out.println("Armazenamento total/emUso");
//        System.out.println(armazenamentoTotal);
//        System.out.println(armazenamentoEmUso);
////    }
//    }

    public Integer retornarFkConfigCpu(String id, String senha) {
        return con.queryForObject("select idConfiguracao from configuracao as c join maquina as m on c.fk_maquina = m.idMaquina join componente as comp on c.fk_componente = comp.idComponente where m.idMaquina = ? and m.senha = ? and comp.total = ?; ", Integer.class, id, senha, totalCpu);
    }

    public Integer retornarFkConfigRam(String id, String senha) {
        return con.queryForObject("select idConfiguracao from configuracao as c join maquina as m on c.fk_maquina = m.idMaquina join componente as comp on c.fk_componente = comp.idComponente where m.idMaquina = ? and m.senha = ? and comp.total = ?; ", Integer.class, id, senha, ramTotal);
    }

    public Integer retornarFkConfigArmazenamento(String id, String senha) {
        return con.queryForObject("select idConfiguracao from configuracao as c join maquina as m on c.fk_maquina = m.idMaquina join componente as comp on c.fk_componente = comp.idComponente where m.idMaquina = ? and m.senha = ? and comp.total = ?; ", Integer.class, id, senha, armazenamentoTotal);
    }

    //MYSQL
//    public Integer retornarFkConfigCpuMySQL(String id, String senha) {
//        return conMySQL.queryForObject("select idConfiguracao from configuracao as c join maquina as m on c.fk_maquina = m.idMaquina join componente as comp on c.fk_componente = comp.idComponente where m.idMaquina = ? and m.senha = ? and comp.total = ?;", Integer.class, id, senha, totalCpu);
//    }
//
//    public Integer retornarFkConfigRamMySQL(String id, String senha) {
//        return conMySQL.queryForObject("select idConfiguracao from configuracao as c join maquina as m on c.fk_maquina = m.idMaquina join componente as comp on c.fk_componente = comp.idComponente where m.idMaquina = ? and m.senha = ? and comp.total = ?;", Integer.class, id, senha, ramTotal);
//    }
//
//    public Integer retornarFkConfigArmazenamentoMySQL(String id, String senha) {
//        return conMySQL.queryForObject("select idConfiguracao from configuracao as c join maquina as m on c.fk_maquina = m.idMaquina join componente as comp on c.fk_componente = comp.idComponente where m.idMaquina = ? and m.senha = ? and comp.total = ?;", Integer.class, id, senha, armazenamentoTotal);
//    }
//    
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

            conMySQL.update("insert into log_captura ( data_, hora, medicao, fk_configuracao, fk_tipo_log) values(?, ?, ?, ?, ?);",
                    data, hora, ramEmUso, retornarFkConfigRam(id, senha), 1);

            System.out.println("Inseriu no banco MySQL os dados da mamória ram");

//            con.update("insert into log_captura values(?, ?, ?, ?);",
//                    data, hora, janelasTotal, retornaxxx);
//            System.out.println("Inseriu no banco os dados das janelas");
            conMySQL.update("insert into log_captura ( data_, hora, medicao, fk_configuracao, fk_tipo_log) values(?, ?, ?, ?, ?);",
                    data, hora, armazenamentoEmUso, retornarFkConfigArmazenamento(id, senha), 1);

            System.out.println("Inseriu no banco MySQL os dados do armazenamento");

        } catch (Exception e) {
            System.out.println("Erro ao inserir dados no banco MySQL.");
            System.out.println(e);
        }
    }
    public void mostrarInfoSistema() {

        System.out.println(sistema.toString());
    }

}
