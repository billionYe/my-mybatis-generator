package test;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

public class Main {
	private static final Logger logger = Logger.getLogger(Main.class);

	public static void main(String[] args) {
		Main generator = new Main();
		//generator.process("mysql_auth_wifi.xml");
		//generator.process("mysql_mao_trade_01.xml");
		generator.process("mysql_book.xml");
		//generator.process("mysql_auth_wifi.xml");
	}

	public void process(String fileName) {
		try {
			List<String> warnings = new ArrayList<String>();
			ConfigurationParser cp = new ConfigurationParser(warnings);
			Configuration config = cp
					.parseConfiguration(this.getClass().getClassLoader().getResourceAsStream(fileName));

			DefaultShellCallback shellCallback = new DefaultShellCallback(true);

			MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
			Set<String> ids = new HashSet<String>();
			myBatisGenerator.generate(null,ids);
			//myBatisGenerator.generate(null);
		} catch (Exception e) {
			logger.error("Exception:", e);
		}
	}

}
