package org.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
/**
 * @author 王欣
 * @version v1.0 , 2012-10-19 下午3:49:04
 */
public class MysqlPaginationPlugin extends PluginAdapter {  
    @Override  
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,  
            IntrospectedTable introspectedTable) {  
        // add field, getter, setter for limit clause  
        addLimit(topLevelClass, introspectedTable, "mysqlOffset");  
        addLimit(topLevelClass, introspectedTable, "mysqlLength");  
        return super.modelExampleClassGenerated(topLevelClass,  
                introspectedTable);  
    }  
    @Override  
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(  
            XmlElement element, IntrospectedTable introspectedTable) {  
        XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$  
        isNotNullElement.addAttribute(new Attribute("test", "mysqlOffset != null and mysqlLength != null")); //$NON-NLS-1$ //$NON-NLS-2$  
        isNotNullElement.addElement(new TextElement("<![CDATA[ limit #{mysqlOffset} , #{mysqlLength} ]]>"));  
        element.addElement(isNotNullElement);
        return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element,  
                introspectedTable);  
    } 
    
    private void addLimit(TopLevelClass topLevelClass,  
            IntrospectedTable introspectedTable, String name) {  
        CommentGenerator commentGenerator = context.getCommentGenerator();  
        Field field = new Field();  
        field.setVisibility(JavaVisibility.PROTECTED);  
        field.setType(FullyQualifiedJavaType.getInteger());  
        field.setName(name);  
        commentGenerator.addFieldComment(field, introspectedTable);  
        topLevelClass.addField(field);  
        char c = name.charAt(0);  
        String camel = Character.toUpperCase(c) + name.substring(1);  
        Method method = new Method();  
        method.setVisibility(JavaVisibility.PUBLIC);  
        method.setName("set" + camel);  
        method.addParameter(new Parameter(FullyQualifiedJavaType  
                .getIntInstance(), name));  
        addSetterComment(method, OutputUtilities.lineSeparator+"\t *            指定返回记录行的偏移量<br>"+OutputUtilities.lineSeparator+"\t *            "
				+ "mysqlOffset= 5,mysqlLength=10;  // 检索记录行 6-15", "mysqlOffset");
        method.addBodyLine("this." + name + "=" + name + ";");  
        commentGenerator.addGeneralMethodComment(method, introspectedTable);  
        topLevelClass.addMethod(method);  
        method = new Method();  
        method.setVisibility(JavaVisibility.PUBLIC);  
        method.setReturnType(FullyQualifiedJavaType.getInteger());  
        method.setName("get" + camel);  
        addSetterComment(method, OutputUtilities.lineSeparator+"\t *            指定返回记录行的最大数目"
				+ "<br>"+OutputUtilities.lineSeparator+"\t *            mysqlOffset= 5,mysqlLength=10;  // 检索记录行 6-15", "mysqlLength");
        method.addBodyLine("return " + name + ";");  
        
        commentGenerator.addGeneralMethodComment(method, introspectedTable);  
        topLevelClass.addMethod(method);  
    }  
    /** 
     * This plugin is always valid - no properties are required 
     */  
    public boolean validate(List<String> warnings) {  
        return true;  
    }  
    public static void generate() {  
        String config = MysqlPaginationPlugin.class.getClassLoader().getResource(  
                "generatorConfig.xml").getFile();  
        String[] arg = { "-configfile", config, "-overwrite" };  
        ShellRunner.main(arg);  
    }  
    public static void main(String[] args) {  
        generate();  
    }  
}  
