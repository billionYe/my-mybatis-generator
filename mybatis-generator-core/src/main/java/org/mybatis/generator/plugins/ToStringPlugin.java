
package org.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class ToStringPlugin extends PluginAdapter {

	private FullyQualifiedJavaType tsb;
	private FullyQualifiedJavaType tss;

	public ToStringPlugin() {
		super();
		tsb = new FullyQualifiedJavaType("org.apache.commons.lang.builder.ToStringBuilder"); //$NON-NLS-1$
		tss = new FullyQualifiedJavaType("org.apache.commons.lang.builder.ToStringStyle"); //$NON-NLS-1$
	}

	public boolean validate(List<String> warnings) {
		// this plugin is always valid
		return true;
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		makeToString(topLevelClass, introspectedTable);
		return true;
	}

	@Override
	public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		makeToString(topLevelClass, introspectedTable);
		return true;
	}

	@Override
	public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		makeToString(topLevelClass, introspectedTable);
		return true;
	}

	protected void makeToString(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		topLevelClass.addImportedType(tsb);
		topLevelClass.addImportedType(tss);

		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getStringInstance());
		method.setName("toString"); //$NON-NLS-1$
		if (introspectedTable.isJava5Targeted()) {
			method.addAnnotation("@Override"); //$NON-NLS-1$
		}
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		method.addBodyLine("return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);");
		topLevelClass.addMethod(method);
	}
}
