package com.abby.jiaqing.generators;

import java.util.List;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellRunner;

public class MybatisGenerator extends PluginAdapter {
    public boolean validate(List<String> list) {
        return true;
    }
    public static void main(String[] args){
        String generatorConfig=MybatisGenerator.class.getClassLoader().getResource("generatorConfig.xml").getFile();
        String[] arg={"-configfile",generatorConfig};
        ShellRunner.main(arg);
    }
}
